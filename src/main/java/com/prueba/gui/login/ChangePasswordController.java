package com.prueba.gui.login;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.gui.MessageManager;
import com.prueba.model.userLogged.UserLogged;
import com.prueba.service.security.SecurityService;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

@ManagedBean(name="changePasswordController")
@ViewScoped
public class ChangePasswordController extends CommonController implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordController.class);
	private String newPassword;
	private String oldPassword;
	private String confirmationPassword;
	@ManagedProperty(value="#{securityService}") 
	private SecurityService securityService;
	private UserLogged userLogged;
	private static final String ERROR_LOG = "Error when trying to change password: By User ";
	
	public ChangePasswordController() {
		generateRandom();
	}
	
	@PostConstruct
	public void init(){
		this.userLogged = securityService.getUserlogged();
	}

	public String changePassword(){
		if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_CHANGE_PASSWORD)))
		{
			generateRandom();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
		}
		generateRandom();
		if (newPassword.equals(confirmationPassword)) 
		{
			switch (securityService.changePassword(oldPassword, newPassword)) {
			case Constants.OPTION_ONE: LOGGER.debug(ERROR_LOG+userLogged.getUsername());
					 MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("error.old.password.distint"));
				 	 break;
			case Constants.OPTION_TWO: LOGGER.debug(ERROR_LOG+userLogged.getUsername());
					 MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("error.length.password")+" "+securityService.getMinLengthPassword());
		 	         break;
		 	 
			case Constants.OPTION_THREE: LOGGER.debug(ERROR_LOG+userLogged.getUsername());
					 MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("error.character.password"));
					 break;
			default: LOGGER.debug("Password successfully changed: By User "+userLogged.getUsername());
					 MessageManager.createInfoMessage(null,JSFUtils.getStringFromBundle("succes.password.change"));
					 break;
			}
		} 
		else 
		{
			LOGGER.debug("Error when trying to change password: By User "+userLogged.getUsername());
			MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("error.password.distint"));
		}
		return null;
	}		
	
	/* Method GET and SET attributes */
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	public String getConfirmationPassword() {
		return confirmationPassword;
	}

	public void setConfirmationPassword(String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
