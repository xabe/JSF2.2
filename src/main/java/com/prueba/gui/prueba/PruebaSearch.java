package com.prueba.gui.prueba;

import java.beans.IntrospectionException;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import com.prueba.gui.search.Search;
import com.prueba.model.prueba.PruebaExample;
import com.prueba.model.prueba.PruebaExample.Criteria;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;
import com.prueba.util.introspector.BeanProxy;

public class PruebaSearch extends Search<PruebaExample> {
	private static final long serialVersionUID = 1L;
	//Campos para la busqueda
	public static String FECHA = "andFechaLike";
	public static String ID = "andIdLike";
	public static String NOMBRE = "andNombreLike";
	public static String NUMERO = "andNumeroLike";

	public PruebaSearch(){
		try
    	{
			PruebaExample example = new PruebaExample();
    		beanProxy = new BeanProxy(example.createCriteria());
    	}catch (IntrospectionException e) {
    		LOGGER.error("Error al crear el beanIntrospector en search Prueba: "+e.getMessage());
    	}
	}
	
	
	@Override
	protected void createFiels() {
		fields = new ArrayList<SelectItem>();
		fields.add(new SelectItem(Constants.ALL_CRITERIA, JSFUtils.getStringFromBundle(Constants.GET_ALL_FILEDS),Constants.GET_ALL_FILEDS));      
		  	fields.add(new SelectItem(FECHA,JSFUtils.getStringFromBundle("prueba.fecha"),"prueba.fecha")); 
		  	fields.add(new SelectItem(ID,JSFUtils.getStringFromBundle("prueba.id"),"prueba.id")); 
		  	fields.add(new SelectItem(NOMBRE,JSFUtils.getStringFromBundle("prueba.nombre"),"prueba.nombre")); 
		  	fields.add(new SelectItem(NUMERO,JSFUtils.getStringFromBundle("prueba.numero"),"prueba.numero")); 
		}
	
	
	
	
	@Override
	public PruebaExample createExampleSearch() {
		PruebaExample  example = new PruebaExample();
		Criteria criteria = example.createCriteria();
		String textToFind =  getTextSearch();
		if(textToFind.length() > 0 && getCriteriaSelection().equalsIgnoreCase(Constants.ALL_CRITERIA))
		{
						
					criteria.andFechaLike(textToFind);
							
					example.or(example.createCriteria().andIdLike(textToFind));
							
					example.or(example.createCriteria().andNombreLike(textToFind));
							
					example.or(example.createCriteria().andNumeroLike(textToFind));
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
