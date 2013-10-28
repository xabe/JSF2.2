package com.prueba.util;

import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.prueba.model.userLogged.UserLogged;
import com.prueba.service.security.SecurityService;

@Component
public class ListenerAutentificacion implements ApplicationListener<ApplicationEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListenerAutentificacion.class);
	
	@Autowired
	private SecurityService securityService;


	public void onApplicationEvent(final ApplicationEvent e) {
	
		//if (e instanceof AbstractAuthenticationEvent) {
			//if (e instanceof InteractiveAuthenticationSuccessEvent) {
				// handle InteractiveAuthenticationSuccessEvent
			//} else if (e instanceof AbstractAuthenticationFailureEvent) {
				// handle AbstractAuthenticationFailureEvent
			//} else if (e instanceof AuthenticationSuccessEvent) {
				// handle AuthenticationSuccessEvent
			//} else if (e instanceof AuthenticationSwitchUserEvent) {
				// handle AuthenticationSwitchUserEvent
			//} else {
				// handle other authentication event
			//}
		//} else if (e instanceof AbstractAuthorizationEvent) {
			// handle authorization event
		//}
		if (e instanceof AbstractAuthenticationEvent && e instanceof AbstractAuthenticationFailureEvent) {
			AbstractAuthenticationFailureEvent event = (AbstractAuthenticationFailureEvent) e;
			String username = event.getAuthentication().getName();
			if(username != null && username.length() > 0){
				securityService.attemptsIncreaseLogin(username);
			}
			LOGGER.error("acceso fallido al sistema "+username);
		}
		if(e instanceof AuthenticationSuccessEvent)
		{
			//Aumentamos la variable de numero de usuarios
			if(((AuthenticationSuccessEvent) e).getAuthentication().getPrincipal() instanceof UserLogged)
			{
				UserLogged userLogin = (UserLogged) ((AuthenticationSuccessEvent) e).getAuthentication().getPrincipal();
				if(userLogin != null)
				{
					securityService.zeroattemptsLogin(userLogin.getUsername());
				}
				if(FacesContext.getCurrentInstance() != null)
				{
					String authString = userLogin.getUsername() + ":" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Constants.PASSWORD_DECODE);
					byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
					String authStringEnc = new String(authEncBytes);					
					
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.PASSWORD_REST, "Basic " + authStringEnc);
					LOGGER.info("acceso correcto al sistema "+Constants.getIp()+" "+userLogin.getUsername());
				}
				else
				{
					LOGGER.info("acceso correcto al sistema rest "+userLogin.getUsername());
				}
			}
		}
		
	}
}