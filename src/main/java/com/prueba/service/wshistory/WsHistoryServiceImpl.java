package com.prueba.service.wshistory;

										
										
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.model.wshistory.WsHistory;
import com.prueba.persistence.wshistory.WsHistoryMapper;
import com.prueba.persistence.PaginationContext;
import com.prueba.model.wshistory.WsHistoryExample;
import com.prueba.model.wshistory.WsHistoryExample.Criteria;
import com.prueba.util.introspector.BeanProxy;
import com.prueba.util.Constants;

/**
 * Servicio que gestiona todas las operaciones CRUD 
 * sobre objetos WsHistory.
 * Esta clase a parte de gestionar dichas operaciones CRUD se puede extender
 * para realizar otro tipo de procesamientos sobre los objetos WsHistory.
 *
 */ 
 
@Service("wsHistoryService")
@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
public class WsHistoryServiceImpl implements WsHistoryService {
	@Autowired
	private WsHistoryMapper wsHistoryMapper;
	private WsHistoryExample example;
	private BeanProxy proxyEntity;
	private BeanProxy proxyCriteria;
	
	public WsHistoryServiceImpl() {
		try
		{
			proxyCriteria = new BeanProxy(Criteria.class);
			proxyEntity = new BeanProxy(WsHistory.class);
		}catch (Exception e) {
			LOGGER.error("Error los proxy de los servicios WsHistory : "+e.getMessage());
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void add(WsHistory aWsHistory) {
		wsHistoryMapper.insert(aWsHistory);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(WsHistory aWsHistory) {
					wsHistoryMapper.updateByPrimaryKey(aWsHistory);
			}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void update(WsHistory aWsHistory, WsHistoryExample aWsHistoryExample) {
					wsHistoryMapper.updateByExample(aWsHistory, aWsHistoryExample);
			}

		@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(WsHistory aWsHistory) {
					WsHistoryExample aWsHistoryExample = new WsHistoryExample();
			aWsHistoryExample.createCriteria().andIdEqualTo(aWsHistory.getId());
			wsHistoryMapper.deleteByExample(aWsHistoryExample);
			}
		
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void delete(WsHistoryExample aWsHistoryExample) {
					wsHistoryMapper.deleteByExample(aWsHistoryExample);
			}

	public List<WsHistory> getAll() {		
		return wsHistoryMapper.selectByExample(new WsHistoryExample());
	}

	public List<WsHistory> getAll(WsHistoryExample aWsHistoryExample) {		
		return wsHistoryMapper.selectByExample(aWsHistoryExample);
	}
	
	public int getTotal(){
		return wsHistoryMapper.countByExample(new WsHistoryExample());
	}
	
	public int getTotal(WsHistoryExample aWsHistoryExample){
		return wsHistoryMapper.countByExample(aWsHistoryExample);
	}	

		
	//Borra todos los datos de la tabla
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void deleteAllData(){
			wsHistoryMapper.deleteAllData();
		}
	
	public List<WsHistory> findSearch(WsHistoryExample example,
			PaginationContext paginationContext, int page) {
		if(page < 1)
		{
			page = 1;
		}
		this.example = example;
		paginationContext.setSkipResults(paginationContext.getMaxResults() * (page - 1));
		paginationContext.updateTotalCount(wsHistoryMapper.countByExample(example));
		example.setLimit(paginationContext.getSkipResults() + paginationContext.getMaxResults());
		example.setOffset(paginationContext.getSkipResults());
		return  wsHistoryMapper.selectByExamplePagination(example);
	}

	public List<WsHistory> getPaginated(String operation, PaginationContext paginationContext) { //previous,next,first,last,
		List<WsHistory> wsHistories = null;
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
		paginationContext.updateTotalCount(wsHistoryMapper.countByExample(example));
		wsHistories = wsHistoryMapper.selectByExamplePagination(example);

		// Imprime los resultados
		String msg = String.format(
				"skipResults %d de un total de %d records, en paginas de %d ",
				paginationContext.getSkipResults(), paginationContext
						.getTotalCount(), paginationContext.getMaxResults());
		LOGGER.debug(msg);
		return wsHistories;
	}
	
	@Override
	public List<WsHistory> getPaginated(WsHistoryExample example) {
		return wsHistoryMapper.selectByExamplePagination(example);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED)
	public void updateOrInsert(WsHistory aWsHistory,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException{		
		WsHistoryExample tableConditions = new WsHistoryExample();
		if (conditions.length>0){
			tableConditions= createUpdateCriteria(aWsHistory,tableConditions,conditions);			
			int rows = wsHistoryMapper.updateByExample(aWsHistory, tableConditions);
			if (rows==0){
				wsHistoryMapper.insert(aWsHistory);
			}
		}
		else
		{
			wsHistoryMapper.insert(aWsHistory);
		}
	}
	
	private WsHistoryExample createUpdateCriteria(WsHistory aWsHistory,WsHistoryExample tableConditions,String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException {
		int numberOfConditions=conditions.length;
		Criteria criteria = tableConditions.createCriteria();
		proxyCriteria.setBean(criteria);
		proxyEntity.setBean(aWsHistory);
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
	