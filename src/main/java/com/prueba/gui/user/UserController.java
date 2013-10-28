package com.prueba.gui.user;

															
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.validator.ValidatorException;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.springframework.dao.DuplicateKeyException;

import com.prueba.gui.BaseController;
import com.prueba.gui.BeanItem;
import com.prueba.gui.MessageManager;
import com.prueba.model.group.Group;
import com.prueba.model.user.User;
import com.prueba.model.user.UserExample;
import com.prueba.model.userLogged.UserLogged;
import com.prueba.model.vusergroup.VUserGroup;
import com.prueba.model.vusergroup.VUserGroupExample;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.mail.MailService;
import com.prueba.service.security.SecurityService;
import com.prueba.service.user.UserService;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

@ManagedBean(name="userController")
@ViewScoped
public class UserController extends BaseController<User, UserExample, UserService, UserExporter, UserSearch> {
	private static final long serialVersionUID = 1L;
	
	private UserImporter importer;
	@ManagedProperty(value="#{userService}") 
	protected UserService service;
	@ManagedProperty(value="#{securityService}") 
	private SecurityService securityService;
	@ManagedProperty(value="#{mailService}") 
	private MailService mailService;
	
	private DualListModel<BeanItem> dualListModelGroup;
	private List<Group> listGroups;
	private List<BeanItem> sourceListGroup;
	private List<BeanItem> targetListGroup;

	private UserLogged userLogged;
	private String password;
	
	private List<User> users;
	
	private List<UserLogged> userLoggeds;
	private String removeUser;

	public UserController() {
		try
		{
			this.currentEntity = new User();
			importer = new UserImporter();
			exporter = new UserExporter();
			search = new UserSearch();
			search.clean();
			generateRandom();
			paginationContext = new PaginationContext();
			
			sourceListGroup = new ArrayList<BeanItem>();
			targetListGroup = new ArrayList<BeanItem>();
			dualListModelGroup = new DualListModel<BeanItem>(sourceListGroup, targetListGroup);
		}catch(Exception e){
			LOGGER.error("Error al crear el UserController : " + e.getMessage());
		}
	}
	
	@PostConstruct
	public void init(){
		listGroups = this.securityService.getGroups();
		this.userLogged = this.securityService.getUserlogged();
		importer.setService(service);
		typeSearch = Constants.TYPE_ALL;
		model = new UserLazyTable(service, paginationContext, search);
	}

	/************************************************************** Metodos de Busquedas **************************************************************/
	

	public String getAll() {
		typeSearch = Constants.TYPE_ALL;
		getPaginationContext().reset();
		users = service.findSearch(new UserExample(), getPaginationContext(), 0);
		return null;
	}


	public String findSearch() {
		typeSearch = Constants.TYPE_ALL;
		getPaginationContext().reset();
		users = service.findSearch(search.createExampleSearch(), getPaginationContext(), 0);
		return null;
	}	
	
	public String getNext() {
		typeSearch = Constants.TYPE_ALL;
		users = service.getPaginated(Constants.NEXT, getPaginationContext());
		return null;
	}
	
	public String getPrevious() {
		typeSearch = Constants.TYPE_ALL;
		users = service.getPaginated(Constants.PREVIOUS, getPaginationContext());
		return null;
	}
	
	
	/************************************************************** Metodos de Busquedas **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Exportar  **************************************************************/
	
	protected List<User> getData(){
		List<User> results = null;
		int numeroRegistro = 0;
		paginationContext.setMaxResults(10000);
		List<User> auxResults;
		results = new ArrayList<User>();
		results.addAll(service.getPaginated(Constants.FRIST, paginationContext));
		numeroRegistro = results.size();
		while( numeroRegistro < paginationContext.getTotalCount()){
			auxResults = service.getPaginated(Constants.NEXT, paginationContext);
			numeroRegistro += auxResults.size();
			results.addAll(auxResults);
		}
		return results;
	}
	
	public String exportCsv() {
		exportCsv("Permission.csv");
		return null;
	}
	
	public String exportPdf() {
		exportPdf("Permission.pdf");
		return null;
	}
	
