package com.prueba.ws.wshistory;

										
										
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.prueba.model.wshistory.WsHistory;
import com.prueba.model.wshistory.WsHistoryExample;
import com.prueba.model.wshistory.WsHistoryExample.Criteria;
import com.prueba.service.wshistory.WsHistoryService;
import com.prueba.util.Constants;
import com.prueba.util.introspector.BeanProxy;
import  com.prueba.ws.BaseRest;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/wsHistory")
@Component
@Scope(value="request")
public class WsHistoryRest extends BaseRest {
	private static final String MODELO = "wsHistory";
	
	
	@Autowired
	private WsHistoryService service;	
	
	@GET	
	@Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public List<WsHistory> getAll(){
		List<WsHistory> result = service.getAll();
		logger.debug("Obtiene todos los registro de WsHistory");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL, Constants.getStringNumber(result.size()));
        return result;
	}
	
	@GET
	@Path("/jsonp")
	@Produces({"application/javascript", "application/ecmascript", "application/x-ecmascript", "application/x-javascript"})
	public JSONWithPadding getAllJsonp(@QueryParam("callback") String callback){
		List<WsHistory> result = service.getAll();
		logger.debug("Obtiene todos los registro de WsHistory de jsonp");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL, Constants.getStringNumber(result.size()));
		return new JSONWithPadding(new GenericEntity<List<WsHistory>>(result){}, callback);
	}
	
	@GET	
	@Path("/limit/{limit}/offset/{offset}")
	@Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public List<WsHistory> getAllLimit(@PathParam("limit") Integer limit,@PathParam("offset") Integer offset){
		WsHistoryExample  example = new WsHistoryExample();
		example.setLimit(limit);
		example.setOffset(offset);
		List<WsHistory> result = service.getPaginated(example);
		logger.debug("Obtiene todos los registro de WsHistory en limit");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL_PAGINATION, Constants.getStringNumber(result.size()));
        return result;
	}
	
	@GET	
	@Path("/limit/{limit}/offset/{offset}/jsonp")
	@Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public JSONWithPadding getAllLimitJsonp(@PathParam("limit") Integer limit,@PathParam("offset") Integer offset, @QueryParam("callback") String callback){
		WsHistoryExample  example = new WsHistoryExample();
		example.setLimit(limit);
		example.setOffset(offset);
		List<WsHistory> result = service.getPaginated(example);
		logger.debug("Obtiene todos los registro de WsHistory en limit");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL_PAGINATION, Constants.getStringNumber(result.size()));
		return new JSONWithPadding(new GenericEntity<List<WsHistory>>(result){}, callback);
	}
	
	public WsHistoryExample createExampleSearch(String value, String field) throws IntrospectionException{
		WsHistoryExample  example = new WsHistoryExample();
		BeanProxy beanProxy = new BeanProxy(example.createCriteria());
		Criteria criteria = example.createCriteria();
		if(value.length() > 0 && field.equalsIgnoreCase(Constants.ALL_CRITERIA))
		{						
									
							criteria.andIdLike(value);
									
							example.or(example.createCriteria().andIpLike(value));
									
							example.or(example.createCriteria().andNumberofelementsLike(value));
									
							example.or(example.createCriteria().andResponseformatLike(value));
									
							example.or(example.createCriteria().andServiceLike(value));
									
							example.or(example.createCriteria().andServicenameLike(value));
									
							example.or(example.createCriteria().andServicerequestdateLike(value));
									
							example.or(example.createCriteria().andUseridLike(value));
									
							example.or(example.createCriteria().andUsernameLike(value));
							}
		else if(value.length() > 0)
		{
			try
			{
				beanProxy.setBean(criteria);
				beanProxy.invoke(field, new Class<?>[] {String.class}, new Object[]{value});
			}catch (IllegalAccessException e) {
				logger.error("Error al buscar en servicio rest de WsHistory : "+e.getMessage());
			}catch (Exception e) {
				logger.error("Error al buscar en servicio rest de WsHistory : "+e.getMessage());
			}
		}	
		return example;	
	}

 
    @GET 
    @Path("{field}")
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public List<WsHistory> findById(@PathParam("field") String field,@DefaultValue("*") @QueryParam("value") String value) {
    	List<WsHistory> result = new ArrayList<WsHistory>();
    	try
    	{
    		result = service.getAll(createExampleSearch(value, field));
    		logger.debug("Obtiene los registro de WsHistory por el campo : "+ field +" y valor : "+ value);
    		saveAction(MODELO, Constants.ACTION_NAME_GET_FILTER, Constants.getStringNumber(result.size()));
    	}catch (Exception e) {
    		logger.error("Error al obtener el por el campo : "+ field +" y valor : "+ value + " en el modelo de WsHistory motivo del error : "+e.getMessage());
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);	
		}
    	return result;
    }
    
    @GET 
    @Path("{field}/jsonp")
	@Produces({"application/javascript", "application/ecmascript", "application/x-ecmascript", "application/x-javascript"})
    public JSONWithPadding findByIdJsonp(@PathParam("field") String field,@DefaultValue("*") @QueryParam("value") String value, @QueryParam("callback") String callback){
    	List<WsHistory> result = new ArrayList<WsHistory>();
    	try
    	{
    		result = service.getAll(createExampleSearch(value, field));
    		logger.debug("Obtiene los registro de WsHistory por el campo : "+ field +" y valor : "+ value);
    		saveAction(MODELO, Constants.ACTION_NAME_GET_FILTER, Constants.getStringNumber(result.size()));
    	}catch (Exception e) {
    		logger.error("Error al obtener el por el campo : "+ field +" y valor : "+ value + " en el modelo de WsHistory motivo del error : "+e.getMessage());
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);	
		}
    	return new JSONWithPadding(new GenericEntity<List<WsHistory>>(result){}, callback);
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response create(WsHistory modelo) {
    	try
    	{
    		service.add(modelo);
    		saveAction(MODELO, Constants.ACTION_NAME_CREATE, Constants.UNO);
    		logger.debug("Se crear un nuevo registro de WsHistory");
    		return Response.status(Response.Status.OK).build();
    	}catch (Exception e) {
    		logger.error("Error al crear el registro de WsHistory en rest motivo del error : "+e.toString());
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
 
    @PUT 
    @Path("{key}")
    @Consumes({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response update(@PathParam("key") String key, WsHistory modelo) {
    	try
    	{
    		service.update(modelo);
    		saveAction(MODELO, Constants.ACTION_NAME_UPDATE, Constants.UNO);
    		logger.info("Se actualiza el registro WsHistory con la key : "+key);
    		return Response.status(Response.Status.OK).build();
    	}catch (Exception e) {
    		logger.error("Error al actualiza el registro de WsHistory con key : "+key);
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
 
 	 	
    @DELETE 
    @Path("{key}")
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response remove(@PathParam("key") String key) {
    	try
    	{
       		WsHistoryExample example = new WsHistoryExample();
       		       		example.createCriteria().andIdEqualTo(Constants.parseNumerInteger(key));
       		    		service.delete(example);
    		saveAction(MODELO, Constants.ACTION_NAME_DELETE, Constants.UNO);
    		logger.info("Se elimina el registro de WsHistory con key : "+key);
    		return Response.status(Response.Status.OK).build();
    	}catch (Exception e) {
    		logger.error("Error al eliminar el registro WsHistory con el key : "+key);
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
    
    	
}