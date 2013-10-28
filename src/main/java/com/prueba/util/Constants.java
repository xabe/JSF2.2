package com.prueba.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.prueba.model.userLogged.UserLogged;

public final class Constants {
	
	private Constants() {}
	public static final int MAX_RESULTS_TABLE = 5;
	public static final int MAX_RESULTS_EXPORT = 10000;
	private static final Integer TRUE = Integer.valueOf("1");
	public static final BigDecimal ACTIVO = BigDecimal.ONE;
	public static final String FICHEROS =  "ficheros";
	public static final String ERROR = "Error";
	public static final int BYTES = 1024;
	public static final int BYTES_FILES = 4096;
	public static final String UTF_8 = "UTF-8";
	public static final String UTF_8_BOM_BEGINING = "\uFEFF";
	public static final String DELIMITER_CSV = "\"";
	public static final String EMPTY = "";
	public static final String PATTERN_FILE_NAME_CSV = "([^\\\\]*)[\\.]csv";
	public static final String FIELD = "importer/fields/field";
	public static final String VALIDATION = "importer/fields/field/validation-rule";
	public static final String ERROR_VALIDATION = "error.validation";
	public static final String ERROR_BLANK = "error.blank";
	public static final String SUCCES_IMPORT = "succes.import";
	public static final String SUCCES_CREATE = "succes.create";
	public static final String SUCCES_UPDATE = "succes.update";
	public static final String SUCCES_DELETE = "succes.delete";
	public static final String ERROR_IMPORT = "error.import";
	public static final String GET_ALL_FILEDS = "all.fields";
	
	public static final String MESSAGES_ERROR_IMPORT = "Error";
	public static final int OPTION_ZERO = 0;
	public static final int OPTION_ONE = -1;
	public static final int OPTION_TWO = -2;
	public static final int OPTION_THREE = -3;
	public static final String FORMAT_DATE = "dd/MM/yyyy";
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(FORMAT_DATE);
	public static final String RETURN = "\n";
	public static final String CRLF = "\r\n";
	public static final double TIME = 1000.0;
	public static final String ALL_CRITERIA = "allCriteria";
	public static final String FRIST = "first";
	public static final String NEXT = "next";
	public static final String PREVIOUS = "previous";
	public static final String LAST = "last";
	public static final String GET_IP_REMOTE = "X-FORWARDED-FOR";
	public static final int ZERO = 0;
	public static final  String ANONYMOUSLY = "anonymously";
	
	public static final String CONTENT_TYPE_CSV = "application/csv";
	public static final String CONTENT_TYPE_PDF = "application/pdf";
	public static final String CONTENT_TYPE_XML = "application/xml";
	public static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
	
	
	public static final String MESSAGE_IMPORT = "importForm:message";
	public static final String MESSAGE_NEW = "newForm:message";
	public static final String MESSAGE_UPDATE = "editForm:message";
	public static final String MESSAGE_DELETE = "deleteForm:message";
	
	public static final String CSFR_REQUEST_LOGGED = "loggedForm:csrf";
	public static final String CSFR_REQUEST_ADD = "newForm:csrf";
	public static final String CSFR_REQUEST_DELETE = "deleteForm:csrf";
	public static final String CSFR_REQUEST_UPDATE = "editForm:csrf";
	public static final String CSFR_REQUEST_IMPORT = "importForm:csrf";
	public static final String CSFR_REQUEST_PASSWORD = "passwordForm:csrf";
	public static final String CSFR_REQUEST_CHANGE_PASSWORD = "formChangePassword:csrf";
	public static final String CSFR_REQUEST_REGISTRATION = "formRegistration:csrf";
	public static final String CSFR_REQUEST_GROUP_PERMISSION = "editFormPermission:csrf";
	public static final String CSFR_REQUEST_GROUP_USER = "editFormGroup:csrf";
	public static final String CSFR_REQUEST_USER_GROUP = "editFormUser:csrf";
	public static final String CSFR_REQUEST_UPDATE_USER = "form:csrf";
	public static final String CSRF_SESSION = "csrf_session";
	
