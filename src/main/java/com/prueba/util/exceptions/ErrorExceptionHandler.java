package com.prueba.util.exceptions;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ErrorExceptionHandler extends ExceptionHandlerWrapper {
 
	public static final String ATTRIBUTE_ERROR_EXCEPTION = "javax.servlet.error.exception";
	public static final String ATTRIBUTE_ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
	public static final String ATTRIBUTE_ERROR_MESSAGE = "javax.servlet.error.message";
	public static final String ATTRIBUTE_ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
	public static final String ATTRIBUTE_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorExceptionHandler.class);
    private ExceptionHandler wrapped;
 
    public ErrorExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }
 
    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }
 
    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable exception = context.getException();
        	final FacesContext fc = FacesContext.getCurrentInstance();
        	final ExternalContext externalContext = fc.getExternalContext();
        	final NavigationHandler nav = fc.getApplication().getNavigationHandler();
        	
            //here you do what ever you want with exception
            try 
            {
            	exception = Exceptions.unwrap(exception, FacesException.class);
            	LOGGER.error("Se ha producido un error de tipo : " + exception.getClass().getSimpleName());
            	HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
                request.setAttribute(ATTRIBUTE_ERROR_EXCEPTION, exception);
                request.setAttribute(ATTRIBUTE_ERROR_EXCEPTION_TYPE, exception.getClass());
                request.setAttribute(ATTRIBUTE_ERROR_MESSAGE, exception.getMessage());
                request.setAttribute(ATTRIBUTE_ERROR_REQUEST_URI, request.getRequestURI());
                request.setAttribute(ATTRIBUTE_ERROR_STATUS_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);             
               
                nav.handleNavigation(fc, null, "error");
                fc.renderResponse();
            } finally {
            	i.remove();
            }
        }
        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();
    }
    
	public void doRedirect(FacesContext fc, String redirectPage)
			throws FacesException {
		ExternalContext ec = fc.getExternalContext();

		try {
			if (ec.isResponseCommitted()) {
				// redirect is not possible
				return;
			}

			// fix for renderer kit (Mojarra's and PrimeFaces's ajax redirect)
			if ((RequestContext.getCurrentInstance().isAjaxRequest() || fc
					.getPartialViewContext().isPartialRequest())
					&& fc.getResponseWriter() == null
					&& fc.getRenderKit() == null) {
				ServletResponse response = (ServletResponse) ec.getResponse();
				ServletRequest request = (ServletRequest) ec.getRequest();
				response.setCharacterEncoding(request.getCharacterEncoding());

				RenderKitFactory factory = (RenderKitFactory) FactoryFinder
						.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

				RenderKit renderKit = factory.getRenderKit(fc, fc
						.getApplication().getViewHandler()
						.calculateRenderKitId(fc));

				ResponseWriter responseWriter = renderKit.createResponseWriter(
						response.getWriter(), null,
						request.getCharacterEncoding());
				fc.setResponseWriter(responseWriter);
			}

			ec.redirect(ec.getRequestContextPath()
					+ (redirectPage != null ? redirectPage : ""));
		} catch (IOException e) {
			LOGGER.error("Redirect to the specified page '" + redirectPage+ "' failed");
			throw new FacesException(e);
		}
	}

}

