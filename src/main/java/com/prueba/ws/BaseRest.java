package com.prueba.ws;

										
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prueba.model.userLogged.UserLogged;
import com.prueba.model.wshistory.WsHistory;
import com.prueba.service.wshistory.WsHistoryService;
import com.prueba.util.Constants;

public abstract class BaseRest {
	protected Logger logger = LoggerFactory.getLogger(BaseRest.class);
	
	@Context
	protected HttpServletRequest httpServletRequest;
	
	@Context
	protected UriInfo uriInfo;
	
	@Autowired
	protected WsHistoryService historyService;
	
	public UserLogged getUserLogged(){
		return (UserLogged) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	
	public URI getAbsolutePath(){
		return uriInfo.getAbsolutePath();
	}
	
	public String getIp() {
		HttpServletRequest request = getHttpServletRequest();
		String sIP = request.getHeader(Constants.GET_IP_REMOTE);
		if (sIP == null || sIP.length() == 0) {
			sIP = request.getRemoteAddr();
		}
		return sIP;
	}
	
	public void saveAction(String action,String actionName,String numberOfElements){
		WsHistory aWsHistory = new WsHistory();
		aWsHistory.setUserid(getUserLogged().getUser().getId());
		aWsHistory.setUsername(getUserLogged().getUsername());
		aWsHistory.setIp(getIp());
		aWsHistory.setServicerequestdate(new Timestamp(new Date().getTime()));
		aWsHistory.setService(action);
		aWsHistory.setResponseformat(getHttpServletRequest().getHeader("accept"));
		aWsHistory.setServicename(actionName);
		aWsHistory.setNumberofelements(Constants.parseNumerInteger(numberOfElements));
		historyService.add(aWsHistory);
	}
}
