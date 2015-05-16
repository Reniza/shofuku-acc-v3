package com.shofuku.accsystem.action.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class AddCustomerAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String subModule;
	
	Customer customer;
	CustomerPurchaseOrder custpo;
	DeliveryReceipt dr;
	CustomerSalesInvoice invoice;
	private String forWhat;
	private String forWhatDisplay;
	

	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
	RecordCountHelper rch = new RecordCountHelper(actionSession);
	DateFormatHelper dfh = new DateFormatHelper();

	List customerNoList;
	List purchaseOrderNoList;
	List deliveryReceiptNoList;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
	List accountProfileCodeList;
	List<Transaction> transactionList;
	List<Transaction> transactions;
	//END 2013 - PHASE 3 : PROJECT 1: MARK  

	AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
	TransactionManager transactionManager = (TransactionManager) actionSession.get("transactionManager");
	InventoryManager inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
	CustomerManager customerManager = (CustomerManager) actionSession.get("customerManager");
	FinancialsManager financialsManager = (FinancialsManager) actionSession.get("financialsManager");
	
	InventoryUtil invUtil = new InventoryUtil(actionSession);

	public String newCustomerEntry(){
		Session session = getSession();
		customerManager.setUser(user);
		
		try {
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);			
			
		if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
			customerNoList = customerManager.listAllCustomerNo(session);
			/*custpo=new CustomerPurchaseOrder();
			custpo.setCustomerPurchaseOrderId(rch.getPrefix(SASConstants.CUSTOMERPO,SASConstants.CUSTOMERPO_PREFIX)); */
			return "purchaseOrder";
		} else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
			purchaseOrderNoList = customerManager.listAllCustomerPurchaseOrderNo(session);
			/*dr=new DeliveryReceipt();
			dr.setDeliveryReceiptNo(rch.getPrefix(SASConstants.DELIVERYREPORT, SASConstants.DELIVERYREPORT_PREFIX)); */
			return "deliveryReceipt";
		} else {
			deliveryReceiptNoList = customerManager.listAllCustomerDeliveryReceiptNo(session);
			/*invoice=new CustomerSalesInvoice();
			invoice.setCustomerInvoiceNo(rch.getPrefix(SASConstants.CUSTOMERINVOICE, SASConstants.CUSTOMERINVOICE_PREFIX)); */
			return "invoice";
		}
		}catch (Exception e) {
			 if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrder";
			}else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				return "deliveryReceipt";
			}else {
				return "invoice";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		financialsManager.setUser(user);
		accountEntryManager.setUser(user);
		customerManager.setUser(user);
		inventoryManager.setUser(user);
		
		try {
			boolean addResult = false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);			
			
			if (getSubModule().equalsIgnoreCase("profile")) {
				
				if (validateCustomer()) {
					
				} else {
					List cusList = null;
					cusList = customerManager.listByParameter(Customer.class,
							"customerNo", getCustomer().getCustomerNo(),session);
					if (!(cusList.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						if (validateCustomer()) {
						}else {
						char firstLetter = customer.getCustomerName().charAt(0);
						customer.setCustomerNo(rch.getLastCustomerByInitialLetter(firstLetter));
						addResult = customerManager.addCustomerObject(customer,session);
						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat="true";
							forWhatDisplay ="edit";
						} else {
							addActionError(SASConstants.FAILED);
							}
						}
					}		
				}
				return "profileAdded";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				customerNoList = customerManager.listAlphabeticalAscByParameter(Customer.class, "customerNo", session);
				if (validateCustomerPO()) {
					includePoDetails();
				} else {
					List cusPo = null;
					cusPo = customerManager.listByParameter(
							CustomerPurchaseOrder.class,
							"customerPurchaseOrderId", getCustpo()
									.getCustomerPurchaseOrderId(),session);
					if (!(cusPo.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						List cusPo2 = null;
						cusPo2 = customerManager.listByParameter(Customer.class,
								"customerNo", this.getCustpo().getCustomer()
										.getCustomerNo(),session);
						includePoDetails();
						if (cusPo2.isEmpty()) {
							addActionError("CUSTOMER NO: "
									+ SASConstants.NON_EXISTS);
						} else {
							poDetailsHelper.setOrderDate(custpo.getPurchaseOrderDate());
							Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
							custpo.setCustomer((Customer) cusPo2.get(0));
							//get payment date based on terms
							DateFormatHelper dfh = new DateFormatHelper();
							custpo.setPaymentDate(dfh.getPaymentDateByTerm(custpo.getDateOfDelivery(), custpo.getPaymentTerm()));
							custpo.setTotalAmount(poDetailsHelper.getTotalAmount());
							
							custpo.setPurchaseOrderDetails(podetailSet);
							poDetailsHelper.generatePODetailsListFromSet(custpo.getPurchaseOrderDetails());
							custpo.setTotalAmount(poDetailsHelper.getTotalAmount());
							if (validateCustomerPO()) {
								
							}else {
								if (custpo.getPurchaseOrderDetails().size()==0) {
									addActionError(SASConstants.EMPTY_ORDER_DETAILS);
								}else {
									custpo.setCustomerPurchaseOrderId(rch.getPrefix(SASConstants.CUSTOMERPO,SASConstants.CUSTOMERPO_PREFIX)); 
									List customerPO = null;
									customerPO = customerManager.listByParameter(CustomerPurchaseOrder.class,
											"customerPurchaseOrderId", custpo.getCustomerPurchaseOrderId(),session);
									if (customerPO.size()==0){
										addResult = customerManager.addCustomerObject(custpo,session);
									}else{
										addActionError("CUSTOMER PO NO.: "
												+ SASConstants.EXISTS);
									}
								if (addResult == true) {
										rch.updateCount(SASConstants.CUSTOMERPO, "add");
										addActionMessage(SASConstants.ADD_SUCCESS);
										forWhat="true";
										forWhatDisplay ="edit";
								} else {
										addActionError(SASConstants.FAILED);
										}
								}
							}
						}
					}
				}
				return "purchaseOrderAdded";

			} else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				purchaseOrderNoList = customerManager.listAlphabeticalAscByParameter(CustomerPurchaseOrder.class, "customerPurchaseOrderId", session);
				
				if (validateCustomerDR()) {
					includePoDetails();
				} else {
					List cusDr = null;
					cusDr = customerManager
							.listByParameter(DeliveryReceipt.class,
									"deliveryReceiptNo", getDr()
											.getDeliveryReceiptNo(),session);
					if (!(cusDr.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						List cusDr2 = null;
						cusDr2 = customerManager.listByParameter(CustomerPurchaseOrder.class,"customerPurchaseOrderId", this.getDr().getCustomerPurchaseOrder().getCustomerPurchaseOrderId(),session);
						if (cusDr2.isEmpty()) {
							addActionError("PURCHASE ORDER NO: "+ SASConstants.NON_EXISTS);
							includePoDetails();
						} else {
							dr.setCustomerPurchaseOrder((CustomerPurchaseOrder) cusDr2.get(0));
							dr.setDueDate(dr.getCustomerPurchaseOrder().getPaymentDate());

							if(null==poDetailsHelperToCompare) {
								poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
							}
							poDetailsHelperToCompare.generatePODetailsListFromSet(dr.getCustomerPurchaseOrder().getPurchaseOrderDetails());
							poDetailsHelperToCompare.generateCommaDelimitedValues();
							if(null==poDetailsHelper) {
							}else {
								poDetailsHelper.generatePODetailsListFromSet(dr.getCustomerPurchaseOrder().getPurchaseOrderDetails());
								poDetailsHelper.generateCommaDelimitedValues();
								//2014 - ITEM COLORING
			 					poDetailsHelper.generateItemTypesForExistingItems(session);

								
								poDetailsHelper.setOrderDate(dfh.dynamicParseDateToTimestamp(dr.getDeliveryReceiptDate(), SASConstants.TIMESTAMP_FORMAT));
								Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
								
								dr.setPurchaseOrderDetails(podetailSet);
								dr.setTotalAmount(poDetailsHelper.getTotalAmount());
							}
							boolean inventoryUpdateSuccess;
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
							PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.getChangeInOrder(new PurchaseOrderDetailHelper(actionSession), poDetailsHelper , SASConstants.ORDER_TYPE_DR);
							
						
							try {
								inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
								inventoryUpdateSuccess = true;
							} catch (Exception e) {
								e.printStackTrace();
								addActionError(e.getMessage());
								inventoryUpdateSuccess=false;
							}
							//START - 2013 - PHASE 3 : PROJECT 1: MARK
							transactionList = new ArrayList();
							Transaction transaction = new Transaction();
							transactionList.add(transaction);
							//END - 2013 - PHASE 3 : PROJECT 1: MARK
							dr.setDeliveryReceiptNo(rch.getPrefix(SASConstants.DELIVERYREPORT, SASConstants.DELIVERYREPORT_PREFIX));
							
							if(inventoryUpdateSuccess) {
								addResult = customerManager.addCustomerObject(dr,session);
							}else {
								addResult=false;
							}
							if (addResult == true) {
								addActionMessage(SASConstants.ADD_SUCCESS);
								rch.updateCount(SASConstants.DELIVERYREPORT, "add");
								forWhat="true";
								forWhatDisplay ="edit";
							} else {
								addActionError(SASConstants.FAILED);
							}
						}
					}
				}
				return "deliveryReceiptAdded";
			} else {
				deliveryReceiptNoList = customerManager.listAlphabeticalAscByParameter(DeliveryReceipt.class, "deliveryReceiptNo", session);
				
				if (validateCustomerInv()) {
					includePoDetails();
				} else {
					
					//checking if invoice no already exist
					List cusInv = new ArrayList();
					cusInv = customerManager.listByParameter(
							CustomerSalesInvoice.class, "customerInvoiceNo",
							this.getInvoice().getCustomerInvoiceNo(),session);
					
					
					if (!(cusInv.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						//query for checking if DR exists
						List cusInv2 = new ArrayList();
						cusInv2 = customerManager.listByParameter(
								DeliveryReceipt.class, "deliveryReceiptNo",
								this.getInvoice().getDeliveryReceipt()
										.getDeliveryReceiptNo(),session);
						
						//query for checking if Invoice with DR no already exists
						List cusInvWithExistingDRList = new ArrayList();
						cusInvWithExistingDRList = customerManager.listByParameter(
								CustomerSalesInvoice.class, "deliveryReceipt.deliveryReceiptNo",
								this.getInvoice().getDeliveryReceipt().getDeliveryReceiptNo(),session);
						//condition for existing/not existing DR
						if (cusInv2.isEmpty()) {
							addActionError("DELIVERY RECEIPT NO.: "
									+ SASConstants.NON_EXISTS);
							includePoDetails();
						
						}else if (!(cusInvWithExistingDRList.isEmpty())) {
							addActionError("DELIVERY RECEIPT NO.: "
									+ SASConstants.TAKEN);
							includePoDetails();
							
						}else {
							invoice.setDeliveryReceipt((DeliveryReceipt) cusInv2.get(0));
							
							if(null==poDetailsHelperToCompare) {
								poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
							}
							poDetailsHelperToCompare.generatePODetailsListFromSet(invoice.getDeliveryReceipt().getPurchaseOrderDetails());
							poDetailsHelperToCompare.generateCommaDelimitedValues();
							
							if(null==poDetailsHelper) {
							}else {
								poDetailsHelper.generatePODetailsListFromSet(invoice.getDeliveryReceipt().getPurchaseOrderDetails());
								poDetailsHelper.generateCommaDelimitedValues();
								//2014 - ITEM COLORING
			 					poDetailsHelper.generateItemTypesForExistingItems(session);

								poDetailsHelper.setOrderDate(invoice.getCustomerInvoiceDate());
								Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
								invoice.setTotalAmount(poDetailsHelper.getTotalAmount());
								invoice.setPurchaseOrderDetails(podetailSet);
							}
							invoice.setSoldTo(invoice.getDeliveryReceipt().getCustomerPurchaseOrder().getCustomer().getCustomerName());
							invoice.setAddress(invoice.getDeliveryReceipt().getCustomerPurchaseOrder().getCustomer().getBillingAddress());
							invoice.setTotalSales(poDetailsHelper.getTotalAmount());
							
							//START - 2013 - PHASE 3 : PROJECT 1: MARK
							transactionList = new ArrayList();
							Transaction transaction = new Transaction();
							transactionList.add(transaction);
							//END - 2013 - PHASE 3 : PROJECT 1: MARK
							
							invoice.setCustomerInvoiceNo(rch.getPrefix(SASConstants.CUSTOMERINVOICE, SASConstants.CUSTOMERINVOICE_PREFIX));
							
							//START: 2013 - PHASE 3 : PROJECT 4: MARK
							Vat vatDetails = new Vat();
							vatDetails.setAddress(invoice.getDeliveryReceipt().getCustomerPurchaseOrder().getCustomer().getBillingAddress());
							//TEST ONLY WHILE WAITING FOR TIN FOR SUPPLIER
							vatDetails.setTinNumber("000-000-000-001");
							vatDetails.setVatAmount(poDetailsHelper.getTotalVatAmount());
							vatDetails.setVattableAmount(poDetailsHelper.getTotalVattableAmount());
							vatDetails.setVatReferenceNo(invoice.getCustomerInvoiceNo());
							vatDetails.setOrNo(invoice.getVatDetails().getOrNo());
							vatDetails.setOrDate(invoice.getCustomerInvoiceDate());
							invoice.setVatDetails(vatDetails);
							
							financialsManager.insertVatDetails(vatDetails, session);							
							
							//END: 2013 - PHASE 3 : PROJECT 4: MARK
							addResult = customerManager.addCustomerObject(invoice,session);
							if (addResult == true) {
								addActionMessage(SASConstants.ADD_SUCCESS);
								rch.updateCount(SASConstants.CUSTOMERINVOICE, "add");
								forWhat="true";
								forWhatDisplay ="edit";
							} else {
								addActionError(SASConstants.FAILED);
							}
						}
					}
				}
				return "invoiceAdded";
			}
		} catch (RuntimeException re) {
			if (getSubModule().equalsIgnoreCase("profile")) {
				return "profileAdded";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrderAdded";
			}else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				return "deliveryReceiptAdded";
			}else {
				return "invoiceAdded";
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

	private boolean validateCustomer() {
		boolean errorFound = false;

//		if ("".equals(customer.getCustomerNo())) {
//			addFieldError("customer.customerNo", "REQUIRED");
//			errorFound = true;
//		}
		if ("".equals(customer.getCustomerName())) {
			addFieldError("customer.customerName", "REQUIRED");
			errorFound = true;		
		} else if (customer.getCustomerName().trim().length()>100) {
			 addFieldError("customer.customerName", "MAXIMUM LENGTH: 100 characters");
			 errorFound = true;
		 }
//
//		if ("".equals(customer.getContactName())) {
//			addFieldError("customer.contactName", "REQUIRED");
//			errorFound = true;
//		}
//		if ("".equals(customer.getContactTitle())) {
//			addFieldError("customer.contactTitle", "REQUIRED");
//			errorFound = true;
//		}
		
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
//		}
//		if ("".equals(customer.getMobileNumber())) {
//			addFieldError("customer.mobileNumber", "REQUIRED");
//			errorFound = true;
//		}
//		if ("".equals(customer.getEmailAddress())) {
//			addFieldError("customer.emailAddress", "REQUIRED");
//			errorFound = true;
//		}
//		if ("".equals(customer.getWebsite())) {
//			addFieldError("customer.website", "REQUIRED");
//			errorFound = true;
//		}
		
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

		/*if ("".equals(custpo.getCustomerPurchaseOrderId())) {
			addFieldError("custpo.customerPurchaseOrderId", "REQUIRED");
			errorFound = true;
		} */
		if (null == (custpo.getPurchaseOrderDate())) {
			addActionMessage("REQUIRED: Purchase Order Date");
			errorFound = true;
		}
//		if ("".equals(custpo.getPaymentTerm())) {
//			addFieldError("po.paymentTerm", "REQUIRED");
//			errorFound = true;
//		}
		if ("".equals(custpo.getCustomer().getCustomerNo())) {
			addActionMessage("REQUIRED: Customer ID");
			errorFound = true;
		}
//		if (null == (custpo.getPaymentDate())) {
//			addActionMessage("REQUIRED: Payment Date");
//			errorFound = true;
//		}
//		if (null == (custpo.getDateOfDelivery())) {
//			addActionMessage("REQUIRED: Delivery Date");
//			errorFound = true;
//		}
		return errorFound;

	}

	private boolean validateCustomerDR() {
		boolean errorFound = false;

	/*	if ("".equals(dr.getDeliveryReceiptNo())) {
			addFieldError("dr.deliveryReceiptNo", "REQUIRED");
			errorFound = true;
		} */
		if (null == (dr.getDeliveryReceiptDate())) {
			addActionMessage("REQUIRED: Delivery Receipt Date");
			errorFound = true;
		}
		if ("".equals(dr.getCustomerPurchaseOrder()
				.getCustomerPurchaseOrderId())) {
			addActionMessage("REQUIRED: Purchase Order No");
			errorFound = true;
		}
//		if ("".equals(dr.getShippingMethod())) {
//			addFieldError("dr.shippingMethod", "REQUIRED");
//			errorFound = true;
//		}
//		if (null == (dr.getShippingDate())) {
//			addActionMessage("REQUIRED: Shipping Date");
//			errorFound = true;
//		}
//		if (null == (dr.getDueDate())) {
//			addActionMessage("REQUIRED: Due Date");
//			errorFound = true;
//		}
//		if ("".equals(dr.getRemarks())) {
//			addFieldError("dr.remarks", "REQUIRED");
//			errorFound = true;
//		}
		return errorFound;

	}

	private boolean validateCustomerInv() {
		boolean errorFound = false;
	/*	if ("".equals(invoice.getCustomerInvoiceNo())) {
			addFieldError("invoice.customerInvoiceNo", "REQUIRED");
			errorFound = true;
		} */
		if (null == (invoice.getCustomerInvoiceDate())) {
			addActionMessage("REQUIRED: Sales Invoice Date");
			errorFound = true;
		}
		if ("".equals(invoice.getDeliveryReceipt().getDeliveryReceiptNo())) {
			addActionMessage("REQUIRED: Delivery Receipt No.");
			errorFound = true;
		}
//		if ("".equals(invoice.getSoldTo())) {
//			addFieldError("invoice.soldTo", "REQUIRED");
//			errorFound = true;
//		}
//		if ("".equals(invoice.getAddress())) {
//			addFieldError("invoice.address", "REQUIRED");
//			errorFound = true;
//		}
//		if ("".equals(invoice.getBusStyle())) {
//			addFieldError("invoice.busStyle", "REQUIRED");
//			errorFound = true;
//		}
//		if ("".equals(invoice.getTin())) {
//			addFieldError("invoice.tin", "REQUIRED");
//			errorFound = true;
//		}
		return errorFound;

	}
	

/*
 * GETTERS AND SETTERS
 */

	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public CustomerPurchaseOrder getCustpo() {
		return custpo;
	}

	public void setCustpo(CustomerPurchaseOrder po) {
		this.custpo = po;
	}

	public CustomerSalesInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(CustomerSalesInvoice invoice) {
		this.invoice = invoice;
	}

	public DeliveryReceipt getDr() {
		return dr;
	}

	public void setDr(DeliveryReceipt dr) {
		this.dr = dr;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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
