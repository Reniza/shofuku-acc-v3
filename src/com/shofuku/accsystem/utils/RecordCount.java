package com.shofuku.accsystem.utils;

import java.io.Serializable;

public class RecordCount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7831644834075899280L;


	private int id;
	
	private String moduleName;
	private int recordCount;
	
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
