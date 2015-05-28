package com.shofuku.accsystem.action.suppliers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class UpdateSupplierAction extends ActionSupport implements Preparable{

	/**
	 * 
	 */
	
	Map actionSession;
	UserAccount user;

	SupplierManager supplierManager;
	AccountEntryManager accountEntryManager;
	TransactionManager transactionManager;
	InventoryManager inventoryManager;
	FinancialsManager financialsManager;	
	RecordCountHelper rch;
	InventoryUtil invUtil;
	AccountEntryProfileUtil apeUtil;

	DisbursementManager disbursementManager;
	
	
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	PurchaseOrderDetailHelper poDetailsHelper;
	
	@Override
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		supplierManager = (SupplierManager) actionSession.get("supplierManager");
		accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
		transactionManager = (TransactionManager) actionSession.get("transactionManager");
		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
		financialsManager = (FinancialsManager) actionSession.get("financialsManager");
		disbursementManager = (DisbursementManager) actionSession.get("disbursementManager");
		
		rch = new RecordCountHelper(actionSession);
		invUtil = new InventoryUtil(actionSession);
		apeUtil = new AccountEntryProfileUtil(actionSession);
		
		if(poDetailsHelper==null) {
			poDetailsHelper = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsHelper.setActionSession(actionSession);
		}
		if(poDetailsHelperToCompare==null) {
			poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsHelperToCompare.setActionSession(actionSession);
		}
		
	}
	
	
	private static final long serialVersionUID = 1L;
	private String subModule;
	Supplier supplier;
	SupplierPurchaseOrder po;
	ReceivingReport rr;
	SupplierInvoice invoice;
	private String forWhat;
	private String forWhatDisplay;
	private double tempTotal;
	
	private String supId;
	private String poId;
	private String rrId;
	private String invId;
	
	List purchaseOrderNoList;
	List supplierNoList;
	List receivingReportNoList;
	List checkVoucherList;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
	List accountProfileCodeList;
	List<Transaction> transactionList;
	List<Transaction> transactions;

	Vat vatDetails;
	
	DateFormatHelper df = new DateFormatHelper();
	
	private Session getSession(){
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public String execute() throws Exception{
		Session session = getSession();
		try {
			forWhatDisplay="edit";
			boolean updateResult=false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
			if (getSubModule().equalsIgnoreCase("supplierProfile")){
				supplier.setSupplierId(supId);
				if (validateSupplierProfile()) {
				}else {
				updateResult = supplierManager.updateSupplier(supplier,session);
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
						forWhat="true";	
						//forWhatDisplay="edit";
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
						}
				}	
			
			return "profileUpdated";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")){
				List supPo2=null;
				supplierNoList = supplierManager.listAlphabeticalAscByParameter(Supplier.class, "supplierId", session);
				supPo2 = supplierManager.listSuppliersByParameter(Supplier.class, "supplierId" , getPo().getSupplier().getSupplierId(),session);
					if (supPo2.isEmpty()){
						addActionMessage("Supplier ID: " + SASConstants.NON_EXISTS);
						po.setSupplierPurchaseOrderId(poId);
						includePoDetails();
					}else{
						po.setSupplierPurchaseOrderId(poId);
						po.setSupplier((Supplier) supPo2.get(0));
						//get payment date based on terms
						DateFormatHelper dfh = new DateFormatHelper();
						po.setPaymentDate(dfh.getPaymentDateByTerm(po.getDateOfDelivery(), po.getSupplier().getPaymentTerm()));
						if(null==poDetailsHelper) {
							addActionError("ORDER DETAILS IS EMPTY");
						}else {
						poDetailsHelper.setOrderDate(po.getPurchaseOrderDate());
						po.setPurchaseOrderDetails(poDetailsHelper.persistNewSetElements(session));
						poDetailsHelper.generatePODetailsListFromSet(po.getPurchaseOrderDetails());
						}
						po.setPaymentDate(dfh.getPaymentDateByTerm(po
								.getDateOfDelivery(), po.getSupplier()
								.getPaymentTerm()));
						po.setTotalAmount(poDetailsHelper.getTotalAmount());
						if (validatePurchaseOrder()) {
							includePoDetails();
							
						}else {
							if (po.getPurchaseOrderDetails().size()==0) {
								addActionError(SASConstants.EMPTY_ORDER_DETAILS);
							}else {
								updateResult = supplierManager.updateSupplier(po,session);
								if (updateResult== true) {
									addActionMessage(SASConstants.UPDATED);
									forWhat="true";
								}else {
									addActionMessage(SASConstants.UPDATE_FAILED);
								}	
							}
							
						}
							
					}
				return "poUpdated";
			}else if (getSubModule().equalsIgnoreCase("receivingReport")){
				List supRr2 = null;
				purchaseOrderNoList = supplierManager.listAlphabeticalAscByParameter(SupplierPurchaseOrder.class, "supplierPurchaseOrderId", session);
				
				supRr2 = supplierManager.listSuppliersByParameter(SupplierPurchaseOrder.class, "supplierPurchaseOrderId", getRr().getSupplierPurchaseOrder().getSupplierPurchaseOrderId(),session);
				boolean inventoryUpdateSuccess= false;
					if (supRr2.isEmpty()){
						addActionError("Puchase Order No.: " + SASConstants.NON_EXISTS);
						rr.setReceivingReportNo(rrId);
						includePoDetails();
					}else{
						rr.setReceivingReportNo(rrId);
						rr.setSupplierPurchaseOrder((SupplierPurchaseOrder) supRr2.get(0));
						
						/*
						 * Checking and fetching existing return slips
						 */
						
						Session rsSession = getSession();
						List returnSlipList = inventoryManager.listInventoryByParameter(ReturnSlip.class, "returnSlipReferenceOrderNo", rr.getReceivingReportNo(), rsSession);
						
						if(returnSlipList.size()>0) {
							rr.setReturnSlipList(returnSlipList);
						}else {
							rr.setReturnSlipList(null);
						}
						
						if(null==poDetailsHelperToCompare) {
							poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
						}
							poDetailsHelperToCompare.generatePODetailsListFromSet(rr.getSupplierPurchaseOrder().getPurchaseOrderDetails());
							
						if(null==poDetailsHelper) {
						}else {
							poDetailsHelper.setOrderDate(df.dynamicParseDateToTimestamp(rr.getReceivingReportDate(),SASConstants.TIMESTAMP_FORMAT));
							Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
							rr.setPurchaseOrderDetails(podetailSet);
							poDetailsHelper.generatePODetailsListFromSet(rr.getPurchaseOrderDetails());
						
						}

						/*
						 * this is the part to update inventory
						 * inventoryManager
						 * .updateInventoryFromOrders(poDetailsHelper
						 * ,"rr");
						 */
						
						/*parameters for the changein order
						 *  1st - old orderDetail helper (for update use only , for add leave it blank)
						 *  2nd - incoming order
						 *  3rd - order type to determine if there is an addition or deduction to inventory
						*/
						ReceivingReport oldRR = 
								(ReceivingReport) supplierManager.listSuppliersByParameter(rr.getClass(), "receivingReportNo", 
										rrId,getSession()).get(0);
						PurchaseOrderDetailHelper helperOld = new PurchaseOrderDetailHelper(actionSession);
						helperOld.generatePODetailsListFromSet(oldRR.getPurchaseOrderDetails());
						PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.getChangeInOrder(helperOld, poDetailsHelper , SASConstants.ORDER_TYPE_RR);
						
						try {
							inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
							inventoryUpdateSuccess = true;
						} catch (Exception e) {
							e.printStackTrace();
							addActionError(e.getMessage());
							inventoryUpdateSuccess=false;
						}
						DateFormatHelper dfh = new DateFormatHelper();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String tryTS = sdf.format(rr.getReceivingReportDate());
						
						rr.setReceivingReportPaymentDate(dfh.getPaymentDateByTerm(dfh.parseStringToTimestamp(tryTS), rr.getSupplierPurchaseOrder().getSupplier().getPaymentTerm()));
						rr.setTotalAmount(poDetailsHelper.getTotalAmount());
						//START - 2013 - PHASE 3 : PROJECT 1: MARK
						transactionManager.discontinuePreviousTransactions(rr.getReceivingReportNo(),session);
						//transactionList = new ArrayList();
						transactionList = getTransactionList();
						updateAccountingEntries(rr.getReceivingReportNo(),session,SASConstants.RECEIVINGREPORT);
						this.setTransactionList(transactions);
						rr.setTransactions(transactions);
						//END
						if (validateReceivingReport()) {
							includePoDetails();
						}else {
							if (rr.getPurchaseOrderDetails().size()==0) {
								addActionError(SASConstants.EMPTY_ORDER_DETAILS);
								
							}else {
								if(inventoryUpdateSuccess) {
									
									//computation of payment date in rr
									updateResult = supplierManager.updateSupplier(rr,session);
								}else {
									updateResult=false;
								}
								
								if (updateResult== true) {
									addActionMessage(SASConstants.UPDATED);
									forWhat="true";
								}else {
									addActionMessage(SASConstants.UPDATE_FAILED);
								}
								
							}
						}
					}
					return "rrUpdated";
			}else {
				List supInv2 = null;
				receivingReportNoList = supplierManager.listAlphabeticalAscByParameter(ReceivingReport.class, "receivingReportNo", session);
				supInv2 =  supplierManager.listSuppliersByParameter(ReceivingReport.class, "receivingReportNo", getInvoice().getReceivingReport().getReceivingReportNo(),session);

				
				if (supInv2.isEmpty()){
						addActionError("Receiving Report No.: " + SASConstants.NON_EXISTS);
						invoice.setSupplierInvoiceNo(invId);
						includePoDetails();
					}else{
						if (invId.isEmpty()){
							invoice.setSupplierInvoiceNo(invoice.getSupplierInvoiceNo());
						}else{
							invoice.setSupplierInvoiceNo(invId);
						}
							invoice.setReceivingReport((ReceivingReport) supInv2.get(0));
						
						if(null==poDetailsHelperToCompare) {
							poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
						}
							poDetailsHelperToCompare.generatePODetailsListFromSet(invoice.getReceivingReport().getPurchaseOrderDetails());
						
						if(null==poDetailsHelper) {
						}else {
							poDetailsHelper.setOrderDate(invoice.getSupplierInvoiceDate());
							Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
							invoice.setPurchaseOrderDetails(podetailSet);
							poDetailsHelper.generatePODetailsListFromSet(invoice.getPurchaseOrderDetails());
							//2014 - ITEM COLORING
		 					poDetailsHelper.generateItemTypesForExistingItems(session);
							
						}
						invoice.setPurchaseOrderDetailsTotalAmount(poDetailsHelper.getTotalAmount());
						
						Session disbursementSession= getSession();
						checkVoucherList= disbursementManager.listDisbursementsByParameter(CheckPayments.class, "invoice.supplierInvoiceNo", invId, disbursementSession);
						
						
						Iterator itr = checkVoucherList.iterator();
						while (itr.hasNext()){
							CheckPayments chpFromList = (CheckPayments) itr.next();
							tempTotal = tempTotal + chpFromList.getAmountToPay();
						}
						if (validateInvoice()) {
							//includePoDetails();
						}else {
							if (invoice.getPurchaseOrderDetails().size()==0) {
								addActionError(SASConstants.EMPTY_ORDER_DETAILS);
							}else {
								//added to update the remaining balance field with all the paid vouchers associated with this invoice
								invoice.setRemainingBalance(checkForPaidVouchers(session));
//								invoice.setRemainingBalance(checkForReturnedItems(session));
								
								//START - 2013 - PHASE 3 : PROJECT 1: MARK
								transactionManager.discontinuePreviousTransactions(invoice.getSupplierInvoiceNo(),session);
								transactionList = getTransactionList();
								updateAccountingEntries(invoice.getSupplierInvoiceNo(),session,SASConstants.SUPPLIERINVOICE);
								this.setTransactionList(transactions);
								invoice.setTransactions(transactions);
								//END
								
								//START: 2013 - PHASE 3 : PROJECT 4: MARK
								Vat vatDetails = invoice.getVatDetails();
								vatDetails.setVatReferenceNo(invId);
								vatDetails.setAmount(invoice.getDebit1Amount());
								vatDetails.setPayee(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getSupplierName());
								vatDetails.setOrDate(invoice.getSupplierInvoiceDate());
								vatDetails.setAddress(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getCompanyAddress());
								this.invoice.setVatDetails(vatDetails);
								financialsManager.updateVatDetails(vatDetails, session);							
								//END: 2013 - PHASE 3 : PROJECT 4: MARK
								
								updateResult = supplierManager.updateSupplier(invoice,session);
								
								if (updateResult== true) {
									addActionMessage(SASConstants.UPDATED);
									forWhat="true";
								}else {
									addActionMessage(SASConstants.UPDATE_FAILED);
								}
							}
							
						}
					}
				return "invoiceUpdated";
			}
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("supplierProfile")) {
				return "profileUpdated";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "poUpdated";
			}else if (getSubModule().equalsIgnoreCase("receivingReport")) {
				return "rrUpdated";
			}else {
				return "invoiceUpdated";
			}
		}
		finally {
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
		//return transactions;
	}

	//END - 2013 - PHASE 3 : PROJECT 1: MARK

	private double checkForPaidVouchers(Session session) {
		List disbursementList = disbursementManager.listDisbursementsByParameter(CheckPayments.class,
				"invoice.supplierInvoiceNo", invoice.getSupplierInvoiceNo(),session);
		
		Iterator checkVoucherIterator = disbursementList.iterator();
		double amountPaid = 0;
		while (checkVoucherIterator.hasNext()) {
			CheckPayments cp = (CheckPayments) checkVoucherIterator.next();
			amountPaid = amountPaid + cp.getAmountToPay();
		}
		
		return invoice.getPurchaseOrderDetailsTotalAmount() - amountPaid;
	}
	
	
	private double checkForReturnedItems(Session session) {
		
		List returnSlipList = inventoryManager.listInventoryByParameter(ReturnSlip.class, "returnSlipReferenceOrderNo", invoice.getReceivingReport().getReceivingReportNo(), session);
		
		if(returnSlipList.size()>0) {

		}else {
		}
		
		Iterator rsIterator = returnSlipList.iterator();
		double amountReturned = 0;
		while (rsIterator.hasNext()) {
			ReturnSlip rs = (ReturnSlip) rsIterator.next(); 
			
			Iterator poIterator = rs.getPurchaseOrderDetails().iterator();
			while(poIterator.hasNext()) {
				PurchaseOrderDetails poDetail = (PurchaseOrderDetails) poIterator.next();
				amountReturned = amountReturned + poDetail.getAmount();
			}
			
		}
		
		return invoice.getPurchaseOrderDetailsTotalAmount() - amountReturned;
	}

	private void includePoDetails() {
		if(poDetailsHelper!=null) {
			poDetailsHelper.prepareSetAndList();
		}if(poDetailsHelperToCompare!=null) {
			poDetailsHelperToCompare.prepareSetAndList();
		}
	}
	
	private boolean validateSupplierProfile() {

		boolean errorFound = false;
		if ("".equals(supplier.getSupplierId())) {
			addFieldError("supplier.supplierId", "REQUIRED");
			errorFound = true;
		}
		 if ("".equals(supplier.getSupplierName())) {
		 addFieldError("supplier.supplierName", "REQUIRED");
		 errorFound = true;
		 }
		 else if (supplier.getSupplierName().trim().length()>100) {
			 addFieldError("supplier.supplierName", "MAXIMUM LENGTH: 100 characters");
			 errorFound = true;
		 }
		 if ("".equals(supplier.getSupplierStatus())) {
		 addFieldError("supplier.supplierStatus","REQUIRED");
		 errorFound = true;
		 }
		// if ("".equals(supplier.getContactName())) {
		// addFieldError("supplier.contactName", "REQUIRED");
		// errorFound = true;
		// }
		// if ("".equals(supplier.getContactTitle())) {
		// addFieldError("supplier.contactTitle", "REQUIRED");
		// errorFound = true;
		// }
		 if ("".equals(supplier.getPhoneNumber())) {
			 addFieldError("supplier.phoneNumber", "REQUIRED");
			 errorFound = true;
		 }
		 else {
			 if (!(supplier.getPhoneNumber().contains("-"))) {
			 addFieldError("supplier.phoneNumber", "AREACODE-#######");
			 errorFound = true;
			 	}
			}
		 if (!("".equals(supplier.getFaxNumber()))) {
			 if (!(supplier.getPhoneNumber().contains("-"))) {
			 addFieldError("supplier.faxNumber", "AREACODE-#######");
			 errorFound = true;
			 	}
		 	}
		 // if ("".equals(supplier.getMobileNumber())) {
		// addFieldError("supplier.mobileNumber", "REQUIRED");
		// errorFound = true;
		// }
		 if (!("".equals(supplier.getEmailAddress()))) {
		 	if (!(supplier.getEmailAddress().contains("@") && supplier.getEmailAddress().contains(".com"))) {
		 		addFieldError("supplier.emailAddress", "REQUIRED: @domain.com");
		 		 errorFound = true;
		 	}
		
		 }
		 if ("".equals(supplier.getCompanyAddress())) {
			 addFieldError("supplier.companyAddress", "REQUIRED");
			 errorFound = true;
		 }else {
			 if (supplier.getCompanyAddress().trim().length()>200) {
				 addFieldError("supplier.companyAddress",
				 "MAXIMUM LENGTH: 200 characters");
				 errorFound = true;
			 }
		 }
		return errorFound;
	}
	private boolean validatePurchaseOrder() {
		boolean errorFound = false;
		/*
		 * Please dont delete me this logic is correct apply it to all insert
		 * other validations inside the IFs
		 */

		if ("".equals(po.getSupplierPurchaseOrderId())) {
			addFieldError("po.supplierPurchaseOrderId", "REQUIRED");
			errorFound = true;
		}
		 if (null == po.getPurchaseOrderDate()) {
		 addActionMessage("REQUIRED: PO Date");
		 errorFound = true;
		 }
		//
		// if (null == po.getPaymentDate()) {
		// addActionMessage("REQUIRED: Payment Date");
		// errorFound = true;
		// }
		//
		 if (null == po.getDateOfDelivery()) {
		 addActionMessage("REQUIRED: Delivery Date");
		 errorFound = true;
		 }
		
		if ("".equals(po.getSupplier().getSupplierId())) {
			addActionMessage("REQUIRED: Supplier ID");
			errorFound = true;
		}
		// if ("".equals(po.getPaymentTerm())) {
		// addFieldError("po.paymentTerm", "REQUIRED");
		// errorFound = true;
		// }

		return errorFound;
	}

	private boolean validateReceivingReport() {
		boolean errorFound = false;
		if ("".equals(rr.getReceivingReportNo())) {
			addFieldError("rr.receivingReportNo", "REQUIRED");
			errorFound = true;
		}
		 if (null==rr.getReceivingReportDate()){
		 addActionMessage("REQUIRED: RR Date.");
		 errorFound = true;
		 }
		if ("".equals(rr.getSupplierPurchaseOrder()
				.getSupplierPurchaseOrderId())) {
			addActionMessage("REQUIRED: Purchase Order No");
			errorFound = true;
		}
		// if ("".equals(rr.getRemarks())) {
		// addFieldError("rr.remarks", "REQUIRED");
		// errorFound = true;
		// }
		return errorFound;

	}

	private boolean validateInvoice() {
		boolean errorFound = false;
		if ("".equals(invoice.getSupplierInvoiceNo())) {
			addFieldError("invoice.supplierInvoiceNo", "REQUIRED");
			errorFound = true;
		}
		 if (null==invoice.getSupplierInvoiceDate()) {
		 addActionMessage("REQUIRED: Invoice Date");
		 errorFound = true;
		 }
		if ("".equals(invoice.getReceivingReport().getReceivingReportNo())) {
			addActionMessage("REQUIRED: Receiving Report No");
			errorFound = true;
		}
		if ((getTransactionList().get(0).getAmount() == 0 )) {
			addActionMessage("REQUIRED: Accounting Entries Details");
			errorFound = true;
		}
		return errorFound;
	}

	//getters and setter
	
	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public SupplierPurchaseOrder getPo() {
		return po;
	}

	public void setPo(SupplierPurchaseOrder po) {
		this.po = po;
	}

	public ReceivingReport getRr() {
		return rr;
	}

	public void setRr(ReceivingReport rr) {
		this.rr = rr;
	}

	public SupplierInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SupplierInvoice invoice) {
		this.invoice = invoice;
	}

	public String getRrId() {
		return rrId;
	}

	public void setRrId(String rrId) {
		this.rrId = rrId;
	}

	
	
	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	
	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	
	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}
	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}
	
	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}
	public void setPoDetailsHelperToCompare(PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
	}

	public List getPurchaseOrderNoList() {
		return purchaseOrderNoList;
	}

	public void setPurchaseOrderNoList(List purchaseOrderNoList) {
		this.purchaseOrderNoList = purchaseOrderNoList;
	}

	public List getSupplierNoList() {
		return supplierNoList;
	}

	public void setSupplierNoList(List supplierNoList) {
		this.supplierNoList = supplierNoList;
	}

	public List getReceivingReportNoList() {
		return receivingReportNoList;
	}

	public void setReceivingReportNoList(List receivingReportNoList) {
		this.receivingReportNoList = receivingReportNoList;
	}

	public String getForWhatDisplay() {
		return forWhatDisplay;
	}

	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}

	public List getCheckVoucherList() {
		return checkVoucherList;
	}

	public void setCheckVoucherList(List checkVoucherList) {
		this.checkVoucherList = checkVoucherList;
	}

	public double getTempTotal() {
		return tempTotal;
	}

	public void setTempTotal(double tempTotal) {
		this.tempTotal = tempTotal;
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

		public Vat getVatDetails() {
			return vatDetails;
		}

		public void setVatDetails(Vat vatDetails) {
			this.vatDetails = vatDetails;
		}
		
		//END 2013 - PHASE 3 : PROJECT 1: MARK 
}
