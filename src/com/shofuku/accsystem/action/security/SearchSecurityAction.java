package com.shofuku.accsystem.action.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.security.Module;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.security.Role;
import com.shofuku.accsystem.helpers.UserRoleHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchSecurityAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5977333283601416207L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");

	private String securityModule;
	private String moduleParameter;
	private String moduleParameterValue;
	
	private String clicked;
	List securityList;
	private List modulesNotGrantedList= new ArrayList<>();
	private List modulesGrantedList= new ArrayList<>();
	
	private Map modulesGrantedMap;
	

	SecurityManager securityManager = (SecurityManager) actionSession.get("securityManager");
	UserRoleHelper roleHelper = new UserRoleHelper();

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		try {
			if (getClicked().equals("true")) {

				if (null != getModuleParameter()&& getSecurityModule().equalsIgnoreCase("userAccount")) {
					
					if (getModuleParameter().equalsIgnoreCase("userName") || getModuleParameter().equalsIgnoreCase("fullName")) {
						securityList = securityManager.listToolsByParameterLike(
										UserAccount.class, moduleParameter,
										moduleParameterValue,session);
					}else {
						securityList = securityManager.listAlphabeticalAscByParameter(UserAccount.class, "fullName", session);
						moduleParameterValue="all";
					} 
						if (securityList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
						
					return "userAccount";
				} else if (null != getModuleParameter()&& getSecurityModule().equalsIgnoreCase("userRole")) {
					
					if (getModuleParameter().equalsIgnoreCase("roleName")) {
						securityList = securityManager.listToolsByParameterLike(
										Role.class, moduleParameter,
										moduleParameterValue,session);
					}else  {
						securityList = securityManager.listAlphabeticalAscByParameter(Role.class, "roleName", session);
						moduleParameterValue="all";
					} 
						if (securityList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					
					return "userRole";
				} 
			}
		moduleParameterValue="";
		return "populateList";
		
		} catch (RuntimeException re) {
			if (getSecurityModule().equals("userAccount")){
				return "userAccount";
			}else {
				return "userRole";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}

	//this method is strictly for this action class only
	public String loadModules() {
		
		Session session = getSession();
		
		try{
			modulesNotGrantedList = roleHelper.loadModules();
			
		} catch (IndexOutOfBoundsException iobe3) {
			addActionError("ERROR LOADING MODULES");
			return ERROR;
		}finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
		return SUCCESS;
		
	}
	
	
	public String getSecurityModule() {
		return securityModule;
	}
	public void setSecurityModule(String securityModule) {
		this.securityModule = securityModule;
	}
	public String getModuleParameter() {
		return moduleParameter;
	}
	public void setModuleParameter(String moduleParameter) {
		this.moduleParameter = moduleParameter;
	}
	public String getModuleParameterValue() {
		return moduleParameterValue;
	}
	public void setModuleParameterValue(String moduleParameterValue) {
		this.moduleParameterValue = moduleParameterValue;
	}
	public String getClicked() {
		return clicked;
	}
	public void setClicked(String clicked) {
		this.clicked = clicked;
	}
	public List getSecurityList() {
		return securityList;
	}
	public void setSecurityList(List securityList) {
		this.securityList = securityList;
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
