package com.shofuku.accsystem.action.disbursement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.annotations.Check;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.lookups.ExpenseClassification;
import com.shofuku.accsystem.domain.lookups.PaymentClassification;
import com.shofuku.accsystem.domain.lookups.PaymentTerms;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class UpdateDisbursementAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String pcNo;
	private String cpNo;
	private String chpNo;
	private String subModule;
	private String forWhat;
	private String forWhatDisplay;
	private String invId;
	List orderDetails;
	private String payee;
	private String invAmount;
	SupplierInvoice invoice;
	
	List classifList;
	List invoiceNoList;
	
	//START 2013 - PHASE 3 : PROJECT 1: AZ
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
		AccountEntryProfileUtil apeUtil = new AccountEntryProfileUtil(actionSession);
	//END 2013 - PHASE 3 : PROJECT 1: AZ  
		
	AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
	TransactionManager transactionManager = (TransactionManager) actionSession.get("transactionManager");
	FinancialsManager financialsManager = (FinancialsManager) actionSession.get("financialsManager");
	LookupManager lookupManager = (LookupManager) actionSession.get("lookupManager");
	SupplierManager supplierManager = (SupplierManager) actionSession.get("supplierManager");
	DisbursementManager disbursementManager = (DisbursementManager) actionSession.get("disbursementManager");
	
	DateFormatHelper df = new DateFormatHelper();
	
	PettyCash pc;
	CashPayment cp;
	CheckPayments chp;
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
	
	try {
		forWhatDisplay = "edit";
		boolean updateResult=false;
		accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
		if (getSubModule().equals("pettyCash")){
			
			pc.setPcVoucherNumber(getPcNo());
			pc.setCreditTitle("PETTY CASH");
			pc.setDebitTitle(pc.getDescription());
			pc.setDebitAmount(pc.getAmount());
			pc.setCreditAmount(pc.getAmount());
			classifList = lookupManager.getLookupElements(ExpenseClassification.class, "PETTYCASH",session);
			
			//START - 2013 - PHASE 3 : PROJECT 1: MARK
			transactionManager.discontinuePreviousTransactions(pc.getPcVoucherNumber(),session);
			//transactionList = new ArrayList();
			transactionList = getTransactionList();
			AccountEntryProfileUtil apeUtil = new AccountEntryProfileUtil();
			updateAccountingEntries(pc.getPcVoucherNumber(),session,SASConstants.PETTYCASH);
			this.setTransactionList(transactions);
			pc.setTransactions(transactions);
			//END
			//START: 2013 - PHASE 3 : PROJECT 4: AZ
			Vat vatDetails = pc.getVatDetails();
			vatDetails.setVatReferenceNo(pcNo);
			vatDetails.setPayee(pc.getPayee());
			vatDetails.setOrDate(pc.getPcVoucherDate());
			financialsManager.updateVatDetails(pc.getVatDetails(), session);							
			this.pc.setVatDetails(vatDetails);
			//END: 2013 - PHASE 3 : PROJECT 4: AZ
			if (validatePettyCash()) {
			}else {
				updateResult = disbursementManager.updateDisbursement(pc,session);
				if (updateResult== true) {
					addActionMessage(SASConstants.UPDATED);
					forWhat="true";
				}else {
					addActionMessage(SASConstants.UPDATE_FAILED);
				}
			}
			return "pettyCashUpdated";
		}else if (getSubModule().equals("cashPayment")){
			cp.setCashVoucherNumber(cpNo);
			cp.setCreditTitle("CASH FUND");
			cp.setDebitTitle(cp.getDescription());
			cp.setDebitAmount(cp.getAmount());
			cp.setCreditAmount(cp.getAmount());
			classifList = lookupManager.getLookupElements(PaymentClassification.class, "CASHPAYMENT",session);
			
			//START - 2013 - PHASE 3 : PROJECT 1: MARK
			transactionManager.discontinuePreviousTransactions(cp.getCashVoucherNumber(),session);
			transactionList = getTransactionList();
			updateAccountingEntries(cp.getCashVoucherNumber(),session,SASConstants.CASHPAYMENT);
			this.setTransactionList(transactions);
			cp.setTransactions(transactions);
			//END
			//START: 2013 - PHASE 3 : PROJECT 4: AZ
			Vat vatDetails = cp.getVatDetails();
			vatDetails.setVatReferenceNo(cpNo);
			vatDetails.setPayee(cp.getPayee());
			vatDetails.setOrDate(cp.getCashVoucherDate());
			financialsManager.updateVatDetails(cp.getVatDetails(), session);							
			this.cp.setVatDetails(vatDetails);
			//END: 2013 - PHASE 3 : PROJECT 4: AZ
			if (validateCashPayment()) {
			}else {
				updateResult = disbursementManager.updateDisbursement(cp,session);
				if (updateResult== true) {
					addActionMessage(SASConstants.UPDATED);
					forWhat="true";
				}else {
					addActionMessage(SASConstants.UPDATE_FAILED);
				}
				
			}
			return "cashPaymentUpdated";
		}else if (getSubModule().equals("supplierCheckVoucher")){
			return updateSupplierCheckVoucher(session, updateResult);
		}else {
			chp.setCheckVoucherNumber(chpNo);
			chp.setDebitTitle(chp.getDescription());
			//chp.setCreditTitle("CIB-BDO");
			chp.setDebitAmount(chp.getAmount());
		/* START P3 - AZ - REMOVED
		 * 	if (chp.getVat().equalsIgnoreCase("VAT")){
				chp.setFinalAmount(manager.computeVat(chp.getAmount()));
				chp.setVatAmount(chp.getAmount() - chp.getFinalAmount());
			}else{
				chp.setVatAmount(0.00);
				chp.setFinalAmount(chp.getAmount());
			}
				chp.setCreditAmount(chp.getAmount());*/
			
			classifList = lookupManager.getLookupElements(PaymentTerms.class, "CHECKPAYMENT",session);
			//START - 2013 - PHASE 3 : PROJECT 1: MARK
			transactionManager.discontinuePreviousTransactions(chp.getCheckVoucherNumber(),session);
			transactionList = getTransactionList();
			updateAccountingEntries(chp.getCheckVoucherNumber(),session,SASConstants.CHECKPAYMENT);
			this.setTransactionList(transactionList);
			chp.setTransactions(transactionList);
			//END
			//START: 2013 - PHASE 3 : PROJECT 4: AZ
			Vat vatDetails = chp.getVatDetails();
			vatDetails.setVatReferenceNo(chpNo);
			vatDetails.setPayee(chp.getPayee());
			vatDetails.setOrDate(chp.getCheckVoucherDate());
			financialsManager.updateVatDetails(chp.getVatDetails(), session);							
			this.chp.setVatDetails(vatDetails);
			//END: 2013 - PHASE 3 : PROJECT 4: AZ
			if (validateCheckPayment()) {
			}else {
				updateResult = disbursementManager.updateDisbursement(chp,session);
				if (updateResult== true) {
					addActionMessage(SASConstants.UPDATED);
					forWhat="true";
				}else {
					addActionMessage(SASConstants.UPDATE_FAILED);
				}
			}
			return "checkPaymentUpdated";
		}
	}catch (RuntimeException re) {
		re.printStackTrace();
		if (getSubModule().equals("pettyCash")){
			return "pettyCashUpdated";
		}else if (getSubModule().equals("cashPayment")){
			return "cashPaymentUpdated";
		}else if (getSubModule().equals("supplierCheckVoucher")){
			return "checkVoucherUpdated";
		}		else {
			return "checkPaymentUpdated";
		}
	}finally {
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
	}
	
}
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
private String updateSupplierCheckVoucher(Session session, boolean updateResult) throws Exception {

	List supInv = null;
	invoiceNoList = disbursementManager.listAlphabeticalAscByParameter(SupplierInvoice.class, "supplierInvoiceNo", session);
	
	supInv = supplierManager.listSuppliersByParameter(SupplierInvoice.class, "supplierInvoiceNo", invId,session);
		SupplierInvoice invoice = new SupplierInvoice();
		invoice = (SupplierInvoice) supInv.get(0);

	orderDetails = new ArrayList<PurchaseOrderDetails>();
	Set<PurchaseOrderDetails> invoiceDetailSet = invoice.getPurchaseOrderDetails();
	Iterator<PurchaseOrderDetails> itr = invoiceDetailSet.iterator();
	if (orderDetails!=null) {
		while(itr.hasNext()) {
			PurchaseOrderDetails invoiceDetails = itr.next();
			orderDetails.add(invoiceDetails);
			}
		}
	chp.setCheckVoucherNumber(chpNo);
	chp.setInvoice(invoice);
	//START - 2013 - PHASE 3 : PROJECT 1: MARK
	transactionManager.discontinuePreviousTransactions(chp.getCheckVoucherNumber(),session);
	transactionList = getTransactionList();
	updateAccountingEntries(chp.getCheckVoucherNumber(),session,SASConstants.CHECK_VOUCHER);
	this.setTransactionList(transactions);
	chp.setTransactions(transactions);
	//END
	//START: 2013 - PHASE 3 : PROJECT 4: AZ
	Vat vatDetails = chp.getVatDetails();
	vatDetails.setVatReferenceNo(chpNo);
	vatDetails.setPayee(chp.getPayee());
	vatDetails.setOrDate(chp.getCheckVoucherDate());
	this.chp.setVatDetails(vatDetails);
	financialsManager.updateVatDetails(chp.getVatDetails(), session);							
	//END: 2013 - PHASE 3 : PROJECT 4: AZ
	if (validateCheckVoucher()) {
	}else {
		if (invoice.getPurchaseOrderDetails().size()==0) {
			addActionError(SASConstants.EMPTY_ORDER_DETAILS);
		}else {
			//update invoice's remaining balance with the value placed on the amount to pay field
			updateInvoiceRemainingBalance(invoice);
			updateResult = disbursementManager.updateDisbursement(chp,session);
			if (updateResult== true) {
				addActionMessage(SASConstants.UPDATED);
				forWhat="true";
			}else {
				addActionMessage(SASConstants.UPDATE_FAILED);
			}
		}
	}
	return "checkVoucherUpdated";
	}
