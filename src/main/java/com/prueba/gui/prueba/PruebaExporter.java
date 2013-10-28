package com.prueba.gui.prueba;

import com.prueba.gui.exporter.Exporter;
import com.prueba.model.prueba.Prueba;

public class PruebaExporter extends Exporter<Prueba>{
	private static final long serialVersionUID = 1L;  
	
	public PruebaExporter() {
		init(Prueba.class);
	}
}