package com.prueba.ws.prueba;

					
					
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

import com.prueba.model.prueba.Prueba;
import com.prueba.model.prueba.PruebaExample;
import com.prueba.model.prueba.PruebaExample.Criteria;
import com.prueba.service.prueba.PruebaService;
import com.prueba.util.Constants;
import com.prueba.util.introspector.BeanProxy;
import  com.prueba.ws.BaseRest;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/prueba")
@Component
@Scope(value="request")
public class PruebaRest extends BaseRest {
	private static final String MODELO = "prueba";
	
	
	@Autowired
	private PruebaService service;	
	
	@GET	
	@Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public List<Prueba> getAll(){
		List<Prueba> result = service.getAll();
		logger.debug("Obtiene todos los registro de Prueba");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL, Constants.getStringNumber(result.size()));
        return result;
	}
	
	@GET
	@Path("/jsonp")
	@Produces({"application/javascript", "application/ecmascript", "application/x-ecmascript", "application/x-javascript"})
	public JSONWithPadding getAllJsonp(@QueryParam("callback") String callback){
		List<Prueba> result = service.getAll();
		logger.debug("Obtiene todos los registro de Prueba de jsonp");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL, Constants.getStringNumber(result.size()));
		return new JSONWithPadding(new GenericEntity<List<Prueba>>(result){}, callback);
	}
	
	@GET	
	@Path("/limit/{limit}/offset/{offset}")
	@Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public List<Prueba> getAllLimit(@PathParam("limit") Integer limit,@PathParam("offset") Integer offset){
		PruebaExample  example = new PruebaExample();
		example.setLimit(limit);
		example.setOffset(offset);
		List<Prueba> result = service.getPaginated(example);
		logger.debug("Obtiene todos los registro de Prueba en limit");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL_PAGINATION, Constants.getStringNumber(result.size()));
        return result;
	}
	
	@GET	
	@Path("/limit/{limit}/offset/{offset}/jsonp")
	@Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public JSONWithPadding getAllLimitJsonp(@PathParam("limit") Integer limit,@PathParam("offset") Integer offset, @QueryParam("callback") String callback){
		PruebaExample  example = new PruebaExample();
		example.setLimit(limit);
		example.setOffset(offset);
		List<Prueba> result = service.getPaginated(example);
		logger.debug("Obtiene todos los registro de Prueba en limit");
		saveAction(MODELO, Constants.ACTION_NAME_GET_ALL_PAGINATION, Constants.getStringNumber(result.size()));
		return new JSONWithPadding(new GenericEntity<List<Prueba>>(result){}, callback);
	}
	
	public PruebaExample createExampleSearch(String value, String field) throws IntrospectionException{
		PruebaExample  example = new PruebaExample();
		BeanProxy beanProxy = new BeanProxy(example.createCriteria());
		Criteria criteria = example.createCriteria();
		if(value.length() > 0 && field.equalsIgnoreCase(Constants.ALL_CRITERIA))
		{						
									
							criteria.andFechaLike(value);
									
							example.or(example.createCriteria().andIdLike(value));
									
							example.or(example.createCriteria().andNombreLike(value));
									
							example.or(example.createCriteria().andNumeroLike(value));
							}
		else if(value.length() > 0)
		{
			try
			{
				beanProxy.setBean(criteria);
				beanProxy.invoke(field, new Class<?>[] {String.class}, new Object[]{value});
			}catch (IllegalAccessException e) {
				logger.error("Error al buscar en servicio rest de Prueba : "+e.getMessage());
			}catch (Exception e) {
				logger.error("Error al buscar en servicio rest de Prueba : "+e.getMessage());
			}
		}	
		return example;	
	}

 
    @GET 
    @Path("{field}")
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public List<Prueba> findById(@PathParam("field") String field,@DefaultValue("*") @QueryParam("value") String value) {
    	List<Prueba> result = new ArrayList<Prueba>();
    	try
    	{
    		result = service.getAll(createExampleSearch(value, field));
    		logger.debug("Obtiene los registro de Prueba por el campo : "+ field +" y valor : "+ value);
    		saveAction(MODELO, Constants.ACTION_NAME_GET_FILTER, Constants.getStringNumber(result.size()));
    	}catch (Exception e) {
    		logger.error("Error al obtener el por el campo : "+ field +" y valor : "+ value + " en el modelo de Prueba motivo del error : "+e.getMessage());
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);	
		}
    	return result;
    }
    
    @GET 
    @Path("{field}/jsonp")
	@Produces({"application/javascript", "application/ecmascript", "application/x-ecmascript", "application/x-javascript"})
    public JSONWithPadding findByIdJsonp(@PathParam("field") String field,@DefaultValue("*") @QueryParam("value") String value, @QueryParam("callback") String callback){
    	List<Prueba> result = new ArrayList<Prueba>();
    	try
    	{
    		result = service.getAll(createExampleSearch(value, field));
    		logger.debug("Obtiene los registro de Prueba por el campo : "+ field +" y valor : "+ value);
    		saveAction(MODELO, Constants.ACTION_NAME_GET_FILTER, Constants.getStringNumber(result.size()));
    	}catch (Exception e) {
    		logger.error("Error al obtener el por el campo : "+ field +" y valor : "+ value + " en el modelo de Prueba motivo del error : "+e.getMessage());
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);	
		}
    	return new JSONWithPadding(new GenericEntity<List<Prueba>>(result){}, callback);
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response create(Prueba modelo) {
    	try
    	{
    		service.add(modelo);
    		saveAction(MODELO, Constants.ACTION_NAME_CREATE, Constants.UNO);
    		logger.debug("Se crear un nuevo registro de Prueba");
    		return Response.status(Response.Status.OK).build();
    	}catch (Exception e) {
    		logger.error("Error al crear el registro de Prueba en rest motivo del error : "+e.toString());
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
 
    @PUT 
    @Path("{key}")
    @Consumes({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response update(@PathParam("key") String key, Prueba modelo) {
    	try
    	{
    		service.update(modelo);
    		saveAction(MODELO, Constants.ACTION_NAME_UPDATE, Constants.UNO);
    		logger.info("Se actualiza el registro Prueba con la key : "+key);
    		return Response.status(Response.Status.OK).build();
    	}catch (Exception e) {
    		logger.error("Error al actualiza el registro de Prueba con key : "+key);
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
 
 	 	
    @DELETE 
    @Path("{key}")
    @Produces({ MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response remove(@PathParam("key") String key) {
    	try
    	{
       		PruebaExample example = new PruebaExample();
       		       		example.createCriteria().andIdEqualTo(Constants.parseNumerInteger(key));
       		    		service.delete(example);
    		saveAction(MODELO, Constants.ACTION_NAME_DELETE, Constants.UNO);
    		logger.info("Se elimina el registro de Prueba con key : "+key);
    		return Response.status(Response.Status.OK).build();
    	}catch (Exception e) {
    		logger.error("Error al eliminar el registro Prueba con el key : "+key);
    		throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
    
    	
}