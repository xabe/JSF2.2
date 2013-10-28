package com.prueba.gui.group;

import com.prueba.gui.exporter.Exporter;
import com.prueba.model.group.Group;

public class GroupExporter extends Exporter<Group>{
	private static final long serialVersionUID = 1L;  
	
	public GroupExporter() {
		init(Group.class);
	}
}