package com.prueba.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RestHttpRequest extends HttpServletRequestWrapper {
	private Map<String, String> headers = new HashMap<String, String>();

	public RestHttpRequest(HttpServletRequest request) {
		super(request);
	}

	
	@Override
	public String getHeader(String name) {
		if (headers.get(name) != null) {
			return headers.get(name);
		}
		return super.getHeader(name);
	}
	
	@Override
	public Enumeration<String> getHeaderNames() {
		List<String> list = new ArrayList<String>();
		
		// loop over request headers from wrapped request object
		HttpServletRequest request = (HttpServletRequest) getRequest();
		Enumeration<String> e = (Enumeration<String>) request.getHeaderNames();
		while (e.hasMoreElements()) {
			// add the names of the request headers into the list
			String n = (String) e.nextElement();
			list.add(n);
		}
		for(Entry<String, String> header : headers.entrySet()){
			list.add(header.getKey());
		}
						// create an enumeration from the list and return
		Enumeration<String> en = Collections.enumeration(list);
		return en;
	}
	

	public void addParameter(String name, String value) {
		headers.put(name, value);
	}

}
