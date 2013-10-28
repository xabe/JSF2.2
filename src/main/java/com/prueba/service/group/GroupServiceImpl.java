package com.prueba.service.group;

			
			
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.group.Group;
import com.prueba.model.group.GroupExample;
import com.prueba.model.group.GroupExample.Criteria;
import com.prueba.persistence.PaginationContext;
import com.prueba.persistence.group.GroupMapper;
import com.prueba.util.Constants;
import com.prueba.util.introspector.BeanProxy;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos Group.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos Group.
 *
 */ 
 
@Service("groupService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupMapper groupMapper;
	private GroupExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public GroupServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(Group.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios Group : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(Group aGroup) {
		groupMapper.insert(aGroup);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(Group aGroup) {
					groupMapper.updateByPrimaryKey(aGroup);
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(Group aGroup, GroupExample aGroupExample) {
					groupMapper.updateByExample(aGroup, aGroupExample);
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(Group aGroup) {
					GroupExample aGroupExample = new GroupExample();
			aGroupExample.createCriteria().andIdEqualTo(aGroup.getId());
			groupMapper.deleteByExample(aGroupExample);
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(GroupExample aGroupExample) {
					groupMapper.deleteByExample(aGroupExample);
			}

	public List<Group> getAll() {		
		return groupMapper.selectByExample(new GroupExample());
	}

	public List<Group> getAll(GroupExample aGroupExample) {		
		return groupMapper.selectByExample(aGroupExample);
	}
	
	public int getTotal(){
		return groupMapper.countByExample(new GroupExample());
	}
	
	public int getTotal(GroupExample aGroupExample){
		return groupMapper.countByExample(aGroupExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
			groupMapper.deleteAllData();
		}
	
	public List<Group> findSearch(GroupExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(groupMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  groupMapper.selectByExamplePagination(example);
	}

	public List<Group> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<Group> groups = null;
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
		paginationContext.updateTotalCount(groupMapper.countByExample(example));
		groups = groupMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return groups;
	}
	
	@Override
	public List<Group> getPaginated(GroupExample example) {
		return groupMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(Group aGroup,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		GroupExample tableConditions = new GroupExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aGroup,tableConditions,conditions);			
			int rows = groupMapper.updateByExample(aGroup, tableConditions);
			if (rows==0){
				groupMapper.insert(aGroup);
			}
		}
		else
		{
			groupMapper.insert(aGroup);
		}
	}
	
	private GroupExample createUpdateCriteria(Group aGroup,GroupExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aGroup);
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
	