package com.prueba.gui.login;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.gui.MessageManager;
import com.prueba.model.user.User;
import com.prueba.service.mail.MailService;
import com.prueba.service.security.SecurityService;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

@ManagedBean(name="registrationController")
@ViewScoped
public class RegistrationController extends CommonController implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RememberPasswordController.class);
	
	@ManagedProperty(value="#{securityService}") 
	private SecurityService securityService;
	@ManagedProperty(value="#{mailService}") 
	private MailService mailService;
	
	private String email;
	private String user;
	private String password;
	
	
	public RegistrationController() {
		generateRandom();
	}
	
	private boolean dataCheckRequired(){
		boolean required = true;
		if(getPassword() == null || getPassword().trim().length() == 0)
		{
			MessageManager.createErrorMessage("formRegistration:password",JSFUtils.getStringFromBundle(Constants.ERROR_BLANK));
			required = false;
		}				
		if(!securityService.checkPasswordLenght(getPassword()))
		{
			MessageManager.createErrorMessage("formRegistration:password",JSFUtils.getStringFromBundle("error.length.password")+" "+securityService.getMinLengthPassword());
			required = false;
		}
		if(!securityService.checkPasswordCharacter(getPassword()))
		{
			MessageManager.createErrorMessage("formRegistration:password",JSFUtils.getStringFromBundle("error.character.password"));
			required = false;
		}
		if(getEmail() == null || getEmail().trim().length() == 0)
		{
			MessageManager.createErrorMessage("formRegistration:email",JSFUtils.getStringFromBundle(Constants.ERROR_BLANK));
			required = false;
		}
		if(!securityService.checkEmail(getEmail()))
		{
			MessageManager.createErrorMessage("formRegistration:email",JSFUtils.getStringFromBundle("error.email"));
			required = false;
		}
		return required;
	}
	
	public String registration(){
		if(!dataCheckRequired())
		{
			generateRandom();
			return null;
		}
		if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_REGISTRATION)))
		{
			generateRandom();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
		}
		generateRandom();
		
		User user = new User();
		user.setPassword(password);
		user.setEmail(email);
		user.setUsername(this.user);
		user.setAttemptsLogin(Constants.getCero(user.getAttemptsLogin()));
		user.setBlocked(Constants.getFalse(user.getBlocked()));
		user.setEnable(Constants.getTrue(user.getId()));
		user.setDateLastPassword(new Timestamp(new Date().getTime()));		
		String password = user.getPassword();
		user.setPassword(securityService.encodePassword(password));
		try
		{
			securityService.addUser(user);
			securityService.addGroupUser(user.getId().toString());
			String text = JSFUtils.getStringFromBundle("user.text.new");
			mailService.send(user.getEmail(), JSFUtils.getStringFromBundle("user.subject.new"), MessageFormat.format(text,user.getUsername(),password));
			MessageManager.createInfoMessage(null,JSFUtils.getStringFromBundle(Constants.SUCCES_CREATE));
		}catch(Exception e){
			LOGGER.error("Error al hacer un registro de un usuario : "+e.getMessage());
		}
		return null;
	}


	/* Method GET and SET attributes */
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	
}
