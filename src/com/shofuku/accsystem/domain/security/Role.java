package com.shofuku.accsystem.domain.security;

import java.io.Serializable;

public class Role implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 967830983335276636L;
	
	private int roleId;
	private String roleName;
	private String roleAccessString;
	
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleAccessString() {
		return roleAccessString;
	}
	public void setRoleAccessString(String roleAccessString) {
		this.roleAccessString = roleAccessString;
	}
	
	

}
