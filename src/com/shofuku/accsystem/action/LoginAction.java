package com.shofuku.accsystem.action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.BaseController;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.ReceiptsManager;
import com.shofuku.accsystem.controllers.ReportAndSummaryManager;
import com.shofuku.accsystem.controllers.SecurityManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.helpers.UserRoleHelper;
import com.shofuku.accsystem.utils.FinancialReportsPoiHelper;
import com.shofuku.accsystem.utils.HibernateUtil;


public class LoginAction extends ActionSupport {
	
	private static final long serialVersionUID = 1162594281489858399L;
	
	/**
	 * <p> Validate a user login. </p>
	 */
	SecurityManager manager = new SecurityManager();
	private String username;
	private String password;
	private String forWhat;
	private String forWhatDisplay;
	
	List<UserAccount> userList = new ArrayList<UserAccount>();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	UserRoleHelper roleHelper = new UserRoleHelper();
	
	@SuppressWarnings("unchecked")
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
						Map<String,Object> sess = ActionContext.getContext().getSession();
						sess.put("user",user);
						sess.put("loggedUser",user.getUserName());
						sess.put("rolesList", roleHelper.loadModules());
						
						initializeControllers(sess,user);
						
				}
				return "success";
			}
		}
		return "input";
	}
	 
		private void initializeControllers(Map<String,Object> sess, UserAccount user) {
			
			//controllers
			
			BaseController manager = new BaseController();
			AccountEntryManager accountEntryManager = new AccountEntryManager();
			CustomerManager customerManager = new CustomerManager();
			DisbursementManager disbursementManager = new DisbursementManager();
			FinancialsManager financialsManager = new FinancialsManager();
			InventoryManager inventoryManager 		= new InventoryManager(); 
			LookupManager lookupManager = new LookupManager();
			ReceiptsManager receiptsManager = new ReceiptsManager();
			SecurityManager securityManager = new SecurityManager();
			
			SupplierManager supplierManager = new SupplierManager ();
			TransactionManager transactionMananger = new TransactionManager();
			
			
			manager.setUser(user);
			accountEntryManager.setUser(user);
			customerManager.setUser(user);
			disbursementManager.setUser(user);
			financialsManager.setUser(user);
			inventoryManager.setUser(user);
			lookupManager.setUser(user);
			receiptsManager.setUser(user);
			securityManager.setUser(user);
			supplierManager.setUser(user);
			transactionMananger.setUser(user);
			
			
			//include in context session for use
			sess.put("manager", manager);
			sess.put("accountEntryManager", accountEntryManager);
			sess.put("customerManager",customerManager);
			sess.put("disbursementManager",disbursementManager);
			sess.put("financialsManager",financialsManager);
			sess.put("inventoryManager",inventoryManager);
			sess.put("lookupManager", lookupManager);
			sess.put("receiptsManager",receiptsManager);
			sess.put("securityManager", securityManager);
			sess.put("supplierManager",supplierManager);
			sess.put("transactionManager",transactionMananger);
			
			manager.initializeDaos();
			accountEntryManager.initializeDaos();
			customerManager.initializeDaos();
			disbursementManager.initializeDaos();
			financialsManager.initializeDaos();
			inventoryManager.initializeDaos();
			lookupManager.initializeDaos();
			receiptsManager.initializeDaos();
			securityManager.initializeDaos();
			supplierManager.initializeDaos();
			transactionMananger.initializeDaos();

			
			//special case since this controller contains POIUTIL(), always put this at the end of this method
			ReportAndSummaryManager reportAndSummaryManager = new ReportAndSummaryManager(sess); 
			reportAndSummaryManager.setUser(user);
			sess.put("reportAndSummaryManager", reportAndSummaryManager);
			reportAndSummaryManager.initializeDaos();
			
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
		Map<String,Object> sess = ActionContext.getContext().getSession();
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


