package com.shofuku.accsystem.controllers;

import com.shofuku.accsystem.domain.security.UserAccount;

public class BaseController {

	private UserAccount user;
	public UserAccount getUser() {
		return user;
	}
	public void setUser(UserAccount user) {
		this.user = user;
	}
	
	
}
