package com.shofuku.accsystem.action.receipts;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.ReceiptsManager;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchReceiptAction extends ActionSupport implements Preparable{
	
	private static final long serialVersionUID = 1L;
	
	Map actionSession;
	UserAccount user;

	ReceiptsManager receiptsManager;
	
	
	@Override
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		receiptsManager = (ReceiptsManager) actionSession.get("receiptsManager");
	}

	private String receiptModule;
	private String moduleParameter;
	private String moduleParameterValue;
	ORSales orSales;
	OROthers orOthers;
	CashCheckReceipts ccReceipts;
	private String clicked;
	List receiptList;

	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() throws Exception {
		Session session = getSession();
		try {
			if (getClicked().equals("true")) {

				if (null != getModuleParameter()&& getReceiptModule().equalsIgnoreCase("orSales")) {
				
					if (getModuleParameter().endsWith("Date")) {
						receiptList = receiptsManager.getReceiptElementsByDate((new DateFormatHelper().parseStringToTime(moduleParameterValue)), ORSales.class.getName(), moduleParameter,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						receiptList = receiptsManager.listAlphabeticalAscByParameter(ORSales.class, "orNumber",session);
						moduleParameterValue="all";
					}
					else {
						receiptList = receiptsManager.listReceiptsByParameter(ORSales.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (receiptList == null || 0 == receiptList.size()) {
							addActionMessage(SASConstants.NO_LIST);
						}
						
					return "orSales";
				} else if (null != getModuleParameter()&& getReceiptModule().equalsIgnoreCase("orOthers")) {
					
					if (getModuleParameter().endsWith("Date")) {
						receiptList = receiptsManager.getReceiptElementsByDate((new DateFormatHelper().parseStringToTime(moduleParameterValue)), OROthers.class.getName(), moduleParameter,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						receiptList = receiptsManager.listAlphabeticalAscByParameter(OROthers.class, "orNumber",session);
						moduleParameterValue="all";
					}
					else {
						receiptList = receiptsManager.listReceiptsByParameter(OROthers.class,
										moduleParameter, moduleParameterValue,session);
					}
					if (receiptList == null || 0 == receiptList.size()) {
						addActionMessage(SASConstants.NO_LIST);
					}
					return "orOthers";
				} else if (null != getModuleParameter()&& getReceiptModule().equalsIgnoreCase("ccReceipts")) {
				
					if (getModuleParameter().endsWith("Date")) {
						receiptList = receiptsManager.getReceiptElementsByDate((new DateFormatHelper().parseStringToTime(moduleParameterValue)), CashCheckReceipts.class.getName(), moduleParameter,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						receiptList = receiptsManager.listAlphabeticalAscByParameter(CashCheckReceipts.class, "cashReceiptNo",session);
						moduleParameterValue="all";
						
					}
					else {
						receiptList = receiptsManager.listReceiptsByParameter(CashCheckReceipts.class,
										moduleParameter, moduleParameterValue,session);
					}
					if (receiptList == null || 0 == receiptList.size()) {
						addActionMessage(SASConstants.NO_LIST);
					}
						
					return "cashCheckReceipts";
				}
			}
			moduleParameterValue="";
		return "populateList";
		} catch (RuntimeException re) {
			if (getReceiptModule().equalsIgnoreCase("orSales")) {
				return "orSales";
			}else if (getReceiptModule().equalsIgnoreCase("orOthers")) { 
				return "orOthers";
			}else if (getReceiptModule().equalsIgnoreCase("ccReceipts")) { 
				return "cashCheckReceipts";
			}else {
				return "populateList";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}

	public String getReceiptModule() {
		return receiptModule;
	}


	public void setReceiptModule(String receiptModule) {
		this.receiptModule = receiptModule;
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


	

	public ORSales getOrSales() {
		return orSales;
	}


	public void setOrSales(ORSales orSales) {
		this.orSales = orSales;
	}


	public OROthers getOrOthers() {
		return orOthers;
	}


	public void setOrOthers(OROthers orOthers) {
		this.orOthers = orOthers;
	}


	public CashCheckReceipts getCashCheckReceipt() {
		return ccReceipts;
	}


	public void setCashCheckReceipt(CashCheckReceipts cashCheckReceipt) {
		this.ccReceipts = cashCheckReceipt;
	}


	public List getReceiptList() {
		return receiptList;
	}


	public void setReceiptList(List receiptList) {
		this.receiptList = receiptList;
	}


	public String getClicked() {
		return clicked;
	}


	public void setClicked(String clicked) {
		this.clicked = clicked;
	}

}



