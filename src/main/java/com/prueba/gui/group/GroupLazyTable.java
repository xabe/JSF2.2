package com.prueba.gui.group;

import java.beans.IntrospectionException;
import java.util.List;

import com.prueba.gui.DataModelLazy;
import com.prueba.model.group.Group;
import com.prueba.model.group.GroupExample;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.group.GroupService;
import com.prueba.util.introspector.BeanProxy;

public class GroupLazyTable extends DataModelLazy<Group, GroupExample, GroupService, GroupSearch> {

	private static final long serialVersionUID = 1L;

	public GroupLazyTable(GroupService service,PaginationContext paginationContext, GroupSearch search) {
		super(service, paginationContext, search);
		try {
			GroupExample example = new GroupExample();
			beanProxy = new BeanProxy(example.createCriteria());

		} catch (IntrospectionException e) {
			LOGGER.error("Error al crear el beanIntrospector en GroupLazyTable : "+ e.getMessage());
		}
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public Group getRowData(String rowKey) {
		for (Group entity : (List<Group>) getWrappedData()) {
			if (entity.equals(rowKey))
				return entity;
		}
		return null;
	}

	@Override
	public Object getRowKey(Group entity) {
		return entity;
	}

}