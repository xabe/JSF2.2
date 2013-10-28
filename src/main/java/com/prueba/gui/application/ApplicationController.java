package com.prueba.gui.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@ManagedBean(name="applicationController", eager = true)
public class ApplicationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
	
	private String jsfImpl;
	private String primeFaces;
	private String primeFacesExt;
	private String buildTime;

	
	
	public ApplicationController() {
		// TODO Auto-generated constructor stub
	}
	
	@PostConstruct
    protected void initialize() {
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle("pe-showcase");
 
            String strAppProps = rb.getString("application.properties");
            int lastBrace = strAppProps.indexOf("}");
            strAppProps = strAppProps.substring(1, lastBrace);
 
            Map<String, String> appProperties = new HashMap<String, String>();
            String[] appProps = strAppProps.split("[\\s,]+");
            for (String appProp : appProps) {
                String[] keyValue = appProp.split("=");
                if (keyValue != null && keyValue.length > 1) {
                    appProperties.put(keyValue[0], keyValue[1]);
                }
            }
 
            primeFaces = "PrimeFaces: " + appProperties.get("primefaces.core.version");
            primeFacesExt = "PrimeFaces Extensions: " + appProperties.get("primefaces.extensions.version");
            jsfImpl = "JSF-Impl.: " + appProperties.get("pe.jsf.displayname") + " " + appProperties.get("jsf.version");
 
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(appProperties.get("timestamp")==null?Calendar.getInstance().getTimeInMillis()+"":appProperties.get("timestamp")));
            buildTime = "Build time: " + formatter.format(calendar.getTime());

        } catch (MissingResourceException e) {
            LOGGER.warn("Resource bundle 'pe-showcase' was not found");
        }
    }

	public String getJsfImpl() {
		return jsfImpl;
	}

	public void setJsfImpl(String jsfImpl) {
		this.jsfImpl = jsfImpl;
	}

	public String getPrimeFaces() {
		return primeFaces;
	}

	public void setPrimeFaces(String primeFaces) {
		this.primeFaces = primeFaces;
	}

	public String getPrimeFacesExt() {
		return primeFacesExt;
	}

	public void setPrimeFacesExt(String primeFacesExt) {
		this.primeFacesExt = primeFacesExt;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}
	
}