	public static final String ACTION_NAME_GET_ALL = "GET_ALL";
	public static final String ACTION_NAME_GET_ALL_PAGINATION = "GET_ALL_PAGINATION";
	public static final String ACTION_NAME_GET_FILTER = "GET_FILTER";
	
	public static final String ACTION_NAME_CREATE = "CREATE";
	public static final String ACTION_NAME_UPDATE = "UPDATE";
	public static final String ACTION_NAME_DELETE = "DELETE";
	
	public static final String SORT_FIELD = "sortField";
	
	public static final String SEPARATOR = ";";
	public static final String UPLOADS = "uploads";
	
	public static final int TYPE_NONE = -1;
	public static final int TYPE_ALL = 3;
	
	private static String PATH;
	private static String PATH_IMAGES;
	private static String PATH_UPLOAD;
	
	public static String TYPE_IMPORT_TOTAL = "total";
	public static String TYPE_IMPORT_INCREMENTAL = "total";
	
	public static final String PASSWORD_REST = "PASSWORD_REST";
	public static final String PASSWORD_DECODE = "PASSWORD_DECODE";
	
	public static final int USER_IMAGE_WIDTH = 120;
	public static final int USER_IMAGE_HEIGHT = 120;
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public static final String UNO = "1";
	
	public static synchronized String getDateString(Date date){
		return FORMATTER.format(date);
	}
	
	public static boolean getBoolean(Number number){
		return number.intValue() == TRUE.intValue();
	}
	
	public static Integer getTrue(Integer id){
		return Integer.valueOf("1");
	}
	
	public static BigDecimal getTrue(BigDecimal id){
		return BigDecimal.ONE;
	}
	
	public static Integer getFalse(Integer id){
		return Integer.valueOf("0");
	}
	
	public static BigDecimal getFalse(BigDecimal id){
		return BigDecimal.ZERO;
	}
	
	public static Integer getCero(Integer id){
		return Integer.valueOf("0");
	}
	
	public static BigDecimal getCero(BigDecimal id){
		return BigDecimal.ZERO;
	}
	
	public static Integer getValor(Integer id,int valor){
		return Integer.valueOf(valor);
	}
	
	public static BigDecimal getValor(BigDecimal id, int valor){
		return new BigDecimal(valor);
	}
	
	public static void downloadImporterLog(StringBuffer importLog) throws IllegalAccessException, IOException{
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		
		response.setContentType("text/plain");		
		response.setHeader("Expires", "0");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
		response.setHeader("Content-Disposition", "attachment;filename=\"importer.txt\"");
			
		ServletOutputStream os = response.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, Constants.UTF_8);
		
		PrintWriter logFile = new PrintWriter(osw);	
		
		logFile.append(importLog);
			
		logFile.flush();
		logFile.close();
			

