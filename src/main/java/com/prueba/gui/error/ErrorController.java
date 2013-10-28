package com.prueba.gui.error;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;
import com.prueba.util.exceptions.ErrorExceptionHandler;

@ManagedBean(name="errorController")
@ViewScoped
public class ErrorController implements Serializable {
	private static final long serialVersionUID = 1L;
    private String fecha;
    private String accion;
    private String exception;
    private String mensaje;
    private String code;
    private String previousURL = "";
    
    public String getFecha() {
    	return fecha;
	}
    
    public String getAccion(){
    	return accion;
    }
    
    public String getException(){
    	return exception;
    }
    
    public String getMensaje(){
    	return mensaje;
    }
    
    public String getCode(){
    	return code;
    }
    
    public String getPreviousURL() {
		return previousURL;
	}

    public ErrorController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        fecha = Constants.getDateString(new Date());
        
        if(requestMap.get("SPRING_SECURITY_403_EXCEPTION") != null) {        	
        	accion = JSFUtils.getStringFromBundle("error.login");
        	exception = JSFUtils.getStringFromBundle("error.exception");
        	mensaje = JSFUtils.getStringFromBundle("error.message");
        	code = JSFUtils.getStringFromBundle("error.code");
        	
        	SecurityContext securityContext = SecurityContextHolder.getContext();
        	securityContext.setAuthentication(null);
        	((HttpSession)context.getExternalContext().getSession(false)).invalidate();
        
        } else {
        	accion = requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_REQUEST_URI) != null ? requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_REQUEST_URI).toString() : "";
        	exception = requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_EXCEPTION) != null ? requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_EXCEPTION).toString() : "";
        	mensaje = requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_MESSAGE) != null ? requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_MESSAGE).toString() : "";
        	code = requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_STATUS_CODE) != null ? requestMap.get(ErrorExceptionHandler.ATTRIBUTE_ERROR_STATUS_CODE).toString() : "";
        }        
          
        
        previousURL = context.getExternalContext().getRequestHeaderMap().get("referer");
        
        if(previousURL == null) {
        	previousURL = "../commons/index.faces";
        } 
    }
	
}
