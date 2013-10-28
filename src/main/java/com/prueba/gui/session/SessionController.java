package com.prueba.gui.session;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prueba.model.userLogged.UserLogged;
import com.prueba.service.security.SecurityService;

@ManagedBean(name="sessionController")
@SessionScoped
public class SessionController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class);
	private Locale locale;
	private Date date;
	@ManagedProperty(value="#{securityService}")
	private SecurityService securityService;
	private TimeZone timeZone;
	private static final String FORMAT_DATE = " dd/MM/yyyy HH:mm:ss";
	
	
	public SessionController(){
		date = new Date();
		locale = new Locale("es");
		timeZone = TimeZone.getDefault();
	}
	
	public UserLogged getUserLogged() {
		UserLogged logged = null;
		if(this.securityService != null)
		{
			logged = this.securityService.getUserlogged();
		}
		return logged;
	}	
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}
	
	public String getLanguage() {
        return getLocaleUse().getLanguage();
    }
		
	public Locale getLocaleUse(){
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getViewRoot().getLocale();
	}
	 
	public Date getDate() {
		return (Date) date.clone();
	}
		
	/**
	 * Cambia el lenguaje al especificado como parametro
	 * @param language la aplicacion esta preparada para multiples
	 */
	public String changeLanguage(String language){
		FacesContext context = FacesContext.getCurrentInstance();
		setLocale(new Locale(language));
		context.getViewRoot().setLocale(getLocale());
		return null;
	}
	
	public String getFormatoFecha(){
		return FORMAT_DATE;
	}
	
	/**
	*Eliminar la sesión al cerrar el navegador
	*/
	public String logOut() throws Exception{
		closeSession();
		return "logout";
	}
	
	private boolean closeSession() {
		try {
			ExternalContext ctx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ctx.getSession(false);
			session.invalidate();
			SecurityContextHolder.getContext().setAuthentication(null);
			SecurityContextHolder.clearContext();
			LOGGER.info("Info close session");
			return true;
		} catch (IllegalStateException e) {
			LOGGER.error("Error close session");
			return false;
		}
	}
	
}