		context.responseComplete(); 
	}	
	
	@SuppressWarnings("unchecked")
	public static <T> T findControllerApplication(String beanName) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T findVariableApplication(String variable) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return (T) context.getExternalContext().getApplicationMap().get(variable);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T findSession(String beanName) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return (T) context.getExternalContext().getSessionMap().get(beanName);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T findComponent(String id){
		FacesContext ctx = FacesContext.getCurrentInstance();
	    return (T) ctx.getViewRoot().findComponent(id);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBeanApplicationContext(Class<?> bean){
		return (T) WebApplicationContextUtils.getWebApplicationContext((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getBean(bean);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBeanApplicationContext(String bean){
		return (T) WebApplicationContextUtils.getWebApplicationContext((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getBean(bean);
	}
	
	public static FileInputStream readFile(String rutaFile) throws FileNotFoundException {
		return new FileInputStream(rutaFile);
	}
	
	public static HttpServletRequest getRequest(ServletRequest requests) {
		return (HttpServletRequest) requests;
	}
	
	public static HttpServletResponse getResponse(ServletResponse responses) {
		return (HttpServletResponse) responses;
	}
	
	public static String getIp() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String sIP = request.getHeader(GET_IP_REMOTE);
		if (sIP == null || sIP.length() == 0) {
			sIP = request.getRemoteAddr();
		}
		return sIP;
	}
	
	private static String getLookup(String variable) {
		Object object = null;
		try {
			Context inicial = new InitialContext();
			Context miCtx = (Context) inicial.lookup("java:comp/env");
			object = miCtx.lookup(variable);
		} catch (NamingException e) {
			System.err.println("Error al obtener un recurso por lookup : " + variable
					+ " motivo error : " + e.getMessage());
		}
		return object == null ? "" : object.toString();
	}
	
	public static String getPath() {
		if (PATH == null) {
			PATH = getLookup("PATH_REPO");
		}
		return PATH;
	}
	
	public static String getPathImages() {
		if (PATH_IMAGES == null) {
			PATH_IMAGES = getLookup("PATH_REPO") + File.separator + "images";
		}
		return PATH_IMAGES;
	}
	
	public static String getPathUploads() {
		if (PATH_UPLOAD == null) {
			PATH_UPLOAD = getLookup("PATH_REPO") + File.separator + "uploads";
		}
		return PATH_UPLOAD;
	}
	
	public static Integer parseNumerInteger(String number){
		return Integer.parseInt(number);
	}
	
	public static Long parseNumerLong(String number){
		return Long.parseLong(number);
	}
	
	public static Float parseNumerFloat(String number){
		return Float.parseFloat(number);
	}
	
	public static Double parseNumerDouble(String number){
		return Double.parseDouble(number);
	}
	
	public static BigDecimal parseNumerBigDecimal(String number){
		return new BigDecimal(number);
	}
	
	public static String getURL(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getRequestURL().substring(0,request.getRequestURL().length() - request.getRequestURI().length())+request.getContextPath()+"/";
	}	
	
	public static String getStringNumber(int value){
		return ""+value;
	}
	
	public static String getStringNumber(float value){
		return ""+value;
	}
	
	public static String getStringNumber(double value){
		return ""+value;
	}
	
	public static String getStringNumber(long value){
		return ""+value;
	}
	
	public static String getStringNumber(BigDecimal value){
		return value.toString();
	}
	
	public synchronized static List<UserLogged> getUsers() {
	    SessionRegistry sr = getBeanApplicationContext(SessionRegistry.class);
	    List<Object> principals = sr.getAllPrincipals();
	    List<UserLogged> result = new ArrayList<UserLogged>();

	    for(int i = 0; i < principals.size(); i++) {
	    	List<SessionInformation> sis = sr.getAllSessions((UserLogged)principals.get(i), false);
			if(sis != null && sis.size() > 0 && !sis.get(0).isExpired())
			{
				result.add((UserLogged)principals.get(i));
			}
	    }

	    return result;
	}
	
	public synchronized static boolean checkUsers(String username) {
	    List<UserLogged> result = getUsers();

	    for(int i = 0; i < result.size(); i++) {
	      if(result.get(i).getUsername().equals(username))
	      {
	    	  return true;
	      }
	    }

	    return false;
	}
	
	public synchronized static void removeUser(UserLogged user) {
		SessionRegistry sr = getBeanApplicationContext(SessionRegistry.class);	
		List<SessionInformation> sis = sr.getAllSessions(user, false);
		if(sis != null && sis.size() > 0)
		{
			sis.get(0).expireNow();
		}
	}
	
	static{
		File file = new File(Constants.getPath());
		if(!file.exists())
		{
			file.mkdir();
		}
		file = new File(Constants.getPathUploads());
		if(!file.exists())
		{
			file.mkdir();
		}
		file = new File(Constants.getPathImages());
		if(!file.exists())
		{
			file.mkdir();
		}
	}
}