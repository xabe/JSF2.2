package com.prueba.service.usergroup;

				
				
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.usergroup.UserGroup;
import com.prueba.persistence.usergroup.UserGroupMapper;
import com.prueba.persistence.PaginationContext;
import com.prueba.model.usergroup.UserGroupExample;
import com.prueba.model.usergroup.UserGroupExample.Criteria;
import com.prueba.util.introspector.BeanProxy;
import com.prueba.util.Constants;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos UserGroup.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos UserGroup.
 *
 */ 
 
@Service("userGroupService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class UserGroupServiceImpl implements UserGroupService {
	@Autowired
	private UserGroupMapper userGroupMapper;
	private UserGroupExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public UserGroupServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(UserGroup.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios UserGroup : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(UserGroup aUserGroup) {
		userGroupMapper.insert(aUserGroup);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(UserGroup aUserGroup) {
					userGroupMapper.updateByPrimaryKey(aUserGroup);
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(UserGroup aUserGroup, UserGroupExample aUserGroupExample) {
					userGroupMapper.updateByExample(aUserGroup, aUserGroupExample);
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(UserGroup aUserGroup) {
					UserGroupExample aUserGroupExample = new UserGroupExample();
			aUserGroupExample.createCriteria().andIdEqualTo(aUserGroup.getId());
			userGroupMapper.deleteByExample(aUserGroupExample);
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(UserGroupExample aUserGroupExample) {
					userGroupMapper.deleteByExample(aUserGroupExample);
			}

	public List<UserGroup> getAll() {		
		return userGroupMapper.selectByExample(new UserGroupExample());
	}

	public List<UserGroup> getAll(UserGroupExample aUserGroupExample) {		
		return userGroupMapper.selectByExample(aUserGroupExample);
	}
	
	public int getTotal(){
		return userGroupMapper.countByExample(new UserGroupExample());
	}
	
	public int getTotal(UserGroupExample aUserGroupExample){
		return userGroupMapper.countByExample(aUserGroupExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
			userGroupMapper.deleteAllData();
		}
	
	public List<UserGroup> findSearch(UserGroupExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(userGroupMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  userGroupMapper.selectByExamplePagination(example);
	}

	public List<UserGroup> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<UserGroup> userGroups = null;
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
		paginationContext.updateTotalCount(userGroupMapper.countByExample(example));
		userGroups = userGroupMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return userGroups;
	}
	
	@Override
	public List<UserGroup> getPaginated(UserGroupExample example) {
		return userGroupMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(UserGroup aUserGroup,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		UserGroupExample tableConditions = new UserGroupExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aUserGroup,tableConditions,conditions);			
			int rows = userGroupMapper.updateByExample(aUserGroup, tableConditions);
			if (rows==0){
				userGroupMapper.insert(aUserGroup);
			}
		}
		else
		{
			userGroupMapper.insert(aUserGroup);
		}
	}
	
	private UserGroupExample createUpdateCriteria(UserGroup aUserGroup,UserGroupExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aUserGroup);
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
	