private void updateInvoiceRemainingBalance(SupplierInvoice invoice) {
	Session session = getSession();
	double remainingBalance  =getRemainingBalanceFromAllChecksAssociated(session,invoice);
	invoice.setRemainingBalance(remainingBalance);
	chp.setRemainingBalance(remainingBalance);
	supplierManager.updateSupplier(invoice, session);
}
private double getRemainingBalanceFromAllChecksAssociated(Session session,SupplierInvoice invoice) {
	List disbursementList = disbursementManager.listDisbursementsByParameter(CheckPayments.class,
			"invoice.supplierInvoiceNo", invoice.getSupplierInvoiceNo(),session);
	
	Iterator checkVoucherIterator = disbursementList.iterator();
	double amountPaid = 0;
	while (checkVoucherIterator.hasNext()) {
		CheckPayments cp = (CheckPayments) checkVoucherIterator.next();
		if(cp.getCheckVoucherNumber().equalsIgnoreCase(chp.getCheckVoucherNumber())){
			amountPaid = amountPaid + chp.getAmountToPay();
		}else {
			amountPaid = amountPaid + cp.getAmountToPay();
		}
	}
	
	return invoice.getPurchaseOrderDetailsTotalAmount() - amountPaid;
}
private void sortListsAlphabetically(){
		
		List purchaseOrderDetailsList= this.orderDetails;
		List sortedPurchaseOrderDetailsList= new ArrayList();
		
		HashMap<String,PurchaseOrderDetails> map = new HashMap<String,PurchaseOrderDetails>();
		List itemCodeList = new ArrayList();
		try {
			Iterator<PurchaseOrderDetails> itr =purchaseOrderDetailsList.iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
				map.put(podetails.getItemCode(),podetails);
				itemCodeList.add(podetails.getItemCode());
			}
			Collections.sort(itemCodeList);
			Iterator<String> iteratorSorted =itemCodeList.iterator();
			while(iteratorSorted.hasNext()) {
				String  code = (String)iteratorSorted.next();
				sortedPurchaseOrderDetailsList.add(map.get(code));
			}
			this.setOrderDetails(sortedPurchaseOrderDetailsList);
		}catch(NullPointerException nfe) {
			nfe.printStackTrace();
		}
	}

