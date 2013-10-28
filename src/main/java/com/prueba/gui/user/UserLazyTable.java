package com.prueba.gui.user;

import java.beans.IntrospectionException;
import java.util.List;

import com.prueba.gui.DataModelLazy;
import com.prueba.model.user.User;
import com.prueba.model.user.UserExample;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.user.UserService;
import com.prueba.util.introspector.BeanProxy;

public class UserLazyTable extends DataModelLazy<User, UserExample, UserService, UserSearch> {

	private static final long serialVersionUID = 1L;

	public UserLazyTable(UserService service,PaginationContext paginationContext, UserSearch search) {
		super(service, paginationContext, search);
		try {
			UserExample example = new UserExample();
			beanProxy = new BeanProxy(example.createCriteria());

		} catch (IntrospectionException e) {
			LOGGER.error("Error al crear el beanIntrospector en UserLazyTable : "+ e.getMessage());
		}
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public User getRowData(String rowKey) {
		for (User entity : (List<User>) getWrappedData()) {
			if (entity.equals(rowKey))
				return entity;
		}
		return null;
	}

	@Override
	public Object getRowKey(User entity) {
		return entity;
	}

}