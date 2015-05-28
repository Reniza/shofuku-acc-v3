package com.shofuku.accsystem.action.disbursement;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.lookups.ExpenseClassification;
import com.shofuku.accsystem.domain.lookups.PaymentClassification;
import com.shofuku.accsystem.domain.lookups.PaymentTerms;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class AddDisbursementAction extends ActionSupport {

	private static final long serialVersionUID = -5808746491946120539L;
	

	Map actionSession;
	UserAccount user;

	RecordCountHelper rch;
	
	SupplierManager supplierManager;
	AccountEntryManager accountEntryManager;
	TransactionManager transactionManager;
	LookupManager lookupManager;
	DisbursementManager disbursementManager;
	FinancialsManager financialsManager;

	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		rch = new RecordCountHelper(actionSession);
		
		supplierManager 		= (SupplierManager) 	actionSession.get("supplierManager");
		accountEntryManager		= (AccountEntryManager) actionSession.get("accountEntryManager");
		transactionManager 		= (TransactionManager) 	actionSession.get("transactionManager");
		lookupManager 			= (LookupManager) 		actionSession.get("lookupManager");
		disbursementManager 	= (DisbursementManager) actionSession.get("disbursementManager");
		financialsManager 		= (FinancialsManager) 	actionSession.get("financialsManager");
		
	}
	// search parameters
	private String subModule;
	private String moduleParameter;
	private String moduleParameterValue;
	private String populate;
	private String forWhat;
	private String forWhatDisplay;
	
	List classifList;
	List invoiceNoList;
	SupplierInvoice invoice;
	Supplier supplier;
	// disbursement objects
	PettyCash pc;
	CashPayment cp;
	CheckPayments chp;
	private String invId;

	List orderDetails;
	List supplierList;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
		//END 2013 - PHASE 3 : PROJECT 1: MARK 
	
	public String newDisbursementEntry() {
		Session session = getSession();
		supplierList = supplierManager.listAlphabeticalAscByParameter(Supplier.class,"supplierName", session);
		try {
			if (getSubModule().equalsIgnoreCase("PettyCash")) {
				classifList = lookupManager.getLookupElements(
						ExpenseClassification.class, "PETTYCASH",session);
				pc = new PettyCash();
				pc.setPcVoucherNumber(rch.getPrefix(SASConstants.PETTYCASH,
						SASConstants.PETTYCASH_PREFIX));
								return "pettyCash";
			} else if (getSubModule().equalsIgnoreCase("cashPayment")) {

				classifList = lookupManager.getLookupElements(
						PaymentClassification.class, "CASHPAYMENT",session);
				cp = new CashPayment();
				cp.setCashVoucherNumber(rch.getPrefix(SASConstants.CASHPAYMENT,
						SASConstants.CASHPAYMENT_PREFIX));
				return "cashPayment";
			} else if (getSubModule().equalsIgnoreCase("checkPayment")) {
				classifList = lookupManager.getLookupElements(
						PaymentTerms.class, "CHECKPAYMENT",session);
				chp = new CheckPayments();
				chp.setCheckVoucherNumber(rch.getPrefix(SASConstants.CHECK_VOUCHER,
						SASConstants.CHECK_VOUCHER_PREFIX));
				return "checkPayment";
			}else {
				invoiceNoList = disbursementManager.listAlphabeticalAscByParameter(SupplierInvoice.class, "supplierInvoiceNo", session);
				chp = new CheckPayments();
				chp.setCheckVoucherNumber(rch.getPrefix(SASConstants.CHECK_VOUCHER,
						SASConstants.CHECK_VOUCHER_PREFIX));
				return "checkVoucher";
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("pettyCash")) {
				return "pettyCash";
			} else if (getSubModule().equalsIgnoreCase("cashPayment")) {
				return "cashPayment";
			}else if (getSubModule().equalsIgnoreCase("checkPayment")) {
				return "checkPayment";
			} else {
				return "checkVoucher";
			}

		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() throws Exception {
		Session session = getSession();
		supplierList = supplierManager.listAlphabeticalAscByParameter(Supplier.class,"supplierName", session);
		try {
			// insert addFunction
			boolean addResult = false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);			
			
			if (getSubModule().equalsIgnoreCase("pettyCash")) {
				classifList = lookupManager.getLookupElements(
						ExpenseClassification.class, "PETTYCASH",session);
				supplier = (Supplier) supplierManager.listSuppliersByParameter(Supplier.class, "supplierName", pc.getPayee(), session).get(0);
				
				if (validatePettyCash()) {
				} else {
					List pcList = disbursementManager.listDisbursementsByParameter(
							PettyCash.class, "pcVoucherNumber", getPc()
									.getPcVoucherNumber(),session);
					if (!(pcList.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						pc.setCreditTitle("PETTY CASH");
						pc.setDebitTitle(pc.getDescription());
						pc.setDebitAmount(pc.getAmount());
						pc.setCreditAmount(pc.getAmount());
						
						//START PHASE 3 
						transactionList = new ArrayList();
						Transaction transaction = new Transaction();
						transactionList.add(transaction);
						//END PHASE 3
						//START: 2013 - PHASE 3 : PROJECT 4: AZ
						Vat vatDetails = new Vat();
						//vatDetails.setAddress(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getCompanyAddress());
						//TEST ONLY WHILE WAITING FOR TIN FOR SUPPLIER
						vatDetails.setTinNumber(supplier.getTin());
						vatDetails.setPayee(pc.getPayee());
						vatDetails.setAmount(pc.getAmount());
						vatDetails.setVattableAmount(disbursementManager.computeVat(pc.getAmount()));
						vatDetails.setVatAmount(disbursementManager.computeVatAmount(vatDetails.getVattableAmount()));
						vatDetails.setVatReferenceNo(pc.getPcVoucherNumber());
						//vatDetails.setAddress(pc.getVatDetails().getAddress());
						vatDetails.setAddress(supplier.getCompanyAddress());
						vatDetails.setOrNo(pc.getVatDetails().getOrNo());
						vatDetails.setOrDate(pc.getPcVoucherDate());
						pc.setVatDetails(vatDetails);
						
						financialsManager.insertVatDetails(vatDetails, session);							
						//END: 2013 - PHASE 3 : PROJECT 4: AZ
						addResult = disbursementManager.addDisbursementObject(pc,session);
						if (addResult == true) {
							rch.updateCount(SASConstants.PETTYCASH, "add");
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionMessage(SASConstants.FAILED);
						}
					}
				}
				return "pettyCash";
			} else if (getSubModule().equalsIgnoreCase("cashPayment")) {
				classifList = lookupManager.getLookupElements(
						PaymentClassification.class, "CASHPAYMENT",session);
				supplier = (Supplier) supplierManager.listSuppliersByParameter(Supplier.class, "supplierName", cp.getPayee(), session).get(0);
				
				if (validateCashPayment()) {
				} else {
					List cpList = disbursementManager.listDisbursementsByParameter(
							CashPayment.class, "cashVoucherNumber", getCp()
									.getCashVoucherNumber(),session);
					if (!(cpList.isEmpty())) {
						addActionMessage(SASConstants.EXISTS);
					} else {
						cp.setCreditTitle("CASH");
						cp.setDebitTitle(cp.getDescription());
						cp.setDebitAmount(cp.getAmount());
						cp.setCreditAmount(cp.getAmount());
						//START PHASE 3 
						transactionList = new ArrayList();
						Transaction transaction = new Transaction();
						transactionList.add(transaction);
						//END PHASE 3
						
						//START: 2013 - PHASE 3 : PROJECT 4: MARK
						Vat vatDetails = new Vat();
						//vatDetails.setAddress(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getCompanyAddress());
						//TEST ONLY WHILE WAITING FOR TIN FOR SUPPLIER
						vatDetails.setTinNumber(supplier.getTin());
						vatDetails.setPayee(cp.getPayee());
						vatDetails.setAmount(cp.getAmount());
						vatDetails.setVattableAmount(disbursementManager.computeVat(cp.getAmount()));
						vatDetails.setVatAmount(disbursementManager.computeVatAmount(vatDetails.getVattableAmount()));
						vatDetails.setVatReferenceNo(cp.getCashVoucherNumber());
						//vatDetails.setAddress(cp.getVatDetails().getAddress());
						vatDetails.setAddress(supplier.getCompanyAddress());
						vatDetails.setOrNo(cp.getVatDetails().getOrNo());
						vatDetails.setOrDate(cp.getCashVoucherDate());
						cp.setVatDetails(vatDetails);
						financialsManager.insertVatDetails(vatDetails, session);							
						addResult = disbursementManager.addDisbursementObject(cp,session);
						if (addResult == true) {
							rch.updateCount(SASConstants.CASHPAYMENT, "add");
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionMessage(SASConstants.FAILED);
						}
					}
				}
				return "cashPayment";
			} else if (getSubModule().equalsIgnoreCase("supplierCheckVoucher")) {
				return addSupplierCheckVoucher();
			} else {
				classifList = lookupManager.getLookupElements(
						PaymentTerms.class, "CHECKPAYMENT",session);
				supplier = (Supplier) supplierManager.listSuppliersByParameter(Supplier.class, "supplierName", chp.getPayee(), session).get(0);
				
				if (validateCheckPayment()) {
				} else {
					List chpList = disbursementManager.listDisbursementsByParameter(
							CheckPayments.class, "checkVoucherNumber", getChp()
									.getCheckVoucherNumber(),session);
					if (!(chpList.isEmpty())) {
						addActionMessage(SASConstants.EXISTS);
					} else {
						chp.setDebitTitle(chp.getDescription());
						//chp.setCreditTitle("CIB-BDO");
						chp.setDebitAmount(chp.getAmount());
						/*START P3 AZ - REMOVED
						 * if (chp.getVat().equalsIgnoreCase("VAT")){
							chp.setFinalAmount(manager.computeVat(chp.getAmount()));
							chp.setVatAmount(chp.getAmount() - chp.getFinalAmount());
						}else{
							chp.setVatAmount(0.00);
							chp.setFinalAmount(chp.getAmount());
						}*/
						chp.setCreditAmount(chp.getAmount());
						//chp.setBankName("BDO");
						//START PHASE 3 
						transactionList = new ArrayList();
						Transaction transaction = new Transaction();
						transactionList.add(transaction);
						//END PHASE 3
						//START: 2013 - PHASE 3 : PROJECT 4: MARK
						Vat vatDetails = new Vat();
						//vatDetails.setAddress(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getCompanyAddress());
						//TEST ONLY WHILE WAITING FOR TIN FOR SUPPLIER
						vatDetails.setTinNumber(supplier.getTin());
						vatDetails.setPayee(chp.getPayee());
						vatDetails.setAmount(chp.getAmount());
						vatDetails.setVattableAmount(disbursementManager.computeVat(chp.getAmount()));
						vatDetails.setVatAmount(disbursementManager.computeVatAmount(vatDetails.getVattableAmount()));
						vatDetails.setVatReferenceNo(chp.getCheckVoucherNumber());
						//vatDetails.setAddress(chp.getVatDetails().getAddress());
						vatDetails.setAddress(supplier.getCompanyAddress());
						vatDetails.setOrNo(chp.getVatDetails().getOrNo());
						vatDetails.setOrDate(chp.getCheckVoucherDate());
						chp.setVatDetails(vatDetails);
						financialsManager.insertVatDetails(vatDetails, session);							
						addResult = disbursementManager.addDisbursementObject(chp,session);
						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							rch.updateCount(SASConstants.CHECK_VOUCHER, "add");
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionMessage(SASConstants.FAILED);
						}
					}
				}
				return "checkPayment";
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("pettyCash")) {
				return "pettyCash";
			} else if (getSubModule().equalsIgnoreCase("cashPayment")) {
				return "cashPayment";
			} else if (getSubModule().equalsIgnoreCase("supplierCheckVoucher")) {
				return "checkVoucher";
			} else {
				return "checkPayment";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	private String addSupplierCheckVoucher() {
		boolean addResult = false;
		Session session = getSession();
		
		invoiceNoList = disbursementManager.listAlphabeticalAscByParameter(SupplierInvoice.class, "supplierInvoiceNo", session);
		try {
			if (validateCheckVoucher()) {
			}else {
				List chpList = disbursementManager.listDisbursementsByParameter(
						CheckPayments.class, "checkVoucherNumber", getChp()
						.getCheckVoucherNumber(),session);
				if (!(chpList.isEmpty())) {
					addActionMessage(SASConstants.EXISTS);
				} else {
					List supInv = null;
					supInv = supplierManager.listSuppliersByParameter(
							SupplierInvoice.class, "supplierInvoiceNo", chp
							.getInvoice().getSupplierInvoiceNo(),session);
					if (supInv.size() == 0) {
						addActionMessage("Invoice NO:"
								+ SASConstants.NON_EXISTS);
					} else {
						SupplierInvoice invoice = new SupplierInvoice();
						invoice = (SupplierInvoice) supInv.get(0);
						orderDetails = new ArrayList<PurchaseOrderDetails>();
						Set<PurchaseOrderDetails> invoiceDetailSet = invoice
								.getPurchaseOrderDetails();
						Iterator<PurchaseOrderDetails> itr = invoiceDetailSet
								.iterator();
						if (orderDetails != null) {
							while (itr.hasNext()) {
								PurchaseOrderDetails invoiceDetails = itr
										.next();
								orderDetails.add(invoiceDetails);
							}
						}
						chp.setPayee(invoice.getReceivingReport()
								.getSupplierPurchaseOrder().getSupplier()
								.getSupplierName());
						chp.setAmount(invoice.getPurchaseOrderDetailsTotalAmount());
						chp.setDueDate(invoice.getReceivingReport().getReceivingReportPaymentDate());
						chp.setTotalPurchases(invoice.getPurchaseOrderDetailsTotalAmount());
						chp.setInvoice(invoice);
						chp.setAmountToPay(0);
						chp.setRemainingBalance(invoice.getRemainingBalance());
						//START PHASE 3 
						
						transactionList = new ArrayList();
						Transaction transaction = new Transaction();
						transactionList.add(transaction);
						//END PHASE 3
						
						//START: 2013 - PHASE 3 : PROJECT 4: MARK
						Vat vatDetails = new Vat();
						vatDetails.setAddress(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getCompanyAddress());
						//TEST ONLY WHILE WAITING FOR TIN FOR SUPPLIER
						vatDetails.setTinNumber(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getTin());
						vatDetails.setPayee(chp.getPayee());
						vatDetails.setAmount(chp.getAmount());
						vatDetails.setVattableAmount(disbursementManager.computeVat(chp.getAmountToPay()));
						vatDetails.setVatAmount(disbursementManager.computeVatAmount(vatDetails.getVattableAmount()));
						vatDetails.setVatReferenceNo(chp.getCheckVoucherNumber());
						vatDetails.setOrNo(chp.getVatDetails().getOrNo());
						vatDetails.setOrDate(chp.getCheckVoucherDate());
						chp.setVatDetails(vatDetails);
						financialsManager.insertVatDetails(vatDetails, session);							
						addResult = disbursementManager.addDisbursementObject(chp,session);
//						supplierManager.updateSupplier(invoice, supplierSession);
						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							rch.updateCount(SASConstants.CHECK_VOUCHER, "add");
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionMessage(SASConstants.FAILED);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "checkVoucher";
	}
	private void updateInvoiceRemainingBalance(SupplierInvoice invoice2) {
		Session session = getSession();
		invoice2.setRemainingBalance(invoice.getRemainingBalance() - chp.getAmountToPay());
		supplierManager.updateSupplier(invoice2, session);
		
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
		
		return errorFound;
	}
	private boolean validateCheckVoucher() throws Exception {

		boolean errorFound = false;
		if ("".equals(chp.getCheckVoucherNumber())) {
			addFieldError("chp.checkVoucherNumber", "REQUIRED");
			errorFound = true;
		}
		 /*if (null == chp.getCheckVoucherDate()) {
		 addActionMessage("REQUIRED: Voucher Date");
		 errorFound = true;
		 }
		 if ("".equals(chp.getCheckNo())) {
			 addFieldError("chp.checkNo", "REQUIRED");
			 errorFound = true;
			 }
			*/
		 if ("".equals(chp.getBankName())) {
			 addFieldError("chp.bankName", "REQUIRED");
			 errorFound = true;
			 }
		
		return errorFound;
	}

	public List getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List orderDetails) {
		this.orderDetails = orderDetails;
	}

	public SupplierInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SupplierInvoice invoice) {
		this.invoice = invoice;
	}

	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public String getPopulate() {
		return populate;
	}

	public void setPopulate(String populate) {
		this.populate = populate;
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

	public String getModuleParameterValue() {
		return moduleParameterValue;
	}

	public void setModuleParameterValue(String moduleParameterValue) {
		this.moduleParameterValue = moduleParameterValue;
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

	List classifications;

	public List getClassifications() {
		return classifications;
	}

	public void setClassifications(List classifications) {
		this.classifications = classifications;
	}

	public List getClassifList() {
		return classifList;
	}

	public void setClassifList(List classifList) {
		this.classifList = classifList;
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

		public String getInvId() {
			return invId;
		}

		public void setInvId(String invId) {
			this.invId = invId;
		}

		public List getSupplierList() {
			return supplierList;
		}

		public void setSupplierList(List supplierList) {
			this.supplierList = supplierList;
		}
		
		//END 2013 - PHASE 3 : PROJECT 1: MARK 

	
}