private boolean validatePettyCash() throws Exception {
	boolean errorFound = false;
	if ("".equals(pc.getPcVoucherNumber())) {
		addFieldError("pc.pcVoucherNumber", "REQUIRED");
		errorFound = true;
	}
	 if (null == pc.getPcVoucherDate()) {
	 addActionMessage("REQUIRED Voucher Date");
	 errorFound = true;
	 }
	 if ("".equals(pc.getPayee())) {
	 addFieldError("pc.payee", "REQUIRED");
	 errorFound = true;
	 }else {
		 if (pc.getPayee().trim().length()>100) {
			 addFieldError("pc.payee", SASConstants.MAXIMUM_LENGTH_100);
			 errorFound = true;
		 }
	 }
	 if ("".equals(pc.getParticulars())) {
	 addFieldError("pc.particulars", "REQUIRED");
	 errorFound = true;
	 }else {
		 if (pc.getParticulars().trim().length()>200) {
			 addFieldError("pc.particulars", SASConstants.MAXIMUM_LENGTH_200);
			 errorFound = true;
		 }
	 }
	 if (0==(pc.getAmount())) {
	 addFieldError("pc.amount", "REQUIRED");
	 errorFound = true;
	 }
	 if ((getTransactionList().get(0).getAmount() == 0 )) {
			addActionMessage("REQUIRED: Accounting Entries Details");
			errorFound = true;
		}
	return errorFound;
}

