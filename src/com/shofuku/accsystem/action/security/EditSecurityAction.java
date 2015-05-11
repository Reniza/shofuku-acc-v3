package com.shofuku.accsystem.action.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.Module;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.security.Role;
import com.shofuku.accsystem.helpers.UserRoleHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class EditSecurityAction extends ActionSupport{

	private String securityModule;
	private String forWhat;
	private String forWhatDisplay;

	private String moduleParameter;
	UserAccount user;
	Role role;
	
	List roleList;
	SecurityManager securityManager = new SecurityManager();

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	private List modulesNotGrantedList = new ArrayList<>();
	private List modulesGrantedList= new ArrayList<>();
	
	private Map<Integer,String> modulesGrantedMap;
	
	UserRoleHelper roleHelper = new UserRoleHelper();
	

	public String execute() throws Exception {
		Session session = getSession();
		try {
			forWhatDisplay="edit";
			roleList = securityManager.listAlphabeticalAscByParameter(Role.class, "roleName", session);
			if (getSecurityModule().equalsIgnoreCase("userAccount")) {
				UserAccount users = new UserAccount();
				users = (UserAccount) securityManager.listSecurityByParameter(
						UserAccount.class, "userName",
						this.getUser().getUserName(),session).get(0);
				this.setUser(users);
				
				return "userAccount";
			} else {
				Role userRole = new Role();
				userRole = (Role) securityManager.listSecurityByParameter(
						Role.class, "roleName",	this.getRole().getRoleName(),session).get(0);
				
				this.setRole(userRole);

				modulesNotGrantedList=roleHelper.loadModules();
				modulesGrantedMap = roleHelper.parseModulesListToMap(modulesNotGrantedList);
				modulesGrantedList = roleHelper.addRolesAccessStringToGrantedList(role,modulesGrantedMap);
				modulesNotGrantedList = roleHelper.removeGrantedModulesToAvailableModulesList(modulesGrantedList,modulesNotGrantedList);
				
				
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


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	public List getRoleList() {
		return roleList;
	}


	public void setRoleList(List roleList) {
		this.roleList = roleList;
	}
	
	
	public List getModulesNotGrantedList() {
		return modulesNotGrantedList;
	}
	public void setModulesNotGrantedList(List modulesNotGrantedList) {
		this.modulesNotGrantedList = modulesNotGrantedList;
	}
	public List getModulesGrantedList() {
		return modulesGrantedList;
	}
	public void setModulesGrantedList(List modulesGrantedList) {
		this.modulesGrantedList = modulesGrantedList;
	}
	public Map getModulesGrantedMap() {
		return modulesGrantedMap;
	}
	public void setModulesGrantedMap(Map modulesGrantedMap) {
		this.modulesGrantedMap = modulesGrantedMap;
	}
	
}
