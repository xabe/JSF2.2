package com.prueba.service.prueba;

					
					
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.prueba.Prueba;
import com.prueba.persistence.prueba.PruebaMapper;
import com.prueba.persistence.PaginationContext;
import com.prueba.model.prueba.PruebaExample;
import com.prueba.model.prueba.PruebaExample.Criteria;
import com.prueba.util.introspector.BeanProxy;
import com.prueba.util.Constants;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos Prueba.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos Prueba.
 *
 */ 
 
@Service("pruebaService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class PruebaServiceImpl implements PruebaService {
	@Autowired
	private PruebaMapper pruebaMapper;
	private PruebaExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public PruebaServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(Prueba.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios Prueba : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(Prueba aPrueba) {
		pruebaMapper.insert(aPrueba);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(Prueba aPrueba) {
					pruebaMapper.updateByPrimaryKey(aPrueba);
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(Prueba aPrueba, PruebaExample aPruebaExample) {
					pruebaMapper.updateByExample(aPrueba, aPruebaExample);
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(Prueba aPrueba) {
					PruebaExample aPruebaExample = new PruebaExample();
			aPruebaExample.createCriteria().andIdEqualTo(aPrueba.getId());
			pruebaMapper.deleteByExample(aPruebaExample);
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(PruebaExample aPruebaExample) {
					pruebaMapper.deleteByExample(aPruebaExample);
			}

	public List<Prueba> getAll() {		
		return pruebaMapper.selectByExample(new PruebaExample());
	}

	public List<Prueba> getAll(PruebaExample aPruebaExample) {		
		return pruebaMapper.selectByExample(aPruebaExample);
	}
	
	public int getTotal(){
		return pruebaMapper.countByExample(new PruebaExample());
	}
	
	public int getTotal(PruebaExample aPruebaExample){
		return pruebaMapper.countByExample(aPruebaExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
			pruebaMapper.deleteAllData();
		}
	
	public List<Prueba> findSearch(PruebaExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(pruebaMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  pruebaMapper.selectByExamplePagination(example);
	}

	public List<Prueba> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<Prueba> pruebas = null;
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
		paginationContext.updateTotalCount(pruebaMapper.countByExample(example));
		pruebas = pruebaMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return pruebas;
	}
	
	@Override
	public List<Prueba> getPaginated(PruebaExample example) {
		return pruebaMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(Prueba aPrueba,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		PruebaExample tableConditions = new PruebaExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aPrueba,tableConditions,conditions);			
			int rows = pruebaMapper.updateByExample(aPrueba, tableConditions);
			if (rows==0){
				pruebaMapper.insert(aPrueba);
			}
		}
		else
		{
			pruebaMapper.insert(aPrueba);
		}
	}
	
	private PruebaExample createUpdateCriteria(Prueba aPrueba,PruebaExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aPrueba);
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
	