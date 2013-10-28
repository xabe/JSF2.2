package com.prueba.gui.prueba;

import java.io.IOException;
import java.util.ArrayList;
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
import com.prueba.model.prueba.Prueba;
import com.prueba.model.prueba.PruebaExample;
import com.prueba.model.userLogged.UserLogged;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.prueba.PruebaService;
import com.prueba.service.security.SecurityService;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

@ManagedBean(name="pruebaController")
@ViewScoped
public class PruebaController extends BaseController<Prueba, PruebaExample, PruebaService, PruebaExporter, PruebaSearch> {
	private static final long serialVersionUID = 1L;
	
	private PruebaImporter importer;
	@ManagedProperty(value="#{pruebaService}") 
	protected PruebaService service;
	@ManagedProperty(value="#{securityService}") 
	private SecurityService securityService;
	
	private UserLogged userLogged;
	
	public PruebaController() {
		try
		{
			this.currentEntity = new Prueba();
			importer = new PruebaImporter();
			exporter = new PruebaExporter();
			search = new PruebaSearch();
			search.clean();
			generateRandom();
			paginationContext = new PaginationContext(); 
		} catch(Exception e){
			LOGGER.error("Error al crear el PruebaController : " + e.getMessage());
		}
	}
	
	@PostConstruct
	public void init(){
		importer.setService(service);
		typeSearch = Constants.TYPE_ALL;
		model = new PruebaLazyTable(service, paginationContext, search);
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
	
	protected List<Prueba> getData(){
		List<Prueba> results = null;
		int numeroRegistro = 0;
		paginationContext.setMaxResults(Constants.MAX_RESULTS_EXPORT);
		List<Prueba> auxResults;
		results = new ArrayList<Prueba>();
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
		exportCsv("Prueba.csv");
		return null;
	}
	
	public String exportPdf() {
		exportPdf("Prueba.pdf");
		return null;
	}
	
	public String exportXml() {
		exportXml("Prueba.xml");
		return null;
	}
	
	public String exportXls() {
		exportXls("Prueba.xls");
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
			LOGGER.error("Error al importar log de subida de fichero de Prueba "+e.getMessage());
		}
		catch (IllegalAccessException e) {
			LOGGER.error("Error al importar log de subida de fichero de Prueba "+e.getMessage());
		}
		return null;
	}
	
	/************************************************************** Metodos de Importar  **************************************************************/
		
	
	
	/************************************************************** Metodos de Añadir  **************************************************************/
	
	public String create() {
		
		this.currentEntity = new Prueba();
		generateRandom();
		return null;
	}
	
	public String addCurrent() {		
		try
		{
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_ADD)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			service.add(currentEntity);
			LOGGER.info("Creado un nuevo Prueba por :"+userLogged.getUsername());
			MessageManager.createInfoMessage(Constants.MESSAGE_NEW,JSFUtils.getStringFromBundle(Constants.SUCCES_CREATE));
		} catch (Exception ex) {
			LOGGER.error("Error crear un nuevo Prueba: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_NEW,ex.getLocalizedMessage());
		}
		reload();
		return null;
	}
	
	/************************************************************** Metodos de Añadir  **************************************************************/
	
	
	
	
	/************************************************************** Metodos de Editar  **************************************************************/
	
	public String updateCurrent() {		 
		 try
		{
			if(!getSession(Constants.CSRF_SESSION).equals(getParam(Constants.CSFR_REQUEST_UPDATE)))
			{
				generateRandom();
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION),JSFUtils.getStringFromBundle(Constants.ERROR_VALIDATION)));
			}
			generateRandom();
			service.update(currentEntity);
			LOGGER.info("Actulizado Prueba by :"+userLogged.getUsername());
			MessageManager.createInfoMessage(Constants.MESSAGE_UPDATE,JSFUtils.getStringFromBundle(Constants.SUCCES_UPDATE));
		} catch (Exception ex) {
			LOGGER.error("Error al actulizar Prueba : "+ex.getMessage(), ex);
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
			service.delete(currentEntity);
			LOGGER.info("Borrado Prueba por :"+userLogged.getUsername()+" :"+currentEntity.toString());
			MessageManager.createInfoMessage(null,JSFUtils.getStringFromBundle(Constants.SUCCES_DELETE));
			executeCloseModalDelete();
		} catch (Exception ex) {
			LOGGER.error("Error al borrar Prueba: "+ex.getMessage(), ex);
			MessageManager.createErrorMessage(Constants.MESSAGE_DELETE,ex.getLocalizedMessage());
		}
		reload();
		return null;
	}	
	
	/************************************************************** Metodos de Borrar  **************************************************************/
    


	
	/************************************************************** Metodos de GET y SET  **************************************************************/
	
	public  void setService(PruebaService service) {
		this.service = service;
	}

	public PruebaSearch getSearch() {
		return search;
	}	
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
		this.userLogged = this.securityService.getUserlogged();
	}
}