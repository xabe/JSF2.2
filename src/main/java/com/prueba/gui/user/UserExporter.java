package com.prueba.gui.user;

import com.prueba.gui.exporter.Exporter;
import com.prueba.model.user.User;

public class UserExporter extends Exporter<User>{
	private static final long serialVersionUID = 1L;  
	
	public UserExporter() {
		init(User.class);
	}
}