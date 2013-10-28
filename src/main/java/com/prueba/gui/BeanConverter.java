package com.prueba.gui;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("beanConverter")
public class BeanConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		String [] values = value.split(":");
		return new BeanItem(values[0], values[1]);
	}
	
	
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return value.toString();
	}
}