	public String exportXml() {
		exportXml("Permission.xml");
		return null;
	}
	
	public String exportXls() {
		exportXls("Permission.xls");
		return null;
	}
	
	/************************************************************** Metodos de Exportar  **************************************************************/
	
	

	/************************************************************** Metodos de Importar  **************************************************************/	
	
	public void handleFileUpload(FileUploadEvent event) {  

		if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_IMPORT)))
		{
			generateRandom();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
		}
		
		String importProcessResult= importer.importFile(event.getFile());

		if (importProcessResult.contains(Constants.MESSAGES_ERROR_IMPORT))
		{
			MessageManager.createErrorMessage(Constants.MESSAGE_IMPORT,JSFUtils.getStringFromBundle(Constants.ERROR_IMPORT));
		}
		else
		{
			MessageManager.createInfoMessage(Constants.MESSAGE_IMPORT,JSFUtils.getStringFromBundle(Constants.SUCCES_IMPORT));
		}  
	 }  
	 
	public String downloadImportLog(){
		try
		{
			importer.downloadImporterLog(); 
		}catch (IOException e) {
			LOGGER.error("Error al importar log de subida de fichero de User "+e.getMessage());
		}
		catch (IllegalAccessException e) {
			LOGGER.error("Error al importar log de subida de fichero de User "+e.getMessage());
		}
		return null;
	}	
	
	/************************************************************** Metodos de Importar  **************************************************************/
		
	
	
	/************************************************************** Metodos de Añadir  **************************************************************/
	
	public void create() {
		currentEntity = new User();
		currentEntity.setAttemptsLogin(Constants.getCero(currentEntity.getAttemptsLogin()));
		currentEntity.setBlocked(Constants.getFalse(currentEntity.getBlocked()));
		currentEntity.setEnable(Constants.getTrue(currentEntity.getId()));
		currentEntity.setDateLastPassword(new Timestamp(new Date().getTime()));
		generateRandom();
	}
	
	private boolean dataCheckRequired(){
		boolean required = true;
		if(currentEntity.getPassword() == null || currentEntity.getPassword().trim().length() == 0)
		{
			MessageManager.createErrorMessage("newForm:password",JSFUtils.getStringFromBundle(Constants.ERROR_BLANK));
			required = false;
		}				
		if(!securityService.checkPasswordLenght(currentEntity.getPassword()))
		{
			MessageManager.createErrorMessage("newForm:password",JSFUtils.getStringFromBundle("error.length.password")+" "+securityService.getMinLengthPassword());
			required = false;
		}
		if(!securityService.checkPasswordCharacter(currentEntity.getPassword()))
		{
			MessageManager.createErrorMessage("newForm:password",JSFUtils.getStringFromBundle("error.character.password"));
			required = false;
		}
		if(currentEntity.getEmail() == null || currentEntity.getEmail().trim().length() == 0)
		{
			MessageManager.createErrorMessage("newForm:email",JSFUtils.getStringFromBundle(Constants.ERROR_BLANK));
			required = false;
		}
		if(!securityService.checkEmail(currentEntity.getEmail()))
		{
			MessageManager.createErrorMessage("newForm:email",JSFUtils.getStringFromBundle("error.email"));
			required = false;
		}
		if(currentEntity.getTelephone() != null && securityService.checkTelephone(currentEntity.getTelephone().toString()))
		{
			MessageManager.createErrorMessage("newForm:telephone",JSFUtils.getStringFromBundle("error.user.telephone"));
			required = false;
		}
		return required;
	}
	
	public String addCurrent() {
		try {
			if(!dataCheckRequired())
			{
				generateRandom();
				return null;
			}	
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_ADD)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			LOGGER.debug("Create a new user by :"+userLogged.getUsername()+" user :"+currentEntity.getName());
			String password = currentEntity.getPassword();
			currentEntity.setPassword(securityService.encodePassword(password));
			securityService.addUser(currentEntity);
			String text = JSFUtils.getStringFromBundle("user.text.new");
			mailService.send(currentEntity.getEmail(), JSFUtils.getStringFromBundle("user.subject.new"), MessageFormat.format(text,currentEntity.getUsername(),password));
			MessageManager.createInfoMessage(Constants.MESSAGE_NEW,JSFUtils.getStringFromBundle(Constants.SUCCES_CREATE));
		} catch (DuplicateKeyException e) {
			LOGGER.error("Error duplicate user: "+e.getMessage(), e);
			MessageManager.createErrorMessage(Constants.MESSAGE_NEW,JSFUtils
					.getStringFromBundle("ErrorDuplicateUsername"));
		} catch (Exception ex) {
			LOGGER.error("Error add user: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_NEW,ex.getLocalizedMessage());
		} finally {

		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Añadir  **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Editar  **************************************************************/
	
	private boolean dataCheckRequiredUpdate(){
		boolean required = true;
		if(currentEntity.getEmail() == null || currentEntity.getEmail().trim().length() == 0)
		{
			MessageManager.createErrorMessage("editForm:email",JSFUtils.getStringFromBundle(Constants.ERROR_BLANK));
			required = false;
		}
		if(!securityService.checkEmail(currentEntity.getEmail()))
		{
			MessageManager.createErrorMessage("editForm:email",JSFUtils.getStringFromBundle("error.email"));
			required = false;
		}
		if(currentEntity.getTelephone() != null && securityService.checkTelephone(currentEntity.getTelephone().toString()))
		{
			MessageManager.createErrorMessage("editForm:telephone",JSFUtils.getStringFromBundle("error.user.telephone"));
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
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_UPDATE)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			LOGGER.debug("Update user by :"+userLogged.getUsername()+" user :"+currentEntity.getName());
			service.update(currentEntity);
			MessageManager.createInfoMessage(Constants.MESSAGE_UPDATE,JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error update user: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_UPDATE,ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Editar  **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Borrar  **************************************************************/
	
	public String deleteCurrent() {
		try
		{
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_DELETE)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			LOGGER.debug("Delete user by :"+userLogged.getUsername()+" user :"+currentEntity.getName());
			securityService.deleteUser(currentEntity);
			mailService.send(currentEntity.getEmail(), JSFUtils.getStringFromBundle("user.subject.delete.account"),JSFUtils.getStringFromBundle("user.text.delete.account"));
			MessageManager.createInfoMessage(null,JSFUtils.getStringFromBundle(Constants.SUCCES_DELETE));
			executeCloseModalDelete();
		} catch (Exception ex) {
			LOGGER.error("Error delete user: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_DELETE,ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Borrar  **************************************************************/
    


	
	/************************************************************** Metodos generar password  *******************************************************/
	
	public String generaterPassword() {
		try {
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_PASSWORD)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),
						JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			currentEntity.setPassword(securityService.encodePassword(password));
			service.update(currentEntity);
			String text = JSFUtils
					.getStringFromBundle("user.text.generater.new.password");
			mailService.send(
					currentEntity.getEmail(),
					JSFUtils.getStringFromBundle("user.subject.generater.new.password"),
					MessageFormat.format(text, password));
			MessageManager.createInfoMessage("generaterPasswordForm:message",
					JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error generate password: " + ex.getMessage(), ex);
			MessageManager.createErrorMessage("generaterPasswordForm:message",
					ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos generar password  *******************************************************/

	
	
	/************************************************************** Metodos de Asignacion de grupos a un usuario  **************************************************************/
	
	private List<BeanItem> group(List<VUserGroup> list){
		List<BeanItem> result = new ArrayList<BeanItem>();
		HashMap< Integer,  Integer> map = new HashMap< Integer,  Integer>();
		for(int i=0; i < list.size(); i++){
			if(!map.containsKey(list.get(i).getIdGroup()))
			{
				result.add(new BeanItem(list.get(i).getNameGroup(), list.get(i).getIdGroup().toString()));
				map.put(list.get(i).getIdGroup(), list.get(i).getIdGroup());
			}
		}
		return result;
	}

	public String editGroupUser(){
		
		VUserGroupExample groupViewExample = new VUserGroupExample();
		com.prueba.model.vusergroup.VUserGroupExample.Criteria criteria = groupViewExample.createCriteria();
		List< Integer> list = new ArrayList< Integer>();
		list.add(currentEntity.getId());
		criteria.andIdUserIn(list);
		List<VUserGroup> listUserIn = securityService.getUserGroupExample(groupViewExample);
		targetListGroup = group(listUserIn);
		
		
		HashMap< Integer,  Integer> map = new HashMap< Integer,  Integer>();
		for(int i=0; i < targetListGroup.size(); i++){
			map.put(Constants.parseNumerInteger(targetListGroup.get(i).getId()), Constants.parseNumerInteger(targetListGroup.get(i).getId()));
		}
		
		sourceListGroup = new ArrayList<BeanItem>();
		for(int i=0; i < listGroups.size(); i++){
			if(!map.containsKey(listGroups.get(i).getId()))
			{
				sourceListGroup.add(new BeanItem(listGroups.get(i).getName(), listGroups.get(i).getId().toString()));
			}
		}	
		
		dualListModelGroup = new DualListModel<BeanItem>(sourceListGroup, targetListGroup);
		generateRandom();
		return null;
	}
	
	private boolean dataCheckRequiredGroup(){
		boolean required = true;
		if(dualListModelGroup.getTarget() == null || dualListModelGroup.getTarget().size() == 0)
		{
			MessageManager.createErrorMessage("editFormUser:message",JSFUtils.getStringFromBundle("error.user.no.select.group"));
			required = false;
		}
		return required;
	}
	
	public String updateCurrentUserGroupView() {
		try
		{
			if(!dataCheckRequiredGroup())
			{
				return null;
			}
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_USER_GROUP)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			securityService.addManyGroupUserEdit(currentEntity.getId().toString(), dualListModelGroup.getTarget());
			MessageManager.createInfoMessage("editFormUser:message",JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error update user with groups: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage("editFormUser:message",ex.getLocalizedMessage());
		}
		return null;
	}
	
	/************************************************************** Metodos de Asignacion de grupos a un usuario  **************************************************************/
	
	
	/************************************************************** Gestion de sessiones activas **************************************************************/
	
	public String showUserlogged() {
		generateRandom();
		userLoggeds = Constants.getUsers();
		return null;
	}
	
	public String disbleSession(){
		if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_LOGGED)))
		{
			generateRandom();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
		}
		generateRandom();
		try
		{			
			for(UserLogged userLogged : userLoggeds){
				if(userLogged.getUsername().equals(removeUser))
				{
					Constants.removeUser(userLogged);
					MessageManager.createInfoMessage("loggedForm:message",JSFUtils.getStringFromBundle("successDisableSession"));		
					break;
				}
			}			
		}
		catch (Exception e) {
			LOGGER.error("Error al eliminar la sesión del usuario: "+removeUser, e);
			MessageManager.createErrorMessage("loggedForm:message",e.getLocalizedMessage());
		}
		finally{
			userLoggeds = Constants.getUsers();
		}
		return null;
	}
	
	/************************************************************** Gestion de sessiones activas **************************************************************/
	

	/************************************************************** Metodos de GET y SET  **************************************************************/

	public void setService(UserService service) {
		this.service = service;
	}

	public UserSearch getSearch() {
		return search;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public DualListModel<BeanItem> getDualListModelGroup() {
		return dualListModelGroup;
	}
	
	public void setDualListModelGroup(DualListModel<BeanItem> dualListModelGroup) {
		this.dualListModelGroup = dualListModelGroup;
	}

	public String getRemoveUser() {
		return removeUser;
	}
	
	public void setRemoveUser(String removeUser) {
		this.removeUser = removeUser;
	}
	
	public List<UserLogged> getUserLoggeds() {
		return userLoggeds;
	}
	
	public void setUserLoggeds(List<UserLogged> userLoggeds) {
		this.userLoggeds = userLoggeds;
	}
	
	/*Nueva pagination */
	
	public List<User> getUsers() {
		return users;
	}
	
	public int getSize() {
		return users == null || users.size() == 0 ? 0 : users.size();
	}
}