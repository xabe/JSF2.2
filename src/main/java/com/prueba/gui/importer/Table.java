package com.prueba.gui.importer;

public class Table {
	private String type;
	private String []keys;
	private boolean ignoreError;
	
	public Table(String type, String []keys, boolean ignoreError) {
		this.type = type;
		this.keys = keys;
		this.ignoreError = ignoreError;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public boolean isIgnoreError() {
		return ignoreError;
	}

	public void setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
	}

}
