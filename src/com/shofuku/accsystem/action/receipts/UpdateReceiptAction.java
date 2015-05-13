package com.shofuku.accsystem.action.receipts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.ReceiptsManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class UpdateReceiptAction extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");

	private String forWhat;
	private String forWhatDisplay;
	private String orSNo;
	private String orONo;
	private String crNo;
	private String subModule;
	
	ORSales orSales;
	OROthers orOthers;
	CashCheckReceipts ccReceipts;
	DateFormatHelper df = new DateFormatHelper();
	
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
		AccountEntryProfileUtil apeUtil = new AccountEntryProfileUtil(actionSession);
	//END 2013 - PHASE 3 : PROJECT 1: MARK  
	
	ReceiptsManager receiptsManager = (ReceiptsManager) actionSession.get("receiptsManager");
	AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
	TransactionManager transactionManager = (TransactionManager) actionSession.get("transactionManager");
	FinancialsManager financialsManager = (FinancialsManager) actionSession.get("financialsManager");
	
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() throws Exception{
		Session session = getSession();
		try {
			
			forWhatDisplay="edit";
			boolean updateResult=false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
			
			if (getSubModule().equalsIgnoreCase("orSales")){
				
				orSales.setOrNumber(orSNo);
				//START - 2013 - PHASE 3 : PROJECT 1: MARK
				transactionManager.discontinuePreviousTransactions(orSales.getOrNumber(),session);
				transactionList = getTransactionList();
				updateAccountingEntries(orSales.getOrNumber(),session,SASConstants.ORSALES);
				this.setTransactionList(transactions);
				orSales.setTransactions(transactions);
				//END
				//START: 2013 - PHASE 3 : PROJECT 4: AZ
				Vat vatDetails = orSales.getVatDetails();
				vatDetails.setVatReferenceNo(orSNo);
				vatDetails.setOrDate(orSales.getOrDate());
				this.orSales.setVatDetails(orSales.getVatDetails());
				financialsManager.updateVatDetails(orSales.getVatDetails(), session);							
				//END: 2013 - PHASE 3 : PROJECT 4: AZ
				if (validateORSales()) {
				
				}else {
					updateResult = receiptsManager.updateReceipts(orSales,session);
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
						forWhat="true";
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}	
				}
				return "orSalesUpdated";
			}else if (getSubModule().equalsIgnoreCase("orOthers")){
				orOthers.setOrNumber(orONo);
				//START - 2013 - PHASE 3 : PROJECT 1: MARK
				transactionManager.discontinuePreviousTransactions(orOthers.getOrNumber(),session);
				//transactionList = new ArrayList();
				transactionList = getTransactionList();
				updateAccountingEntries(orOthers.getOrNumber(),session,SASConstants.OROTHERS);
				this.setTransactionList(transactions);
				orOthers.setTransactions(transactions);
				//END
				//START: 2013 - PHASE 3 : PROJECT 4: AZ
				Vat vatDetails = orOthers.getVatDetails();
				vatDetails.setVatReferenceNo(orONo);
				vatDetails.setOrDate(orOthers.getOrDate());
				this.orOthers.setVatDetails(orOthers.getVatDetails());
				financialsManager.updateVatDetails(orOthers.getVatDetails(), session);							
				//END: 2013 - PHASE 3 : PROJECT 4: AZ
				if (validateOROther()) {
					
				}else {
					updateResult = receiptsManager.updateReceipts(orOthers,session);
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
						forWhat="true";
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}	
				}
				return "orOthersUpdated";
			}else {
				ccReceipts.setCashReceiptNo(crNo);
				//START - 2013 - PHASE 3 : PROJECT 1: MARK
				transactionManager.discontinuePreviousTransactions(ccReceipts.getCashReceiptNo(),session);
				//transactionList = new ArrayList();
				transactionList = getTransactionList();
				updateAccountingEntries(ccReceipts.getCashReceiptNo(),session,SASConstants.CASHCHECKRECEIPTS);
				this.setTransactionList(transactions);
				ccReceipts.setTransactions(transactions);
				//END
				//START: 2013 - PHASE 3 : PROJECT 4: AZ
				Vat vatDetails = ccReceipts.getVatDetails();
				vatDetails.setVatReferenceNo(crNo);
				vatDetails.setOrDate(ccReceipts.getCashReceiptDate());
				this.ccReceipts.setVatDetails(ccReceipts.getVatDetails());
				financialsManager.updateVatDetails(ccReceipts.getVatDetails(), session);							
				//END: 2013 - PHASE 3 : PROJECT 4: AZ
				if (validateCashCheckReceipt()) {
					
				}else {
					updateResult = receiptsManager.updateReceipts(ccReceipts,session);
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
						forWhat="true";
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}
				}
				return "cashCheckReceiptsUpdated";
			}
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("orSales")) {
				return "orSalesUpdated";
			}else if (getSubModule().equalsIgnoreCase("orOthers")) { 
				return "orOthersUpdated";
			}else { 
				return "cashCheckReceiptsUpdated";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}
	
	//2013 - PHASE 3 : PROJECT 1: MARK
	
	/*Transactions: 
	 * -retrieve first rules for each account
	 * -set each transaction object the available properties : transactionType,accountCode,amount,transactionDate,isInUse
	 * TODO: include createdBy
	 * TODO: include rules
	 */
