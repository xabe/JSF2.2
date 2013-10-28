package com.prueba.gui.login;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.gui.MessageManager;
import com.prueba.model.user.User;
import com.prueba.model.user.UserExample;
import com.prueba.model.user.UserExample.Criteria;
import com.prueba.service.mail.MailService;
import com.prueba.service.security.SecurityService;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

@ManagedBean(name="rememberPasswordController")
@ViewScoped
public class RememberPasswordController implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RememberPasswordController.class);
	private String email;
	@ManagedProperty(value="#{securityService}") 
	private SecurityService securityService;
	@ManagedProperty(value="#{mailService}") 
	private MailService mailService;

	public String rememberPassword(){
		if(email == null || email.trim().length() == 0)
		{
			MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle(Constants.ERROR_BLANK));
			return null;
		}
		if(!securityService.checkEmail(email))
		{
			MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("error.email"));
			return null;
		}
		UserExample userExample = new UserExample();
		Criteria criteria = userExample.createCriteria();
		criteria.andEmailEqualTo(email);
		List<User> users = securityService.getSelectExample(userExample);
		if(users.size() > 0)
		{
			User user = users.get(0);
			String newPassword = securityService.generatePassword();
			String newEncode = securityService.encodePassword(newPassword);
			try
			{
				user.setPassword(newEncode);
				user.setAttemptsLogin(Constants.getCero(user.getAttemptsLogin()));
				user.setBlocked(Constants.getFalse(user.getBlocked()));
				user.setEnable(Constants.getTrue(user.getId()));
				securityService.updateUser(user);
				String text = JSFUtils.getStringFromBundle("text.generater.new.password");
				mailService.send(user.getEmail(), JSFUtils.getStringFromBundle("subject.generater.new.password"), MessageFormat.format(text,newPassword));
				MessageManager.createInfoMessage(null,JSFUtils.getStringFromBundle("succes.user.update.email"));
			}
			catch (Exception e) {
				LOGGER.error("Error retrieving user: "+email, e);
				MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("error.email"));
				return null;
			}
			return "index";
		}
		else
		{
			MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("error.email.no.exit"));
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
	
}
