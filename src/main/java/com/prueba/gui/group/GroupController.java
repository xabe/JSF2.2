package com.prueba.gui.group;

			
import java.io.IOException;
import java.util.ArrayList;
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

import com.prueba.gui.BaseController;
import com.prueba.gui.BeanItem;
import com.prueba.gui.MessageManager;
import com.prueba.model.group.Group;
import com.prueba.model.group.GroupExample;
import com.prueba.model.permission.Permission;
import com.prueba.model.user.User;
import com.prueba.model.userLogged.UserLogged;
import com.prueba.model.vgrouppermission.VGroupPermission;
import com.prueba.model.vgrouppermission.VGroupPermissionExample;
import com.prueba.model.vusergroup.VUserGroup;
import com.prueba.model.vusergroup.VUserGroupExample;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.group.GroupService;
import com.prueba.service.security.SecurityService;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

@ManagedBean(name="groupController")
@ViewScoped
public class GroupController extends BaseController<Group, GroupExample, GroupService, GroupExporter, GroupSearch> {
	private static final long serialVersionUID = 1L;
	
	private GroupImporter importer;
	@ManagedProperty(value="#{groupService}") 
	protected GroupService service;
	@ManagedProperty(value="#{securityService}") 
	private SecurityService securityService;
	
	private DualListModel<BeanItem> dualListModelUser;
	private List<User> listUsers;
	private List<BeanItem> sourceListUser;
	private List<BeanItem> targetListUser;
	
	private DualListModel<BeanItem> dualListModelPermission;
	private List<Permission> listPermissions;
	private List<BeanItem> sourceListPermission;
	private List<BeanItem> targetListPermission;	

	private UserLogged userLogged;

	public GroupController() {
		try
		{			
			this.currentEntity = new Group();
			importer = new GroupImporter();
			exporter = new GroupExporter();
			search = new GroupSearch();
			search.clean();
			generateRandom();
			paginationContext = new PaginationContext();
			sourceListPermission = new ArrayList<BeanItem>();
			targetListPermission = new ArrayList<BeanItem>();
			dualListModelPermission = new DualListModel<BeanItem>(sourceListPermission, targetListPermission);
			
			sourceListUser = new ArrayList<BeanItem>();
			targetListUser = new ArrayList<BeanItem>();
			dualListModelUser = new DualListModel<BeanItem>(sourceListUser, targetListUser);
		}catch(Exception e){
			LOGGER.error("Error al crear el GroupController : " + e.getMessage());
		}
	}
	
	@PostConstruct
	public void init(){
		this.importer.setService(service);
		this.listPermissions = securityService.getPermissions();
		this.listUsers = securityService.getUsers();
		this.userLogged = securityService.getUserlogged();
		typeSearch = Constants.TYPE_ALL;
		model = new GroupLazyTable(service, paginationContext, search);
	}

	/************************************************************** Metodos de Busquedas **************************************************************/
	

	public String getAll() {
		typeSearch = Constants.TYPE_ALL;
		return null;
	}


	public String findSearch() {
		typeSearch = Constants.TYPE_ALL;
		return null;
	}	
	
	
	/************************************************************** Metodos de Busquedas **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Exportar  **************************************************************/
	
