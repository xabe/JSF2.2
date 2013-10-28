package com.prueba.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.model.EntityBase;
import com.prueba.model.ExampleBase;
import com.prueba.persistence.PaginationContext;

public interface ServiceBase<T extends EntityBase, D extends ExampleBase> {
	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceBase.class);

	void add(T t);

	void update(T t);
	
	void update(T t,D d);

	void delete(T t);
	
	void delete(D d);

	List<T> getAll();
	
	List<T> getAll(D d);
	
	int getTotal();
	
	int getTotal(D d);
	
	void deleteAllData();
	
	List<T> findSearch(D example,PaginationContext paginationContext, int page);
	
	List<T> getPaginated(String operation,PaginationContext paginationContext);
	
	List<T> getPaginated(D example);

	void updateOrInsert(T aGroup, String[] conditions) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException,IllegalAccessException;

}