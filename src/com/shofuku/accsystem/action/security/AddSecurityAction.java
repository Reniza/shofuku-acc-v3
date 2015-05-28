package com.shofuku.accsystem.action.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.security.Role;
import com.shofuku.accsystem.helpers.UserRoleHelper;

public class AddSecurityAction extends ActionSupport implements Preparable{
	
	/**
	 * 
	 */
	
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private static final long serialVersionUID = 4912683471398088090L;

	private static final Logger logger = Logger
			.getLogger(AddSecurityAction.class);

	SecurityManager securityManager = (SecurityManager) actionSession.get("securityManager");
	RecordCountHelper rch = new RecordCountHelper(actionSession);
	UserRoleHelper roleHelper = new UserRoleHelper();
	
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		securityManager = (SecurityManager) actionSession.get("securityManager");
		
	};
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	// beans
		String subModule;
		
		Role role;
		private String forWhat;
		private String forWhatDisplay;
		List roleList = null;
		String tempPassword;
		
		//roles
		private List modulesNotGrantedList= new ArrayList<>();
		private List modulesGrantedList= new ArrayList<>();
		
		private Map modulesGrantedMap;
		
	public String execute() throws Exception {
		Session session = getSession();
		try {
			if (getSubModule().equalsIgnoreCase("userAccount")) {
				loadRoleList();
				return addUserAccount(session);
			}else {
				return addUserRole(session);
			}
		
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("userAccount")) {
				loadRoleList();
				return "userAccountAdded";
			}else {
				return "userRoleAdded";
			}
			
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}
	
	private String addUserRole(Session session) {
		boolean addResult = false;
		
	if (validateUserRoleAccount()) {
		//	loadRoleList();
		}else {
			List existingRole = null;
			existingRole =  securityManager.listSecurityByParameter(Role.class, "roleName", role.getRoleName(), session);
			if (!existingRole.isEmpty()) {
				addActionMessage(SASConstants.EXISTS + "-> ROLE NAME ");
			}else {
				role.setRoleAccessString(roleHelper.parseModulesGrantedListToString(modulesGrantedList));
				addResult = securityManager.insertToolsDetails(role, session); 
				
				if (addResult == true) {
					addActionMessage(SASConstants.ADD_SUCCESS);
					
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
		}
	return "userRoleAdded";
	}

	private boolean validateUserRoleAccount() {
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
		Session session = getSession();
		roleList= securityManager.listAlphabeticalAscByParameter(Role.class, "roleName",  session);
		return "userAccount";
	}

	private String addUserAccount(Session session) {
		boolean addResult = false;
		if (validateUserAccount()) {
			loadRoleList();
		}else {
			List existingUser = null;
			existingUser =  securityManager.listSecurityByParameter(UserAccount.class, "userName", user.getUserName(), session);
			if (!existingUser.isEmpty()) {
				addActionMessage(SASConstants.EXISTS + "-> USER NAME ");
			}else {
				List userRoleList = null;
				userRoleList = securityManager.listSecurityByParameter(Role.class, "roleName", user.getRole().getRoleName(), session);
				Role existingRole = new Role();
				existingRole = (Role) userRoleList.get(0);
				user.setRole(existingRole);
				addResult = securityManager.insertToolsDetails(user, session);
				if (addResult == true) {
						addActionMessage(SASConstants.ADD_SUCCESS);
						forWhat="true";
						forWhatDisplay ="edit";
					} else {
						addActionError(SASConstants.FAILED);
						}
					}
		}
		loadRoleList();
	return "userAccountAdded";
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
