package com.shofuku.accsystem.action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.helpers.UserRoleHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.controllers.ReportAndSummaryManager;
import com.shofuku.accsystem.controllers.SecurityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


public class LoginAction extends ActionSupport {
/**
	 * <p> Validate a user login. </p>
	 */
	SecurityManager manager = new SecurityManager();
	private String username;
	private String password;
	private String forWhat;
	private String forWhatDisplay;
	
	List userList = new ArrayList();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	UserRoleHelper roleHelper = new UserRoleHelper();
	
	public String execute() throws Exception {
		Session session = getSession();
		
		if (getUsername().equals("") || getPassword().equals("") ){
			validateLogin(); 
		}else{
			userList =  manager.listSecurityByParameter(UserAccount.class, "userName", this.getUsername(), session);
			if (userList.size() == 0){
				addActionError("Invalid user name! Please try another user...");
			}else{
				UserAccount user = (UserAccount) manager.listSecurityByParameter(UserAccount.class, "userName", this.getUsername(), session).get(0);
				if(!getUsername().equals(user.getUserName().trim()) || !getPassword().equals(user.getPassword().trim())){
					addActionError("Invalid password! Please try again!");
					return "error";
				}else if (getUsername().equals(user.getUserName()) && getPassword().equals(user.getPassword())){
						Map sess = ActionContext.getContext().getSession();
						sess.put("user",user);
						sess.put("loggedUser",user.getUserName());
						sess.put("rolesList", roleHelper.loadModules());
						
						ReportAndSummaryManager reportSummaryMgr = new ReportAndSummaryManager(); 
						reportSummaryMgr.setUser(user);
						sess.put("reportSummaryMgr", reportSummaryMgr);						
						
				}
				return "success";
			}
		}
		return "input";
	}
	 
		public boolean validateLogin() {
			boolean errorFound= false;
		  if (getUsername().length() == 0){
				addFieldError("username", "Required: username");
				errorFound = true;
			}
			if (getPassword().length() == 0){
				addFieldError("password", "Required: password");
				errorFound = true;
			}
			return errorFound;
		}
	  
	public String logout(){
		Session session = getSession();
		Map sess = ActionContext.getContext().getSession();
		
		sess.remove("user");	
		sess.remove("rolesList");
		return "success";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	  
}


