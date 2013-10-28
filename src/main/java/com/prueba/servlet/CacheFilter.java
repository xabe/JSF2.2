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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.util.Constants;

@WebFilter(filterName="cacheFilter",urlPatterns = {"/javax.faces.resource/js/*","/javax.faces.resource/images/*","/javax.faces.resource/css/*","/resources/images/*"})
public class CacheFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest requests, ServletResponse responses,
			FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest request = Constants.getRequest(requests);
		final HttpServletResponse response = Constants.getResponse(responses);
		String requestPath = request.getRequestURI();
		if (requestPath != null) {
			if (requestPath.contains("/css/") || 
					requestPath.contains("/images/") || 
					requestPath.contains("/js/"))
			{
				if(requestPath.endsWith(".css.faces")
						|| requestPath.endsWith(".js.faces")
						|| requestPath.endsWith(".gif.faces"))
				{
					response.setHeader("Cache-Control", "max-age=31536000");
					LOGGER.debug("Entra control cache");
				}
			}
		}
		chain.doFilter(requests, responses);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}