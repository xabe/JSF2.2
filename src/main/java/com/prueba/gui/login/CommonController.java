package com.prueba.gui.login;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

public abstract class CommonController {
	private String codeSecureAction;
	
	/* Generater Random  CSFR */
	
	public String generateRandom() {
		codeSecureAction = JSFUtils.getNewRandomString();
		putSession(Constants.CSRF_SESSION, codeSecureAction);
		return null;
	}
	
	public void putSession(String key,Object value){
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) ctx.getSession(false);
		session.setAttribute(key, value);
	}
	
	public Object getSession(String key){
		Object result;
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) ctx.getSession(false);
		result = session.getAttribute(key);
		return result;
	}
	
	public String getParam(String key){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
	}
	
	public boolean getUpdateRandom(){
		return true;
	}
	
	public String getCodeSecureAction() {
		return codeSecureAction;
	}

	public void setCodeSecureAction(String codeSecureAction) {
		this.codeSecureAction = codeSecureAction;
	}

}
