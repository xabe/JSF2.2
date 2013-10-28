package com.prueba.gui.permission;

import com.prueba.gui.exporter.Exporter;
import com.prueba.model.permission.Permission;

public class PermissionExporter extends Exporter<Permission>{
	private static final long serialVersionUID = 1L;  
	
	public PermissionExporter() {
		init(Permission.class);
	}
}