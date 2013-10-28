package com.prueba.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/index.jsp"},filterName="mobileFilter")
public class MobileFilter implements Filter{
	
	public void init(FilterConfig filterConfig) throws ServletException {		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse resp = (HttpServletResponse) response;
		String uaString = req.getHeader("User-Agent").toLowerCase();
		if(uaString.indexOf("mobile") != -1  && req.getServletPath().startsWith("/normal/"))
		{
			resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			resp.setHeader("Location", req.getContextPath()+"/mobile/mobile.faces");
		}
		chain.doFilter(request, response);
	}
	
	public void destroy() {
		
	}

}
