package com.prueba.gui.importer;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.csvreader.CsvReader;
import com.prueba.model.EntityBase;
import com.prueba.service.ServiceBase;
import com.prueba.util.Constants;
import com.prueba.util.FileUtil;
import com.prueba.util.JSFUtils;
import com.prueba.util.introspector.BeanProxy;

public abstract class Import  <T extends EntityBase, S extends ServiceBase<T, ?>> implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LoggerFactory.getLogger(Import.class);	
	protected StringBuffer importLog;
	protected S service;
	protected BeanProxy beanProxy;
	protected String fileName;
	protected Map<String,PropertyDescriptor> propertyDescriptors;
	protected Map<String, ConstraintImportField> constraintImportFieldMap;
	protected Table tableInfo; 
	
	public void init(Field attr[]){
		constraintImportFieldMap = new HashMap<String, ConstraintImportField>();		
		for(int i=0; i<attr.length; i++){
			Annotation a[] = attr[i].getAnnotations();    			
			for(int j=0; j<a.length; j++){    				
				if(!(a[j] instanceof ConstraintImportField))
				{
					continue;
				}
				else
				{
					constraintImportFieldMap.put(((ConstraintImportField) a[j]).columnName(), (ConstraintImportField)a[j]);
				}
			}
		}
	}
	
	public void downloadImporterLog() throws IOException,IllegalAccessException{
		Constants.downloadImporterLog(importLog == null ? new StringBuffer() : importLog);
	}	
	
	public String importFile(UploadedFile file) {
		String infoProcess="";
		
		fileName = file.getFileName();
		
		LOGGER.debug("File type: " + file.getContentType());
		LOGGER.debug("File name: " + fileName);
		
		try 
		{
			File inputFile = null;
			String uploadedFileName = FileUtil.trimFilePath(file.getFileName());
			inputFile = createFile(uploadedFileName, file);		
			File uniqueFile = FileUtil.uniqueFile(new File(Constants.getPathUploads()),uploadedFileName);
			FileUtil.createFile(uniqueFile);
			FileInputStream inputStream = new FileInputStream(inputFile);
			FileUtil.write(uniqueFile,inputStream);
			fileName = uniqueFile.getName();
			infoProcess=loadFile(Constants.getPathUploads()+ File.separator + fileName);			
		    if(infoProcess.equals(JSFUtils.getStringFromBundle("succes.import")))
		    {
		    	infoProcess=JSFUtils.getStringFromBundle("succes.load.file");
		    }
		}
		catch (FileNotFoundException e) {
			infoProcess=JSFUtils.getStringFromBundle("error.file.not.found");
			LOGGER.error(e.getMessage());
		}
		catch (IOException e) {
			infoProcess=JSFUtils.getStringFromBundle("error.file.not.found");
			LOGGER.error(e.getMessage());
		}
		catch (Exception e) {
			infoProcess=JSFUtils.getStringFromBundle("error.load.BBDD");
			LOGGER.error(e.getMessage());
		}		
		return infoProcess;
	}
	
	public String loadFile(String fileName){
		String infoProcess=JSFUtils.getStringFromBundle("Success");
		CsvReader reader = null;
		Reader data = null;
		String[] headers;
		try
		{
			String encodingFile = CommonImportUtils.getEncodingFile(fileName);
			data = new InputStreamReader(new FileInputStream(fileName), encodingFile);
			
			reader = new CsvReader(data, Constants.SEPARATOR.charAt(0));

			reader.readHeaders();
			headers = reader.getHeaders();
			
			headers[0] = CommonImportUtils.removeUTF8BOM(headers[0]).replace(Constants.DELIMITER_CSV, Constants.EMPTY).trim();
			reader.setHeaders(headers);
			
			if(tableInfo.getType().equalsIgnoreCase(Constants.TYPE_IMPORT_TOTAL))
			{
				service.deleteAllData();
			}
			importLog = new StringBuffer();
			readData(reader, headers, infoProcess);
			reader.close();
		}catch (Exception e) {
			infoProcess = "Error";
			LOGGER.error("Error al hacer la importacion "+e.getMessage());
		}
		finally{
			if(reader != null)
			{
				reader.close();
			}
			if(data != null)
			{
				try
				{
					data.close();
				}catch (Exception e) {}
			}
		}
		return infoProcess;
	}

	protected abstract void readData(CsvReader reader,String[] headers,String infoProcess) throws ParseException, IOException, IllegalAccessException, NoSuchMethodException, SAXException, IntrospectionException, NoSuchFieldException, InvocationTargetException;
	
	public String validateField(String value,String header){
		String infoProcess="";
		ConstraintImportField field = constraintImportFieldMap.get(header);
		if (field!=null)
		{
			//Si un campo ha de ser obligatorio y viene como null entonces mostramos un error
			if (value.equalsIgnoreCase("NULL") && field.isCompulsory())
			{
				String msg = JSFUtils.getStringFromBundle("error.fiel.not.null");
				infoProcess=JSFUtils.getStringFromBundle("error")+". "+MessageFormat.format(msg,header);
			}
			if((value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("")) && !field.isCompulsory()){
				return infoProcess;
			}		
			//Se comprueba que el campo cumpla con las reglas de validacion establecidas en el xml
			if (!Pattern.matches(field.pattern(),value))
			{
				infoProcess=JSFUtils.getStringFromBundle("error")+". "+JSFUtils.getStringFromBundle(field.messageError());
			}
		}
		return infoProcess;
	}	
	
	public Object getCell(String cell,String type) throws ParseException{
		Object result = null;
		if (cell == null || cell.equalsIgnoreCase("NULL") || cell.length() == 0){
			return null;
		}else if (type.contains("String")){
			result = cell;
		}else if (type.contains("Integer")){
			result = Integer.parseInt(cell);
		}else if (type.contains("Float")){
			result = Float.parseFloat(cell);
		}if (type.contains("Long")){
			result = Long.parseLong(cell);
		}else if (type.contains("Date")){
			DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
			result = df.parse(cell);
		}else if (type.contains("BigDecimal")){
			result = BigDecimal.valueOf(Float.valueOf(cell));
		}
		return result;
	}
	
	public PropertyDescriptor getPropertyElement(String columnId){
		String nameColumn = columnId.replace("_", "");
		return propertyDescriptors.get(nameColumn.toLowerCase());
	}	
	
	public boolean haveIdfilling(String[] keys,T entityBean) throws NoSuchFieldException,InvocationTargetException,IllegalAccessException,NoSuchMethodException{
		beanProxy.setBean(entityBean);
		PropertyDescriptor propertyDescriptor;
		for(int i=0; i < keys.length; i++){
			propertyDescriptor = propertyDescriptors.get(keys[i]);			
			if(propertyDescriptor != null)
			{
				Object object = beanProxy.get(propertyDescriptor.getName());
				if (object == null)
				{
					return false;
				}
			}
		}		
		return true;
	}
	
	public File createFile(String uploadedFileName, UploadedFile file) throws IOException{
		File inputFile = null;
		BufferedOutputStream bufferedOutputStream = null;
		try
		{
			inputFile = new File(new File(Constants.getPathUploads()), uploadedFileName);
			boolean result = inputFile.createNewFile();
			if(result)
			{
				LOGGER.error("Fallo al crear el fichero : "+inputFile.getName());
				throw new IOException("Error al crear el fichero : "+inputFile.getName());
			}
			FileOutputStream fileOutputStream = new FileOutputStream(inputFile);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(file.getContents(),0,file.getContents().length);
			bufferedOutputStream.close();
			fileOutputStream.close();
		}catch (IOException e) {
			LOGGER.error("Fallo al crear el fichero : "+inputFile.getName());
			throw new IOException(e.getMessage());
		}
		finally{
			if(bufferedOutputStream != null)
			{
				bufferedOutputStream.close();
			}
		}
		return inputFile;
	}
	
	
	public String errorLog(int row, String header, String value,Throwable e) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(JSFUtils.getStringFromBundle("number.row"));
		buffer.append(" ");
		buffer.append(row);
		buffer.append(" ---> Campo : \"");
		buffer.append(header);
		buffer.append("\" con el valor : \"");
		buffer.append(value);
		buffer.append("\" motivo del error : ");
		buffer.append(e.getMessage());
		return buffer.toString();
	}
	
	public String errorLog(int row, String header, String value,String message) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(JSFUtils.getStringFromBundle("number.row"));
		buffer.append(" ");
		buffer.append(row);
		buffer.append(" ---> Campo : \"");
		buffer.append(header);
		buffer.append("\" con el valor : \"");
		buffer.append(value);
		buffer.append("\" motivo del error : ");
		buffer.append(message);
		return buffer.toString();
	}
	
	
	public  void setService(S service) {
		this.service = service;
	}	
}