private void updateAccountingEntries(String referenceNo, Session session, String type) {

// TODO Auto-generated method stub
transactions = new ArrayList<Transaction>();
if(transactionList!=null) {
	Iterator itr = transactionList.iterator();
	while(itr.hasNext()) {
		Transaction transaction = (Transaction)itr.next();
		AccountEntryProfile accountEntry = transaction.getAccountEntry();
		accountEntry = accountEntryManager.loadAccountEntryProfile(accountEntry.getAccountCode());
		transaction.setAccountEntry(accountEntry);
		transaction.setTransactionReferenceNumber(referenceNo);
		transaction.setTransactionType(type);
		transaction.setTransactionAction(apeUtil.getActionBasedOnType(accountEntry, type));
		transaction.setTransactionDate(df.getTimeStampToday());
		transaction.setIsInUse(SASConstants.TRANSACTION_IN_USE);
		transactions.add(transaction);
	}
	transactionManager.addTransactionsList(transactions,session);
}
//END - 2013 - PHASE 3 : PROJECT 1: MARK
//return transactions;
}

	private boolean validateORSales() {

		boolean errorFound = false;
		if ("".equals(getOrSales().getOrNumber())) {
			addFieldError("orSales.orNumber", "REQUIRED");
			errorFound = true;
		}
		 if (null==(getOrSales().getOrDate())){
		 addActionError("REQUIRED: OR Date");
		 errorFound= true;
		 }
		 if ("".equals(getOrSales().getReceivedFrom())){
		 addFieldError("orSales.receivedFrom","REQUIRED");
		 errorFound= true;
		 }else {
			 if (getOrSales().getReceivedFrom().trim().length()>100) {
				 addFieldError("orSales.receivedFrom",SASConstants.MAXIMUM_LENGTH_100);
			 }
		 }
		 if ("".equals(getOrSales().getAddress())){
		 addFieldError("orSales.address","REQUIRED");
		 errorFound= true;
		 }else {
			 if (getOrSales().getAddress().trim().length()>200) {
				 addFieldError("orSales.address",SASConstants.MAXIMUM_LENGTH_200);
			 }
		 }
		
		 if ("".equals(getOrSales().getInFullPartialPaymentOf())){
		 addFieldError("orSales.inFullPartialPaymentOf","REQUIRED");
		 errorFound= true;
		 }
		 if ("".equals(getOrSales().getSalesInvoiceNumber())){
		 addFieldError("orSales.salesInvoiceNumber","REQUIRED");
		 errorFound= true;
		 }
		 if ((getTransactionList().get(0).getAmount() == 0 )) {
				addActionMessage("REQUIRED: Accounting Entries Details");
				errorFound = true;
			}
		
		return errorFound;
	}

	private boolean validateOROther() {
		boolean errorFound = false;
		if ("".equals(getOrOthers().getOrNumber())) {
			addFieldError("orOthers.orNumber", "REQUIRED");
			errorFound = true;
		}
		 if (null==(getOrOthers().getOrDate())){
		 addActionError("REQUIRED: OR Date");
		 errorFound= true;
		 }
		 if ("".equals(getOrOthers().getReceivedFrom())){
		 addFieldError("orOthers.receivedFrom","REQUIRED");
		 errorFound= true;
		 }else {
			 if (getOrOthers().getReceivedFrom().trim().length()>100) {
				 addFieldError("orOthers.receivedFrom",SASConstants.MAXIMUM_LENGTH_100);
			 }
		 }
		 if ("".equals(getOrOthers().getAddress())){
		 addFieldError("orOthers.address","REQUIRED");
		 errorFound= true;
		 }else {
			 if (getOrOthers().getAddress().trim().length()>200) {
				 addFieldError("orOthers.address",SASConstants.MAXIMUM_LENGTH_200);
			 }
		 }
		
		 if ("".equals(getOrOthers().getInFullPartialPaymentOf())){
		 addFieldError("orOthers.inFullPartialPaymentOf","REQUIRED");
		 errorFound= true;
		 }
		 if ("".equals(getOrOthers().getSalesInvoiceNumber())){
		 addFieldError("orOthers.salesInvoiceNumber","REQUIRED");
		 errorFound= true;
		 }
		 if ((getTransactionList().get(0).getAmount() == 0 )) {
				addActionMessage("REQUIRED: Accounting Entries Details");
				errorFound = true;
			}
		return errorFound;
	}

	private boolean validateCashCheckReceipt() {
		boolean errorFound = false;
		if ("".equals(getCcReceipts().getCashReceiptNo())) {
			addFieldError("ccReceipts.cashReceiptNo", "REQUIRED");
			errorFound = true;
		}
		 if (null==(getCcReceipts().getCashReceiptDate())){
		 addActionMessage("REQUIRED: Cash Receipt Date");
		 errorFound= true;
		 }
		 if ("".equals(getCcReceipts().getParticulars())){
		 addFieldError("ccReceipts.particulars","REQUIRED");
		 errorFound= true;
		 }else {
			 if (getCcReceipts().getParticulars().trim().length()>200) {
				 addFieldError("ccReceipts.particulars",SASConstants.MAXIMUM_LENGTH_200);
			 }
		 }
		 if (0==(getCcReceipts().getAmount())){
		 addFieldError("ccReceipts.amount","REQUIRED");
		 errorFound= true;
		 }
		 if ((getTransactionList().get(0).getAmount() == 0 )) {
				addActionMessage("REQUIRED: Accounting Entries Details");
				errorFound = true;
			}
		return errorFound;

	}
	public String getForWhat() {
		return forWhat;
	}
	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}
	public String getOrSNo() {
		return orSNo;
	}
	public void setOrSNo(String orSNo) {
		this.orSNo = orSNo;
	}
	public String getOrONo() {
		return orONo;
	}
	public void setOrONo(String orONo) {
		this.orONo = orONo;
	}
	public String getCrNo() {
		return crNo;
	}
	public void setCrNo(String crNo) {
		this.crNo = crNo;
	}
	public String getSubModule() {
		return subModule;
	}
	public void setSubModule(String subModule) {
		this.subModule = subModule;
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
