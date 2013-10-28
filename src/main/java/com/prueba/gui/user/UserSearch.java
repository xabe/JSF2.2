package com.prueba.gui.user;

import java.beans.IntrospectionException;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import com.prueba.gui.search.Search;
import com.prueba.model.user.UserExample;
import com.prueba.model.user.UserExample.Criteria;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;
import com.prueba.util.introspector.BeanProxy;

public class UserSearch extends Search<UserExample> {
	private static final long serialVersionUID = 1L;
	//Campos para la busqueda
	public static String ATTEMPTSLOGIN = "andAttemptsLoginLike";
	public static String BLOCKED = "andBlockedLike";
	public static String DATELASTLOGIN = "andDateLastLoginLike";
	public static String DATELASTPASSWORD = "andDateLastPasswordLike";
	public static String EMAIL = "andEmailLike";
	public static String ENABLE = "andEnableLike";
	public static String ID = "andIdLike";
	public static String NAME = "andNameLike";
	public static String PASSWORD = "andPasswordLike";
	public static String PICTURE = "andPictureLike";
	public static String SURNAME1 = "andSurname1Like";
	public static String SURNAME2 = "andSurname2Like";
	public static String TELEPHONE = "andTelephoneLike";
	public static String USERNAME = "andUsernameLike";

	public UserSearch(){
		try
    	{
			UserExample example = new UserExample();
    		beanProxy = new BeanProxy(example.createCriteria());
    	}catch (IntrospectionException e) {
    		LOGGER.error("Error al crear el beanIntrospector en search User: "+e.getMessage());
    	}
	}
	
	
	@Override
	protected void createFiels() {
		fields = new ArrayList<SelectItem>();
		fields.add(new SelectItem(Constants.ALL_CRITERIA, JSFUtils.getStringFromBundle(Constants.GET_ALL_FILEDS),Constants.GET_ALL_FILEDS));      
		  	fields.add(new SelectItem(ATTEMPTSLOGIN,JSFUtils.getStringFromBundle("user.attemptslogin"),"user.attemptslogin")); 
		  	fields.add(new SelectItem(BLOCKED,JSFUtils.getStringFromBundle("user.blocked"),"user.blocked")); 
		  	fields.add(new SelectItem(DATELASTLOGIN,JSFUtils.getStringFromBundle("user.datelastlogin"),"user.datelastlogin")); 
		  	fields.add(new SelectItem(DATELASTPASSWORD,JSFUtils.getStringFromBundle("user.datelastpassword"),"user.datelastpassword")); 
		  	fields.add(new SelectItem(EMAIL,JSFUtils.getStringFromBundle("user.email"),"user.email")); 
		  	fields.add(new SelectItem(ENABLE,JSFUtils.getStringFromBundle("user.enable"),"user.enable")); 
		  	fields.add(new SelectItem(ID,JSFUtils.getStringFromBundle("user.id"),"user.id")); 
		  	fields.add(new SelectItem(NAME,JSFUtils.getStringFromBundle("user.name"),"user.name")); 
		  	fields.add(new SelectItem(PASSWORD,JSFUtils.getStringFromBundle("user.password"),"user.password")); 
		  	fields.add(new SelectItem(PICTURE,JSFUtils.getStringFromBundle("user.picture"),"user.picture")); 
		  	fields.add(new SelectItem(SURNAME1,JSFUtils.getStringFromBundle("user.surname1"),"user.surname1")); 
		  	fields.add(new SelectItem(SURNAME2,JSFUtils.getStringFromBundle("user.surname2"),"user.surname2")); 
		  	fields.add(new SelectItem(TELEPHONE,JSFUtils.getStringFromBundle("user.telephone"),"user.telephone")); 
		  	fields.add(new SelectItem(USERNAME,JSFUtils.getStringFromBundle("user.username"),"user.username")); 
		}
	
	
	
	
	@Override
	public UserExample createExampleSearch() {
		UserExample  example = new UserExample();
		Criteria criteria = example.createCriteria();
		String textToFind =  getTextSearch();
		if(textToFind.length() > 0 && getCriteriaSelection().equalsIgnoreCase(Constants.ALL_CRITERIA))
		{
						
					criteria.andAttemptsLoginLike(textToFind);
							
					example.or(example.createCriteria().andBlockedLike(textToFind));
							
					example.or(example.createCriteria().andDateLastLoginLike(textToFind));
							
					example.or(example.createCriteria().andDateLastPasswordLike(textToFind));
							
					example.or(example.createCriteria().andEmailLike(textToFind));
							
					example.or(example.createCriteria().andEnableLike(textToFind));
							
					example.or(example.createCriteria().andIdLike(textToFind));
							
					example.or(example.createCriteria().andNameLike(textToFind));
							
					example.or(example.createCriteria().andPasswordLike(textToFind));
							
					example.or(example.createCriteria().andPictureLike(textToFind));
							
					example.or(example.createCriteria().andSurname1Like(textToFind));
							
					example.or(example.createCriteria().andSurname2Like(textToFind));
							
					example.or(example.createCriteria().andTelephoneLike(textToFind));
							
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
