package com.prueba.gui.exporter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.prueba.model.EntityBase;
import com.prueba.util.Constants;
import com.prueba.util.WordsConverter;
import com.prueba.util.introspector.BeanIntrospector;
import com.prueba.util.introspector.BeanProxy;


public abstract class Exporter<T extends EntityBase> implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class);
	protected static WordsConverter converter;
	protected BeanProxy proxy;
	protected Map<String,PropertyDescriptor> headers;
	
	static{
		converter = WordsConverter.getInstance();
	}
	
	public void init(Class<T> entityBase) {
		try
		{
			proxy = new BeanProxy(entityBase);
			headers= BeanIntrospector.getProperties(entityBase);
		}catch (IntrospectionException e) {
			LOGGER.error("Error al crear el proxy para exportar "+e.getMessage());
		}
	}
	
	public HttpServletResponse getResponse(){
		FacesContext context = FacesContext.getCurrentInstance();
		return (HttpServletResponse) context.getExternalContext().getResponse();
	}
	
	public FacesContext getFacesContext(){
		return FacesContext.getCurrentInstance();
	}
	
	public void setHeaderResponse(HttpServletResponse response, String contentType, String fileName){
		response.setContentType(contentType);			
		response.setHeader("Expires", "0");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename="+ fileName);
	}
	
	/************************************************************** Metodos de de CSV **************************************************************/
	
	public void csvExporter(List<T> results, String fileName) throws IOException, IllegalAccessException, NoSuchMethodException, SAXException, IntrospectionException, NoSuchFieldException, InvocationTargetException{
		HttpServletResponse response = getResponse();
		
		setHeaderResponse(response, Constants.CONTENT_TYPE_CSV, fileName);
		
		PrintWriter writer = response.getWriter();
		
		writer.append(Constants.UTF_8_BOM_BEGINING);

		writeHeaderCsv(writer);
		
		if(results != null)
		{
			String header="";
			int j = 0;
			for (int i=0; i<results.size(); i++){
				proxy.setBean(results.get(i));
				j = 0;
				for (Entry<String, PropertyDescriptor> entry : headers.entrySet()) {
					header=entry.getValue().getName();
					Object value = proxy.get(header);
					if(value != null)
					{
						if(value instanceof Date)
						{
							writer.append(Constants.DELIMITER_CSV+Constants.getDateString((Date)value)+Constants.DELIMITER_CSV);
						}
						else
						{
							writer.append(Constants.DELIMITER_CSV+value.toString()+Constants.DELIMITER_CSV);
						}
					}
					else
					{
						writer.append(Constants.DELIMITER_CSV+Constants.EMPTY+Constants.DELIMITER_CSV);
					}					
					if (j<headers.size()-1){
						writer.append(Constants.SEPARATOR);
					}
					j++;
				}
				writer.append(Constants.RETURN);
			}
			writer.flush();
			writer.close();
		}
		getFacesContext().responseComplete();
	}
	
	protected void writeHeaderCsv(Writer writer) throws IntrospectionException,IOException{
		String header="";
		int i = 0;
		for (Entry<String, PropertyDescriptor> entry : headers.entrySet()) {
			header=entry.getValue().getName();
			header=converter.desCamelCase(header);
			writer.append(Constants.DELIMITER_CSV+header+Constants.DELIMITER_CSV);
			if (i<(headers.size()-1))
			{
				writer.append(Constants.SEPARATOR);
			}
			else
			{
				writer.append(Constants.RETURN);
			}
			i++;
		}
	}
	
	/************************************************************** Metodos de de CSV **************************************************************/
	
	
	
	/************************************************************** Metodos de de PDF **************************************************************/
	
	public void pdfExporter(List<T> results,String fileName) throws DocumentException, IOException, IntrospectionException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException{
		HttpServletResponse response = getResponse();
		
		Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
		
        document.open();  
        document.setPageSize(PageSize.A4);  
		
		PdfPTable pdfTable = new PdfPTable(headers.size());
    	Font font = FontFactory.getFont(FontFactory.TIMES, Constants.UTF_8);
    	Font headerFont = FontFactory.getFont(FontFactory.TIMES, Constants.UTF_8, Font.DEFAULTSIZE, Font.BOLD);    	
    	
    	writeHeaderPdf(pdfTable, headerFont);
    	
    	String header="";
		for (int i=0; i<results.size(); i++){
			proxy.setBean(results.get(i));
			for (Entry<String, PropertyDescriptor> entry : headers.entrySet()) {
				header=entry.getValue().getName();
				Object value = proxy.get(header);
				if(value != null)
				{
					if(value instanceof Date)
					{
						pdfTable.addCell(new Paragraph(Constants.getDateString((Date)value), font));
					}
					else
					{
						pdfTable.addCell(new Paragraph(value.toString(), font));
					}
				}
				else
				{
					pdfTable.addCell(new Paragraph(Constants.EMPTY, font));
				}					
			}			
		}
		
		document.add(pdfTable);
		
		document.close();
		
        setHeaderResponse(response, Constants.CONTENT_TYPE_PDF, fileName);
        response.setContentLength(baos.size());
        
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        getFacesContext().responseComplete();
	}
	
	protected void writeHeaderPdf(PdfPTable pdfTable, Font font) throws IntrospectionException,IOException{
		String header="";
		for (Entry<String, PropertyDescriptor> entry : headers.entrySet()) {
			header=entry.getValue().getName();
			header=converter.desCamelCase(header);
			pdfTable.addCell(new Paragraph(header, font));
		}
	}
	
	/************************************************************** Metodos de de PDF **************************************************************/
	
	
	/************************************************************** Metodos de de XML **************************************************************/
	
	
	public void xmlExporter(List<T> results,String fileName) throws DocumentException, IOException, IntrospectionException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException{		
		HttpServletResponse response = getResponse();
		
		setHeaderResponse(response, Constants.CONTENT_TYPE_XML, fileName);
		
		OutputStream os = response.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, Constants.UTF_8);
		
		PrintWriter writer = new PrintWriter(osw);	
		
		writer.write("<?xml version=\"1.0\"?>\n");
    	writer.write("<Permissions>\n");
    	 
    	String header="";
    	
    	for (int i=0; i<results.size(); i++){
             writer.write("\t<permission>\n");
             proxy.setBean(results.get(i));
 			 for (Entry<String, PropertyDescriptor> entry : headers.entrySet()) {
 				header=entry.getValue().getName();
 				Object value = proxy.get(header);
 				String tag = header.toLowerCase();
 				writer.write("\t\t<" + tag + ">");
 				if(value != null)
 				{
 					if(value instanceof Date)
 					{
 						writer.write(Constants.getDateString((Date)value));
 					}
 					else
 					{
 						writer.write(value.toString());
 					}
 				}
 				else
 				{
 					writer.write(Constants.EMPTY);
 				}
 				writer.write("</" + tag + ">\n");
 			}
            writer.write("\t</permission>\n");
        }
		
    	writer.write("<Permissions>\n");
    	
    	writer.flush();
		writer.close();

		 getFacesContext().responseComplete();
	}
	
	/************************************************************** Metodos de de XML **************************************************************/
	
	
	/************************************************************** Metodos de de XLS **************************************************************/
	
	public void xlsExporter(List<T> results, String fileName) throws DocumentException, IOException, IntrospectionException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException{
		HttpServletResponse response = getResponse();
		
		Workbook wb = new HSSFWorkbook();
    	Sheet sheet = wb.createSheet();		  	
    	
    	writeHeaderXls(sheet, 0);
    	
    	String header="";
    	int sheetRowIndex = 1;
    	Row row;
    	int j = 0;
		for (int i=0; i<results.size(); i++){
			
			proxy.setBean(results.get(i));			
			row = sheet.createRow(sheetRowIndex++);
			j = 0;
			for (Entry<String, PropertyDescriptor> entry : headers.entrySet()) {
				header=entry.getValue().getName();
				Object value = proxy.get(header);
				Cell cell = row.createCell(j);
				if(value != null)
				{
					if(value instanceof Date)
					{
						 cell.setCellValue(new HSSFRichTextString(Constants.getDateString((Date)value)));
					}
					else
					{
						cell.setCellValue(new HSSFRichTextString(value.toString()));
					}
				}
				else
				{
					cell.setCellValue(new HSSFRichTextString(Constants.EMPTY));
				}
				j++;
			}			
		}
		
		setHeaderResponse(response, Constants.CONTENT_TYPE_XLS, fileName);

        wb.write(response.getOutputStream());
        getFacesContext().responseComplete();    
	}
	
	protected void writeHeaderXls(Sheet sheet, int rowIndex) throws IntrospectionException,IOException{
		String header="";
		Row rowHeader = sheet.createRow(rowIndex);
		int i = 0;
		for (Entry<String, PropertyDescriptor> entry : headers.entrySet()) {
			header=entry.getValue().getName();
			header=converter.desCamelCase(header);
			Cell cell = rowHeader.createCell(i);
			cell.setCellValue(new HSSFRichTextString(header));
			i++;
		}
	}
	
	/************************************************************** Metodos de de XLS **************************************************************/
	
}
