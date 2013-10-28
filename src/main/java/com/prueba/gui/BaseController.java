package com.prueba.gui;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.gui.exporter.Exporter;
import com.prueba.gui.search.Search;
import com.prueba.model.EntityBase;
import com.prueba.model.ExampleBase;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.ServiceBase;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;

public abstract class BaseController <T extends EntityBase, D extends ExampleBase, S extends ServiceBase<T, D>, E extends Exporter<T>, B extends Search<D> > implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	protected T currentEntity;
	protected String codeSecureAction;
	protected DataModelLazy<T, D, S, B> model;
	protected int maxRowsTable = Constants.MAX_RESULTS_TABLE;
	protected PaginationContext paginationContext;
	protected int typeSearch = Constants.TYPE_NONE;
	protected UploadedFile file;  
	protected E exporter;
	protected B search;
	
	
	
	public BaseController() {
		generateRandom();
	}
	
	/* Generater Random  CSFR */
	
	public String  generateRandom() {
		codeSecureAction = JSFUtils.getNewRandomString();
		putSession(Constants.CSRF_SESSION, codeSecureAction);
		return null;
	}
	
	public boolean getUpdateRandom(){
		return true;
	}
	
	public void setCodeSecureAction(String codeSecureAction) {
		this.codeSecureAction = codeSecureAction;
	}
	
	public String getCodeSecureAction() {
		return codeSecureAction;
	}
	
	public DataModelLazy<T, D, S, B> getDataModel() {
		return model;
	}
	
	public T getCurrent() {
		return currentEntity;
	}

	public void setCurrent(T t) {
		this.currentEntity = t;
	}
	
	public PaginationContext getPaginationContext() {
		return paginationContext;
	}
	
	public String getParam(String key){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
	}
	
	public void putSession(String key,Object value){
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) ctx.getSession(false);
		session.setAttribute(key, value);
	}
	
	public Object getSession(String key){
		Object result;
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) ctx.getSession(false);
		result = session.getAttribute(key);
		return result;
	}
	
	public int getMaxRowsTable() {
		return maxRowsTable;
	}
	
	public void setMaxRowsTable(int maxRowsTable) {
		this.maxRowsTable = maxRowsTable;
	}
	
	public UploadedFile getFile() {
		return file;
	}
	
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	public void reload(){
		if(typeSearch != Constants.TYPE_NONE)
		{
			findSearch();
		}
	}
	
    public void executeCloseModalDelete(){  
    	RequestContext.getCurrentInstance().execute("deleteModal.hide();");  
    }  
    
	public String getUrl() {
		return Constants.getURL();
	}
	
	public abstract String findSearch();	
	
	protected abstract List<T> getData();
	
	public void exportCsv(String fileName) {
		try
		{
			List<T> results = new ArrayList<T>();
			if(typeSearch != Constants.TYPE_NONE && paginationContext != null)
			{
				results = getData();
			}
			exporter.csvExporter(results,fileName);
			LOGGER.info("Exportado todo el "+getCurrent().getClass().getSimpleName()+" csv");
		}catch (Exception e) {
			LOGGER.error("Error al exportar el "+getCurrent().getClass().getSimpleName()+" csv : "+e.getMessage());
		}
		finally{
			if(paginationContext != null)
			{
				paginationContext.setMaxResults(Constants.MAX_RESULTS_TABLE);
			}
		}
	}
	
	public void exportPdf(String fileName) {
		try
		{
			List<T> results = new ArrayList<T>();
			if(typeSearch != Constants.TYPE_NONE && paginationContext != null)
			{
				results = getData();
			}
			exporter.pdfExporter(results,fileName);
			LOGGER.info("Exportado todo el "+getCurrent().getClass().getSimpleName()+" pdf");
		}catch (Exception e) {
			LOGGER.error("Error al exportar el "+getCurrent().getClass().getSimpleName()+" pdf: "+e.getMessage());
		}
		finally{
			if(paginationContext != null)
			{
				paginationContext.setMaxResults(Constants.MAX_RESULTS_TABLE);
			}
		}
	}
	
	public void exportXml(String fileName) {
		try
		{
			List<T> results = new ArrayList<T>();
			if(typeSearch != Constants.TYPE_NONE && paginationContext != null)
			{
				results = getData();
			}
			exporter.xmlExporter(results,fileName);
			LOGGER.info("Exportado todo el "+getCurrent().getClass().getSimpleName()+" xml");
		}catch (Exception e) {
			LOGGER.error("Error al exportar el "+getCurrent().getClass().getSimpleName()+" xml : "+e.getMessage());
		}
		finally{
			if(paginationContext != null)
			{
				paginationContext.setMaxResults(Constants.MAX_RESULTS_TABLE);
			}
		}
	}
	
	public void exportXls(String fileName) {
		try
		{
			List<T> results = new ArrayList<T>();
			if(typeSearch != Constants.TYPE_NONE && paginationContext != null)
			{
				results = getData();
			}
			exporter.xlsExporter(results,fileName);
			LOGGER.info("Exportado todo el "+getCurrent().getClass().getSimpleName()+" xls");
		}catch (Exception e) {
			LOGGER.error("Error al exportar el "+getCurrent().getClass().getSimpleName()+" xls : "+e.getMessage());
		}
		finally{
			if(paginationContext != null)
			{
				paginationContext.setMaxResults(Constants.MAX_RESULTS_TABLE);
			}
		}
	}
}
