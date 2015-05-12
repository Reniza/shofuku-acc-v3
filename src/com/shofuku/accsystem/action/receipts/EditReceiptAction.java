package com.shofuku.accsystem.action.receipts;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.ReceiptsManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class EditReceiptAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String receiptModule;
	private String forWhat;
	private String forWhatDisplay;

	private String moduleParameter;
	ORSales orSales;
	OROthers orOthers;
	CashCheckReceipts ccReceipts;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
			List accountProfileCodeList;
			List<Transaction> transactionList;
			List<Transaction> transactions;
			Iterator itr;
	//END 2013 - PHASE 3 : PROJECT 1: MARK  
	
	ReceiptsManager receiptsManager = (ReceiptsManager) actionSession.get("receiptsManager");
	AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
	TransactionManager transactionManager = (TransactionManager) actionSession.get("transactionManager");


	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}


	public String execute() throws Exception {
		Session session = getSession();
		try {
			forWhatDisplay="edit";
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
			if (getReceiptModule().equalsIgnoreCase("orSales")) {
				ORSales orSales = new ORSales();
				orSales = (ORSales) receiptsManager.listReceiptsByParameter(
						ORSales.class, "orNumber",
						this.getOrSales().getOrNumber(),session).get(0);
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", orSales.getOrNumber(), session);
				if(tempList.size()>0) {
					itr = tempList.iterator();
					transactionList = new ArrayList<Transaction>(); 
					while(itr.hasNext()) {
						Transaction transaction = (Transaction)itr.next();
						if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
							transactionList.add(transaction);
						}
					}
				}else {
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transactionList.add(transaction);
				}
				this.setTransactionList(transactionList);
				//END Phase 3 - Azhee
				this.setOrSales(orSales);
				return "orSales";
			} else if (getReceiptModule().equalsIgnoreCase("orOthers")) {
				OROthers orOthers= new OROthers();
				orOthers = (OROthers) receiptsManager.listReceiptsByParameter(
						OROthers.class, "orNumber",
						this.getOrOthers().getOrNumber(),session).get(0);
				
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", orOthers.getOrNumber(), session);
				if(tempList.size()>0) {
					itr = tempList.iterator();
					transactionList = new ArrayList<Transaction>(); 
					while(itr.hasNext()) {
						Transaction transaction = (Transaction)itr.next();
						if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
							transactionList.add(transaction);
						}
					}
				}else {
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transactionList.add(transaction);
				}
				this.setTransactionList(transactionList);
				//END Phase 3 - Azhee
				setOrOthers(orOthers);
				return "orOthers";
			} else {
				CashCheckReceipts cashCheckReceipts = new CashCheckReceipts();
				cashCheckReceipts = (CashCheckReceipts) receiptsManager.listReceiptsByParameter(
						CashCheckReceipts.class, "cashReceiptNo",
						this.getCcReceipts().getCashReceiptNo(),session).get(0);
				
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", ccReceipts.getCashReceiptNo(), session);
				if(tempList.size()>0) {
					itr = tempList.iterator();
					transactionList = new ArrayList<Transaction>(); 
					while(itr.hasNext()) {
						Transaction transaction = (Transaction)itr.next();
						if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
							transactionList.add(transaction);
						}
					}
				}else {
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transactionList.add(transaction);
				}
				this.setTransactionList(transactionList);
				//END Phase 3 - Azhee
				this.setCcReceipts(cashCheckReceipts);
				return "cashCheckReceipts";
			}		

		} catch (RuntimeException re) {
			if (getReceiptModule().equalsIgnoreCase("orSales")) {
				return "orSales";
			}else if (getReceiptModule().equalsIgnoreCase("orOthers")) { 
				return "orOthers";
			}else {
				return "cashCheckReceipts";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	


	public String getReceiptModule() {
		return receiptModule;
	}

	public void setReceiptModule(String receiptModule) {
		this.receiptModule = receiptModule;
	}

	public String getForWhat() {
		return forWhat;
	}
	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}
	public String getModuleParameter() {
		return moduleParameter;
	}
	public void setModuleParameter(String moduleParameter) {
		this.moduleParameter = moduleParameter;
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
	public CashCheckReceipts getCcReceipts() {
		return ccReceipts;
	}
	public void setCcReceipts(CashCheckReceipts ccReceipts) {
		this.ccReceipts = ccReceipts;
	}
	public String getForWhatDisplay() {
		return forWhatDisplay;
	}
	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}
	//START 2013 - PHASE 3 : PROJECT 1: MARK 
			public List getAccountProfileCodeList() {
				return accountProfileCodeList;
			}
			public void setAccountProfileCodeList(List accountProfileCodeList) {
				this.accountProfileCodeList = accountProfileCodeList;
			}
			public List<Transaction> getTransactionList() {
				return transactionList;
			}
			
			public void setTransactionList(List<Transaction> transactionList) {
				this.transactionList = transactionList;
			}
			public List<Transaction> getTransactions() {
				return transactions;
			}
			public void setTransactions(List<Transaction> transactions) {
				this.transactions = transactions;
			}
			
			//END 2013 - PHASE 3 : PROJECT 1: MARK

}
