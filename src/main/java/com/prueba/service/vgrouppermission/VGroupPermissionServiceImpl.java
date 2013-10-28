package com.prueba.service.vgrouppermission;

					
					
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.vgrouppermission.VGroupPermission;
import com.prueba.persistence.vgrouppermission.VGroupPermissionMapper;
import com.prueba.persistence.PaginationContext;
import com.prueba.model.vgrouppermission.VGroupPermissionExample;
import com.prueba.model.vgrouppermission.VGroupPermissionExample.Criteria;
import com.prueba.util.introspector.BeanProxy;
import com.prueba.util.Constants;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos VGroupPermission.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos VGroupPermission.
 *
 */ 
 
@Service("vGroupPermissionService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class VGroupPermissionServiceImpl implements VGroupPermissionService {
	@Autowired
	private VGroupPermissionMapper vGroupPermissionMapper;
	private VGroupPermissionExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public VGroupPermissionServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(VGroupPermission.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios VGroupPermission : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(VGroupPermission aVGroupPermission) {
		vGroupPermissionMapper.insert(aVGroupPermission);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(VGroupPermission aVGroupPermission) {
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(VGroupPermission aVGroupPermission, VGroupPermissionExample aVGroupPermissionExample) {
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(VGroupPermission aVGroupPermission) {
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(VGroupPermissionExample aVGroupPermissionExample) {
			}

	public List<VGroupPermission> getAll() {		
		return vGroupPermissionMapper.selectByExample(new VGroupPermissionExample());
	}

	public List<VGroupPermission> getAll(VGroupPermissionExample aVGroupPermissionExample) {		
		return vGroupPermissionMapper.selectByExample(aVGroupPermissionExample);
	}
	
	public int getTotal(){
		return vGroupPermissionMapper.countByExample(new VGroupPermissionExample());
	}
	
	public int getTotal(VGroupPermissionExample aVGroupPermissionExample){
		return vGroupPermissionMapper.countByExample(aVGroupPermissionExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
		}
	
	public List<VGroupPermission> findSearch(VGroupPermissionExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(vGroupPermissionMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  vGroupPermissionMapper.selectByExamplePagination(example);
	}

	public List<VGroupPermission> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<VGroupPermission> vGroupPermissions = null;
		if (operation==null){
			paginationContext.firstPage();
		}
		else if (operation.equalsIgnoreCase(Constants.PREVIOUS)){
			paginationContext.previousPage();
		}
		else if (operation.equalsIgnoreCase(Constants.NEXT)){
			paginationContext.nextPage();
		}
		else if (operation.equalsIgnoreCase(Constants.LAST)){
			paginationContext.lastPage();
		}
		else
		{
			paginationContext.firstPage();
		}
		// Acceso a la paginacion
		// Acceso a la paginacion
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		paginationContext.updateTotalCount(vGroupPermissionMapper.countByExample(example));
		vGroupPermissions = vGroupPermissionMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return vGroupPermissions;
	}
	
	@Override
	public List<VGroupPermission> getPaginated(VGroupPermissionExample example) {
		return vGroupPermissionMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(VGroupPermission aVGroupPermission,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		VGroupPermissionExample tableConditions = new VGroupPermissionExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aVGroupPermission,tableConditions,conditions);			
			int rows = vGroupPermissionMapper.updateByExample(aVGroupPermission, tableConditions);
			if (rows==0){
				vGroupPermissionMapper.insert(aVGroupPermission);
			}
		}
		else
		{
			vGroupPermissionMapper.insert(aVGroupPermission);
		}
	}
	
	private VGroupPermissionExample createUpdateCriteria(VGroupPermission aVGroupPermission,VGroupPermissionExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aVGroupPermission);
		for (int i=0;i<numberOfConditions;i++){
			String condition=conditions[i];
			condition= condition.replace("_", "");
			Object value = proxyEntity.get(condition);
			if(value != null){
				proxyCriteria.invoke(getNameMethod(condition), new Class<?>[]{value.getClass()}, new Object[] {value});
			}
		}
		return tableConditions;
	}
	
	private String getNameMethod(String key){
		StringBuffer buffer = new StringBuffer();
		buffer.append("and");
		buffer.append(key.substring(0, 1).toUpperCase());
		buffer.append(key.substring(1,key.length()));
		buffer.append("EqualTo");
		return buffer.toString();
	}
}
	