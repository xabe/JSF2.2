package com.prueba.gui.user;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.validator.ValidatorException;

import org.primefaces.event.FileUploadEvent;

import com.prueba.gui.BaseController;
import com.prueba.gui.MessageManager;
import com.prueba.model.user.User;
import com.prueba.model.user.UserExample;
import com.prueba.model.userLogged.UserLogged;
import com.prueba.service.security.SecurityService;
import com.prueba.service.user.UserService;
import com.prueba.util.Constants;
import com.prueba.util.FileUtil;
import com.prueba.util.ImageUtils;
import com.prueba.util.JSFUtils;

@ManagedBean(name = "personalAccountController")
@ViewScoped
public class PersonalAccountController extends BaseController<User, UserExample, UserService, UserExporter, UserSearch> {
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{userService}") 
	protected UserService service;
	@ManagedProperty(value="#{securityService}") 
	private SecurityService securityService;
	private UserLogged userLogged;
	
	public PersonalAccountController() {
		generateRandom();
	}
	
	@PostConstruct
	public void init(){
		this.userLogged = this.securityService.getUserlogged();
		this.currentEntity = this.userLogged.getUser();
	}
	 
	@Override
	public String findSearch() {
		return null;
	}

	@Override
	protected List<User> getData() {
		return null;
	}
	
	public void handleFileUpload(FileUploadEvent event) {  
		try
		{
			String ext= event.getFile().getContentType();
			ext=ext.substring(ext.indexOf("/")+1,ext.length());
		
			String uploadDirectory=Constants.getPathImages()+File.separator;
			String picture="picture_"+currentEntity.getId()+"."+ext;
			String filename=uploadDirectory+File.separator+picture;        
        
			File destFile=new File(filename);
        
			FileUtil.write(destFile,event.getFile().getContents());
			currentEntity.setPicture(picture);
			service.update(currentEntity);
			//Hacemos un resize de la imagen
			ImageUtils imageUtils=new ImageUtils();
			imageUtils.resizeAndSave(filename,Constants.USER_IMAGE_WIDTH,Constants.USER_IMAGE_HEIGHT);
			MessageManager.createInfoMessage(null, JSFUtils.getStringFromBundle("uploadOK"));  
		}catch (IOException e){
			LOGGER.error("Error al subir la imagen : "+e.getMessage(), e);
			MessageManager.createErrorMessage(null,JSFUtils.getStringFromBundle("uploadKO"));
		}
    }
	
	
	private boolean dataCheckRequiredUpdate(){
		boolean required = true;
		if(currentEntity.getEmail() == null || currentEntity.getEmail().trim().length() == 0)
		{
			MessageManager.createErrorMessage("form:email",JSFUtils.getStringFromBundle("ErrorlBlank"));
			required = false;
		}
		if(!securityService.checkEmail(currentEntity.getEmail()))
		{
			MessageManager.createErrorMessage("form:email",JSFUtils.getStringFromBundle("ErrorlEmail"));
			required = false;
		}
		if(currentEntity.getTelephone() != null && securityService.checkTelephone(currentEntity.getTelephone().toString()))
		{
			MessageManager.createErrorMessage("form:telephone",JSFUtils.getStringFromBundle("ErrorTelephone"));
			required = false;
		}
		return required;
	}
	
	public String updateCurrent() {
		try
		{
			if(!dataCheckRequiredUpdate())
			{
				generateRandom();
				return null;
			}
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_UPDATE_USER)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			LOGGER.debug("Update user by :"+userLogged.getUsername()+" user :"+currentEntity.getName());
			service.update(currentEntity);
			MessageManager.createInfoMessage(null,JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error update user: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage(null,ex.getLocalizedMessage());
		}
		return null;
	}
	
	public void setService(UserService service) {
		this.service = service;
	}
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
}
