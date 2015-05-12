package com.shofuku.accsystem.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.ImportOfflineOrdersUtil;

public class ImportOfflineOrdersAction extends ActionSupport {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	Map<String,Object> actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String fileUpload;
	List<String> errorStringList;
	private String importType;
	
	// add other managers for other modules Manager()
	CustomerManager customerManager 		= (CustomerManager) 	actionSession.get("customerManager");
	InventoryManager inventoryManager 		= (InventoryManager) 	actionSession.get("inventoryManager"); 
	
	public String execute(){
		
		Session session = getSession();
		ImportOfflineOrdersUtil util = new ImportOfflineOrdersUtil(customerManager,inventoryManager);
		
		errorStringList = new ArrayList<String>();
		
		util.readImportFile(fileUpload,importType, session);
		errorStringList = util.getErrorStrings();
		return "imported";
	}

	Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public List<String> getErrorStringList() {
		return errorStringList;
	}

	public void setErrorStringList(List<String> errorStringList) {
		this.errorStringList = errorStringList;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}
}
