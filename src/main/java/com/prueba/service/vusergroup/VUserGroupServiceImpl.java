package com.prueba.service.vusergroup;

					
					
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.vusergroup.VUserGroup;
import com.prueba.persistence.vusergroup.VUserGroupMapper;
import com.prueba.persistence.PaginationContext;
import com.prueba.model.vusergroup.VUserGroupExample;
import com.prueba.model.vusergroup.VUserGroupExample.Criteria;
import com.prueba.util.introspector.BeanProxy;
import com.prueba.util.Constants;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos VUserGroup.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos VUserGroup.
 *
 */ 
 
@Service("vUserGroupService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class VUserGroupServiceImpl implements VUserGroupService {
	@Autowired
	private VUserGroupMapper vUserGroupMapper;
	private VUserGroupExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public VUserGroupServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(VUserGroup.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios VUserGroup : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(VUserGroup aVUserGroup) {
		vUserGroupMapper.insert(aVUserGroup);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(VUserGroup aVUserGroup) {
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(VUserGroup aVUserGroup, VUserGroupExample aVUserGroupExample) {
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(VUserGroup aVUserGroup) {
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(VUserGroupExample aVUserGroupExample) {
			}

	public List<VUserGroup> getAll() {		
		return vUserGroupMapper.selectByExample(new VUserGroupExample());
	}

	public List<VUserGroup> getAll(VUserGroupExample aVUserGroupExample) {		
		return vUserGroupMapper.selectByExample(aVUserGroupExample);
	}
	
	public int getTotal(){
		return vUserGroupMapper.countByExample(new VUserGroupExample());
	}
	
	public int getTotal(VUserGroupExample aVUserGroupExample){
		return vUserGroupMapper.countByExample(aVUserGroupExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
		}
	
	public List<VUserGroup> findSearch(VUserGroupExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(vUserGroupMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  vUserGroupMapper.selectByExamplePagination(example);
	}

	public List<VUserGroup> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<VUserGroup> vUserGroups = null;
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
		paginationContext.updateTotalCount(vUserGroupMapper.countByExample(example));
		vUserGroups = vUserGroupMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return vUserGroups;
	}
	
	@Override
	public List<VUserGroup> getPaginated(VUserGroupExample example) {
		return vUserGroupMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(VUserGroup aVUserGroup,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		VUserGroupExample tableConditions = new VUserGroupExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aVUserGroup,tableConditions,conditions);			
			int rows = vUserGroupMapper.updateByExample(aVUserGroup, tableConditions);
			if (rows==0){
				vUserGroupMapper.insert(aVUserGroup);
			}
		}
		else
		{
			vUserGroupMapper.insert(aVUserGroup);
		}
	}
	
	private VUserGroupExample createUpdateCriteria(VUserGroup aVUserGroup,VUserGroupExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aVUserGroup);
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
	