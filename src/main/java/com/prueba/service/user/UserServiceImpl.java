package com.prueba.service.user;

															
															
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.user.User;
import com.prueba.persistence.user.UserMapper;
import com.prueba.persistence.PaginationContext;
import com.prueba.model.user.UserExample;
import com.prueba.model.user.UserExample.Criteria;
import com.prueba.util.introspector.BeanProxy;
import com.prueba.util.Constants;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos User.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos User.
 *
 */ 
 
@Service("userService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	private UserExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public UserServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(User.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios User : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(User aUser) {
		userMapper.insert(aUser);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(User aUser) {
					userMapper.updateByPrimaryKey(aUser);
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(User aUser, UserExample aUserExample) {
					userMapper.updateByExample(aUser, aUserExample);
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(User aUser) {
					UserExample aUserExample = new UserExample();
			aUserExample.createCriteria().andIdEqualTo(aUser.getId());
			userMapper.deleteByExample(aUserExample);
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(UserExample aUserExample) {
					userMapper.deleteByExample(aUserExample);
			}

	public List<User> getAll() {		
		return userMapper.selectByExample(new UserExample());
	}

	public List<User> getAll(UserExample aUserExample) {		
		return userMapper.selectByExample(aUserExample);
	}
	
	public int getTotal(){
		return userMapper.countByExample(new UserExample());
	}
	
	public int getTotal(UserExample aUserExample){
		return userMapper.countByExample(aUserExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
			userMapper.deleteAllData();
		}
	
	public List<User> findSearch(UserExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(userMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  userMapper.selectByExamplePagination(example);
	}

	public List<User> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<User> users = null;
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
		paginationContext.updateTotalCount(userMapper.countByExample(example));
		users = userMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return users;
	}
	
	@Override
	public List<User> getPaginated(UserExample example) {
		return userMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(User aUser,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		UserExample tableConditions = new UserExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aUser,tableConditions,conditions);			
			int rows = userMapper.updateByExample(aUser, tableConditions);
			if (rows==0){
				userMapper.insert(aUser);
			}
		}
		else
		{
			userMapper.insert(aUser);
		}
	}
	
	private UserExample createUpdateCriteria(User aUser,UserExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aUser);
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
	