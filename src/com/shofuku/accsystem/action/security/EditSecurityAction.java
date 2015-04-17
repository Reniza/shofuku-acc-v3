package com.shofuku.accsystem.action.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.security.UserRole;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class EditSecurityAction extends ActionSupport{

	private String securityModule;
	private String forWhat;
	private String forWhatDisplay;

	private String moduleParameter;
	UserAccount user;
	UserRole role;
	
	List roleList;
	SecurityManager manager = new SecurityManager();

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}


	public String execute() throws Exception {
		Session session = getSession();
		try {
			forWhatDisplay="edit";
			roleList = manager.listAlphabeticalAscByParameter(UserRole.class, "userRoleName", session);
			if (getSecurityModule().equalsIgnoreCase("userAccount")) {
				UserAccount users = new UserAccount();
				users = (UserAccount) manager.listSecurityByParameter(
						UserAccount.class, "userName",
						this.getUser().getUserName(),session).get(0);
				this.setUser(users);
				return "userAccount";
			} else {
				UserRole userRole = new UserRole();
				userRole = (UserRole) manager.listSecurityByParameter(
						UserRole.class, "userRoleName",	this.getRole().getUserRoleName(),session).get(0);
				
				this.setRole(userRole);
				return "userRole";
			}		

		} catch (RuntimeException re) {
			if (getSecurityModule().equalsIgnoreCase("userAccount")) {
				return "userAccount";
			}else {
				return "userRole";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}


	public String getSecurityModule() {
		return securityModule;
	}


	public void setSecurityModule(String securityModule) {
		this.securityModule = securityModule;
	}


	public String getForWhat() {
		return forWhat;
	}


	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}


	public String getForWhatDisplay() {
		return forWhatDisplay;
	}


	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}


	public String getModuleParameter() {
		return moduleParameter;
	}


	public void setModuleParameter(String moduleParameter) {
		this.moduleParameter = moduleParameter;
	}


	public UserAccount getUser() {
		return user;
	}


	public void setUser(UserAccount user) {
		this.user = user;
	}


	public UserRole getRole() {
		return role;
	}


	public void setRole(UserRole role) {
		this.role = role;
	}


	public List getRoleList() {
		return roleList;
	}


	public void setRoleList(List roleList) {
		this.roleList = roleList;
	}
	
}