package com.shofuku.accsystem.action.customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.CustomerStockLevel;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class UpdateCustomerAction extends ActionSupport implements Preparable{

	private static final long serialVersionUID = 1L;
	

	Map actionSession;
	UserAccount user;

	InventoryUtil inventoryUtil;
	AccountEntryProfileUtil accountEntryUtil;
	
	CustomerManager customerManager;
	InventoryManager inventoryManager; 
	AccountEntryManager accountEntryManager;
	TransactionManager transactionManager;
	FinancialsManager financialsManager;

	PurchaseOrderDetails orderDetails;
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	PurchaseOrderDetailHelper poDetailsGrouped;
	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperDraft;

	// add other managers for other modules Manager()
	
	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		inventoryUtil = new InventoryUtil(actionSession);
		accountEntryUtil = new AccountEntryProfileUtil(actionSession);
		
		customerManager 		= (CustomerManager) 	actionSession.get("customerManager");
		inventoryManager 		= (InventoryManager) 	actionSession.get("inventoryManager"); 
		accountEntryManager		= (AccountEntryManager) actionSession.get("accountEntryManager");
		transactionManager 		= (TransactionManager) 	actionSession.get("transactionManager");
		financialsManager 		= (FinancialsManager)	actionSession.get("financialsManager ");

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
		if(poDetailsHelperDraft==null) {
			poDetailsHelperDraft = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsHelperDraft.setActionSession(actionSession);
		}
		if(poDetailsGrouped==null) {
			poDetailsGrouped = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsGrouped.setActionSession(actionSession);
		}
		
	}
 	private String subModule;
	private String cusId;
	private String custpoid;
	private String drId;
	private String invId;
	private String forWhat;
	private String forWhatDisplay;
	
	Customer customer;
	CustomerPurchaseOrder custpo;
	DeliveryReceipt dr;
	CustomerSalesInvoice invoice;
	
	List customerNoList;
	List purchaseOrderNoList;
	List deliveryReceiptNoList;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
	//END 2013 - PHASE 3 : PROJECT 1: MARK  
		
	DateFormatHelper df = new DateFormatHelper();

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();

		try {
			boolean updateResult = false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
			
			if (getSubModule().equalsIgnoreCase("profile")) {

				customer.setCustomerNo(cusId);
				if (validateCustomer()) {
					
				}else {
					if(null==ActionContext.getContext().getSession().get(customer.getCustomerNo()+"stockLevel")) {
					}else {
						customer.setCustomerStockLevelMap((Map) ActionContext.getContext().getSession().get(customer.getCustomerNo()+"stockLevel"));
					}
					updateResult = customerManager.updateCustomer(customer,session);
					if (updateResult == true) {
						addActionMessage(SASConstants.UPDATED);
						forWhat = "true";
						forWhatDisplay = "edit";
					} else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}
				}
				return "profileUpdated";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				List cusPo = null;
				customerNoList = customerManager.listAlphabeticalAscByParameter(Customer.class, "customerNo", session);
				
				cusPo = customerManager.listByParameter(Customer.class, "customerNo",
						getCustpo().getCustomer().getCustomerNo(),session);
				if (cusPo.isEmpty()) {
					addActionMessage("Customer No: " + SASConstants.NON_EXISTS);
					custpo.setCustomerPurchaseOrderId(custpoid);
					includePoDetails();
					
				} else {
					custpo.setCustomerPurchaseOrderId(custpoid);
					custpo.setCustomer((Customer) cusPo.get(0));
					//get payment date based on terms
					DateFormatHelper dfh = new DateFormatHelper();
					custpo.setPaymentDate(dfh.getPaymentDateByTerm(custpo.getDateOfDelivery(), custpo.getPaymentTerm()));
					
					if(null==poDetailsHelper) {
					}else {
					poDetailsHelper.setOrderDate(custpo.getPurchaseOrderDate());
					custpo.setPurchaseOrderDetails(poDetailsHelper.persistNewSetElements(session));
					poDetailsHelper.generatePODetailsListFromSet(custpo.getPurchaseOrderDetails());
					}
					custpo.setTotalAmount(poDetailsHelper.getTotalAmount());
					custpo.setPaymentDate(dfh.getPaymentDateByTerm(custpo.getDateOfDelivery(), custpo.getPaymentTerm()));
					
					if (validateCustomerPO()) {
						includePoDetails();
					}else {
						if (custpo.getPurchaseOrderDetails().size()==0) {
							addActionError(SASConstants.EMPTY_ORDER_DETAILS);
						}else {
							updateResult = customerManager.updateCustomer(custpo,session);
							
							if (updateResult == true) {
								addActionMessage(SASConstants.UPDATED);
								forWhat = "true";
								forWhatDisplay = "edit";
							} else {
								addActionMessage(SASConstants.UPDATE_FAILED);
							}
							
						}
					}
				}
				return "poUpdated";
			} else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				List cusDr = null;
				purchaseOrderNoList = customerManager.listAlphabeticalAscByParameter(CustomerPurchaseOrder.class, "customerPurchaseOrderId", session);
				
				cusDr = customerManager.listByParameter(CustomerPurchaseOrder.class,
						"customerPurchaseOrderId", getDr()
								.getCustomerPurchaseOrder()
								.getCustomerPurchaseOrderId(),session);
				if (cusDr.isEmpty()) {
					addActionError("Purchase Order No.: "
							+ SASConstants.NON_EXISTS);
					dr.setDeliveryReceiptNo(drId);
					includePoDetails();
				} else {
					dr.setDeliveryReceiptNo(drId);
					dr.setCustomerPurchaseOrder((CustomerPurchaseOrder) cusDr
							.get(0));
					dr.setDueDate(dr.getCustomerPurchaseOrder().getPaymentDate());
					/*
					 * Checking and fetching existing return slips
					 */
					
					
					Session drSession = getSession();
					List returnSlipList = inventoryManager.listInventoryByParameter(ReturnSlip.class, "returnSlipReferenceOrderNo", dr.getDeliveryReceiptNo(), drSession);
					
					if(returnSlipList.size()>0) {
						dr.setReturnSlipList(returnSlipList);
					}else {
						dr.setReturnSlipList(null);
					}
					
					if(null==poDetailsHelperToCompare) {
						poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
					}
						poDetailsHelperToCompare.generatePODetailsListFromSet(dr.getCustomerPurchaseOrder().getPurchaseOrderDetails());
					
					boolean inventoryUpdateSuccess= false;
					
					if(null==poDetailsHelper) {
					}else {
						poDetailsHelper.setOrderDate(df.dynamicParseDateToTimestamp(dr.getDeliveryReceiptDate(),SASConstants.TIMESTAMP_FORMAT));
						Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
						dr.setPurchaseOrderDetails(podetailSet);
						poDetailsHelper.generatePODetailsListFromSet(dr.getPurchaseOrderDetails());
						//2014 - ITEM COLORING
	 					poDetailsHelper.generateItemTypesForExistingItems(session);
						
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
						DeliveryReceipt oldCustDr = (DeliveryReceipt) customerManager.listByParameter(
								DeliveryReceipt.class, "deliveryReceiptNo",drId,session).get(0);
						PurchaseOrderDetailHelper helperOld = new PurchaseOrderDetailHelper(actionSession);
						helperOld.generatePODetailsListFromSet(oldCustDr.getPurchaseOrderDetails());
						PurchaseOrderDetailHelper inventoryUpdateRequest = inventoryUtil.getChangeInOrder(helperOld, poDetailsHelper , SASConstants.ORDER_TYPE_DR);
						
						try {
							inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
							inventoryUpdateSuccess = true;
						} catch (Exception e) {
							e.printStackTrace();
							addActionError(e.getMessage());
							inventoryUpdateSuccess=false;
						}
						
					}
					dr.setTotalAmount(poDetailsHelper.getTotalAmount());
					if (validateCustomerDR()) {
						//includePoDetails();
					}else {
						if (dr.getPurchaseOrderDetails().size()==0) {
							addActionError(SASConstants.EMPTY_ORDER_DETAILS);
						}else {
							if(inventoryUpdateSuccess) {
								//START - 2013 - PHASE 3 : PROJECT 1: MARK
								transactionManager.discontinuePreviousTransactions(dr.getDeliveryReceiptNo(),session);
								transactionList = getTransactionList();
								if (transactionList.size()==0){
									addActionMessage("REQUIRED: Accounting Entries Details");
								}else{
									updateAccountingEntries(dr.getDeliveryReceiptNo(),session,SASConstants.DELIVERYREPORT);
									this.setTransactionList(transactions);
									dr.setTransactions(transactions);
									//END
								}
								updateResult = customerManager.updateCustomer(dr,session);
							}else {
								updateResult = false;
							}
							if (updateResult == true) {
								addActionMessage(SASConstants.UPDATED);
								forWhat = "true";
								forWhatDisplay = "edit";
							} else {
								addActionMessage(SASConstants.UPDATE_FAILED);
							}
						}
					}
				}
				return "deliveryReceiptUpdated";
			} else {
				List cusInv = null;
				deliveryReceiptNoList = customerManager.listAlphabeticalAscByParameter(DeliveryReceipt.class, "deliveryReceiptNo", session);
				
				cusInv = customerManager.listByParameter(DeliveryReceipt.class,
						"deliveryReceiptNo", getInvoice().getDeliveryReceipt()
								.getDeliveryReceiptNo(),session);
				if (cusInv.isEmpty()) {
					addActionError("Delivery Report No.: "
							+ SASConstants.NON_EXISTS);
					invoice.setCustomerInvoiceNo(invId);
				} else {
					invoice.setCustomerInvoiceNo(invId);
					invoice.setDeliveryReceipt((DeliveryReceipt) cusInv.get(0));
					
//					if(null==poDetailsHelperToCompare) {
//						poDetailsHelperToCompare = new PurchaseOrderDetailHelper();
//					}
//						poDetailsHelperToCompare.generatePODetailsListFromSet(invoice.getDeliveryReceipt().getPurchaseOrderDetails());
//					
					poDetailsHelper = new PurchaseOrderDetailHelper(actionSession);
					
					poDetailsHelper.generatePODetailsListFromSet(invoice.getDeliveryReceipt().getPurchaseOrderDetails());
					poDetailsHelper.generateCommaDelimitedValues();
					//2014 - ITEM COLORING
 					poDetailsHelper.generateItemTypesForExistingItems(session);
					
					poDetailsHelper.setOrderDate(invoice.getCustomerInvoiceDate());
					Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
					invoice.setPurchaseOrderDetails(podetailSet);
					invoice.setTotalSales(poDetailsHelper.getTotalAmount());
					
					//START - 2013 - PHASE 3 : PROJECT 1: MARK
					transactionManager.discontinuePreviousTransactions(invoice.getCustomerInvoiceNo(),session);
					transactionList = getTransactionList();
					if (transactionList.size()==0){
						addActionMessage("REQUIRED: Accounting Entries Details");
					}else{
						updateAccountingEntries(invoice.getCustomerInvoiceNo(),session,SASConstants.CUSTOMERINVOICE);
						this.setTransactionList(transactions);
						invoice.setTransactions(transactions);
					}
					//END
					
					//START: 2013 - PHASE 3 : PROJECT 4: MARK
					invoice.getVatDetails().setAddress(invoice.getDeliveryReceipt().getCustomerPurchaseOrder().getCustomer().getBillingAddress());
					invoice.getVatDetails().setVatReferenceNo(invId);
					invoice.getVatDetails().setAmount(invoice.getTotalSales());
					invoice.getVatDetails().setOrDate(invoice.getCustomerInvoiceDate());
					invoice.setVatDetails(invoice.getVatDetails());
					
					financialsManager.updateVatDetails(invoice.getVatDetails(), session);							
					//END: 2013 - PHASE 3 : PROJECT 4: MARK
					
						if (invoice.getVat()== 0) {
							invoice.setTotalAmount(poDetailsHelper.getTotalAmount());
						}
						
					if (validateCustomerInv()) {
						//includePoDetails();
					}else {
						if (invoice.getPurchaseOrderDetails().size()==0) {
							addActionError(SASConstants.EMPTY_ORDER_DETAILS);
						}else {
							updateResult = customerManager.updateCustomer(invoice,session);
							if (updateResult == true) {
								addActionMessage(SASConstants.UPDATED);
								forWhat = "true";
								forWhatDisplay = "edit";
							} else {
								addActionMessage(SASConstants.UPDATE_FAILED);
							}
							
						}
					}
				}
				return "invoiceUpdated";
			}
		} catch (RuntimeException re) {
			if (getSubModule().equalsIgnoreCase("profile")) {
				return "profileUpdated";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "poUpdated";
			}else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				return "deliveryReceiptUpdated";
			}else  {
				return "invoiceUpdated";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}
	private void includePoDetails() {
		if(poDetailsHelper!=null) {
			poDetailsHelper.prepareSetAndList();
		}if(poDetailsHelperToCompare!=null) {
			poDetailsHelperToCompare.prepareSetAndList();
		}
	}
	
	// 2013 - PHASE 3 : PROJECT 1: MARK

	/*
	 * Transactions: -retrieve first rules for each account -set each
	 * transaction object the available properties :
	 * transactionType,accountCode,amount,transactionDate,isInUse TODO: include
	 * createdBy TODO: include rules
	 */
	private void updateAccountingEntries(String referenceNo, Session session,
			String type) {

		// TODO Auto-generated method stub
		transactions = new ArrayList<Transaction>();
		if (transactionList != null) {
			Iterator itr = transactionList.iterator();
			while (itr.hasNext()) {
				Transaction transaction = (Transaction) itr.next();
				AccountEntryProfile accountEntry = transaction
						.getAccountEntry();
				accountEntry = accountEntryManager
						.loadAccountEntryProfile(accountEntry.getAccountCode());
				transaction.setAccountEntry(accountEntry);
				transaction.setTransactionReferenceNumber(referenceNo);
				transaction.setTransactionType(type);
				transaction.setTransactionAction(accountEntryUtil.getActionBasedOnType(
						accountEntry, type));
				transaction.setTransactionDate(df.getTimeStampToday());
				transaction.setIsInUse(SASConstants.TRANSACTION_IN_USE);
				transactions.add(transaction);
			}
			transactionManager.addTransactionsList(transactions, session);
		}
		// return transactions;
	}

	private String getActionBasedOnType(AccountEntryProfile accountEntry, String type) {

		String action = "";

		if (type.equalsIgnoreCase(SASConstants.RECEIVINGREPORT)) {
			action = accountEntry.getAccountingRules()
					.getSupplierReceivingReport();
		} else if (type.equalsIgnoreCase(SASConstants.SUPPLIERINVOICE)) {
			action = accountEntry.getAccountingRules().getSupplierInvoice();
		} else if (type.equalsIgnoreCase(SASConstants.DELIVERYREPORT)) {
			action = accountEntry.getAccountingRules()
					.getCustomerDeliveryReceipt();
		} else if (type.equalsIgnoreCase(SASConstants.CUSTOMERINVOICE)) {
			action = accountEntry.getAccountingRules().getCustomerInvoice();
		} else if (type.equalsIgnoreCase(SASConstants.PETTYCASH)) {
			action = accountEntry.getAccountingRules()
					.getDisbursementPettyCash();
		} else if (type.equalsIgnoreCase(SASConstants.CASHPAYMENT)) {
			action = accountEntry.getAccountingRules()
					.getDisbursementCashPayment();
		} else if (type.equalsIgnoreCase(SASConstants.CHECK_VOUCHER)) {
			action = accountEntry.getAccountingRules()
					.getDisbursementCheckVoucher();
		} else if (type
				.equalsIgnoreCase(SASConstants.INVENTORY_RETURN_SLIP_FORM)) {
			action = accountEntry.getAccountingRules().getInventoryRS();
		} else if (type
				.equalsIgnoreCase(SASConstants.INVENTORY_REQUISITION_FORM)) {
			action = accountEntry.getAccountingRules().getInventoryOR();
		} else if (type.equalsIgnoreCase(SASConstants.INVENTORY_FPTS)) {
			action = accountEntry.getAccountingRules().getInventoryFPTS();
		} else if (type.equalsIgnoreCase(SASConstants.ORSALES)) {
			action = accountEntry.getAccountingRules().getReceiptsOrSales();
		} else if (type.equalsIgnoreCase(SASConstants.OROTHERS)) {
			action = accountEntry.getAccountingRules().getReceiptsOrOther();
		} else if (type.equalsIgnoreCase(SASConstants.CASHCHECKRECEIPTS)) {
			action = accountEntry.getAccountingRules().getReceiptsCheck();
		}
		return action;
	}

	// END - 2013 - PHASE 3 : PROJECT 1: MARK

	private boolean validateCustomer() {
		boolean errorFound = false;

		if ("".equals(customer.getCustomerNo())) {
			addFieldError("customer.customerNo", "REQUIRED");
			errorFound = true;
		}
		if ("".equals(customer.getCustomerName())) {
			addFieldError("customer.customerName", "REQUIRED");
			errorFound = true;		
		} else if (customer.getCustomerName().trim().length()>100) {
			 addFieldError("customer.customerName", "MAXIMUM LENGTH: 100 characters");
			 errorFound = true;
		 }

		
		 if ("".equals(customer.getPhoneNumber())) {
			 addFieldError("customer.phoneNumber", "REQUIRED");
			 errorFound = true;
			 }
			 else {
				 if (!(customer.getPhoneNumber().contains("-"))) {
				 addFieldError("customer.phoneNumber", "AREACODE-#######");
				 errorFound = true;
				 	}
			 }	
		 if (!("".equals(customer.getFaxNumber()))) {
			 if (!(customer.getPhoneNumber().contains("-"))) {
			 addFieldError("customer.faxNumber", "AREACODE-#######");
			 errorFound = true;
			 	}
		 	}

		
		if ("".equals(customer.getBillingAddress())) {
			 addFieldError("customer.billingAddress", "REQUIRED");
			 errorFound = true;
		 }else {
			 if (customer.getBillingAddress().trim().length()>200) {
				 addFieldError("customer.billingAddress",
				 "MAXIMUM LENGTH: 200 characters");
				 errorFound = true;
			 }
		 }
		return errorFound;

	}

	private boolean validateCustomerPO() {
		boolean errorFound = false;

		if ("".equals(custpo.getCustomerPurchaseOrderId())) {
			addFieldError("custpo.customerPurchaseOrderId", "REQUIRED");
			errorFound = true;
		}
		if (null == (custpo.getPurchaseOrderDate())) {
			addActionMessage("REQUIRED: Purchase Order Date");
			errorFound = true;
		}

		if ("".equals(custpo.getCustomer().getCustomerNo())) {
			addActionMessage("REQUIRED: Customer ID");
			errorFound = true;
		}

		return errorFound;

	}

	private boolean validateCustomerDR() {
		boolean errorFound = false;

		if ("".equals(dr.getDeliveryReceiptNo())) {
			addFieldError("dr.deliveryReceiptNo", "REQUIRED");
			errorFound = true;
		}
		if (null == (dr.getDeliveryReceiptDate())) {
			addActionMessage("REQUIRED: Delivery Receipt Date");
			errorFound = true;
		}
		if ("".equals(dr.getCustomerPurchaseOrder()
				.getCustomerPurchaseOrderId())) {
			addActionMessage("REQUIRED: Purchase Order No");
			errorFound = true;
		}
		/*if ((getTransactionList().get(0).getAmount() == 0 )) {
			addActionMessage("REQUIRED: Accounting Entries Details");
			errorFound = true;
		}*/
		return errorFound;

	}

	private boolean validateCustomerInv() {
		boolean errorFound = false;
		if ("".equals(invoice.getCustomerInvoiceNo())) {
			addFieldError("invoice.customerInvoiceNo", "REQUIRED");
			errorFound = true;
		}
		if (null == (invoice.getCustomerInvoiceDate())) {
			addActionMessage("REQUIRED: Sales Invoice Date");
			errorFound = true;
		}
		if ("".equals(invoice.getDeliveryReceipt().getDeliveryReceiptNo())) {
			addActionMessage("REQUIRED: Delivery Receipt No.");
			errorFound = true;
		}
		if ("".equals(invoice.getSoldTo())) {
		addFieldError("invoice.soldTo", "REQUIRED");
		errorFound = true;
		}
		else if (invoice.getSoldTo().trim().length()>100) {
			 addFieldError("invoice.soldTo", SASConstants.MAXIMUM_LENGTH_100);
			 errorFound = true;
		}
		if ("".equals(invoice.getAddress())) {
			addFieldError("invoice.address", "REQUIRED");
			errorFound = true;
		}else if (invoice.getAddress().trim().length()>200) {
			 addFieldError("invoice.address", SASConstants.MAXIMUM_LENGTH_200);
			 errorFound = true;
		}
		/*if ((getTransactionList().get(0).getAmount() == 0 )) {
			addActionMessage("REQUIRED: Accounting Entries Details");
			errorFound = true;
		}*/
		return errorFound;

	}
	
	/*
	 * GETTERS AND SETTERS
	 * */
	
	
	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getCustpoid() {
		return custpoid;
	}

	public void setCustpoid(String poId) {
		this.custpoid = poId;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getDrId() {
		return drId;
	}

	public void setDrId(String drId) {
		this.drId = drId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerPurchaseOrder getCustpo() {
		return custpo;
	}

	public void setCustpo(CustomerPurchaseOrder po) {
		this.custpo = po;
	}

	public DeliveryReceipt getDr() {
		return dr;
	}

	public void setDr(DeliveryReceipt dr) {
		this.dr = dr;
	}

	public CustomerSalesInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(CustomerSalesInvoice invoice) {
		this.invoice = invoice;
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

	public void setPoDetailsHelperToCompare(
			PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
	}
	public List getCustomerNoList() {
		return customerNoList;
	}
	public void setCustomerNoList(List customerNoList) {
		this.customerNoList = customerNoList;
	}
	public List getPurchaseOrderNoList() {
		return purchaseOrderNoList;
	}
	public void setPurchaseOrderNoList(List purchaseOrderNoList) {
		this.purchaseOrderNoList = purchaseOrderNoList;
	}
	public List getDeliveryReceiptNoList() {
		return deliveryReceiptNoList;
	}
	public void setDeliveryReceiptNoList(List deliveryReceiptNoList) {
		this.deliveryReceiptNoList = deliveryReceiptNoList;
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