private boolean validateCashPayment() throws Exception {

	boolean errorFound = false;
	if ("".equals(cp.getCashVoucherNumber())) {
		addFieldError("cp.cashVoucherNumber", "REQUIRED");
		errorFound = true;
	}
	 if (null == cp.getCashVoucherDate()) {
	 addActionMessage("REQUIRED Voucher Date");
	 errorFound = true;
	 }
	 if ("".equals(cp.getPayee())) {
	 addFieldError("cp.payee", "REQUIRED");
	 errorFound = true;
	 }else {
		 if (cp.getPayee().trim().length()>100) {
			 addFieldError("cp.payee", SASConstants.MAXIMUM_LENGTH_100);
			 errorFound = true;
		 }
	 }
	 if ("".equals(cp.getParticulars())) {
	 addFieldError("cp.particulars", "REQUIRED");
	 errorFound = true;
	 }else {
		 if (cp.getParticulars().trim().length()>200) {
			 addFieldError("cp.particulars", SASConstants.MAXIMUM_LENGTH_200);
			 errorFound = true;
		 }
	 }
	 if (0==(cp.getAmount())) {
	 addFieldError("cp.amount", "REQUIRED");
	 errorFound = true;
	 }
	 if ((getTransactionList().get(0).getAmount() == 0 )) {
			addActionMessage("REQUIRED: Accounting Entries Details");
			errorFound = true;
		}
	return errorFound;
}

