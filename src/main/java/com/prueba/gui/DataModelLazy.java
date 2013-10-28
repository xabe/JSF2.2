package com.prueba.gui;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.gui.search.Search;
import com.prueba.model.CriteriaBase;
import com.prueba.model.EntityBase;
import com.prueba.model.ExampleBase;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.ServiceBase;
import com.prueba.util.WordsConverter;
import com.prueba.util.introspector.BeanProxy;

public abstract class DataModelLazy <T extends EntityBase, D extends ExampleBase, S extends ServiceBase<T, D>, E extends Search<D>> extends LazyDataModel<T>{

	protected static final Logger LOGGER = LoggerFactory.getLogger(DataModelLazy.class);
	private static final long serialVersionUID = 1L;
	private S service;
	private PaginationContext paginationContext;
	protected BeanProxy beanProxy;	
	private E search;

	public DataModelLazy(S service, PaginationContext paginationContext, E search){
		this.service = service;
		this.search = search;
		this.paginationContext = paginationContext;
	}
	
	public D getExampleOriginal() {
		return search.createExampleSearch();
	}
  
	protected int getCurrentPage(int skipResults,int maxResults) {
		return 1+(skipResults/maxResults);
	}
	
	public PaginationContext getPaginationContext() {
		return paginationContext;
	}
	
	public void setPaginationContext(PaginationContext paginationContext) {
		this.paginationContext = paginationContext;
	}
	
	public S getService() {
		return service;
	}
	
	public void setService(S service) {
		this.service = service;
	}
	
	protected CriteriaBase getLastCriteria(D example){
		List< ? extends CriteriaBase> criterias = example.getOredCriteria();
		if(criterias == null || criterias.size() == 0)
		{
			return example.createCriteria();
		}
		else
		{
			return criterias.get(criterias.size() - 1);
		}
	}	
	
	public D createExampleFilter(D example, Map<String,String> filters) {
 		try
		{
			String value = "";
			CriteriaBase criteria = getLastCriteria(example);
			beanProxy.setBean(criteria);		
			for(Entry<String, String> entry: filters.entrySet()){
				value = entry.getValue().trim();
				if(value != null && value.length() > 0){
					if(entry.getValue().contains("*")){
						value = value.replace('*', '%');
					}
					else
					{
						value += "%";
					}
					beanProxy.invoke("and"+WordsConverter.getInstance().camelCase(entry.getKey())+"Like", new Class<?>[] {String.class}, new Object[]{value});
				}
			}
		}catch (Exception e) {
			LOGGER.error("Error al filtrar : "+e.getMessage());
		}	
		return example;
	}
	
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
		D example = getExampleOriginal();
		if(sortField != null)
		{
			example.setOrderByClause(sortField + (sortOrder == SortOrder.ASCENDING ?" ASC":" DESC"));
		}
		createExampleFilter(example, filters);		
		List<T> list = getService().findSearch(example, getPaginationContext(), getCurrentPage(first, getPaginationContext().getMaxResults()));
		
		this.setPageSize(getPaginationContext().getMaxResults());
	    this.setRowCount(getPaginationContext().getTotalCount());  
		return list;
	}
}
