package com.shofuku.accsystem.action.security;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.domain.financials.AccountingRules;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.security.UserRole;

public class AddSecurityAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4912683471398088090L;

	private static final Logger logger = Logger
			.getLogger(AddSecurityAction.class);

	private static final Logger logger2 = logger.getRootLogger();

	SecurityManager securityManager = new SecurityManager();
	RecordCountHelper rch = new RecordCountHelper();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	// beans
		String subModule;
		UserAccount user;
		UserRole role;
		private String forWhat;
		private String forWhatDisplay;
		List roleList = null;
		String tempPassword;
	
	public String execute() throws Exception {
		Session session = getSession();
		try {
			
			if (getSubModule().equalsIgnoreCase("userAccount")) {
				return addUserAccount(session);
			}else {
				return addUserRole(session);
			}
		
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("userAccount")) {
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
		// TODO Auto-generated method stub
		boolean addResult = false;
		
	if (validateUserRoleAccount()) {
		//	loadRoleList();
		}else {
			List existingRole = null;
			existingRole =  securityManager.listSecurityByParameter(UserRole.class, "userRoleName", role.getUserRoleName(), session);
			if (!existingRole.isEmpty()) {
				addActionMessage(SASConstants.EXISTS + "-> ROLE NAME ");
			}else {
			addResult = securityManager.insertToolsDetails(role, session); 
				if (addResult == true) {
					addActionMessage(SASConstants.ADD_SUCCESS);
					
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
		// TODO Auto-generated method stub
		boolean errorFound = false;
		
		if ("".equals(role.getUserRoleName())) {
			 addFieldError("role.userRoleName", "REQUIRED");
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
		roleList= securityManager.listAlphabeticalAscByParameter(UserRole.class, "userRoleId",  session);
		return "userAccount";
	}

	private String addUserAccount(Session session) {
		// TODO Auto-generated method stub
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
				userRoleList = securityManager.listSecurityByParameter(UserRole.class, "userRoleName", user.getRole().getUserRoleName(), session);
				UserRole existingRole = new UserRole();
				existingRole = (UserRole) userRoleList.get(0);
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

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
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
	
	

}