	protected List<Group> getData(){
		List<Group> results = null;
		int numeroRegistro = 0;
		paginationContext.setMaxResults(10000);
		List<Group> auxResults;
		results = new ArrayList<Group>();
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
			LOGGER.error("Error al importar log de subida de fichero de rest "+e.getMessage());
		}
		catch (IllegalAccessException e) {
			LOGGER.error("Error al importar log de subida de fichero de rest "+e.getMessage());
		}
		return null;
	}
	
	/************************************************************** Metodos de Importar  **************************************************************/
		
	
	
	/************************************************************** Metodos de Añadir  **************************************************************/	

	public String create() {
		this.currentEntity = new Group();
		generateRandom();
		return null;
	}

	public String addCurrent() {
		try {
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_ADD)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			LOGGER.debug("Create a new Group by :" + userLogged.getUsername()+ " :" + currentEntity.toString());
			service.add(currentEntity);
			MessageManager.createInfoMessage(Constants.MESSAGE_NEW,JSFUtils.getStringFromBundle(Constants.SUCCES_CREATE));
		} catch (Exception ex) {
			LOGGER.error("Error add group: " + ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_NEW,ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Añadir  **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Editar  **************************************************************/

	public String updateCurrent() {
		try {
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_UPDATE)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			LOGGER.debug("Update Group by :" + userLogged.getUsername()+ " group :" + currentEntity.toString());
			service.update(currentEntity);
			MessageManager.createInfoMessage(Constants.MESSAGE_UPDATE,JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error update permission: " + ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_UPDATE,ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Editar  **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Borrar  **************************************************************/

	public String deleteCurrent() {
		try {
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_DELETE)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			LOGGER.debug("Delete Group by :" + userLogged.getUsername()+ " group :" + currentEntity.toString());
			securityService.deleteAllGroupPermission(currentEntity.getId().toString());
			MessageManager.createInfoMessage(null,JSFUtils.getStringFromBundle(Constants.SUCCES_DELETE));
			executeCloseModalDelete();
		} catch (Exception ex) {
			LOGGER.error("Error delete permission: " + ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_DELETE,ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Borrar  **************************************************************/
    


	
	/************************************************************** Metodos de Asignacion de usuarios a un grupo  **************************************************************/
	
	private List<BeanItem> user(List<VUserGroup> list){
		List<BeanItem> result = new ArrayList<BeanItem>();
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i=0; i < list.size(); i++){
			if(!map.containsKey(list.get(i).getIdUser()))
			{
				result.add(new BeanItem(list.get(i).getNameUser(), list.get(i).getIdUser().toString()));
				map.put(list.get(i).getIdUser(), list.get(i).getIdUser());
			}
		}
		return result;
	}	
	
	public String editGroupUser(){
	
		VUserGroupExample groupViewExample = new VUserGroupExample();
		com.prueba.model.vusergroup.VUserGroupExample.Criteria criteria = groupViewExample.createCriteria();
		List<Integer> list = new ArrayList<Integer>();
		list.add(currentEntity.getId());
		criteria.andIdGroupIn(list);
		List<VUserGroup> listUserIn = securityService.getUserGroupExample(groupViewExample);
		targetListUser = user(listUserIn);
		
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i=0; i < targetListUser.size(); i++){
			map.put(Constants.parseNumerInteger(targetListUser.get(i).getId()), Constants.parseNumerInteger(targetListUser.get(i).getId()));
		}
		
		sourceListUser = new ArrayList<BeanItem>();
		for(int i=0; i < listUsers.size(); i++){
			if(!map.containsKey(listUsers.get(i).getId()))
			{
				sourceListUser.add(new BeanItem(listUsers.get(i).getName(), listUsers.get(i).getId().toString()));
			}
		}
		
		dualListModelUser = new DualListModel<BeanItem>(sourceListUser, targetListUser);
		generateRandom();
		return null;
	}
	
	private boolean dataCheckRequiredGroup(){
		boolean required = true;
		if(dualListModelUser.getTarget() == null || dualListModelUser.getTarget().size() == 0)
		{
			MessageManager.createErrorMessage("editFormGroup:message",JSFUtils.getStringFromBundle("error.group.no.select.user"));
			required = false;
		}
		return required;
	}
	
	public String updateCurrentUserGroupView() {
		try
		{
			if(!dataCheckRequiredGroup())
			{
				return "";
			}
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_GROUP_USER)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			securityService.addManyUserGroupEdit(currentEntity.getId().toString(), dualListModelUser.getTarget());
			MessageManager.createInfoMessage("editFormGroup:message",JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error update group with users: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage("editFormGroup:message",ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Asignacion de usuarios a un grupo  **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Asignacion de permisos a un grupo  **************************************************************/
	
	private List<BeanItem> permission(List<VGroupPermission> list){
		List<BeanItem> result = new ArrayList<BeanItem>();
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i=0; i < list.size(); i++){
			if(!map.containsKey(list.get(i).getIdPermission()))
			{
				result.add(new BeanItem(list.get(i).getNamePermission(), list.get(i).getIdPermission().toString()));
				map.put(list.get(i).getIdPermission(), list.get(i).getIdPermission());
			}
		}
		return result;
	}

	
	/**
	 * Metodo para carga los permisos asociados a ese grupo
	 * @return
	 */
	public String editGroupPermission(){
		
		VGroupPermissionExample groupPermissionViewExample = new VGroupPermissionExample();
		com.prueba.model.vgrouppermission.VGroupPermissionExample.Criteria criteria = groupPermissionViewExample.createCriteria();
		List<Integer> list = new ArrayList<Integer>();
		list.add(currentEntity.getId());
		criteria.andIdGroupIn(list);
		List<VGroupPermission> listUserIn = securityService.getGroupPermissionExample(groupPermissionViewExample);
		targetListPermission = permission(listUserIn);	
		
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i=0; i < targetListPermission.size(); i++){
			map.put(Constants.parseNumerInteger(targetListPermission.get(i).getId()), Constants.parseNumerInteger(targetListPermission.get(i).getId()));
		}
		
		sourceListPermission = new ArrayList<BeanItem>();
		for(int i=0; i < listPermissions.size(); i++){
			if(!map.containsKey(listPermissions.get(i).getId()))
			{
				sourceListPermission.add(new BeanItem(listPermissions.get(i).getName(), listPermissions.get(i).getId().toString()));
			}
		}
		
		dualListModelPermission = new DualListModel<BeanItem>(sourceListPermission, targetListPermission);
		generateRandom();
		return null;
	}
	
	private boolean dataCheckRequiredPermission(){
		boolean required = true;
		if(dualListModelPermission.getTarget() == null || dualListModelPermission.getTarget().size() == 0)
		{
			MessageManager.createErrorMessage("editFormPermission:message",JSFUtils.getStringFromBundle("error.group.no.select.permission"));
			required = false;
		}
		return required;
	}
	
	public String updateCurrentGroupPermissionView() {
		try {
			if(!dataCheckRequiredPermission())
			{
				return "";
			}
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_GROUP_PERMISSION)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			securityService.addManyGroupPermissionEdit(currentEntity.getId().toString(), dualListModelPermission.getTarget());
			MessageManager.createInfoMessage("editFormPermission:message",JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error update Gruop with permissions: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage("editFormPermission:message",ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Asignacion de usuarios a un grupo  **************************************************************/
	
	
	

	/************************************************************** Metodos de GET y SET  **************************************************************/

	public GroupSearch getSearch() {
		return search;
	}
	
	public void setService(GroupService service) {
		this.service = service;
	}
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	public DualListModel<BeanItem> getDualListModelUser() {
		return dualListModelUser;
	}
	
	public void setDualListModelUser(DualListModel<BeanItem> dualListModelUser) {
		this.dualListModelUser = dualListModelUser;
	}
	
	public DualListModel<BeanItem> getDualListModelPermission() {
		return dualListModelPermission;
	}
	
	public void setDualListModelPermission(
			DualListModel<BeanItem> dualListModelPermission) {
		this.dualListModelPermission = dualListModelPermission;
	}

}