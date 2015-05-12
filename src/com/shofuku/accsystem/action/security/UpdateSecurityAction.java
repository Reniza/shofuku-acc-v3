package com.shofuku.accsystem.action.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.security.Role;
import com.shofuku.accsystem.helpers.UserRoleHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class UpdateSecurityAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 934004434406221113L;
	
	String subModule;
	UserAccount user;
	Role role;
	private String forWhat;
	private String forWhatDisplay;
	
	private int userId;
	private int userRoleId;
	List roleList = null;
	String tempPassword;
	
	SecurityManager securityManager = new SecurityManager();
	

	//roles
	private List modulesNotGrantedList= new ArrayList<>();
	private List modulesGrantedList= new ArrayList<>();
	
	private Map modulesGrantedMap;
	UserRoleHelper roleHelper = new UserRoleHelper();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

public String execute() throws Exception {
	Session session = getSession();
	try {
		forWhatDisplay="edit";
		if (getSubModule().equalsIgnoreCase("userAccount")) {
			return updateUserAccount(session);
		}else {
			return updateUserRole(session);
		}
	
	}catch (RuntimeException re) {
		re.printStackTrace();
		if (getSubModule().equalsIgnoreCase("userAccount")) {
			return "userAccountUpdated";
		}else {
			return "userRoleUpdated";
		}
		
	}finally {
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
	}
	
}

private String updateUserRole(Session session) {
	// TODO Auto-generated method stub
	boolean updateResult = false;
if (validateUserRoleAccount()) {
	//	loadRoleList();
	}else {
			role.setRoleAccessString(roleHelper.parseModulesGrantedListToString(modulesGrantedList));
			updateResult = securityManager.updateSecurity(role, session); 
			if (updateResult == true) {
				addActionMessage(SASConstants.UPDATED);
				
				modulesNotGrantedList=roleHelper.loadModules();
				modulesGrantedMap = roleHelper.parseModulesListToMap(modulesNotGrantedList);
				modulesGrantedList = roleHelper.addRolesAccessStringToGrantedList(role,modulesGrantedMap);
				modulesNotGrantedList = roleHelper.removeGrantedModulesToAvailableModulesList(modulesGrantedList,modulesNotGrantedList);
				
				forWhat="true";
				forWhatDisplay ="edit";
			} else {
				addActionError(SASConstants.FAILED);
				}
		}
return "userRoleUpdated";
}

private boolean validateUserRoleAccount() {
	// TODO Auto-generated method stub
	boolean errorFound = false;
	
	if ("".equals(role.getRoleName())) {
		 addFieldError("role.roleName", "REQUIRED");
		 errorFound = true;
	}
	return errorFound;
}

private boolean validateUserAccount() {
	boolean errorFound = false;
	
	if ("".equals(user.getUserName())) {
		 addFieldError("user.userName", "REQUIRED");
		 errorFound = true;
	}if ("".equals(user.getPassword())) {
		 addFieldError("user.password", "REQUIRED");
		 errorFound = true;
	}if ("".equals(getTempPassword())) {
		 addFieldError("tempPassword", "REQUIRED");
		 errorFound = true;
	}else {
	 if (!(tempPassword.equals(user.getPassword()))) {
		 addFieldError("tempPassword", "PASSWORDS did not match");
		 errorFound = true;
	 	}
	}
	return errorFound;
}

public String loadRoleList() {
	// TODO Auto-generated method stub
	Session session = getSession();
	roleList= securityManager.listAlphabeticalAscByParameter(Role.class, "roleName",  session);
	return "userAccount";
}

private String updateUserAccount(Session session) {
	// TODO Auto-generated method stub
	boolean updateResult = false;
	user.setUserId(userId);
	if (validateUserAccount()) {
		loadRoleList();
	}else {
		Role userRole = (Role) securityManager.listSecurityByParameter(Role.class, "roleName", user.getRole().getRoleName(), session).get(0);
		this.user.setRole(userRole);
			updateResult = securityManager.updateSecurity(user, session);
			if (updateResult == true) {
					addActionMessage(SASConstants.UPDATED);
					forWhat="true";
					forWhatDisplay ="edit";
				} else {
					addActionError(SASConstants.FAILED);
					}
				}
	
	loadRoleList();
return "userAccountUpdated";
}


public String getSubModule() {
	return subModule;
}

public void setSubModule(String subModule) {
	this.subModule = subModule;
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

public List getRoleList() {
	return roleList;
}

public void setRoleList(List roleList) {
	this.roleList = roleList;
}

public String getTempPassword() {
	return tempPassword;
}

public void setTempPassword(String tempPassword) {
	this.tempPassword = tempPassword;
}

public int getUserId() {
	return userId;
}

public void setUserId(int userId) {
	this.userId = userId;
}

public int getUserRoleId() {
	return userRoleId;
}

public void setUserRoleId(int userRoleId) {
	this.userRoleId = userRoleId;
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
}
