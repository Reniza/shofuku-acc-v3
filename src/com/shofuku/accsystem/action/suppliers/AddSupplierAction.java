package com.shofuku.accsystem.action.suppliers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class AddSupplierAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger
			.getLogger(AddSupplierAction.class);

	private static final Logger logger2 = logger.getRootLogger();
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");

	SupplierManager supplierManager = (SupplierManager) actionSession.get("supplierManager");
	AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
	TransactionManager transactionManager = (TransactionManager) actionSession.get("transactionManager");
	InventoryManager inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
	FinancialsManager financialsManager = (FinancialsManager) actionSession.get("financialsManager");
	
	
	RecordCountHelper rch = new RecordCountHelper(actionSession);

	private String forWhat;
	private String forWhatDisplay;
	private String subModule;
	

	// supplier beans
	private Supplier supplier;
	private SupplierPurchaseOrder po;
	private ReceivingReport rr;
	private SupplierInvoice invoice;
	private PurchaseOrderDetails orderDetails;
	
	List purchaseOrderNoList;
	List supplierNoList;
	List receivingReportNoList;
	//START 2013 - PHASE 3 : PROJECT 1: MARK
	List accountProfileCodeList;
	List<Transaction> transactionList;
	List<Transaction> transactions;
	//END 2013 - PHASE 3 : PROJECT 1: MARK  

	InventoryUtil invUtil = new InventoryUtil(actionSession);
	DateFormatHelper df = new DateFormatHelper();

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String newSupplierEntry() {
		Session session = getSession();
		try {
		if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
			supplierNoList = supplierManager.listAlphabeticalAscByParameter(Supplier.class, "supplierId", session);
			/*	po = new SupplierPurchaseOrder();
			po.setSupplierPurchaseOrderId(rch.getPrefix(
					SASConstants.SUPPLIERPO, SASConstants.SUPPLIERPO_PREFIX));*/
			return "purchaseOrder";
		} else if (getSubModule().equalsIgnoreCase("receivingReport")){
			purchaseOrderNoList = supplierManager.listAlphabeticalAscByParameter(SupplierPurchaseOrder.class, "supplierPurchaseOrderId", session);
			/*rr = new ReceivingReport();
			rr.setReceivingReportNo(rch.getPrefix(SASConstants.RECEIVINGREPORT,
					SASConstants.RECEIVINGREPORT_PREFIX));*/
			return "receivingReport";
		}else {
			receivingReportNoList = supplierManager.listAlphabeticalAscByParameter(ReceivingReport.class, "receivingReportNo", session);
			return "invoice";
			}
		}catch (Exception e) {
			e.printStackTrace();
			if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrder";
			}else if (getSubModule().equalsIgnoreCase("receivingReport")) {
				return "receivingReport";
			}else {
				return "invoice";
			}
		}finally {
			if(session.isOpen()){
			}
			session.close();
			session.getSessionFactory().close();
		}
	}

	
	public String execute() throws Exception {
		Session session = getSession();
		//TODO: try login here
		/*
		Map actionSession = ActionContext.getContext().getSession();
		Object data = (Object) actionSession.get("user");
		*/
		
		try {
			boolean addResult = false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);		

			if (getSubModule().equalsIgnoreCase("supplierProfile")) {
				if (validateSupplierProfile()) {
				} else {
					List supList = null;
					supList = supplierManager.listSuppliersByParameter(Supplier.class,
							"supplierId", getSupplier().getSupplierId(),session);
					if (!(supList.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						char firstLetter = supplier.getSupplierName().charAt(0);						
						supplier.setSupplierId(rch.getLastSupplierByInitialLetter(firstLetter));
						addResult = supplierManager.addSupplierObject(supplier,session);
						if (addResult == true) {
							rch.updateCount(SASConstants.SUPPLIER, "add");
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay="edit";
						} else {
							addActionError(SASConstants.FAILED);
						}
					}
				}
				return "profileAdded";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				supplierNoList = supplierManager.listAlphabeticalAscByParameter(Supplier.class, "supplierId", session);
				if (validatePurchaseOrder()) {
					inlcudePoDetails();
				} else {
					List supPo = null;
					supPo = supplierManager.listSuppliersByParameter(
							SupplierPurchaseOrder.class,
							"supplierPurchaseOrderId", getPo()
									.getSupplierPurchaseOrderId(),session);
					if (!(supPo.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						List supPo2 = null;
						supPo2 = supplierManager.listSuppliersByParameter(
								Supplier.class, "supplierId", getPo()
										.getSupplier().getSupplierId(),session);
						if (supPo2.isEmpty()) {
							addActionMessage("Supplier ID: "
									+ SASConstants.NON_EXISTS);
							inlcudePoDetails();
						} else {
							poDetailsHelper.setOrderDate(po.getPurchaseOrderDate());
							Set<PurchaseOrderDetails> podetailSet = poDetailsHelper
									.persistNewSetElements(session);
							po.setSupplier((Supplier) supPo2.get(0));
							po.setPurchaseOrderDetails(podetailSet);
							poDetailsHelper.generatePODetailsListFromSet(po
									.getPurchaseOrderDetails());
							// get payment date based on terms
							DateFormatHelper dfh = new DateFormatHelper();
							po.setPaymentDate(dfh.getPaymentDateByTerm(po
									.getDateOfDelivery(), po.getSupplier()
									.getPaymentTerm()));
							po.setTotalAmount(poDetailsHelper.getTotalAmount());
							po.setSupplierPurchaseOrderId(rch.getPrefix(
									SASConstants.SUPPLIERPO, SASConstants.SUPPLIERPO_PREFIX));
							int recordCount = Integer.parseInt(po.getSupplierPurchaseOrderId().substring(po.getSupplierPurchaseOrderId().length()-4, po.getSupplierPurchaseOrderId().length()));
							boolean unique=false;
								
									try {
										addResult = supplierManager.addSupplierObject(po,session);
									}catch (ConstraintViolationException cex) {
										
										SupplierPurchaseOrder tempSpo = new SupplierPurchaseOrder();
										tempSpo.setSupplierPurchaseOrderId(rch.getPrefix(
												SASConstants.SUPPLIERPO, SASConstants.SUPPLIERPO_PREFIX));
										tempSpo.setPurchaseOrderDate(po.getPurchaseOrderDate());
										tempSpo.setSupplier((Supplier) supPo2.get(0));
										tempSpo.setPurchaseOrderDetails(podetailSet);
										// get payment date based on terms
										tempSpo.setPaymentDate(dfh.getPaymentDateByTerm(po
												.getDateOfDelivery(), po.getSupplier()
												.getPaymentTerm()));
										tempSpo.setTotalAmount(poDetailsHelper.getTotalAmount());
										session.evict(po);
										addResult = supplierManager.addSupplierObject(tempSpo,session);
									}
								if (addResult == true) {
									//po = new SupplierPurchaseOrder();
									rch.updateCount(SASConstants.SUPPLIERPO, "add");
									addActionMessage(SASConstants.ADD_SUCCESS);
									forWhat = "true";
									forWhatDisplay="edit";
								} else {
									addActionError(SASConstants.FAILED);
								}
						}
					}
				}
				return "purchaseOrderAdded";
			} else if (getSubModule().equalsIgnoreCase("receivingReport")) {
				return addReceivingReport(session);
			} else {
				receivingReportNoList = supplierManager.listAlphabeticalAscByParameter(ReceivingReport.class, "receivingReportNo", session);
				if (validateInvoice()) {
					inlcudePoDetails();
				} else {
					List supInv = null;
					supInv = supplierManager.listSuppliersByParameter(
							SupplierInvoice.class, "supplierInvoiceNo",
							getInvoice().getSupplierInvoiceNo(),session);
					if (!(supInv.isEmpty())) {
						addActionError(SASConstants.EXISTS);
					} else {
						List supInv2 = new ArrayList();
						supInv2 = supplierManager.listSuppliersByParameter(
								ReceivingReport.class, "receivingReportNo",
								getInvoice().getReceivingReport()
										.getReceivingReportNo(),session);
						if (supInv2.isEmpty()) {
							addActionError("Receiving Report No.: "
									+ SASConstants.NON_EXISTS);
							inlcudePoDetails();
						} else {
							invoice.setReceivingReport((ReceivingReport) supInv2
									.get(0));
							if (null == poDetailsHelperToCompare) {
								poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
							}
							poDetailsHelperToCompare
									.generatePODetailsListFromSet(invoice
											.getReceivingReport()
											.getPurchaseOrderDetails());
							poDetailsHelperToCompare
									.generateCommaDelimitedValues();

							if (null == poDetailsHelper) {
							} else {
								poDetailsHelper
										.generatePODetailsListFromSet(invoice
												.getReceivingReport()
												.getPurchaseOrderDetails());
								poDetailsHelper.generateCommaDelimitedValues();
								poDetailsHelper.setOrderDate(invoice.getSupplierInvoiceDate());
								Set<PurchaseOrderDetails> podetailSet = poDetailsHelper
										.persistNewSetElements(session);
								invoice.setPurchaseOrderDetails(podetailSet);
								//2014 - ITEM COLORING
			 					poDetailsHelper.generateItemTypesForExistingItems(session);

							}
							//START - 2013 - PHASE 3 : PROJECT 1: MARK
							transactionList = new ArrayList();
							Transaction transaction = new Transaction();
							transactionList.add(transaction);
							//END - 2013 - PHASE 3 : PROJECT 1: MARK
							
							
							//START: 2013 - PHASE 3 : PROJECT 4: MARK
							Vat vatDetails = new Vat();
							vatDetails.setAddress(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getCompanyAddress());
							//TEST ONLY WHILE WAITING FOR TIN FOR SUPPLIER
							vatDetails.setPayee(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getSupplierName());
							vatDetails.setTinNumber(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getTin());
							vatDetails.setPayee(invoice.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getSupplierName());
							vatDetails.setVatAmount(poDetailsHelper.getTotalVatAmount());
							vatDetails.setVattableAmount(poDetailsHelper.getTotalVattableAmount());
							vatDetails.setVatReferenceNo(invoice.getSupplierInvoiceNo());
							vatDetails.setOrNo(invoice.getVatDetails().getOrNo());
							vatDetails.setOrDate(invoice.getSupplierInvoiceDate());
							invoice.setVatDetails(vatDetails);

							financialsManager.insertVatDetails(vatDetails, session);							
							//END: 2013 - PHASE 3 : PROJECT 4: MARK
							invoice.setDebit1Amount(poDetailsHelper
									.getTotalAmount());
							invoice.setPurchaseOrderDetailsTotalAmount(poDetailsHelper
									.getTotalAmount());
							invoice.setRemainingBalance(poDetailsHelper.getTotalAmount());
							addResult = supplierManager.addSupplierObject(invoice,session);
							if (addResult == true) {
								rch.updateCount(SASConstants.SUPPLIERINVOICE,
										"add");
								addActionMessage(SASConstants.ADD_SUCCESS);
								forWhat = "true";
								forWhatDisplay="edit";
							} else {
								addActionError(SASConstants.FAILED);
							}
						}
					}
				}
				
				return "invoiceAdded";
			}
			
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("supplierProfile")) {
				return "profileAdded";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrderAdded";
			} else if (getSubModule().equalsIgnoreCase("receivingReport")) {
				return "receivingReportAdded";
			} else {
				return "invoiceAdded";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	private String addReceivingReport(Session session) {
		boolean addResult = false;
		purchaseOrderNoList = supplierManager.listAlphabeticalAscByParameter(SupplierPurchaseOrder.class, "supplierPurchaseOrderId", session);
		
		if (validateReceivingReport()) {
			inlcudePoDetails();
		} else {
			List supRr = null;
			supRr = supplierManager.listSuppliersByParameter(
					ReceivingReport.class, "receivingReportNo", getRr()
							.getReceivingReportNo(),session);
			if (!(supRr.isEmpty())) {
				addActionError(SASConstants.EXISTS);
			} else {
				
				List supRr2 = null;
				supRr2 = supplierManager.listSuppliersByParameter(
						SupplierPurchaseOrder.class,
						"supplierPurchaseOrderId", getRr()
								.getSupplierPurchaseOrder()
								.getSupplierPurchaseOrderId(),session);
				if (supRr2.isEmpty()) {
					addActionError("Puchase Order No.: "
							+ SASConstants.NON_EXISTS);
					inlcudePoDetails();
				} else {
					rr.setSupplierPurchaseOrder((SupplierPurchaseOrder) supRr2
							.get(0));
					if (null == poDetailsHelperToCompare) {
						poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
					}
					poDetailsHelperToCompare
							.generatePODetailsListFromSet(rr
									.getSupplierPurchaseOrder()
									.getPurchaseOrderDetails());
					poDetailsHelperToCompare
							.generateCommaDelimitedValues();
					
					boolean inventoryUpdateSuccess= false;
					if (null == poDetailsHelper) {
					} else {
						poDetailsHelper.generatePODetailsListFromSet(rr
								.getSupplierPurchaseOrder()
								.getPurchaseOrderDetails());
						poDetailsHelper.generateCommaDelimitedValues();
						poDetailsHelper.setOrderDate(df.dynamicParseDateToTimestamp(rr.getReceivingReportDate(),SASConstants.TIMESTAMP_FORMAT));
						Set<PurchaseOrderDetails> podetailSet = poDetailsHelper
								.persistNewSetElements(session);

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
						PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.getChangeInOrder(new PurchaseOrderDetailHelper(actionSession), poDetailsHelper , SASConstants.ORDER_TYPE_RR);
						
						try {
							inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
							inventoryUpdateSuccess = true;
						} catch (Exception e) {
							e.printStackTrace();
							addActionError(e.getMessage());
							inventoryUpdateSuccess=false;
						}
						rr.setPurchaseOrderDetails(podetailSet);
					}
					rr.setTotalAmount(poDetailsHelper.getTotalAmount());
					//computation of payment date in rr
					DateFormatHelper dfh = new DateFormatHelper();
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String tryTS = sdf.format(rr.getReceivingReportDate());
					
					rr.setReceivingReportPaymentDate(dfh.getPaymentDateByTerm(dfh.parseStringToTimestamp(tryTS), rr.getSupplierPurchaseOrder().getSupplier().getPaymentTerm()));
					
					//START PHASE 3 
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transactionList.add(transaction);
					//END PHASE 3
					
					if(inventoryUpdateSuccess) {
						rr.setReceivingReportNo(rch.getPrefix(SASConstants.RECEIVINGREPORT,
								SASConstants.RECEIVINGREPORT_PREFIX));
						addResult = supplierManager.addSupplierObject(rr,session);
					}else {
						addResult=false;
					}
					if (addResult == true) {
						rch.updateCount(SASConstants.RECEIVINGREPORT,
								"add");
						addActionMessage(SASConstants.ADD_SUCCESS);
						forWhat = "true";
						forWhatDisplay="edit";
					} else {
						addActionError(SASConstants.FAILED);
					}
				}
			}
		}
		return "receivingReportAdded";
	}
	// reinsert the table inlcudePoDetails();
	private void inlcudePoDetails() {
		if (poDetailsHelper != null) {
			poDetailsHelper.prepareSetAndList();
		}
		if (poDetailsHelperToCompare != null) {
			poDetailsHelperToCompare.prepareSetAndList();
		}
	}
	// validation
	private boolean validateSupplierProfile() {

		boolean errorFound = false;
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
		 if (!("".equals(supplier.getEmailAddress()))) {
		 	if (!(supplier.getEmailAddress().contains("@") || supplier.getEmailAddress().contains(".com"))) {
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
		
		if ("".equals(po.getSupplierPurchaseOrderId())) {
			addFieldError("po.supplierPurchaseOrderId", "REQUIRED");
			errorFound = true;
		}
		 */
		 if (null == po.getPurchaseOrderDate()) {
		 addActionMessage("REQUIRED: PO Date");
		 errorFound = true;
		 }
		 if (null == po.getDateOfDelivery()) {
		 addActionMessage("REQUIRED: Delivery Date");
		 errorFound = true;
		 }
		if ("".equals(po.getSupplier().getSupplierId())) {
			addActionMessage("REQUIRED: Supplier ID");
			errorFound = true;
		}
		return errorFound;
	}

	private boolean validateReceivingReport() {
		boolean errorFound = false;
		/*if ("".equals(rr.getReceivingReportNo())) {
			addFieldError("rr.receivingReportNo", "REQUIRED");
			errorFound = true;
		}*/
		 if (null==rr.getReceivingReportDate()){
		 addActionMessage("REQUIRED: RR Date.");
		 errorFound = true;
		 }
		if ("".equals(rr.getSupplierPurchaseOrder()
				.getSupplierPurchaseOrderId())) {
			addActionMessage("REQUIRED: Purchase Order No");
			errorFound = true;
		}
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
		
		
		return errorFound;
	}

	PurchaseOrderDetailHelper poDetailsHelper;

	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}

	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}

	PurchaseOrderDetailHelper poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);

	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}

	public void setPoDetailsHelperToCompare(
			PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
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

	public PurchaseOrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(PurchaseOrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public SupplierPurchaseOrder getPo() {
		return po;
	}

	public void setPo(SupplierPurchaseOrder po) {
		this.po = po;
	}

	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
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