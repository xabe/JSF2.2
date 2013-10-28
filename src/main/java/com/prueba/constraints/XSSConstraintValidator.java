package com.prueba.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class XSSConstraintValidator implements ConstraintValidator<XSS, String> {
	private static String EMPTY_STRING = "";
	
	@Override
	public void initialize(XSS arg0) {
		
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext ctxt) {
		if (value == null || value.length() == 0) {
	           return true;
	    }
		if (!EMPTY_STRING.equalsIgnoreCase(value)) {
			return isValid(value);
		}
		return true;
	}
	
	public boolean isValid(String t){

		String text = null;
		
		if (t != null) {
			text = t.trim();
		} else {
			return true;
		}
		boolean result = true;
		if (text.indexOf(';') >= 0) {
			result = false;
		} else if (text.indexOf('\'') >= 0) {
			result = false;
		} else if (text.indexOf('"') >= 0) {
			result = false;
		} else if (text.indexOf('|') >= 0) {
			result = false;
		} else if (text.indexOf('<') >= 0) {
			result = false;
		} else if (text.indexOf('>') >= 0) {
			result = false;
		} else if (text.indexOf('=') >= 0) {
			result = false;
		} else if (text.indexOf('(') >= 0) {
			result = false;
		} else if (text.indexOf(')') >= 0) {
			result = false;
		} else if (text.indexOf('*') >= 0) {
			result = false;
		} else if (text.indexOf('&') >= 0) {
			result = false;
		} else if (text.indexOf('%') >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf("SELECT") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf("UPDATE") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf("INSERT") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf("DELETE") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf("DROP") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf("CREATE") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf("SCRIPT") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf(" OR ") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf(" AND ") >= 0) {
			result = false;
		} else if (text.toUpperCase().indexOf(" LIKE ") >= 0) {
			result = false;
		} else if (text.toUpperCase().trim().length() == 0) {
			result = false;
		} 
		return result;
	}


}