private boolean validateCheckPayment() throws Exception {

	boolean errorFound = false;
	if ("".equals(chp.getCheckVoucherNumber())) {
		addFieldError("chp.checkVoucherNumber", "REQUIRED");
		errorFound = true;
	}
	 if (null == chp.getCheckVoucherDate()) {
	 addActionMessage("REQUIRED Voucher Date");
	 errorFound = true;
	 }
	 if ("".equals(chp.getPayee())) {
	 addFieldError("chp.payee", "REQUIRED");
	 errorFound = true;
	 }else {
		 if (chp.getPayee().trim().length()>100) {
			 addFieldError("chp.payee", SASConstants.MAXIMUM_LENGTH_100);
			 errorFound = true;
		 }
	 }
	 if ("".equals(chp.getParticulars())) {
	 addFieldError("chp.particulars", "REQUIRED");
	 errorFound = true;
	 }else {
		 if (chp.getParticulars().trim().length()>200) {
			 addFieldError("chp.particulars", SASConstants.MAXIMUM_LENGTH_200);
			 errorFound = true;
		 }
	 }
	 if (0==(chp.getAmount())) {
	 addFieldError("chp.amount", "REQUIRED");
	 errorFound = true;
	 }
	 if ("".equals(chp.getAmountInWords())) {
		 addFieldError("chp.amountInWords", "REQUIRED");
		 errorFound = true;
		 }
	 if ("".equals(chp.getCheckNo())) {
		 addFieldError("chp.checkNo", "REQUIRED");
		 errorFound = true;
		 }
	 if ("".equals(chp.getBankName())) {
		 addFieldError("chp.bankName", "REQUIRED");
		 errorFound = true;
		 }
	 if ((getTransactionList().get(0).getAmount() == 0 )) {
			addActionMessage("REQUIRED: Accounting Entries Details");
			errorFound = true;
		}
	return errorFound;
}
private boolean validateCheckVoucher() throws Exception {

	boolean errorFound = false;
	if ("".equals(chp.getCheckVoucherNumber())) {
		addFieldError("chp.checkVoucherNumber", "REQUIRED");
		errorFound = true;
	}
	 if (null == chp.getCheckVoucherDate()) {
	 addActionMessage("REQUIRED: Voucher Date");
	 errorFound = true;
	 }
	 if ("".equals(chp.getPayee())) {
	 addFieldError("chp.payee", "REQUIRED");
	 errorFound = true;
	 }else {
		 if (chp.getPayee().trim().length()>100) {
			 addFieldError("chp.payee", SASConstants.MAXIMUM_LENGTH_100);
			 errorFound = true;
		 }
	 }
	 if (0==(chp.getAmount())) {
	 addFieldError("chp.amount", "REQUIRED");
	 errorFound = true;
	 }
	 if ("".equals(chp.getAmountInWords())) {
		 addFieldError("chp.amountInWords", "REQUIRED");
		 errorFound = true;
		 }
	 if ("".equals(chp.getCheckNo())) {
		 addFieldError("chp.checkNo", "REQUIRED");
		 errorFound = true;
		 }
	 if ("".equals(chp.getBankName())) {
		 addFieldError("chp.bankName", "REQUIRED");
		 errorFound = true;
		 }
	 if ((getTransactionList().get(0).getAmount() == 0 )) {
			addActionMessage("REQUIRED: Accounting Entries Details");
			errorFound = true;
		}
	return errorFound;
}
	
public SupplierInvoice getInvoice() {
	return invoice;
}
public void setInvoice(SupplierInvoice invoice) {
	this.invoice = invoice;
}
public String getInvAmount() {
	return invAmount;
}
public void setInvAmount(String invAmount) {
	this.invAmount = invAmount;
}
public String getPayee() {
	return payee;
}
public void setPayee(String payee) {
	this.payee = payee;
}
public String getInvId() {
	return invId;
}
public void setInvId(String invId) {
	this.invId = invId;
}
public List getClassifList() {
	return classifList;
}
public void setClassifList(List classifList) {
	this.classifList = classifList;
}
public String getPcNo() {
	return pcNo;
}
public void setPcNo(String pcNo) {
	this.pcNo = pcNo;
}
public String getCpNo() {
	return cpNo;
}
public void setCpNo(String cpNo) {
	this.cpNo = cpNo;
}
public String getChpNo() {
	return chpNo;
}
public void setChpNo(String chpNo) {
	this.chpNo = chpNo;
}
public String getSubModule() {
	return subModule;
}
public void setSubModule(String subModule) {
	this.subModule = subModule;
}
public PettyCash getPc() {
	return pc;
}
public void setPc(PettyCash pc) {
	this.pc = pc;
}
public CashPayment getCp() {
	return cp;
}
public void setCp(CashPayment cp) {
	this.cp = cp;
}
public CheckPayments getChp() {
	return chp;
}
public void setChp(CheckPayments chp) {
	this.chp = chp;
}

public String getForWhat() {
	return forWhat;
}
public void setForWhat(String forWhat) {
	this.forWhat = forWhat;
}

public List getOrderDetails() {
	return orderDetails;
}
public void setOrderDetails(List orderDetails) {
	this.orderDetails = orderDetails;
}
public List getInvoiceNoList() {
	return invoiceNoList;
}
public void setInvoiceNoList(List invoiceNoList) {
	this.invoiceNoList = invoiceNoList;
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
