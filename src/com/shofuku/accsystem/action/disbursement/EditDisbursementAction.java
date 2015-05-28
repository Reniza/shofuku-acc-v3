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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.lookups.ExpenseClassification;
import com.shofuku.accsystem.domain.lookups.PaymentClassification;
import com.shofuku.accsystem.domain.lookups.PaymentTerms;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class EditDisbursementAction extends ActionSupport implements Preparable{

	private static final long serialVersionUID = 1L;
	

	Map actionSession;
	UserAccount user;

	SupplierManager supplierManager;
	AccountEntryManager accountEntryManager;
	TransactionManager transactionManager;
	LookupManager lookupManager;
	DisbursementManager disbursementManager;

	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		
		supplierManager 		= (SupplierManager) 	actionSession.get("supplierManager");
		accountEntryManager		= (AccountEntryManager) actionSession.get("accountEntryManager");
		transactionManager 		= (TransactionManager) 	actionSession.get("transactionManager");
		lookupManager 			= (LookupManager) 		actionSession.get("lookupManager");
		disbursementManager 	= (DisbursementManager) actionSession.get("disbursementManager");
		
	}
	private String subModule;
	private String forWhat;
	private String forWhatDisplay;
	
	List classifList;
	List orderDetails;
	List invoiceNoList;
	
	private String moduleParameter;
	PettyCash pc;
	CashPayment cp;
	CheckPayments chp;
	

	//START 2013 - PHASE 3 : PROJECT 1: MARK
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
		Iterator itr;
	//END 2013 - PHASE 3 : PROJECT 1: MARK  
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		try {
			forWhatDisplay = "edit";
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
			
			if (getSubModule().equalsIgnoreCase("AA")) {
				PettyCash pc = new PettyCash();
				pc = (PettyCash) disbursementManager.listDisbursementsByParameter(
						pc.getClass(), "pcVoucherNumber",
						this.getPc().getPcVoucherNumber(),session).get(0);
				
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", pc.getPcVoucherNumber(), session);
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
				this.pc.setTransactions(transactionList);
				//END Phase 3 - Azhee
				setPc(pc);
				classifList = lookupManager.getLookupElements(ExpenseClassification.class, "PETTYCASH",session);
				
				return "pettyCash";
			} else if (getSubModule().equalsIgnoreCase("BB")) {
				CashPayment cp = new CashPayment();
				cp = (CashPayment) disbursementManager.listDisbursementsByParameter(
						cp.getClass(), "cashVoucherNumber",
						this.getCp().getCashVoucherNumber(),session).get(0);
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", cp.getCashVoucherNumber(), session);
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
				this.cp.setTransactions(transactionList);
				//END Phase 3 - Azhee
				this.setCp(cp);
				classifList = lookupManager.getLookupElements(PaymentClassification.class, "CASHPAYMENT",session);
				
				return "cashPayment";
			} else if (getSubModule().equalsIgnoreCase("CC"))  {
				CheckPayments chp = new CheckPayments();
				chp = (CheckPayments) disbursementManager.listDisbursementsByParameter(
						chp.getClass(), "checkVoucherNumber",
						this.getChp().getCheckVoucherNumber(),session).get(0);
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", chp.getCheckVoucherNumber(), session);
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
				this.chp.setTransactions(transactionList);
				//END Phase 3 - Azhee
				this.setChp(chp);
				classifList = lookupManager.getLookupElements(PaymentTerms.class, "CHECKPAYMENT",session);
				return "checkPayment";
			} else {
				CheckPayments chp = new CheckPayments();
				invoiceNoList = disbursementManager.listAlphabeticalAscByParameter(SupplierInvoice.class, "supplierInvoiceNo", session);
				
				chp = (CheckPayments) disbursementManager.listDisbursementsByParameter(
						chp.getClass(), "checkVoucherNumber",
						this.getChp().getCheckVoucherNumber(),session).get(0);
				
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", chp.getCheckVoucherNumber(), session);
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
				this.chp.setTransactions(transactionList);
				//END Phase 3 - Azhee
				List invoiceList = null;
				invoiceList = supplierManager.listSuppliersByParameter(SupplierInvoice.class, "supplierInvoiceNo", chp.getInvoice().getSupplierInvoiceNo(),session);
					SupplierInvoice invoice = new SupplierInvoice();
					invoice = (SupplierInvoice) invoiceList.get(0);
			
				orderDetails = new ArrayList<PurchaseOrderDetails>();
				Set<PurchaseOrderDetails> invoiceDetailSet = invoice.getPurchaseOrderDetails();
				
				Iterator<PurchaseOrderDetails> itr = invoiceDetailSet.iterator();
					while(itr.hasNext()) {
						PurchaseOrderDetails invoiceDetails = itr.next();
						orderDetails.add(invoiceDetails);
					}
					sortListsAlphabetically();
				chp.setInvoice(invoice);
				this.setChp(chp);
				classifList = lookupManager.getLookupElements(PaymentTerms.class, "CHECKPAYMENT",session);
				return "checkVoucher";
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("AA")){
				return "pettyCash";
			}else if (getSubModule().equalsIgnoreCase("BB")){
				return "cashPayment";
			}else if (getSubModule().equalsIgnoreCase("CC")){
				return "checkPayment";
			}
			else {
				return "checkVoucher";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

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
	
	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public String getModuleParameter() {
		return moduleParameter;
	}

	public void setModuleParameter(String moduleParameter) {
		this.moduleParameter = moduleParameter;
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
	
	public List getClassifList() {
		return classifList;
	}

	public void setClassifList(List classifList) {
		this.classifList = classifList;
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
