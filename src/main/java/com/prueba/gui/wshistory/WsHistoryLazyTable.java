package com.prueba.gui.wshistory;

import java.beans.IntrospectionException;
import java.util.List;

import com.prueba.gui.DataModelLazy;
import com.prueba.model.wshistory.WsHistory;
import com.prueba.model.wshistory.WsHistoryExample;
import com.prueba.persistence.PaginationContext;
import com.prueba.service.wshistory.WsHistoryService;
import com.prueba.util.introspector.BeanProxy;

public class WsHistoryLazyTable extends DataModelLazy<WsHistory, WsHistoryExample, WsHistoryService, WsHistorySearch> {

	private static final long serialVersionUID = 1L;

	public WsHistoryLazyTable(WsHistoryService service,PaginationContext paginationContext, WsHistorySearch search) {
		super(service, paginationContext, search);
		try {
			WsHistoryExample example = new WsHistoryExample();
			beanProxy = new BeanProxy(example.createCriteria());

		} catch (IntrospectionException e) {
			LOGGER.error("Error al crear el beanIntrospector en WsHistoryLazyTable : "+ e.getMessage());
		}
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public WsHistory getRowData(String rowKey) {
		for (WsHistory entity : (List<WsHistory>) getWrappedData()) {
			if (entity.equals(rowKey))
				return entity;
		}
		return null;
	}

	@Override
	public Object getRowKey(WsHistory entity) {
		return entity;
	}

}