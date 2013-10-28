package com.prueba.model;

import java.util.List;

import com.prueba.model.CriteriaBase;

public abstract class ExampleBase {
	private Integer limit;
	private Integer offset;
		
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
		
	public Integer getLimit() {
		return limit;
	}
	
	public Integer getOffset() {
		return offset;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	public abstract void setOrderByClause(String orderByClause);
	 
	public abstract List< ? extends CriteriaBase> getOredCriteria();
	 
	public abstract CriteriaBase createCriteria();
}
