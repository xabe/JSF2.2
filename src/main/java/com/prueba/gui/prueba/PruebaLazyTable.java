package com.prueba.gui.prueba;

import java.beans.IntrospectionException;
import java.util.List;

import com.prueba.gui.DataModelLazy;
import com.prueba.model.prueba.Prueba;
import com.prueba.model.prueba.PruebaExample;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.prueba.PruebaService;
import com.prueba.util.introspector.BeanProxy;

public class PruebaLazyTable extends DataModelLazy<Prueba, PruebaExample, PruebaService, PruebaSearch> {

	private static final long serialVersionUID = 1L;

	public PruebaLazyTable(PruebaService service,PaginationContext paginationContext, PruebaSearch search) {
		super(service, paginationContext, search);
		try {
			PruebaExample example = new PruebaExample();
			beanProxy = new BeanProxy(example.createCriteria());

		} catch (IntrospectionException e) {
			LOGGER.error("Error al crear el beanIntrospector en PruebaLazyTable : "+ e.getMessage());
		}
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public Prueba getRowData(String rowKey) {
		for (Prueba entity : (List<Prueba>) getWrappedData()) {
			if (entity.equals(rowKey))
				return entity;
		}
		return null;
	}

	@Override
	public Object getRowKey(Prueba entity) {
		return entity;
	}

}