package com.shofuku.accsystem.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerStockLevel;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.POIUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class ManageStockLevelAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	// add other managers for other modules Manager()
	
	
	private String fileUpload;
	Customer customer;
	String result;
	List stockLevelList;
	String cusId;
	
	POIUtil poiUtil = new POIUtil(actionSession);
	private String forWhat = "false";
	private String forWhatDisplay ="edit"; 
	CustomerManager customerManager;

	private void initializeController() {
		customerManager = (CustomerManager) 	actionSession.get("customerManager");	
	}
	
	Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() {
		Session session = getSession();
		initializeController();
		
		customer = (Customer) customerManager.listByParameter(Customer.class, "customerNo", cusId, session).get(0);
		Iterator itr = customer.getCustomerStockLevelMap().keySet().iterator();
		stockLevelList = new ArrayList();
		
		List<String> tempList = new ArrayList();
		tempList.addAll(customer.getCustomerStockLevelMap().keySet());
		Collections.sort(tempList);

		for(String keyset:tempList) {
			stockLevelList.add((CustomerStockLevel)customer.getCustomerStockLevelMap().get(keyset));
		}
		
	return "input";
	}
	
	public String readStockLevelExcel() {
		Session session = getSession();
		initializeController();
		//customer = this.getCustomer();
		customer = (Customer) customerManager.listByParameter(Customer.class, "customerNo", cusId, session).get(0);
		
		if (null == fileUpload) {
			addActionError(SASConstants.NO_LIST);
		}else {
			customer = poiUtil.readCustomerStockLevelForm(customer, fileUpload, session);
			customerManager.updateCustomer(customer, session);
			
			Iterator itr = customer.getCustomerStockLevelMap().keySet().iterator();
			stockLevelList = new ArrayList();
			while(itr.hasNext()) {
				String key = (String)itr.next();
				CustomerStockLevel csl = (CustomerStockLevel)customer.getCustomerStockLevelMap().get(key);
				stockLevelList.add(csl);
			}
		}
		
		return "updated";
	}
	
	public String showCustomerProfile() {
		Session session = getSession();
		initializeController();
			customer = (Customer) customerManager.listByParameter(Customer.class, "customerNo", cusId, session).get(0);
			
			Map sess = ActionContext.getContext().getSession();
			sess.put("customerStockLevelMap",customer.getCustomerStockLevelMap());
			
		return "showCustomer";
		
	}
	
	
	
	//getter and setter		
	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List getStockLevelList() {
		return stockLevelList;
	}

	public void setStockLevelList(List stockLevelList) {
		this.stockLevelList = stockLevelList;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
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
