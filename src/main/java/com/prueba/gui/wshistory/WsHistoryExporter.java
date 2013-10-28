package com.prueba.gui.wshistory;

import com.prueba.gui.exporter.Exporter;
import com.prueba.model.wshistory.WsHistory;

public class WsHistoryExporter extends Exporter<WsHistory>{
	private static final long serialVersionUID = 1L;  
	
	public WsHistoryExporter() {
		init(WsHistory.class);
	}
}