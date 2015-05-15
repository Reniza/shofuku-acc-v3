package com.shofuku.accsystem.domain.security;

import java.io.Serializable;

public class Module implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4277456766424952671L;
	private int moduleId;
	private String moduleName;
	
	public Module(int id, String name) {
		this.moduleId = id;
		this.moduleName = name;
	}
	
	Module(){}
	
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
}
