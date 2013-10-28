package com.prueba.gui.permission;

import java.beans.IntrospectionException;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import com.prueba.gui.search.Search;
import com.prueba.model.permission.PermissionExample;
import com.prueba.model.permission.PermissionExample.Criteria;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;
import com.prueba.util.introspector.BeanProxy;

public class PermissionSearch extends Search<PermissionExample> {
	private static final long serialVersionUID = 1L;
	//Campos para la busqueda
	public static String ID = "andIdLike";
	public static String NAME = "andNameLike";

	public PermissionSearch(){
		try
    	{
			PermissionExample example = new PermissionExample();
    		beanProxy = new BeanProxy(example.createCriteria());
    	}catch (IntrospectionException e) {
    		LOGGER.error("Error al crear el beanIntrospector en search Permission: "+e.getMessage());
    	}
	}
	
	
	@Override
	protected void createFiels() {
		fields = new ArrayList<SelectItem>();
		fields.add(new SelectItem(Constants.ALL_CRITERIA, JSFUtils.getStringFromBundle(Constants.GET_ALL_FILEDS),Constants.GET_ALL_FILEDS));      
		  	fields.add(new SelectItem(ID,JSFUtils.getStringFromBundle("permission.id"),"permission.id")); 
		  	fields.add(new SelectItem(NAME,JSFUtils.getStringFromBundle("permission.name"),"permission.name")); 
		}
	
	
	
	
	@Override
	public PermissionExample createExampleSearch() {
		PermissionExample  example = new PermissionExample();
		Criteria criteria = example.createCriteria();
		String textToFind =  getTextSearch();
		if(textToFind.length() > 0 && getCriteriaSelection().equalsIgnoreCase(Constants.ALL_CRITERIA))
		{
						
					criteria.andIdLike(textToFind);
							
					example.or(example.createCriteria().andNameLike(textToFind));
					}
		else if(textToFind.length() > 0)
		{
			try
			{
				beanProxy.setBean(criteria);
				beanProxy.invoke(getCriteriaSelection(), new Class<?>[] {String.class}, new Object[]{textToFind});
			}catch (IllegalAccessException e) {
				LOGGER.error("Error al search : "+e.getMessage());
			}catch (Exception e) {
				LOGGER.error("Error al search : "+e.getMessage());
			}
		}	
		return example;	
	}
}
