package com.shofuku.accsystem.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ognl.enhance.OrderedReturn;
import org.hibernate.Session;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.OfficeSupplies;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.POIUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class AddOrderDetailsAction extends ActionSupport implements Preparable {
	/*
	 * To checklist:
	 * 
	 * forwhat - true = edit, false= new entry poId,rrId... id, = used to
	 * retrieve in finalize since textField is disabled poDetailsHelper - used
	 * for all poDetailsHelperToCompare - used for table comparissons
	 * 
	 * should contain hidden fields:
	 * 
	 * <s:hidden name="parentPage" value="SupplierPurchaseOrder"/> <s:hidden
	 * name="poDetailsHelper.hiddenDelimetedOrderDetailsItemCode"
	 * value="%{poDetailsHelper.hiddenDelimetedOrderDetailsItemCode}"
	 * ></s:hidden> <s:hidden
	 * name="poDetailsHelper.hiddenDelimetedOrderDetailsDescription"
	 * value="%{poDetailsHelper.hiddenDelimetedOrderDetailsDescription}"
	 * ></s:hidden> <s:hidden
	 * name="poDetailsHelper.hiddenDelimetedOrderDetailsQuantity"
	 * value="%{poDetailsHelper.hiddenDelimetedOrderDetailsQuantity}"
	 * ></s:hidden> <s:hidden
	 * name="poDetailsHelper.hiddenDelimetedOrderDetailsUOM"
	 * value="%{poDetailsHelper.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
	 * <s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsCost"
	 * value="%{poDetailsHelper.hiddenDelimetedOrderDetailsCost}"></s:hidden>
	 * <s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsAmount"
	 * value="%{poDetailsHelper.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
	 * 
	 * should pass parentPage to know where to return to
	 * 
	 * always call poDetailsHelper.prepareSetAndList();
	 * poDetailsHelperToCompare.prepareSetAndList(); on finalize to have a set/
	 * list ready for table iteration
	 */

	private static final long serialVersionUID = 1L;
	
	Map actionSession;
	UserAccount user;

	InventoryUtil inventoryUtil;
	AccountEntryProfileUtil accountEntryUtil;
	
	SupplierManager supplierManager;
	CustomerManager customerManager;
	InventoryManager inventoryManager; 
	AccountEntryManager accountEntryManager;
	TransactionManager transactionManager;
	LookupManager lookupManager;
	DisbursementManager disbursementManager;

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
		
		supplierManager 		= (SupplierManager) 	actionSession.get("supplierManager");
		customerManager 		= (CustomerManager) 	actionSession.get("customerManager");
		inventoryManager 		= (InventoryManager) 	actionSession.get("inventoryManager"); 
		accountEntryManager		= (AccountEntryManager) actionSession.get("accountEntryManager");
		transactionManager 		= (TransactionManager) 	actionSession.get("transactionManager");
		lookupManager 			= (LookupManager) 		actionSession.get("lookupManager");
		disbursementManager 	= (DisbursementManager) actionSession.get("disbursementManager");
		
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
	
	//TransactionUtil transactionUtil = new TransactionUtil();

	private String forWhat;
	private String forWhatDisplay;

	
	private String poId;
	private String rrId;
	private String invId;
	private String fptsId;
	private String rfId;
	String rsIdNo;
	String returnSlipToValue;

	private String custpoid;
	private String drId;
	private String custinvId;

	private String fileUpload;

	private String parentPage;
	private String manageFPTSOrderDetailIdentifier;
	private String manageOrderRequisitionOrderDetailIdentifier;

	private SupplierPurchaseOrder po;
	private ReceivingReport rr;
	private SupplierInvoice invoice;
	private FPTS fpts;
	private ReturnSlip rs;
	private RequisitionForm rf;
	
	private CustomerPurchaseOrder custpo;
	private DeliveryReceipt dr;
	private CustomerSalesInvoice cusInvoice;

	List customerNoList;
	List supplierNoList;
	List checkVoucherList;
	
	private double tempTotal;
	
	List itemCodeList;
	List UOMList;

	//START 2013 - PHASE 3 : PROJECT 1: MARK
	List accountProfileCodeList;
	List<Transaction> transactionList;
	List<Transaction> transactions;
	Iterator itr;
	//END 2013 - PHASE 3 : PROJECT 1: MARK 
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String loadLookLists() {
		Session session = getSession();
		
		try {
			itemCodeList = inventoryManager.loadItemListFromRawAndFin(session);
			return "finGood";
		} catch (Exception e) {
			return SUCCESS;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	public String loadOrdersByReferenceNo() throws Exception{
		Session session = getSession();
		accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
		
		if(rs==null) {
			addActionError("NO RS");
		}else {
			
			try {
				
			ReceivingReport rr = (ReceivingReport)supplierManager.listSuppliersByParameter(
					ReceivingReport.class,
					"receivingReportNo",
					rs.getReturnSlipReferenceOrderNo(),session).get(0);
			if(null==poDetailsHelperToCompare) {
				poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
				poDetailsHelperToCompare.generatePODetailsListFromSet(rr.getPurchaseOrderDetails());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
			}else {
				poDetailsHelperToCompare.generatePODetailsListFromSet(rr.getPurchaseOrderDetails());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
			}
			rs.setReturnSlipTo(SASConstants.RS_WAREHOUSE_TO_SUPPLIER);
			}catch(IndexOutOfBoundsException e) {
				try {
				DeliveryReceipt dr =  (DeliveryReceipt)customerManager.listByParameter(DeliveryReceipt.class, "deliveryReceiptNo", 
						rs.getReturnSlipReferenceOrderNo(),session).get(0);
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
					poDetailsHelperToCompare.generatePODetailsListFromSet(dr.getPurchaseOrderDetails());
					poDetailsHelperToCompare.generateCommaDelimitedValues();
				}else {
					poDetailsHelperToCompare.generatePODetailsListFromSet(dr.getPurchaseOrderDetails());
					poDetailsHelperToCompare.generateCommaDelimitedValues();
				}
				rs.setReturnSlipTo(SASConstants.RS_CUSTOMER_TO_WAREHOUSE);
				}catch(IndexOutOfBoundsException e2) {
					try {
					RequisitionForm rf =  (RequisitionForm)
							inventoryManager.listInventoryByParameter(RequisitionForm.class,"requisitionNo", rs.getReturnSlipReferenceOrderNo(), session).get(0);
					if(null==poDetailsHelperToCompare) {
						poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
						poDetailsHelperToCompare.generatePODetailsListFromSet(rf.getPurchaseOrderDetailsOrdered());
						poDetailsHelperToCompare.generateCommaDelimitedValues();
					}else {
						poDetailsHelperToCompare.generatePODetailsListFromSet(rf.getPurchaseOrderDetailsOrdered());
						poDetailsHelperToCompare.generateCommaDelimitedValues();
					}
					rs.setReturnSlipTo(SASConstants.RS_PRODUCTION_TO_WAREHOUSE);
					}catch(IndexOutOfBoundsException e3) {
						
						try {
							FPTS fpts =  (FPTS)
									inventoryManager.listInventoryByParameter(FPTS.class,"fptsNo", rs.getReturnSlipReferenceOrderNo(), session).get(0);
							if(null==poDetailsHelperToCompare) {
								poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
								poDetailsHelperToCompare.generatePODetailsListFromSet(fpts.getPurchaseOrderDetailsTransferred());
								poDetailsHelperToCompare.generateCommaDelimitedValues();
							}else {
								poDetailsHelperToCompare.generatePODetailsListFromSet(fpts.getPurchaseOrderDetailsTransferred());
								poDetailsHelperToCompare.generateCommaDelimitedValues();
							}
							rs.setReturnSlipTo(SASConstants.RS_WAREHOUSE_TO_PRODUCTION);
							}catch(IndexOutOfBoundsException e4) {
								addActionError("NOT EXISTING RECORD");
								//return "returnSlip";
							}
						//return "returnSlip";
					}
				}
			}
				//forWhatDisplay="new";
			
		}
		if (poDetailsHelperToCompare.getPurchaseOrderDetailsList() != null) {
			itemCodeList = new ArrayList();
			Iterator itr = poDetailsHelperToCompare.getPurchaseOrderDetailsList().iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr.next();
				itemCodeList.add(tempDetails.getItemCode());
				}
			}
		return "returnSlip";
	}
	
	public Set<PurchaseOrderDetails> loadOrdersByReferenceNo(ReturnSlip rs){
		Session session = getSession();
		accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
		
		Set <PurchaseOrderDetails> purchaseOrderDetails = null;
		if(rs==null) {
			addActionError("NO RS");
		}else {
						
			try {
			ReceivingReport rr = (ReceivingReport)supplierManager.listSuppliersByParameter(
					ReceivingReport.class,
					"receivingReportNo",
					rs.getReturnSlipReferenceOrderNo(),session).get(0);
			
			purchaseOrderDetails = rr.getPurchaseOrderDetails();
			
			}catch(IndexOutOfBoundsException e) {
				try {
				DeliveryReceipt dr =  (DeliveryReceipt)customerManager.listByParameter(DeliveryReceipt.class, "deliveryReceiptNo", 
						rs.getReturnSlipReferenceOrderNo(),session).get(0);
				purchaseOrderDetails = dr.getPurchaseOrderDetails();
				
				}catch(IndexOutOfBoundsException e2) {
					//addActionError("NOT EXISTING RECEVING REPORT OR DELIVERY RECEIPT");
					try {
						RequisitionForm rf =  (RequisitionForm)inventoryManager.listByParameter(RequisitionForm.class, "requisitionNo", 
								rs.getReturnSlipReferenceOrderNo(),session).get(0);
						purchaseOrderDetails = rf.getPurchaseOrderDetailsOrdered();
						
						}catch(IndexOutOfBoundsException e3) {
							//addActionError("NOT EXISTING Order Requistion No");
							try{
								FPTS fpts =  (FPTS)inventoryManager.listByParameter(FPTS.class, "fptsNo", 
									rs.getReturnSlipReferenceOrderNo(),session).get(0);
								purchaseOrderDetails = fpts.getPurchaseOrderDetailsTransferred();
							}catch (Exception e4){
								addActionError("NOT EXISTING RECORD");
							}
						}
				}
				
			}			
		}
		return purchaseOrderDetails;
	}
	public String loadOrderDetails() throws Exception{
		loadLookLists();
		Session session = getSession();
		accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
		
		try{
		//load passed values if any
			if(	parentPage.equalsIgnoreCase("InventoryFPTS")) {
				if(manageFPTSOrderDetailIdentifier.equalsIgnoreCase("T")) {
					if (null == poDetailsHelper) {
					} else {
						if (null == poDetailsHelper) {
						} else {
							poDetailsHelper.prepareSetAndList();
							poDetailsHelperDraft = poDetailsHelper;
							}
					}
				}else {
					poDetailsHelperToCompare.prepareSetAndList();
					
					if(poDetailsHelperToCompare.getPurchaseOrderDetailsList().size() > 0){
						poDetailsHelperDraft = poDetailsHelperToCompare;	
					}else{
						poDetailsHelperToCompare = poDetailsHelper;
						poDetailsHelperDraft = poDetailsHelperToCompare ;
					}
				}
			}else if(parentPage.equalsIgnoreCase("InventoryOrderRequisition")) {
				if(manageOrderRequisitionOrderDetailIdentifier.equalsIgnoreCase("O")) {
					if (null == poDetailsHelper) {
						//poDetailsHelperDraft =poDetailsHelper;
					} else {
						if (null == poDetailsHelper) {
						} else {
							poDetailsHelper.prepareSetAndList();
							poDetailsHelperDraft = poDetailsHelper;
						}
					}
				}/*else {
					poDetailsHelperToCompare.prepareSetAndList();
					
					if(poDetailsHelperToCompare.getPurchaseOrderDetailsList().size() > 0){
						poDetailsHelperDraft = poDetailsHelperToCompare;	
					}else{
						poDetailsHelperToCompare = poDetailsHelper;
						poDetailsHelperDraft = poDetailsHelperToCompare ;
					}
				}*/
			}else {
				if (null == poDetailsHelper) {
				} else {
					poDetailsHelperDraft = poDetailsHelper;
				}
				poDetailsHelperDraft.prepareSetAndList();
			}
		String priceType="";
		String customerType ="";
		//will check if will get company or franchise price
		//test
		if(parentPage.equalsIgnoreCase("CustomerPurchaseOrder")|| parentPage.equalsIgnoreCase("DeliveryReceipt")) {
			//check what type of customer - always get transfer price
				List cusPOList = customerManager.listByParameter(CustomerPurchaseOrder.class, "customerPurchaseOrderId",
						custpoid,session);
				if (cusPOList.isEmpty()) {
				} else {
					priceType="transfer";
					CustomerPurchaseOrder cusPo = (CustomerPurchaseOrder )cusPOList.get(0);
					customerType = cusPo.getCustomer().getCustomerType();
					}
		}else if (parentPage.equalsIgnoreCase("SupplierPurchaseOrder")|| parentPage.equalsIgnoreCase("ReceivingReport")||parentPage.equalsIgnoreCase("SupplierInvoice")
				||parentPage.equalsIgnoreCase("InventoryFPTS") || parentPage.equalsIgnoreCase("InventoryOrderRequisition")){
			//always get company standard
			customerType="C";
			priceType="standard";
		}
		if (parentPage.equalsIgnoreCase("ReturnSlip")){
			
			if(poDetailsHelperToCompare==null) {
			}else {

				//to get if disabled
				rs.setReturnSlipNo(rsIdNo);
				rs.setReturnSlipTo(returnSlipToValue);
				forWhat="false";
				poDetailsHelperToCompare.prepareSetAndList();
				Iterator itr = poDetailsHelperToCompare.getPurchaseOrderDetailsList().iterator();
				itemCodeList = new ArrayList();
				while(itr.hasNext()) {
					PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr.next();
					if(orderDetails.getItemCode().equalsIgnoreCase(tempDetails.getItemCode())) {
						orderDetails=tempDetails;
					}
					this.itemCodeList.add(tempDetails.getItemCode());
				}
			}
			
			return "returnSlip";
		}else {
			List list = inventoryManager.listInventoryByParameter(RawMaterial.class, "itemCode", orderDetails.getItemCode(),session);
			List tradedItemlist = inventoryManager.listInventoryByParameter(TradedItem.class, "itemCode", orderDetails.getItemCode(),session);
			List utensilsList = inventoryManager.listInventoryByParameter(Utensils.class, "itemCode", orderDetails.getItemCode(),session);
			List officeSuppliesList = inventoryManager.listInventoryByParameter(OfficeSupplies.class, "itemCode", orderDetails.getItemCode(),session);
			
			if(list.size()>0) {
				RawMaterial tempItem = (RawMaterial)list.get(0);
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				orderDetails= new PurchaseOrderDetails(tempItem.getItemCode(), tempItem.getDescription(), 0, tempItem.getUnitOfMeasurement(), inventoryUtil.getItemPricingByCustomerTypeAndParameter(tempItem.getItemPricing(), customerType, priceType), 0,tempItem.getIsVattable(),0,0);
			}
			else {
				 if (tradedItemlist.size()>0){
						TradedItem tempItem = (TradedItem)tradedItemlist.get(0);
						orderDetails= new PurchaseOrderDetails(tempItem.getItemCode(), tempItem.getDescription(), 0, tempItem.getUnitOfMeasurement(), inventoryUtil.getItemPricingByCustomerTypeAndParameter(tempItem.getItemPricing(), customerType, priceType), 0,tempItem.getIsVattable(),0,0);
				 }else if (utensilsList.size()>0){
					 Utensils tempItem = (Utensils)utensilsList.get(0);
						orderDetails= new PurchaseOrderDetails(tempItem.getItemCode(), tempItem.getDescription(), 0, tempItem.getUnitOfMeasurement(), inventoryUtil.getItemPricingByCustomerTypeAndParameter(tempItem.getItemPricing(), customerType, priceType), 0,tempItem.getIsVattable(),0,0);
				 }else if (officeSuppliesList.size()>0){
					 OfficeSupplies tempItem = (OfficeSupplies)officeSuppliesList.get(0);
						orderDetails= new PurchaseOrderDetails(tempItem.getItemCode(), tempItem.getDescription(), 0, tempItem.getUnitOfMeasurement(), inventoryUtil.getItemPricingByCustomerTypeAndParameter(tempItem.getItemPricing(), customerType, priceType), 0,tempItem.getIsVattable(),0,0);
				 }else{
					list = inventoryManager.listInventoryByParameter(FinishedGood.class, "productCode", orderDetails.getItemCode(),session);
					FinishedGood tempItem2 = (FinishedGood)list.get(0);
					orderDetails= new PurchaseOrderDetails(tempItem2.getProductCode(), tempItem2.getDescription(), 0, tempItem2.getUnitOfMeasurement(), inventoryUtil.getItemPricingByCustomerTypeAndParameter(tempItem2.getItemPricing(), customerType, priceType), 0,tempItem2.getIsVattable(),0,0);
				 }
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
		}
			return INPUT;
		}
		} catch (Exception e) {
			return INPUT;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
	public String execute() throws Exception {

		// load passed values if any
		loadLookLists();
		Session session = getSession();
		accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
		
	if(	parentPage.equalsIgnoreCase("InventoryFPTS")){
		if(manageFPTSOrderDetailIdentifier.equalsIgnoreCase("T")) {
			if (null == poDetailsHelper) {
				poDetailsHelper = new PurchaseOrderDetailHelper(actionSession);
			} else {
				poDetailsHelper.prepareSetAndList();
				addOrderDetailToList(poDetailsHelper);
				poDetailsHelperDraft = poDetailsHelper;
				
			}
		}else {
			
			if (null == poDetailsHelperToCompare) {
			} 
			else {
				poDetailsHelperToCompare.prepareSetAndList();
				if(poDetailsHelperToCompare.getPurchaseOrderDetailsList().size() > 0){
					poDetailsHelperDraft = poDetailsHelperToCompare;
					
				}else{
					poDetailsHelperToCompare=poDetailsHelper;
					poDetailsHelperToCompare.prepareSetAndList();
				}
			}
			addOrderDetailToList(poDetailsHelperToCompare);
			poDetailsHelperDraft = poDetailsHelperToCompare;
		}
		
	}else if(parentPage.equalsIgnoreCase("InventoryOrderRequisition")){
		if(manageOrderRequisitionOrderDetailIdentifier.equalsIgnoreCase("O")) {
			if (null == poDetailsHelper) {
			} else {
				poDetailsHelper.prepareSetAndList();
				addOrderDetailToList(poDetailsHelper);
				poDetailsHelperDraft = poDetailsHelper;
			}
		}/*else {
			if (null == poDetailsHelperToCompare) {
			} else {
				poDetailsHelperToCompare.prepareSetAndList();
				if(poDetailsHelperToCompare.getPurchaseOrderDetailsList().size() > 0){
					poDetailsHelperDraft = poDetailsHelperToCompare;	
				}else{
					poDetailsHelperToCompare=poDetailsHelper;
					poDetailsHelperToCompare.prepareSetAndList();
				}
			
			addOrderDetailToList(poDetailsHelperToCompare);
			poDetailsHelperDraft = poDetailsHelperToCompare;
			}
		}*/
		
	}else if(parentPage.equalsIgnoreCase("returnSlip")) {
		
		if (null == poDetailsHelperDraft) {
			poDetailsHelperDraft = new PurchaseOrderDetailHelper(actionSession);
		} else {
			//to get if disabled
			rs.setReturnSlipNo(rsIdNo);
			rs.setReturnSlipTo(returnSlipToValue);
			
			forWhat="false";
			
			poDetailsHelperDraft.prepareSetAndList();
			poDetailsHelperToCompare.prepareSetAndList();
			addOrderDetailToList(poDetailsHelperDraft);
			itemCodeList = new ArrayList();
			Iterator itr = poDetailsHelperDraft.getPurchaseOrderDetailsList().iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr.next();
				itemCodeList.add(tempDetails.getItemCode());
			}
		}
		return "returnSlip";
	}else {
			if (null == poDetailsHelper) {
			} else {
				poDetailsHelper.prepareSetAndList();
				addOrderDetailToList(poDetailsHelper);
				poDetailsHelperDraft = poDetailsHelper;
			}
		}
	return INPUT;
	}
	
	private void addOrderDetailToList(PurchaseOrderDetailHelper helper) {
		if (null == orderDetails
				|| orderDetails.getItemCode().equalsIgnoreCase("")) {
			// do nothing since nothing is passed
		} else {
			orderDetails.setParent(parentPage);
			helper.modifyPurchaseOrderDetail(orderDetails);
		}
		
	}

	@Deprecated 
	/*- use PurchaseOrderDetailsHelper sortListsAlphabetically() instead
	 * 
	 */
	private PurchaseOrderDetailHelper sortListsAlphabetically(PurchaseOrderDetailHelper poDetailsHelper){
		
		List purchaseOrderDetailsList= poDetailsHelper.generatePODetailsListFromSet(poDetailsHelper.getPurchaseOrderDetailsSet());
		List sortedPurchaseOrderDetailsList= new ArrayList();
		
		HashMap<String,PurchaseOrderDetails> map = new HashMap<String,PurchaseOrderDetails>();
		Set<PurchaseOrderDetails> sortedMap = new HashSet<PurchaseOrderDetails>();
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
				sortedMap.add(map.get(code));
			}
			poDetailsHelper.setPurchaseOrderDetailsSet(sortedMap);
			poDetailsHelper.setPurchaseOrderDetailsList(sortedPurchaseOrderDetailsList);
		}catch(NullPointerException nfe) {
			nfe.printStackTrace();
		}
		
		return poDetailsHelper;
				
	}
	public String supplierPOOrderingFormImport() {
		Session session = getSession();
		
		if(poDetailsHelper==null) {
			poDetailsHelper = new PurchaseOrderDetailHelper(actionSession);
		}
		
		supplierNoList = supplierManager.listAlphabeticalAscByParameter(Supplier.class, "supplierId", session);
		
		//2013 - PHASE 3 : PROJECT 4: MARK
		
		List<Item> allItemList = new ArrayList<Item>();
		allItemList = inventoryManager.getAllItemList(session);
						
		//END 2013 - PHASE 3 : PROJECT 4: MARK
		
		
		try {
			Set<PurchaseOrderDetails> orderUpload = new HashSet<PurchaseOrderDetails>();
			if (null == fileUpload) {
			} else {
				
				POIUtil poiUtilHelper = new POIUtil(actionSession);
				orderUpload = poiUtilHelper.readOrderingForm("C","standard",fileUpload, "",
						session);

				Iterator<PurchaseOrderDetails> itr = orderUpload.iterator();

				// HashMap<String, PurchaseOrderDetails> groups = new
				// HashMap<String, PurchaseOrderDetails>();

				while (itr.hasNext()) {
					PurchaseOrderDetails poDetails = itr.next();
					//START: 2013 - PHASE 3 : PROJECT 4: MARK
					for(Item item: allItemList){
						if(item.getItemCode().equalsIgnoreCase(poDetails.getItemCode())) {
							poDetails.setIsVattable(item.getIsVattable());
							break;
						}
					}
					//END : 2013 - PHASE 3 : PROJECT 4: MARK

					if (poDetails.isInFinishedGoods()) {
						poDetailsHelper.modifyPurchaseOrderDetail(poDetails);
						if (null == po) {
							po = new SupplierPurchaseOrder();
						}
						po.setTotalAmount(po.getTotalAmount()
								+ poDetails.getAmount());
					} else {
						poDetailsHelperToCompare
								.modifyPurchaseOrderDetail(poDetails);
					}

				}
				
				DateFormatHelper dfh = new DateFormatHelper();
				po.setPaymentDate(dfh.getPaymentDateByTerm(po.getDateOfDelivery(), po.getPaymentTerm()));
				
				
			}
			return "PO";
		} catch (Exception e) {
			return "PO";
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}
	public String customerPOOrderingFormImport() {
		Session session = getSession();
		if(poDetailsHelper==null) {
			poDetailsHelper = new PurchaseOrderDetailHelper(actionSession);
		}
		
		customerNoList = customerManager.listAllCustomerNo(session);
		//2013 - PHASE 3 : PROJECT 4: MARK
		
		List<Item> allItemList = new ArrayList<Item>();
		allItemList = inventoryManager.getAllItemList(session);
						
		//END 2013 - PHASE 3 : PROJECT 4: MARK
		
		
		try {
			Set<PurchaseOrderDetails> orderUpload = new HashSet<PurchaseOrderDetails>();
			if (null == fileUpload) {
			} else {
				
				if (null == custpo) {
					custpo = new CustomerPurchaseOrder();
				}
				if (null == custpoid) {
					RecordCountHelper rch = new RecordCountHelper(actionSession);
					custpo.setCustomerPurchaseOrderId(rch.getPrefix(
							SASConstants.CUSTOMERPO,
							SASConstants.CUSTOMERPO_PREFIX));
				} else {
					custpo.setCustomerPurchaseOrderId(custpoid);
					if(null == custpo.getCustomer()){
						addActionMessage("REQUIRED: Customer");
						return "customerPO";
					}else{
						if ("".equals(custpo.getCustomer().getCustomerNo())) {
						addActionMessage("REQUIRED: Customer ID");
						return "customerPO";
						}else {
							String customerType =custpo.getCustomer().getCustomerType();
							Customer customer = (Customer)customerManager.listByParameter(Customer.class, "customerNo", custpo.getCustomer().getCustomerNo(), session).get(0);
							custpo.setCustomer(customer);  
						}
					}
				}
				
				POIUtil poiUtilHelper = new POIUtil(actionSession);
				if(custpo.getCustomer().getCustomerType().equalsIgnoreCase("CC")) {
					orderUpload = poiUtilHelper.readOrderingForm(custpo.getCustomer().getCustomerType(),"standard",fileUpload, "",
							session);
				}else {
					orderUpload = poiUtilHelper.readOrderingForm(custpo.getCustomer().getCustomerType(),"transfer",fileUpload, "",
							session);
				}
				

				Iterator<PurchaseOrderDetails> itr = orderUpload.iterator();

				// HashMap<String, PurchaseOrderDetails> groups = new
				// HashMap<String, PurchaseOrderDetails>();

				while (itr.hasNext()) {
					PurchaseOrderDetails poDetails = itr.next();

					
					//START: 2013 - PHASE 3 : PROJECT 4: MARK
					for(Item item: allItemList){
						if(item.getItemCode().equalsIgnoreCase(poDetails.getItemCode())) {
							poDetails.setIsVattable(item.getIsVattable());
							break;
						}
					}
					//END : 2013 - PHASE 3 : PROJECT 4: MARK
// YOU LEFT HERE PROBLEM: PODETAILS CANT SHOW FOR SAME ITEM CODES WHICH IS BLANK FOR UNLISTED ITEMS
					if (poDetails.isInFinishedGoods()) {
						poDetailsHelper.modifyPurchaseOrderDetail(poDetails);
						if (null == custpo) {
							custpo = new CustomerPurchaseOrder();
						}
						custpo.setTotalAmount(custpo.getTotalAmount()
								+ poDetails.getAmount());
					} else {
						poDetailsHelperToCompare
								.modifyPurchaseOrderDetail(poDetails);
					}

				}
				
				DateFormatHelper dfh = new DateFormatHelper();
				custpo.setPaymentDate(dfh.getPaymentDateByTerm(custpo.getDateOfDelivery(), custpo.getPaymentTerm()));
				
				
			}
			return "customerPO";
		} catch (Exception e) {
			return "customerPO";
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}

	public String deleteOrderDetail() throws Exception {
		loadLookLists();
		if(	parentPage.equalsIgnoreCase("InventoryFPTS")) {
			if(manageFPTSOrderDetailIdentifier.equalsIgnoreCase("T")) {
				if (null == poDetailsHelper) {
				} else {
					poDetailsHelper.removePurchaseOrderDetail(orderDetails.getItemCode());
					poDetailsHelperDraft = poDetailsHelper;
				}
			}else {
				if (null == poDetailsHelperToCompare) {
				} else {
					poDetailsHelperToCompare.removePurchaseOrderDetail(orderDetails.getItemCode());
					poDetailsHelperDraft = poDetailsHelperToCompare;
				}
			}
		}else if(parentPage.equalsIgnoreCase("InventoryOrderRequisition")) {
			if((manageOrderRequisitionOrderDetailIdentifier.equalsIgnoreCase("O"))) {
				if (null == poDetailsHelper) {
				} else {
					poDetailsHelper.removePurchaseOrderDetail(orderDetails.getItemCode());
					poDetailsHelperDraft = poDetailsHelper;
				}
			}else {
				if (null == poDetailsHelperToCompare) {
				} else {
					poDetailsHelperToCompare.removePurchaseOrderDetail(orderDetails.getItemCode());
					poDetailsHelperDraft = poDetailsHelperToCompare;
				}
			}
		}else {
			if (null == poDetailsHelper) {
			} else {
				poDetailsHelper.removePurchaseOrderDetail(orderDetails.getItemCode());
				poDetailsHelperDraft = poDetailsHelper;
			}
		}
		return INPUT;
	}

	private double computerForTotalAmount(PurchaseOrderDetailHelper helper) {

		Set detailsSet = helper.generatePODetailsSet();

		Iterator<PurchaseOrderDetails> iterator = detailsSet.iterator();
		double totalAmount = 0;
		while (iterator.hasNext()) {
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails) iterator
					.next();
			totalAmount = totalAmount
					+ (poDetails.getQuantity() * poDetails.getUnitCost());
		}
		return totalAmount;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String finalizeOrder() throws Exception {
		Session session = getSession();
		try {
			
			double totalAmount = 0;
			double totalAmountToCompareTo = 0;
			totalAmount = computerForTotalAmount(poDetailsHelper);
			//2013 - PHASE 3 : PROJECT 4: MARK
			List<Item> allItemList = new ArrayList<Item>();
			allItemList = inventoryManager.getAllItemList(session);
			
			Set<PurchaseOrderDetails> poDetailsSet = new HashSet<PurchaseOrderDetails>();
			
			poDetailsSet = poDetailsHelper.generatePODetailsSet();
			
			Iterator<PurchaseOrderDetails> poDetailsItr = poDetailsSet.iterator();

			while (poDetailsItr.hasNext()) {
				PurchaseOrderDetails poDetails = poDetailsItr.next();
				for(Item item: allItemList){
					if(item.getItemCode().equalsIgnoreCase(poDetails.getItemCode())) {
						poDetails.setIsVattable(item.getIsVattable());
						poDetailsHelper.modifyPurchaseOrderDetail(poDetails);
						break;
					}
				}
			}
			//END: 2013 - PHASE 3 : PROJECT 4: MARK
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
			
			
			if (null == poDetailsHelperToCompare) {
			} else {
				totalAmountToCompareTo = computerForTotalAmount(poDetailsHelperToCompare);
			}
			if (parentPage.equalsIgnoreCase("SupplierPurchaseOrder")) {
				po = (SupplierPurchaseOrder) supplierManager.listSuppliersByParameter(SupplierPurchaseOrder.class,"supplierPurchaseOrderId", poId, session).get(0);
				po.setTotalAmount(totalAmount);
				// poDetailsHelper.prepareSetAndList();  <--double display
				forWhat="false";
				return "supplierPO";
			} else if (parentPage.equalsIgnoreCase("ReceivingReport")) {
				rr = (ReceivingReport) supplierManager.listSuppliersByParameter(ReceivingReport.class, "receivingReportNo", rrId,session).get(0);
				po = rr.getSupplierPurchaseOrder();
				rr.setTotalAmount(totalAmount);
				po.setTotalAmount(totalAmountToCompareTo);
				//poDetailsHelper.prepareSetAndList(); <--double display
				poDetailsHelperToCompare.prepareSetAndList();
				forWhat="false";
				return "supplierRR";
			} else if (parentPage.equalsIgnoreCase("SupplierInvoice")) {
				invoice = (SupplierInvoice) supplierManager.listSuppliersByParameter(SupplierInvoice.class,"supplierInvoiceNo", invId, session).get(0);
				rr = invoice.getReceivingReport();
				rr.setTotalAmount(totalAmountToCompareTo);
				//20131226 - PHASE 3 : PROJECT 1: MARK
				DecimalFormat df = new DecimalFormat("#.##");
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", invoice.getSupplierInvoiceNo(), session);
				if (tempList.size() == 0) {
					//START - 2013 - PHASE 3 : PROJECT 1: MARK
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transaction.setAmount(0);
					transactionList.add(transaction);
					//END - 2013 - PHASE 3 : PROJECT 1: MARK
				}else {
				itr = tempList.iterator();
				transactionList = new ArrayList<Transaction>(); 
					
				while(itr.hasNext()) {
					Transaction transaction = (Transaction)itr.next();
					if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
						transactionList.add(transaction);
					}
				}
					
				this.setTransactionList(transactionList);
				this.invoice.setTransactions(transactionList);
				}
					
				invoice.setCredit1Amount(Double.valueOf(df.format(invoice.getDebit1Amount() + invoice.getDebit2Amount())));
				invoice.setCredit2Amount(invoice.getCredit1Amount());
				checkVoucherList= disbursementManager.listDisbursementsByParameter(CheckPayments.class, "invoice.supplierInvoiceNo", invId, session);
				
				Iterator itr = checkVoucherList.iterator();
				
				while (itr.hasNext()){
					CheckPayments chpFromList = (CheckPayments) itr.next();
					tempTotal = tempTotal + chpFromList.getAmountToPay();
				}
				
				//poDetailsHelper.prepareSetAndList();   <--double display
				//COLOR CODING
				poDetailsHelper.generateItemTypesForExistingItems(session);
				//
				poDetailsHelperToCompare.prepareSetAndList();
				forWhat="false";
				return "supplierInvoice";
			} else if (parentPage.equalsIgnoreCase("CustomerPurchaseOrder")) {
				custpo = (CustomerPurchaseOrder) customerManager.listByParameter(CustomerPurchaseOrder.class,"customerPurchaseOrderId", custpoid,session).get(0);
				custpo.setTotalAmount(totalAmount);
				//poDetailsHelper.prepareSetAndList(); <--double display
				forWhat="false";
				return "customerPO";
			} else if (parentPage.equalsIgnoreCase("DeliveryReceipt")) {
				dr = (DeliveryReceipt) customerManager.listByParameter(	DeliveryReceipt.class, "deliveryReceiptNo", drId,session).get(0);
				custpo = dr.getCustomerPurchaseOrder();
				dr.setTotalAmount(totalAmount);
				custpo.setTotalAmount(totalAmountToCompareTo);
				//poDetailsHelper.prepareSetAndList();    <-- double display
				poDetailsHelperToCompare.prepareSetAndList();
				//COLOR CODING
				poDetailsHelper.generateItemTypesForExistingItems(session);
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", dr.getDeliveryReceiptNo(), session);
				if (tempList.size() == 0) {
					//START - 2013 - PHASE 3 : PROJECT 1: MARK
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transaction.setAmount(0);
					transactionList.add(transaction);
					//END - 2013 - PHASE 3 : PROJECT 1: MARK
				}else {
				itr = tempList.iterator();
				transactionList = new ArrayList<Transaction>(); 
				while(itr.hasNext()) {
					Transaction transaction = (Transaction)itr.next();
					if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
						transactionList.add(transaction);
					}
				}
				this.setTransactionList(transactionList);
				this.dr.setTransactions(transactionList);
				}
				//END Phase 3 - Azhee
				
				forWhat="false";
				return "customerDR";
				
			}else if (parentPage.equalsIgnoreCase("InventoryFPTS")) {
				fpts = (FPTS) inventoryManager.listInventoryByParameter(FPTS.class,"fptsNo", fptsId,session).get(0);
				//poDetailsHelper.prepareSetAndList(); <--double display
				poDetailsHelperToCompare.prepareSetAndList();
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", fpts.getFptsNo(), session);
				if (tempList.size() == 0) {
					//START - 2013 - PHASE 3 : PROJECT 1: MARK
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transaction.setAmount(0);
					transactionList.add(transaction);
					//END - 2013 - PHASE 3 : PROJECT 1: MARK
				}else {
				itr = tempList.iterator();
				transactionList = new ArrayList<Transaction>(); 
				while(itr.hasNext()) {
					Transaction transaction = (Transaction)itr.next();
					if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
						transactionList.add(transaction);
					}
				}
				this.setTransactionList(transactionList);
				this.fpts.setTransactions(transactionList);
				}
				//END Phase 3 - Azhee
				
				forWhat="false";
				return "fpts";
			}else if (parentPage.equalsIgnoreCase("InventoryOrderRequisition")) {
				rf = (RequisitionForm) inventoryManager.listInventoryByParameter(RequisitionForm.class,"requisitionNo", rfId,session).get(0);
				//poDetailsHelper.prepareSetAndList(); <--double display
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", rf.getRequisitionNo(), session);
				if (tempList.size() == 0) {
					//START - 2013 - PHASE 3 : PROJECT 1: MARK
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transaction.setAmount(0);
					transactionList.add(transaction);
					//END - 2013 - PHASE 3 : PROJECT 1: MARK
				}else {
				itr = tempList.iterator();
				transactionList = new ArrayList<Transaction>(); 
				while(itr.hasNext()) {
					Transaction transaction = (Transaction)itr.next();
					if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
						transactionList.add(transaction);
					}
				}
				this.setTransactionList(transactionList);
				this.rf.setTransactions(transactionList);
				}
				//END Phase 3 - Azhee
				forWhat="false";
				return "rf";
			}
			return "input";
			/*else {
				// add more conditions if parent is invoice + customers
				
			}*/
		
		} catch (Exception e) {
			e.printStackTrace();
			return "input";
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	// GETTER SETTERS

	public String getParentPage() {
		return parentPage;
	}

	public void setParentPage(String parentPage) {
		this.parentPage = parentPage;
	}

	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public String getRrId() {
		return rrId;
	}

	public void setRrId(String rrId) {
		this.rrId = rrId;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getCustpoid() {
		return custpoid;
	}

	public void setCustpoid(String custpoid) {
		this.custpoid = custpoid;
	}

	public String getDrId() {
		return drId;
	}

	public void setDrId(String drId) {
		this.drId = drId;
	}

	public String getCustinvId() {
		return custinvId;
	}

	public void setCustinvId(String custinvId) {
		this.custinvId = custinvId;
	}

	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public ReceivingReport getRr() {
		return rr;
	}

	public void setRr(ReceivingReport rr) {
		this.rr = rr;
	}

	public SupplierPurchaseOrder getPo() {
		return po;
	}

	public void setPo(SupplierPurchaseOrder po) {
		this.po = po;
	}

	public DeliveryReceipt getDr() {
		return dr;
	}

	public void setDr(DeliveryReceipt dr) {
		this.dr = dr;
	}

	public SupplierInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SupplierInvoice invoice) {
		this.invoice = invoice;
	}

	public CustomerPurchaseOrder getCustpo() {
		return custpo;
	}

	public void setCustpo(CustomerPurchaseOrder custpo) {
		this.custpo = custpo;
	}

	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}

	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}

	public PurchaseOrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(PurchaseOrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}

	public void setPoDetailsHelperToCompare(
			PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public PurchaseOrderDetailHelper getPoDetailsGrouped() {
		return poDetailsGrouped;
	}

	public void setPoDetailsGrouped(PurchaseOrderDetailHelper poDetailsGrouped) {
		this.poDetailsGrouped = poDetailsGrouped;
	}

	public CustomerSalesInvoice getCusInvoice() {
		return cusInvoice;
	}

	public void setCusInvoice(CustomerSalesInvoice cusInvoice) {
		this.cusInvoice = cusInvoice;
	}

	public List getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List itemCodeList) {
		this.itemCodeList = itemCodeList;
	}

	public String getForWhatDisplay() {
		return forWhatDisplay;
	}

	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}
	public List getCustomerNoList() {
		return customerNoList;
	}
	public void setCustomerNoList(List customerNoList) {
		this.customerNoList = customerNoList;
	}

	public List getSupplierNoList() {
		return supplierNoList;
	}

	public void setSupplierNoList(List supplierNoList) {
		this.supplierNoList = supplierNoList;
	}

	public FPTS getFpts() {
		return fpts;
	}

	public void setFpts(FPTS fpts) {
		this.fpts = fpts;
	}

	public String getFptsId() {
		return fptsId;
	}

	public void setFptsId(String fptsId) {
		this.fptsId = fptsId;
	}

	public String getManageFPTSOrderDetailIdentifier() {
		return manageFPTSOrderDetailIdentifier;
	}

	public void setManageFPTSOrderDetailIdentifier(
			String manageFPTSOrderDetailIdentifier) {
		this.manageFPTSOrderDetailIdentifier = manageFPTSOrderDetailIdentifier;
	}

	public PurchaseOrderDetailHelper getPoDetailsHelperDraft() {
		return poDetailsHelperDraft;
	}

	public void setPoDetailsHelperDraft(
			PurchaseOrderDetailHelper poDetailsHelperDraft) {
		this.poDetailsHelperDraft = poDetailsHelperDraft;
	}

	public String getManageOrderRequisitionOrderDetailIdentifier() {
		return manageOrderRequisitionOrderDetailIdentifier;
	}

	public void setManageOrderRequisitionOrderDetailIdentifier(
			String manageOrderRequisitionOrderDetailIdentifier) {
		this.manageOrderRequisitionOrderDetailIdentifier = manageOrderRequisitionOrderDetailIdentifier;
	}
	public String getRfId() {
		return rfId;
	}

	public void setRfId(String rfId) {
		this.rfId = rfId;
	
	}

	public RequisitionForm getRf() {
		return rf;
	}

	public void setRf(RequisitionForm rf) {
		this.rf = rf;
	}

	public ReturnSlip getRs() {
		return rs;
	}

	public void setRs(ReturnSlip rs) {
		this.rs = rs;
	}

	public String getReturnSlipToValue() {
		return returnSlipToValue;
	}

	public void setReturnSlipToValue(String returnSlipToValue) {
		this.returnSlipToValue = returnSlipToValue;
	}

	public String getRsIdNo() {
		return rsIdNo;
	}

	public void setRsIdNo(String rsIdNo) {
		this.rsIdNo = rsIdNo;
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
			//END 2013 - PHASE 3 : PROJECT 1: MARK 

}
