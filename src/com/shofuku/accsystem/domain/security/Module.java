package com.shofuku.accsystem.domain.security;

public class Module {

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
