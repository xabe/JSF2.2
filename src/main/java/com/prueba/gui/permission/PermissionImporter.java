package com.prueba.gui.permission;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.xml.sax.SAXException;

import com.csvreader.CsvReader;
import com.prueba.gui.importer.ConstraintImportTable;
import com.prueba.gui.importer.Import;
import com.prueba.gui.importer.Table;
import com.prueba.model.permission.Permission;
import com.prueba.service.permission.PermissionService;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;
import com.prueba.util.introspector.BeanIntrospector;
import com.prueba.util.introspector.BeanProxy;


public class PermissionImporter extends Import<Permission, PermissionService>{
	private static final long serialVersionUID = 1L;	
	
	public PermissionImporter() throws Exception {
		try
    	{
    		propertyDescriptors= BeanIntrospector.getProperties(Permission.class);
    		beanProxy = new BeanProxy(new Permission());    		
    		Annotation annotation = Permission.class.getAnnotation(ConstraintImportTable.class);
    		if (annotation == null) 
    		{

    			LOGGER.error("No tiene anotaciones el modelo de Permission");
    			throw new Exception("No tiene anotaciones el modelo de Permission");
    		}
    		else
    		{
    			ConstraintImportTable table = (ConstraintImportTable) annotation;
    			tableInfo = new Table(table.type(), table.keys(), table.ignoreErrors());
    		}
    		
    		init(Permission.class.getDeclaredFields());
    		
    	}catch (IntrospectionException e) {
    		LOGGER.error("Error al crear el beanProxy PermissionImporter: "+e.getMessage());
		}
	}
	
	protected void readData(CsvReader reader,String[] headers,String infoProcess) throws IOException, IllegalAccessException, NoSuchMethodException, SAXException, IntrospectionException, NoSuchFieldException, InvocationTargetException{
		String header="";
		PropertyDescriptor propertyDescriptor;
		
		String type = "";
		String value = "";
		String info = "";
		Object field = null;
		
		int i=0;
		int row=2;
		boolean rowError=false;
		boolean tableError=false;
		String error = "";
		while (reader.readRecord() && !tableError) {	
			i=0;
			rowError=false;
			beanProxy.setBean(new Permission());
			while ((i<headers.length)&& !rowError){
				header = headers[i];
				try 
				{
					propertyDescriptor=getPropertyElement(header);
					if(propertyDescriptor != null)
					{
						type = propertyDescriptor.getPropertyType().toString();
						value = reader.get(header);
						info = validateField(value,header);
						if(info.contains(JSFUtils.getStringFromBundle(Constants.ERROR)))
						{
							rowError=true;
							error = errorLog(row, header, value, info);
						}	
						else
						{
							field = getCell(value,type);
							beanProxy.set(propertyDescriptor.getName(), field);
						}
					}
				} catch (Exception e) {
					error = errorLog(row, header, value, e);
					rowError=true;
				}
				if (!rowError){
					i++;
				}
			}	
			
			//Comprobamos que no tiene error
			if(!rowError)
			{
				if (tableInfo.getType().equalsIgnoreCase(Constants.TYPE_IMPORT_TOTAL))
				{
					service.add((Permission) beanProxy.getBean());
				}
				else
				{
					//Si la importacion es incremental
					String[] keys= new String[0];
					if(haveIdfilling(tableInfo.getKeys(),(Permission) beanProxy.getBean())){
						keys = tableInfo.getKeys();
					}
					service.updateOrInsert((Permission) beanProxy.getBean(),keys);
				}
			}
			else if(!tableInfo.isIgnoreError()){
				tableError=true;
			}
			else
			{
				importLog.append(error);
				importLog.append(Constants.CRLF);
			}
			row++;		
		}
	}
}