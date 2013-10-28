package com.prueba.gui.permission;

import java.beans.IntrospectionException;
import java.util.List;

import com.prueba.gui.DataModelLazy;
import com.prueba.model.permission.Permission;
import com.prueba.model.permission.PermissionExample;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.permission.PermissionService;
import com.prueba.util.introspector.BeanProxy;

public class PermissionLazyTable extends DataModelLazy<Permission, PermissionExample, PermissionService, PermissionSearch> {

	private static final long serialVersionUID = 1L;

	public PermissionLazyTable(PermissionService service,PaginationContext paginationContext, PermissionSearch search) {
		super(service, paginationContext, search);
		try {
			PermissionExample example = new PermissionExample();
			beanProxy = new BeanProxy(example.createCriteria());

		} catch (IntrospectionException e) {
			LOGGER.error("Error al crear el beanIntrospector en PermissionLazyTable : "+ e.getMessage());
		}
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public Permission getRowData(String rowKey) {
		for (Permission entity : (List<Permission>) getWrappedData()) {
			if (entity.equals(rowKey))
				return entity;
		}
		return null;
	}

	@Override
	public Object getRowKey(Permission entity) {
		return entity;
	}

}