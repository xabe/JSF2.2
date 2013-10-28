package com.prueba.persistence;

import java.util.List;

import com.prueba.model.EntityBase;
import com.prueba.model.ExampleBase;

public interface PersistenceBase <T extends EntityBase, D extends ExampleBase> {

	void deleteAllData();
	
	List<T> selectByExamplePagination(D example);
}