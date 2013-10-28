package com.prueba.service.grouppermission;

				
				
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.grouppermission.GroupPermission;
import com.prueba.persistence.grouppermission.GroupPermissionMapper;
import com.prueba.persistence.PaginationContext;
import com.prueba.model.grouppermission.GroupPermissionExample;
import com.prueba.model.grouppermission.GroupPermissionExample.Criteria;
import com.prueba.util.introspector.BeanProxy;
import com.prueba.util.Constants;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos GroupPermission.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos GroupPermission.
 *
 */ 
 
@Service("groupPermissionService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class GroupPermissionServiceImpl implements GroupPermissionService {
	@Autowired
	private GroupPermissionMapper groupPermissionMapper;
	private GroupPermissionExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public GroupPermissionServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(GroupPermission.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios GroupPermission : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(GroupPermission aGroupPermission) {
		groupPermissionMapper.insert(aGroupPermission);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(GroupPermission aGroupPermission) {
					groupPermissionMapper.updateByPrimaryKey(aGroupPermission);
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(GroupPermission aGroupPermission, GroupPermissionExample aGroupPermissionExample) {
					groupPermissionMapper.updateByExample(aGroupPermission, aGroupPermissionExample);
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(GroupPermission aGroupPermission) {
					GroupPermissionExample aGroupPermissionExample = new GroupPermissionExample();
			aGroupPermissionExample.createCriteria().andIdEqualTo(aGroupPermission.getId());
			groupPermissionMapper.deleteByExample(aGroupPermissionExample);
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(GroupPermissionExample aGroupPermissionExample) {
					groupPermissionMapper.deleteByExample(aGroupPermissionExample);
			}

	public List<GroupPermission> getAll() {		
		return groupPermissionMapper.selectByExample(new GroupPermissionExample());
	}

	public List<GroupPermission> getAll(GroupPermissionExample aGroupPermissionExample) {		
		return groupPermissionMapper.selectByExample(aGroupPermissionExample);
	}
	
	public int getTotal(){
		return groupPermissionMapper.countByExample(new GroupPermissionExample());
	}
	
	public int getTotal(GroupPermissionExample aGroupPermissionExample){
		return groupPermissionMapper.countByExample(aGroupPermissionExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
			groupPermissionMapper.deleteAllData();
		}
	
	public List<GroupPermission> findSearch(GroupPermissionExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(groupPermissionMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  groupPermissionMapper.selectByExamplePagination(example);
	}

	public List<GroupPermission> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<GroupPermission> groupPermissions = null;
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
		paginationContext.updateTotalCount(groupPermissionMapper.countByExample(example));
		groupPermissions = groupPermissionMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return groupPermissions;
	}
	
	@Override
	public List<GroupPermission> getPaginated(GroupPermissionExample example) {
		return groupPermissionMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(GroupPermission aGroupPermission,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		GroupPermissionExample tableConditions = new GroupPermissionExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aGroupPermission,tableConditions,conditions);			
			int rows = groupPermissionMapper.updateByExample(aGroupPermission, tableConditions);
			if (rows==0){
				groupPermissionMapper.insert(aGroupPermission);
			}
		}
		else
		{
			groupPermissionMapper.insert(aGroupPermission);
		}
	}
	
	private GroupPermissionExample createUpdateCriteria(GroupPermission aGroupPermission,GroupPermissionExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aGroupPermission);
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
	