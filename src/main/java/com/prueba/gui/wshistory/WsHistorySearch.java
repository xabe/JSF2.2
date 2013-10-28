package com.prueba.gui.wshistory;

import java.beans.IntrospectionException;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import com.prueba.gui.search.Search;
import com.prueba.model.wshistory.WsHistoryExample;
import com.prueba.model.wshistory.WsHistoryExample.Criteria;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;
import com.prueba.util.introspector.BeanProxy;

public class WsHistorySearch extends Search<WsHistoryExample> {
	private static final long serialVersionUID = 1L;
	//Campos para la busqueda
	public static String ID = "andIdLike";
	public static String IP = "andIpLike";
	public static String NUMBEROFELEMENTS = "andNumberofelementsLike";
	public static String RESPONSEFORMAT = "andResponseformatLike";
	public static String SERVICE = "andServiceLike";
	public static String SERVICENAME = "andServicenameLike";
	public static String SERVICEREQUESTDATE = "andServicerequestdateLike";
	public static String USERID = "andUseridLike";
	public static String USERNAME = "andUsernameLike";

	public WsHistorySearch(){
		try
    	{
			WsHistoryExample example = new WsHistoryExample();
    		beanProxy = new BeanProxy(example.createCriteria());
    	}catch (IntrospectionException e) {
    		LOGGER.error("Error al crear el beanIntrospector en search WsHistory: "+e.getMessage());
    	}
	}
	
	
	@Override
	protected void createFiels() {
		fields = new ArrayList<SelectItem>();
		fields.add(new SelectItem(Constants.ALL_CRITERIA, JSFUtils.getStringFromBundle(Constants.GET_ALL_FILEDS),Constants.GET_ALL_FILEDS));      
		  	fields.add(new SelectItem(ID,JSFUtils.getStringFromBundle("wshistory.id"),"wshistory.id")); 
		  	fields.add(new SelectItem(IP,JSFUtils.getStringFromBundle("wshistory.ip"),"wshistory.ip")); 
		  	fields.add(new SelectItem(NUMBEROFELEMENTS,JSFUtils.getStringFromBundle("wshistory.numberofelements"),"wshistory.numberofelements")); 
		  	fields.add(new SelectItem(RESPONSEFORMAT,JSFUtils.getStringFromBundle("wshistory.responseformat"),"wshistory.responseformat")); 
		  	fields.add(new SelectItem(SERVICE,JSFUtils.getStringFromBundle("wshistory.service"),"wshistory.service")); 
		  	fields.add(new SelectItem(SERVICENAME,JSFUtils.getStringFromBundle("wshistory.servicename"),"wshistory.servicename")); 
		  	fields.add(new SelectItem(SERVICEREQUESTDATE,JSFUtils.getStringFromBundle("wshistory.servicerequestdate"),"wshistory.servicerequestdate")); 
		  	fields.add(new SelectItem(USERID,JSFUtils.getStringFromBundle("wshistory.userid"),"wshistory.userid")); 
		  	fields.add(new SelectItem(USERNAME,JSFUtils.getStringFromBundle("wshistory.username"),"wshistory.username")); 
		}
	
	
	
	
	@Override
	public WsHistoryExample createExampleSearch() {
		WsHistoryExample  example = new WsHistoryExample();
		Criteria criteria = example.createCriteria();
		String textToFind =  getTextSearch();
		if(textToFind.length() > 0 && getCriteriaSelection().equalsIgnoreCase(Constants.ALL_CRITERIA))
		{
						
					criteria.andIdLike(textToFind);
							
					example.or(example.createCriteria().andIpLike(textToFind));
							
					example.or(example.createCriteria().andNumberofelementsLike(textToFind));
							
					example.or(example.createCriteria().andResponseformatLike(textToFind));
							
					example.or(example.createCriteria().andServiceLike(textToFind));
							
					example.or(example.createCriteria().andServicenameLike(textToFind));
							
					example.or(example.createCriteria().andServicerequestdateLike(textToFind));
							
					example.or(example.createCriteria().andUseridLike(textToFind));
							
					example.or(example.createCriteria().andUsernameLike(textToFind));
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
