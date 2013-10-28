package com.prueba.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.prueba.util.Constants;

public class RestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		String uaString = req.getHeader("Authorization");
		if(uaString == null)
		{
			RestHttpRequest request2 = new RestHttpRequest(req);
			Object object = req.getSession().getAttribute(Constants.PASSWORD_REST);
			if(object != null)
			{
				request2.addParameter("Authorization", object.toString());
				chain.doFilter(request2, response);
				return;
			}
		
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {


	}

}

