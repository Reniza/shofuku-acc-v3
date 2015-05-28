package com.shofuku.accsystem.action.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.ItemPricing;
import com.shofuku.accsystem.domain.inventory.OfficeSupplies;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.UnlistedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.inventory.Warehouse;
import com.shofuku.accsystem.domain.lookups.InventoryClassification;
import com.shofuku.accsystem.domain.lookups.UnitOfMeasurements;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.utils.DoubleConverter;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class AddInventoryAction extends ActionSupport implements Preparable{

	private static final long serialVersionUID = 1L;
	
	Map actionSession;
	UserAccount user;

	SupplierManager supplierManager;
	AccountEntryManager accountEntryManager;
	TransactionManager transactionManager;
	InventoryManager inventoryManager;
	FinancialsManager financialsManager;	
	LookupManager lookupManager;
	CustomerManager customerManager;
	
	RecordCountHelper rch;
	InventoryUtil invUtil;
	
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperDraft;
	
	@Override
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		supplierManager = (SupplierManager) actionSession.get("supplierManager");
		customerManager = (CustomerManager) actionSession.get("customerManager");
		accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
		transactionManager = (TransactionManager) actionSession.get("transactionManager");
		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
		financialsManager = (FinancialsManager) actionSession.get("financialsManager");
		lookupManager = (LookupManager) actionSession.get("lookupManager");
		
		rch = new RecordCountHelper(actionSession);
		invUtil = new InventoryUtil(actionSession);
		
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
		
	}
	
	

	private String subModule;
	private String forWhat;
	private String forWhatDisplay;

	RawMaterial rm;
	FinishedGood fg;
	TradedItem ti;
	FPTS fpts;
	RequisitionForm rf;
	ReturnSlip rs;
	Utensils u;
	UnlistedItem unl;
	OfficeSupplies os;

	String returnSlipToValue;
	
	List itemSubClassificationList;
	List rfNoList;

	private String productNo;
	private String itemNo;
	

	Ingredient sangkap;
	Ingredient returnSlipSearchItem;

	List<Ingredient> ingredients;
	List<Ingredient> returnSlipItems;
	
	String pcItr;
	String qtyItr;
	String descItr;
	String uomItr;
	String sppItr;
	String appItr;
	
	String classification;

	double standardTotalOnTable;
	double actualTotalsOnTable;

	private boolean otherUOMSelected;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
		//END 2013 - PHASE 3 : PROJECT 1: MARK 

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String newInventoryEntry() {
		
		Session session = getSession();
		try {
		if (getSubModule().equalsIgnoreCase("fpts")) {
			rfNoList = inventoryManager.listAlphabeticalAscByParameter(RequisitionForm.class, "requisitionNo", session);
		/*	fpts = new FPTS();
			fpts.setFptsNo(rch.getPrefix(
					SASConstants.INVENTORY_FPTS, SASConstants.INVENTORY_FPTS_PREFIX)); */
			return "fpts";
		 }else if (getSubModule().equalsIgnoreCase("rf")) {
				
			/*	rf = new RequisitionForm();
				rf.setRequisitionNo(rch.getPrefix(
						SASConstants.INVENTORY_REQUISITION_FORM, SASConstants.INVENTORY_RF_PREFIX)); */
				return "rf";
		}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
			
		/*	rs = new ReturnSlip();
			rs.setReturnSlipNo(rch.getPrefix(
					SASConstants.INVENTORY_RETURN_SLIP_FORM, SASConstants.INVENTORY_RETURN_SLIP_PREFIX)); */
			return "returnSlip";
		}else {
			return INPUT;
		}
		
		}catch (Exception e) {
			e.printStackTrace();
			if (getSubModule().equalsIgnoreCase("fpts")) {
				return "fpts";
			}else if (getSubModule().equalsIgnoreCase("rf")) {
				return "rf";
			}else {
				return "rs";
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
		
		try {
			boolean addResult = false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
			if (getSubModule().equalsIgnoreCase("rawMat")) {

				if (validateRawMat()) {
				} else {
					if (isExistingInAllItems(getRm().getItemCode())) {
						addActionMessage(SASConstants.EXISTS);
					} else {
						if (otherUOMSelected)
							if (lookupManager.addNewUOM(new UnitOfMeasurements(
									rm.getUnitOfMeasurement(), "GENERAL"),
									session)) {
								session = getSession();
								loadLookLists();
							}
						processItemPricing(session, rm);
						
						//ADDED FOR WAREHOUSE IMPLEMENTATION
						rm.getWarehouse().setItemCode(rm.getItemCode());
						rm.getWarehouse().setLocationCode(inventoryManager.getUser().getLocation());
						rm.setWarehouses(inventoryManager.populateNewWarehousesSet(new HashSet<Warehouse>(0) , rm.getWarehouse()));
						//END WAREHOUSE IMPLEMENTATION
						
						addResult = inventoryManager.addInventoryObject(rm, session);

						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionError(SASConstants.FAILED);
						}
					}
				}
				return "rawMat";
			} else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				
				if (validateTradedItems()) {
				} else {
					if (isExistingInAllItems(getTi()
							.getItemCode())) {
						addActionMessage(SASConstants.EXISTS);
					} else {
						if (otherUOMSelected)
							if (lookupManager.addNewUOM(new UnitOfMeasurements(
									ti.getUnitOfMeasurement(), "GENERAL"),
									session)) {
								session = getSession();
								loadLookLists();
							}
						processItemPricing(session, ti);
						addResult = inventoryManager.addInventoryObject(ti, session);

						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionError(SASConstants.FAILED);
						}
					}
				}
				return "tradedItems";
			} else if (getSubModule().equalsIgnoreCase("utensils")) {
				
				if (validateUtensils()) {
				} else {
				
					if (isExistingInAllItems(getU()
							.getItemCode())) {
						addActionMessage(SASConstants.EXISTS);
					} else {
						if (otherUOMSelected)
							if (lookupManager.addNewUOM(new UnitOfMeasurements(
									u.getUnitOfMeasurement(), "GENERAL"),
									session)) {
								session = getSession();
								loadLookLists();
							}
						processItemPricing(session, u);
						addResult = inventoryManager.addInventoryObject(u, session);

						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionError(SASConstants.FAILED);
						}
					}
				}
				return "utensils";
			} else if (getSubModule().equalsIgnoreCase("unlistedItems")) {
				
				if (isValidateUnlistedItems()) {
				} else {
				
					if (isExistingInAllItems(getUnl()
							.getItemCode())) {
						addActionMessage(SASConstants.EXISTS);
					} else {
						if (otherUOMSelected)
							if (lookupManager.addNewUOM(new UnitOfMeasurements(
									unl.getUom(), "GENERAL"),
									session)) {
								session = getSession();
								loadLookLists();
							}
						//processItemPricing(session, u);
						addResult = inventoryManager.addInventoryObject(unl, session);

						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionError(SASConstants.FAILED);
						}
					}
				}
				return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("ofcSup")) {
				return addOfficeSupplies();
						
			}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
				return addReturnSlip();
						
			}else if (getSubModule().equalsIgnoreCase("fpts")) {
				return addFPTS();
			}else if (getSubModule().equalsIgnoreCase("rf")) {
				return addRequisitionForm();
			}
			else {
				loadLookLists();
				if (validateFinGood()) {
					setIngredientListFromArray(false);
				} else {
					List<FinishedGood> finGoodList = null;
					finGoodList = inventoryManager.listInventoryByParameter(
							FinishedGood.class, "productCode", getFg()
									.getProductCode(), session);
					if (isExistingInAllItems(getFg()
									.getProductCode())) {
						addActionMessage(SASConstants.EXISTS);
					} else {
						if (pcItr == null) {
							ingredients = new ArrayList<Ingredient>();
						} else {
							setIngredientListFromArray(false);
						}
						// Method for item pricing computation for FG company
						// owned
						setFGCompanyOwnedPrice();
						// Instantiate Item Pricing
						processItemPricing(session, fg);

						listToSet();
						fg.setIngredients(inventoryManager.persistsIngredients(
								finalIngredients, session));
						if (otherUOMSelected) {
							if (lookupManager.addNewUOM(new UnitOfMeasurements(
									fg.getUnitOfMeasurement(), "GENERAL"),
									session)) {
								session = getSession();
								loadLookLists();
							}
						}
						fg.setClassification(classification);
						addResult = inventoryManager.addInventoryObject(fg, session);
						if (addResult == true) {
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay = "edit";
						} else {
							addActionError(SASConstants.FAILED);
						}
					}
				}
				loadLookLists();
				return "finGood";
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("rawMat")) {
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")) {
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("ofcSup")) {
				return "ofcSup";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems")) {
				return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("fpts")) {
				return "fpts";
			}else if (getSubModule().equalsIgnoreCase("rf")) {
				return "rf";
			}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
				return "returnSlip";
			}else {
				return "finGood";
			}

		} finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
	private boolean isExistingInAllItems(String itemCode) {
		
		List<Item> tempList = new ArrayList<Item>();
		tempList = getAllItemList(getSession());
					
		//use this for display
					for(Item item: tempList){
						if(item.getItemCode().equalsIgnoreCase(itemCode)){
							return true;
					}
				}
		return false;
	}

	private ArrayList<Item> getAllItemList(Session session) {
		Iterator iterator = null;
		
		List<RawMaterial> rawMatList =inventoryManager.listAlphabeticalAscByParameter(RawMaterial.class, "subClassification",session);
		List<TradedItem> tradedItemList =inventoryManager.listAlphabeticalAscByParameter(TradedItem.class, "subClassification",session);
		List<Utensils> utensilsList =inventoryManager.listAlphabeticalAscByParameter(Utensils.class, "subClassification",session);
		List<OfficeSupplies> ofcSupList =inventoryManager.listAlphabeticalAscByParameter(OfficeSupplies.class, "subClassification",session);
		List<FinishedGood> finList = inventoryManager.listAlphabeticalAscByParameter(FinishedGood.class, "subClassification", session);
		
		HashMap<String,ArrayList<Item>> subClassMap = new HashMap<String,ArrayList<Item>>();
		
		ArrayList<Item> tempList = new ArrayList<Item>();
		
		iterator = rawMatList.iterator();
		while(iterator.hasNext()) {
			RawMaterial rawMaterial = (RawMaterial) iterator.next();
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				Item item = new Item(rawMaterial.getItemCode(), rawMaterial.getDescription(), rawMaterial.getUnitOfMeasurement(),rawMaterial.getClassification(), rawMaterial.getSubClassification(),rawMaterial.getIsVattable());
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				item.setItemType("rawMat");
				tempList.add(item);
				
		}
		
		iterator = tradedItemList.iterator();
		while(iterator.hasNext()) {
			TradedItem tradedItem = (TradedItem) iterator.next();
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				Item item = new Item(tradedItem.getItemCode(), tradedItem.getDescription(), tradedItem.getUnitOfMeasurement(),tradedItem.getClassification(), tradedItem.getSubClassification(),tradedItem.getIsVattable());
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				item.setItemType("tradedItems");
				tempList.add(item);
		}
		iterator = utensilsList.iterator();
		while(iterator.hasNext()) {
			Utensils utensils = (Utensils) iterator.next();
				//START: 2013 - PHASE 3 : PROJECT 4: AZ
				Item item = new Item(utensils.getItemCode(), utensils.getDescription(), utensils.getUnitOfMeasurement(),utensils.getClassification(), utensils.getSubClassification(),utensils.getIsVattable());
				//END: 2013 - PHASE 3 : PROJECT 4: AZ
				item.setItemType("utensils");
				tempList.add(item);
		}
		iterator = ofcSupList.iterator();
		while(iterator.hasNext()) {
			OfficeSupplies ofcSup = (OfficeSupplies) iterator.next();
				//START: 2013 - PHASE 3 : PROJECT 4: AZ
				Item item = new Item(ofcSup.getItemCode(), ofcSup.getDescription(), ofcSup.getUnitOfMeasurement(),ofcSup.getClassification(), ofcSup.getSubClassification(),ofcSup.getIsVattable());
				//END: 2013 - PHASE 3 : PROJECT 4: AZ
				item.setItemType("ofcSup");
				tempList.add(item);
		}
		iterator = finList.iterator();
		while(iterator.hasNext()){
			FinishedGood finGood = (FinishedGood) iterator.next();
				Item item = new Item(finGood.getProductCode(), finGood.getDescription(), finGood.getUnitOfMeasurement(),finGood.getClassification(),finGood.getSubClassification(),finGood.getIsVattable());
				item.setItemType("finGood");
				tempList.add(item);
		}
		return tempList;
	}

	private String addOfficeSupplies(){
		Session session = getSession();
		boolean addResult = false;
		if (validateOfficeSupplies()) {
		} else {
			if (isExistingInAllItems(getOs()
					.getItemCode())) {
				addActionMessage(SASConstants.EXISTS);
			} else {
				if (otherUOMSelected)
					if (lookupManager.addNewUOM(new UnitOfMeasurements(
							os.getUnitOfMeasurement(), "GENERAL"),
							session)) {
						session = getSession();
						loadLookLists();
					}
				processItemPricing(session, os);
				addResult = inventoryManager.addInventoryObject(os, session);

				if (addResult == true) {
					addActionMessage(SASConstants.ADD_SUCCESS);
					forWhat = "true";
					forWhatDisplay = "edit";
				} else {
					addActionError(SASConstants.FAILED);
				}
			}
		}
		return "ofcSup";
	}
	

	private String addRequisitionForm() {
		Session session = getSession();
		boolean addResult = false;
		if (validateRF()) {
		} else {
			//START - 2013 - PHASE 3 : PROJECT 1: AZ
			transactionList = new ArrayList();
			Transaction transaction = new Transaction();
			transactionList.add(transaction);
			//END - 2013 - PHASE 3 : PROJECT 1: AZ
			rf.setRequisitionNo(rch.getPrefix(
					SASConstants.INVENTORY_REQUISITION_FORM, SASConstants.INVENTORY_RF_PREFIX));
			addResult = inventoryManager.addInventoryObject(rf,session);
						if (addResult == true) {
							rch.updateCount(SASConstants.RF, "add");
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay="edit";
						} else {
							addActionError(SASConstants.FAILED);
						}		
		}
		return "rf";
	}

	private boolean validateRF() 
		 {
			boolean errorFound = false;
			
			/*if ("".equals(rf.getRequisitionNo())) {
				addFieldError("rf.rfNo", "REQUIRED");
				errorFound = true;
			} else {
				if (rf.getRequisitionNo().length() > 45) {
					addFieldError("rf.rfNo", "Requisition No. too long");
					errorFound = true;
				}
			} */
			if (null == (rf.getRequisitionDate())) {
				 addActionMessage("REQUIRED: Transaction Date");
				errorFound = true;
			}
			if ("".equals(rf.getRequisitionType())) {
				addFieldError("rf.requisitionType", "REQUIRED");
				errorFound = true;
			}if ("".equals(rf.getRequisitionTo())) {
				addFieldError("rf.requisitionTo", "REQUIRED");
				errorFound = true;
			} 
			if ("".equals(rf.getRequisitionBy())) {
				addFieldError("rf.requisitionBy", "REQUIRED");
				errorFound = true;
			} 
				return errorFound;
			}
	

	private String addFPTS() {
		Session session = getSession();
		boolean addResult = false;
		
		rfNoList = inventoryManager.listAlphabeticalAscByParameter(RequisitionForm.class, "requisitionNo", session);
		if (validateFPTS()) {
		} else {
			List rfList = null;
			rfList = inventoryManager.listInventoryByParameter(RequisitionForm.class, "requisitionNo", getFpts().getRequisitionForm().getRequisitionNo(), session);
			fpts.setRequisitionForm((RequisitionForm) rfList.get(0));
			fpts.setPurchaseOrderDetailsReceived(fpts.getRequisitionForm().getPurchaseOrderDetailsOrdered());

			poDetailsHelperToCompare.generatePODetailsListFromSet(fpts.getRequisitionForm().getPurchaseOrderDetailsOrdered());
			poDetailsHelperToCompare.generateCommaDelimitedValues();
			poDetailsHelperToCompare.setOrderDate(fpts.getTransactionDate());
			Set<PurchaseOrderDetails> podetailSet = poDetailsHelperToCompare
					.persistNewSetElements(session);
			fpts.setPurchaseOrderDetailsReceived(podetailSet);
			//START - 2013 - PHASE 3 : PROJECT 1: AZ
			transactionList = new ArrayList();
			Transaction transaction = new Transaction();
			transactionList.add(transaction);
			//END - 2013 - PHASE 3 : PROJECT 1: AZ
			fpts.setFptsNo(rch.getPrefix(
					SASConstants.INVENTORY_FPTS, SASConstants.INVENTORY_FPTS_PREFIX));
			addResult = inventoryManager.addInventoryObject(fpts,session);
						if (addResult == true) {
							rch.updateCount(SASConstants.FPTS, "add");
							addActionMessage(SASConstants.ADD_SUCCESS);
							forWhat = "true";
							forWhatDisplay="edit";
						} else {
							addActionError(SASConstants.FAILED);
						}
		}
		return "fpts";
	}

	private String addReturnSlip() {
		//Assign returnslipto from returnSlipToValue
		rs.setReturnSlipTo(returnSlipToValue);
		if (validateReturnSlip()) {
		}else {
			boolean addResult = false;
			Session session = getSession();
			
			poDetailsHelperToCompare.prepareSetAndList();
			//2014 - ITEM COLORING
			poDetailsHelperToCompare.generateItemTypesForExistingItems(session);
			poDetailsHelperDraft.setOrderDate(rs.getReturnDate());
			rs.setPurchaseOrderDetails(poDetailsHelperDraft.persistNewSetElements(session));
			poDetailsHelperDraft.generatePODetailsListFromSet(rs.getPurchaseOrderDetails());
			inventoryManager.persistMemo(rs.getMemo(),session);
			//update inventory record count
			updateInventory(rs.getPurchaseOrderDetails());
			
			//START - 2013 - PHASE 3 : PROJECT 1: AZ
			transactionList = new ArrayList();
			Transaction transaction = new Transaction();
			transactionList.add(transaction);
			//END - 2013 - PHASE 3 : PROJECT 1: AZ
			rs.setReturnSlipNo(rch.getPrefix(
					SASConstants.INVENTORY_RETURN_SLIP_FORM, SASConstants.INVENTORY_RETURN_SLIP_PREFIX));
			addResult = inventoryManager.addInventoryObject(rs,session);
			if (addResult == true) {
				rch.updateCount(SASConstants.RETURNSLIP,"add");
				addActionMessage(SASConstants.ADD_SUCCESS);
				forWhat = "true";
				forWhatDisplay="edit";
			} else {
				addActionError(SASConstants.FAILED);
			}
			itemCodeList = new ArrayList();
			Iterator itr = poDetailsHelperDraft.getPurchaseOrderDetailsList().iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr.next();
				itemCodeList.add(tempDetails.getItemCode());
			}
		}
		return "returnSlip";
	}
	
	private boolean validateReturnSlip() {
		
		boolean errorFound = false;
		/*if ("".equals(rs.getReturnSlipNo())) {
			addFieldError("rs.returnSlipNo", "REQUIRED");
			errorFound = true;
		} else {
			if (rs.getReturnSlipNo().length() > 45) {
				addFieldError("rs.returnSlipNo", "Return Slip No. too long");
				errorFound = true;
			}
		} */
		if (null == (rs.getReturnDate())) {
			 addActionMessage("REQUIRED: Return Date");
			errorFound = true;
		
		}
		if ("".equals(rs.getReturnSlipFrom())) {
			addFieldError("rs.returnSlipFrom", "REQUIRED");
			errorFound = true;
		} 
		if ("".equals(rs.getReturnSlipReferenceOrderNo())) {
			addFieldError("rs.returnSlipReferenceOrderNo", "REQUIRED");
			errorFound = true;
		} 
			return errorFound;
		
	}

	private void updateInventory(
			Set<PurchaseOrderDetails> purchaseOrderDetails) {
			Iterator itr = purchaseOrderDetails.iterator();
			Session ss = getSession();
			while(itr.hasNext()) {
				PurchaseOrderDetails poDetails =  (PurchaseOrderDetails)itr.next();
				updateInventoryItems(poDetails, ss);
			}
			
			
	}

	private void updateInventoryItems(PurchaseOrderDetails poDetails,Session session) {
		inventoryManager.updateInventoryItemRecordCountFromOrder(inventoryManager.setQinAndQoutBasedOnItemType(poDetails),session);
		inventoryManager.commitChanges(session);
/*		
			if(rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_CUSTOMER_TO_WAREHOUSE) || rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_PRODUCTION_TO_WAREHOUSE)){
					inventoryManager.addInventoryItem(inventoryManager.determineItemTypeFromPoDetails(poDetails),session);
					inventoryManager.commitChanges(session);
			}else if(rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_WAREHOUSE_TO_SUPPLIER)||rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_WAREHOUSE_TO_PRODUCTION)) {
					inventoryManager.deductInventoryItem(inventoryManager.determineItemTypeFromPoDetails(poDetails),session);
					inventoryManager.commitChanges(session);
			}else {
			}
			*/
			
	}

	@Deprecated
	private boolean isReturnSlipTransactionValid() {
		if(isFromSupplier(rs.getReturnSlipFrom()) && isFromCustomer(rs.getReturnSlipTo())) {
			return true;
		}else if(isFromCustomer(rs.getReturnSlipFrom()) && isFromSupplier(rs.getReturnSlipTo())) {
			return true;
		}else {
			return false;
		}
			
	}

	@Deprecated
	private boolean isFromSupplier(String id) {
		SupplierManager supplierManager = new SupplierManager();
		Supplier supplier = supplierManager.loadSupplier(id);
		if(supplier==null) {
			return false;
		}else {
			return true;
		}
	}
	@Deprecated
	private boolean isFromCustomer(String id) {
		CustomerManager customerManager = new CustomerManager();
		Customer customer = customerManager.loadCustomer(id);
		if(customer==null) {
			return false;
		}else {
			return true;
		}
	}

	private void processItemPricing(Session session, Object obj) {

		ItemPricing itemPricing = new ItemPricing();
		String itemCode = "";
		String itemType = "";

		if (obj instanceof RawMaterial) {
			itemCode = rm.getItemCode();
			itemType = SASConstants.RAW_MATERIAL_ABBR;
		} else if (obj instanceof FinishedGood) {
			itemCode = fg.getProductCode();
			itemType = SASConstants.FINISHED_GOOD_ABBR;
		} else if (obj instanceof TradedItem) {
			itemCode = ti.getItemCode();
			itemType = SASConstants.TRADED_ITEM_ABBR;
		}else if (obj instanceof Utensils) {
			itemCode = u.getItemCode();
			itemType = SASConstants.UTENSILS_ABBR;
		}else if (obj instanceof OfficeSupplies) {
			itemCode = os.getItemCode();
			itemType = SASConstants.OFFICE_SUPPLIES_ABBR;
		}
	
			if (obj instanceof RawMaterial) {
				itemPricing = rm.getItemPricing();
			} else if (obj instanceof FinishedGood) {
				itemPricing = fg.getItemPricing();
			}else if (obj instanceof TradedItem) {
				itemPricing = ti.getItemPricing();
			}else if (obj instanceof Utensils) {
				itemPricing = u.getItemPricing();
			}else if (obj instanceof OfficeSupplies) {
				itemPricing = os.getItemPricing();
			}
			
			itemPricing.setItemCode(itemCode);
			itemPricing.setItemType(itemType);
			inventoryManager.addPersistingInventoryObject(itemPricing, session);

		if (obj instanceof RawMaterial) {
			rm.setItemPricing(itemPricing);
		}else if (obj instanceof FinishedGood) {
			fg.setItemPricing(itemPricing);
		}else if (obj instanceof TradedItem) {
			ti.setItemPricing(itemPricing);
		}else if (obj instanceof Utensils) {
			u.setItemPricing(itemPricing);
		}else if (obj instanceof OfficeSupplies) {
			os.setItemPricing(itemPricing);
		}
	}

	private void setFGCompanyOwnedPrice() {
		ItemPricing itemPricing = fg.getItemPricing();

		if (fg.getYields() == 0) {
			itemPricing.setCompanyOwnedStandardPricePerUnit(0.0);
			itemPricing.setCompanyOwnedActualPricePerUnit(0.0);
			itemPricing.setCompanyOwnedTransferPricePerUnit(0.0);
		} else {
			itemPricing.setCompanyOwnedStandardPricePerUnit(Double.valueOf(fg
					.getStandardTotalCost() / fg.getYields()));
			itemPricing.setCompanyOwnedActualPricePerUnit(fg
					.getActualTotalCost() / fg.getYields());

			// Transfer Price Computation
			Double markup = fg.getMarkUp() / 100;
			double partialTransferPricePerunit = Double.valueOf((fg
					.getActualTotalCost() / fg.getYields())
					* (markup == 0.0 ? 1 : markup));
			if (markup == 0) {
				itemPricing.setCompanyOwnedTransferPricePerUnit(Double
						.valueOf(partialTransferPricePerunit));
			} else {
				itemPricing.setCompanyOwnedTransferPricePerUnit(Double
						.valueOf(partialTransferPricePerunit
								+ (fg.getActualTotalCost() / fg.getYields())));
			}
		}
	}

	private Set finalIngredients;

	private void listToSet() {
		Iterator listitr = ingredients.iterator();
		finalIngredients = new HashSet();
		while (listitr.hasNext()) {
			finalIngredients.add((Ingredient) listitr.next());
		}
	}

	private boolean checkIfExist(String productCode) {
		if (pcItr != null) {
			StringTokenizer pcItrTk = new StringTokenizer(pcItr, ",");
			while (pcItrTk.hasMoreElements()) {
				String toEval = ((String) pcItrTk.nextElement()).trim();
				if (productCode.equalsIgnoreCase(toEval)) {
					return true;
				}
			}
		}
		return false;
	}

	private void setIngredientListFromArray(boolean includeNewItem) {
		boolean forFinGood=false;
		if(subModule.equalsIgnoreCase("returnSlip")) {
		}else {
			forFinGood=true;
		}
		
		StringTokenizer pcItrTk = new StringTokenizer(pcItr, ",");
		StringTokenizer qtyItrTk = new StringTokenizer(qtyItr, ",");
		StringTokenizer descItrTk = new StringTokenizer(descItr, ",");
		StringTokenizer uomItrTk = new StringTokenizer(uomItr, ",");
		ingredients = new ArrayList<Ingredient>();
		DoubleConverter dc = new DoubleConverter();
		Session session = getSession();

		try {
			while (pcItrTk.hasMoreElements()) {

				Ingredient in = new Ingredient();
				in.setProductCode(((String) pcItrTk.nextElement()).trim());
				in.setQuantity(Double.valueOf(dc
						.convertFromStringNoContext(((String) qtyItrTk
								.nextElement()).trim())));
				in.setDescription(((String) descItrTk.nextElement()).trim());
				in.setUnitOfMeasurement(((String) uomItrTk.nextElement())
						.trim());
				in = inventoryManager.loadIngredientPrices(in, session);
				ingredients.add(in);
			}
			if(forFinGood) {
				if (includeNewItem) {
					addEntryToIngredients(sangkap);
				}
			}else {
				if (includeNewItem) {
					addEntryToReturnSlipItems(sangkap);
				}
				returnSlipItems = ingredients;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
	private void setReturnedItemsList(boolean includeNewItem) {
		
		StringTokenizer pcItrTk = new StringTokenizer(pcItr, ",");
		StringTokenizer qtyItrTk = new StringTokenizer(qtyItr, ",");
		StringTokenizer descItrTk = new StringTokenizer(descItr, ",");
		StringTokenizer uomItrTk = new StringTokenizer(uomItr, ",");
		returnSlipItems = new ArrayList<Ingredient>();
		DoubleConverter dc = new DoubleConverter();
		Session session = getSession();

		try {
			while (pcItrTk.hasMoreElements()) {

				Ingredient in = new Ingredient();
				in.setProductCode(((String) pcItrTk.nextElement()).trim());
				in.setQuantity(Double.valueOf(dc
						.convertFromStringNoContext(((String) qtyItrTk
								.nextElement()).trim())));
				in.setDescription(((String) descItrTk.nextElement()).trim());
				in.setUnitOfMeasurement(((String) uomItrTk.nextElement())
						.trim());
				in = inventoryManager.loadIngredientPrices(in, session);
				returnSlipItems.add(in);
			}
				if (includeNewItem) {
					addEntryToReturnSlipItems(returnSlipSearchItem);
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	private void addEntryToIngredients(Ingredient in) {
		in.setStandardPricePerUnit(in.getStandardPricePerUnit()
				* in.getQuantity());
		in.setActualPricePerUnit(in.getActualPricePerUnit() * in.getQuantity());
		in.setTransferPricePerUnit(in.getTransferPricePerUnit()
				* in.getQuantity());
		ingredients.add(in);
		fg.setStandardTotalCost(fg.getStandardTotalCost()
				+ in.getStandardPricePerUnit());
		fg.setActualTotalCost(fg.getActualTotalCost()
				+ in.getActualPricePerUnit());
		fg.setTransferTotalCost(fg.getTransferTotalCost()
				+ in.getTransferPricePerUnit());
	}

	List itemCodeList;

	List UOMList;
	
	List supplierAndCustomerList;

	private List generateUOMStrings(List uomList) {
		List uomStringsList = new ArrayList();
		Iterator iterator = uomList.iterator();
		while (iterator.hasNext()) {
			String uom = ((UnitOfMeasurements) iterator.next()).getValue();
			uomStringsList.add(uom);
		}
		return uomStringsList;
	}

	private String requestingModule;

	

	public String loadLookListsInRawMat() {
		Session session = getSession();
		UOMList = generateUOMStrings(lookupManager.getLookupElements(
				UnitOfMeasurements.class, "GENERAL", session));
		rm = new RawMaterial();
		forWhatDisplay = "new";
		return "rawMat";
	}
	
	public String loadLookListsInTadedItems() {
		Session session = getSession();
		
	
		UOMList = generateUOMStrings(lookupManager.getLookupElements(
				UnitOfMeasurements.class, "GENERAL", session));
		ti = new TradedItem();
		forWhatDisplay = "new";
		return "tradedItems";
	}
	public String loadLookListsInUtensils() {
		Session session = getSession();
		
	
		UOMList = generateUOMStrings(lookupManager.getLookupElements(
				UnitOfMeasurements.class, "GENERAL", session));
		u = new Utensils();
		forWhatDisplay = "new";
		return "utensils";
	}
	public String loadLookListsInOfficeSupplies() {
		Session session = getSession();
			UOMList = generateUOMStrings(lookupManager.getLookupElements(
					UnitOfMeasurements.class, "GENERAL", session));
			os = new OfficeSupplies();
			forWhatDisplay = "new";
		return "ofcSup";
	}
	public String loadLookListsInUnlistedItems() {
		Session session = getSession();
		
		UOMList = generateUOMStrings(lookupManager.getLookupElements(
				UnitOfMeasurements.class, "GENERAL", session));
		unl = new UnlistedItem();
		forWhatDisplay = "new";
		return "unlistedItems";
	}
	


	public String loadLookLists() {
		Session session = getSession();
		try {
//			supplierAndCustomerList = generateCustomerAndSupplierList(session);
			UOMList = generateUOMStrings(lookupManager.getLookupElements(
					UnitOfMeasurements.class, "GENERAL", session));
			if(requestingModule.equalsIgnoreCase("returnSlip")) {
				
			}else {
				itemCodeList = inventoryManager.loadItemListFromRawAndFin(session);
			}
			
		} catch (Exception e) {
			if (getSubModule().equalsIgnoreCase("rawMat") || getRequestingModule().equalsIgnoreCase("rawMaterial")) {
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems") || getRequestingModule().equalsIgnoreCase("tradedItems")) {
					return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils") || getRequestingModule().equalsIgnoreCase("utensils")) {
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("ofcSup") || getRequestingModule().equalsIgnoreCase("ofcSup")) {
				return "ofcSup";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems") || getRequestingModule().equalsIgnoreCase("unlistedItems")) {
					return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("returnSlip") || getRequestingModule().equalsIgnoreCase("returnSlip")) {
				return "returnSlip";
			}else if (getSubModule().equalsIgnoreCase("finishedGood") || getRequestingModule().equalsIgnoreCase("finishedGood")){
				return "finGood";
			}else if (getSubModule().equalsIgnoreCase("fpts") || getRequestingModule().equalsIgnoreCase("fpts")){
				return "fpts";
			}else {
				return "rf";
			}
		} finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
		if (requestingModule == null) {
			if (subModule == null) {
				setRequestingModule("");
			} else {
				setRequestingModule(subModule);
			}
		}

		if (requestingModule != null
				&& requestingModule.equalsIgnoreCase("rawMaterial")) {
			return "rawMat";
		}else if (requestingModule != null
				&& requestingModule.equalsIgnoreCase("tradedItems")) {
			return "tradedItems";
		}else if (requestingModule != null
				&& requestingModule.equalsIgnoreCase("utensils")) {
			return "utensils";
		}else if (requestingModule != null
				&& requestingModule.equalsIgnoreCase("ofcSup")) {
			return "ofcSup";
		} else if (requestingModule != null
				&& requestingModule.equalsIgnoreCase("unlistedItems")) {
			return "unlistedItems";
		} else if (requestingModule != null
				&& requestingModule.equalsIgnoreCase("returnSlip")) {
			return "returnSlip";
		} else if (requestingModule != null
				&& requestingModule.equalsIgnoreCase("rf")) {
			return "rf";
		}else {
			return "finGood";
		}
	}

	public String loadItemSubClassificationList() {
		Session session = getSession();
		loadLookLists();
		
		itemSubClassificationList = null;
		itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
				getClassification(), session);
		
		if (getSubModule().equals("tradedItems")) {
			setItemNo(itemNo);
			
			if (!itemNo.equals("")){
				ti.setItemCode(itemNo);
			}else{
				ti.setItemCode(ti.getItemCode());
			}
			return "tradedItems";
		}else if (getSubModule().equals("utensils")) {
			setItemNo(itemNo);
			
			if (!itemNo.equals("")){
				u.setItemCode(itemNo);
			}else{
				u.setItemCode(u.getItemCode());
			}
			return "utensils";
		}else if (getSubModule().equals("ofcSup")) {
			setItemNo(itemNo);
			
			if (!itemNo.equals("")){
				os.setItemCode(itemNo);
			}else{
				os.setItemCode(os.getItemCode());
			}
			return "ofcSup";
		}else if (getSubModule().equals("unlistedItems")) {
			setItemNo(itemNo);
			
			if (!itemNo.equals("")){
				unl.setItemCode(itemNo);
			}else{
				unl.setItemCode(unl.getItemCode());
			}
			return "unlistedItems";
		}else if (getSubModule().equals("rawMat")) {
			setItemNo(itemNo);
			
			if (!itemNo.equals("")){
				rm.setItemCode(itemNo);
			}else{
				rm.setItemCode(rm.getItemCode());
			}
			return "rawMat";
		}else {
							
			if (!productNo.equals("")){
				fg.setProductCode(productNo);
			}else{
				fg.setProductCode(fg.getProductCode());
			}
			
			try {
				loadIngredientDetails();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		forWhat= "false";
		return "finGood";
		}
	}
	private List generateCustomerAndSupplierList(Session session) {

		

		List supplierNoList = supplierManager.listAlphabeticalAscByParameter(Supplier.class, "supplierId", session);
		List customerNoList = customerManager.listAlphabeticalAscByParameter(Customer.class, "customerNo", session);
		List masterList = new ArrayList<String>();
		Iterator<Supplier> supItr = supplierNoList.iterator();
		while(supItr.hasNext()) {
			Supplier supplier = (Supplier) supItr.next();
			masterList.add(supplier.getSupplierId());
		}
		Iterator<Customer> cusItr = customerNoList.iterator();
		while(cusItr.hasNext()) {
			Customer customer = (Customer) cusItr.next();
			masterList.add(customer.getCustomerNo());
		}
		return masterList;
	}

	public String newFinishedGood() throws Exception {
		loadLookLists();
		
		if (getSubModule().equalsIgnoreCase("rawMat")) {
			return "rawMat";
		}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
			return "tradedItems";
		}else if (getSubModule().equalsIgnoreCase("utensils")) {
			return "utensils";
		}else if (getSubModule().equalsIgnoreCase("ofcSup")) {
			return "ofcSup";
		}else {
			return "finGood";
		}
	}

	public String loadIngredientDetails() throws Exception {
		loadLookLists();
		if (productNo == null) {
			fg.setProductCode(fg.getProductCode());
		} else {
			if (productNo.equals("")){
				fg.setProductCode(fg.getProductCode());
			}else{
			fg.setProductCode(productNo);
			}
		}
		if (pcItr != null) {
			setIngredientListFromArray(false);
		}
		Session session = getSession();
		try {
			//fg.setProductCode(productNo);
			try {
			RawMaterial item = (RawMaterial) (inventoryManager.listInventoryByParameter(
					RawMaterial.class, "itemCode", sangkap.getProductCode(),
					session).get(0));
			sangkap = new Ingredient(item.getItemCode(), item.getDescription(),
					0, item.getUnitOfMeasurement(), item.getItemPricing()
							.getCompanyOwnedStandardPricePerUnit(), item
							.getItemPricing()
							.getCompanyOwnedActualPricePerUnit(), item
							.getItemPricing()
							.getCompanyOwnedTransferPricePerUnit());
			}catch(IndexOutOfBoundsException exception) {
				TradedItem item = (TradedItem) (inventoryManager.listInventoryByParameter(
						TradedItem.class, "itemCode", sangkap.getProductCode(),
						session).get(0)); 
				sangkap = new Ingredient(item.getItemCode(), item.getDescription(),
						0, item.getUnitOfMeasurement(), item.getItemPricing()
								.getCompanyOwnedStandardPricePerUnit(), item
								.getItemPricing()
								.getCompanyOwnedActualPricePerUnit(), item
								.getItemPricing()
								.getCompanyOwnedTransferPricePerUnit());
			}
		} catch (IndexOutOfBoundsException iobe) {
			FinishedGood itemFinGood = (FinishedGood) (inventoryManager
					.listInventoryByParameter(FinishedGood.class,
							"productCode", sangkap.getProductCode(), session)
					.get(0));
			sangkap = new Ingredient(itemFinGood.getProductCode(),
					itemFinGood.getDescription(), 0,
					itemFinGood.getUnitOfMeasurement(), itemFinGood
							.getItemPricing()
							.getCompanyOwnedStandardPricePerUnit(), itemFinGood
							.getItemPricing()
							.getCompanyOwnedActualPricePerUnit(), itemFinGood
							.getItemPricing()
							.getCompanyOwnedTransferPricePerUnit());
		} catch (Exception e) {
			return "finGood";
		} finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
		return SUCCESS;
	}

	public String addIngredient() {
		loadLookLists();

		if (!productNo.equalsIgnoreCase("")) {
			fg.setProductCode(productNo);
		}

		if (validateIngredient()) {
			if (pcItr != null)
				setIngredientListFromArray(false);
			return INPUT;
		} else {
			// walang validation error sa ingredients list then look for
			// rawmaterial details
			fg.setProductCode(productNo);
			if (pcItr == null) {
				// first entry
				ingredients = new ArrayList<Ingredient>();
				addEntryToIngredients(sangkap);
			} else {
				// succeeding entries
				setIngredientListFromArray(true);
			}
		}

		return SUCCESS;
	}

	public String deleteIngredient() {
		loadLookLists();
		if (productNo != null) {
			fg.setProductCode(productNo);
		}
		if ("".equals(sangkap.getProductCode())) {
			addActionError("Unable to delete w/o identifying the product code");
		} else {
			if (pcItr == null) {
				addActionError("nothing to delete here");
			} else {
				removeIngredient(sangkap.getProductCode());
			}
		}
		return SUCCESS;

	}

	private void removeIngredient(String productCode) {
		StringTokenizer pcItrTk = new StringTokenizer(pcItr, ",");
		StringTokenizer qtyItrTk = new StringTokenizer(qtyItr, ",");
		StringTokenizer descItrTk = new StringTokenizer(descItr, ",");
		StringTokenizer uomItrTk = new StringTokenizer(uomItr, ",");
		ingredients = new ArrayList<Ingredient>();
		Session session = getSession();

		DoubleConverter dc = new DoubleConverter();

		try {
			while (pcItrTk.hasMoreElements()) {
				Ingredient in = new Ingredient();
				String toEval = ((String) pcItrTk.nextElement()).trim();
				in.setProductCode(toEval);
				in.setQuantity(Double.valueOf(dc
						.convertFromStringNoContext(((String) qtyItrTk
								.nextElement()).trim())));
				in.setDescription(((String) descItrTk.nextElement()).trim());
				in.setUnitOfMeasurement(((String) uomItrTk.nextElement())
						.trim());
				in = inventoryManager.loadIngredientPrices(in, session);

				if (productCode.equalsIgnoreCase(toEval)) {

					if (fg != null) {
						fg.setStandardTotalCost(fg.getStandardTotalCost()
								- in.getStandardPricePerUnit());
						fg.setActualTotalCost(fg.getActualTotalCost()
								- in.getActualPricePerUnit());
					}
					continue;
				}

				ingredients.add(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	private boolean validateIngredient() {
		boolean errorFound = false;
		if (null == sangkap) {
			errorFound = true;
		} else {
			if ("".equals(sangkap.getProductCode())) {
				addActionError("unable to add an ingredient without a product code");
				errorFound = true;
			} else {
				if (checkIfExist(sangkap.getProductCode())) {
					addActionError("ingredient already exist, remove from the list first to re-enter");
					errorFound = true;
				}
			}
		}
		return errorFound;
	}

	private boolean validateRawMat() {
		loadLookLists();
		boolean errorFound = false;
		if ("".equals(getRm().getItemCode())) {
			addFieldError("rm.itemCode", "REQUIRED");
			errorFound = true;
		} else {
			if (getRm().getItemCode().length() > 45) {
				addFieldError("rm.itemCode", "item code too long");
				errorFound = true;
			}
		}
		if ("".equals(getRm().getDescription())) {
			addFieldError("rm.description", "REQUIRED");
			errorFound = true;
		} else {
			if (getRm().getDescription().trim().length() > 200) {
				addFieldError("rm.description",
						"MAXIMUM LENGTH: 200 characters");
			}
		}
		if (rm.getUnitOfMeasurement() == null) {
		}

		else {
			if (rm.getUnitOfMeasurement().indexOf(", ") > -1) {
				if ("".equals(rm.getUnitOfMeasurement().substring(
						rm.getUnitOfMeasurement().indexOf(", ")))) {
					addFieldError("rm.unitOfMeasurement", "REQUIRED");
					errorFound = true;
				} else {
					otherUOMSelected = true;
					rm.setUnitOfMeasurement(rm
							.getUnitOfMeasurement()
							.substring(
									rm.getUnitOfMeasurement().indexOf(", ") + 2));
				}
			}
		}
		if (rm.getUnitOfMeasurement().equalsIgnoreCase("OTHERS")) {
			addFieldError("rm.unitOfMeasurementText", "REQUIRED");
		}

		return errorFound;
	}

	private boolean validateFinGood() {
		loadLookLists();
		boolean errorFound = false;
		if ("".equals(getFg().getProductCode())) {
			addFieldError("fg.productCode", "REQUIRED");
			errorFound = true;
		}
		if ("".equals(getFg().getDescription())) {
			addFieldError("fg.description", "REQUIRED");
			errorFound = true;
		} else if (getFg().getDescription().trim().length() > 200) {
			addFieldError("fg.description", "MAXIMUM LENGTH: 200 characters");
		}
		if (fg.getUnitOfMeasurement() == null) {
		} else {
			if (fg.getUnitOfMeasurement().indexOf(", ") > -1) {
				if ("".equals(fg.getUnitOfMeasurement().substring(
						fg.getUnitOfMeasurement().indexOf(", ")))) {
					addFieldError("fg.unitOfMeasurement", "REQUIRED");
					errorFound = true;
				} else {
					otherUOMSelected = true;
					fg.setUnitOfMeasurement(fg
							.getUnitOfMeasurement()
							.substring(
									fg.getUnitOfMeasurement().indexOf(", ") + 2));
				}
			}
		}
		if (fg.getUnitOfMeasurement().equalsIgnoreCase("OTHERS")) {
			addFieldError("fg.unitOfMeasurementText", "REQUIRED");
		}
		

		return errorFound;
	}

	private boolean validateTradedItems() {
		loadLookLists();
		boolean errorFound = false;
		if ("".equals(getTi().getItemCode())) {
			addFieldError("ti.itemCode", "REQUIRED");
			errorFound = true;
		} else {
			if (getTi().getItemCode().length() > 45) {
				addFieldError("ti.itemCode", "item code too long");
				errorFound = true;
			}
		}
		if ("".equals(getTi().getDescription())) {
			addFieldError("ti.description", "REQUIRED");
			errorFound = true;
		} else {
			if (getTi().getDescription().trim().length() > 200) {
				addFieldError("ti.description",
						"MAXIMUM LENGTH: 200 characters");
			}
		}
		if (ti.getUnitOfMeasurement() == null) {
		}

		else {
			if (ti.getUnitOfMeasurement().indexOf(", ") > -1) {
				if ("".equals(ti.getUnitOfMeasurement().substring(
						ti.getUnitOfMeasurement().indexOf(", ")))) {
					addFieldError("ti.unitOfMeasurement", "REQUIRED");
					errorFound = true;
				} else {
					otherUOMSelected = true;
					ti.setUnitOfMeasurement(ti
							.getUnitOfMeasurement()
							.substring(
									ti.getUnitOfMeasurement().indexOf(", ") + 2));
				}
			}
		}
		if (ti.getUnitOfMeasurement().equalsIgnoreCase("OTHERS")) {
			addFieldError("ti.unitOfMeasurementText", "REQUIRED");
		}

		return errorFound;
	}
	
	private boolean validateUtensils() {
		loadLookLists();
		boolean errorFound = false;
		if ("".equals(getU().getItemCode())) {
			addFieldError("u.itemCode", "REQUIRED");
			errorFound = true;
		} else {
			if (getU().getItemCode().length() > 45) {
				addFieldError("u.itemCode", "item code too long");
				errorFound = true;
			}
		}
		if ("".equals(getU().getDescription())) {
			addFieldError("u.description", "REQUIRED");
			errorFound = true;
		} else {
			if (getU().getDescription().trim().length() > 200) {
				addFieldError("u.description",
						"MAXIMUM LENGTH: 200 characters");
			}
		}
		if (u.getUnitOfMeasurement() == null) {
		}

		else {
			if (u.getUnitOfMeasurement().indexOf(", ") > -1) {
				if ("".equals(u.getUnitOfMeasurement().substring(
						u.getUnitOfMeasurement().indexOf(", ")))) {
					addFieldError("u.unitOfMeasurement", "REQUIRED");
					errorFound = true;
				} else {
					otherUOMSelected = true;
					u.setUnitOfMeasurement(u
							.getUnitOfMeasurement()
							.substring(
									u.getUnitOfMeasurement().indexOf(", ") + 2));
				}
			}
		}
		if (u.getUnitOfMeasurement().equalsIgnoreCase("OTHERS")) {
			addFieldError("u.unitOfMeasurementText", "REQUIRED");
		}

		return errorFound;
	}
	
	private boolean validateOfficeSupplies() {
		loadLookLists();
		boolean errorFound = false;
		if ("".equals(getOs().getItemCode())) {
			addFieldError("ofcSup.itemCode", "REQUIRED");
			errorFound = true;
		} else {
			if (getOs().getItemCode().length() > 45) {
				addFieldError("ofcSup.itemCode", "item code too long");
				errorFound = true;
			}
		}
		if ("".equals(getOs().getDescription())) {
			addFieldError("ofcSup.description", "REQUIRED");
			errorFound = true;
		} else {
			if (getOs().getDescription().trim().length() > 200) {
				addFieldError("ofcSup.description",
						"MAXIMUM LENGTH: 200 characters");
			}
		}
		if (os.getUnitOfMeasurement() == null) {
		}

		else {
			if (os.getUnitOfMeasurement().indexOf(", ") > -1) {
				if ("".equals(os.getUnitOfMeasurement().substring(
						os.getUnitOfMeasurement().indexOf(", ")))) {
					addFieldError("ofcSup.unitOfMeasurement", "REQUIRED");
					errorFound = true;
				} else {
					otherUOMSelected = true;
					os.setUnitOfMeasurement(os
							.getUnitOfMeasurement()
							.substring(
									os.getUnitOfMeasurement().indexOf(", ") + 2));
				}
			}
		}
		if (os.getUnitOfMeasurement().equalsIgnoreCase("OTHERS")) {
			addFieldError("os.unitOfMeasurementText", "REQUIRED");
		}
		return errorFound;
	}
	private boolean isValidateUnlistedItems() {
		loadLookLists();
		boolean errorFound = false;
		/*if ("".equals(getUnl().getItemCode())) {
			addFieldError("unl.itemCode", "REQUIRED");
			errorFound = true;
		} else {
			if (getUnl().getItemCode().length() > 45) {
				addFieldError("unl.itemCode", "item code too long");
				errorFound = true;
			}
		}*/
		if ("".equals(getUnl().getDescription())) {
			addFieldError("unl.description", "REQUIRED");
			errorFound = true;
		} else {
			if (getUnl().getDescription().trim().length() > 200) {
				addFieldError("unl.description",
						"MAXIMUM LENGTH: 200 characters");
			}
		}
		if (unl.getUom() == null) {
		}

		else {
			if (unl.getUom().indexOf(", ") > -1) {
				if ("".equals(unl.getUom().substring(
						unl.getUom().indexOf(", ")))) {
					addFieldError("unl.uom", "REQUIRED");
					errorFound = true;
				} else {
					otherUOMSelected = true;
					unl.setUom(unl
							.getUom()
							.substring(
									unl.getUom().indexOf(", ") + 2));
				}
			}
		}
		if (unl.getUom().equalsIgnoreCase("OTHERS")) {
			addFieldError("unl.unitOfMeasurementText", "REQUIRED");
		}

		return errorFound;
	}
	
	private boolean validateFPTS() {
		
	boolean errorFound = false;
	
	/*if ("".equals(fpts.getFptsNo())) {
		addFieldError("fpts.fptsNo", "REQUIRED");
		errorFound = true;
	} else {
		if (fpts.getFptsNo().length() > 45) {
			addFieldError("fpts.fptsNo", "FPTS No. too long");
			errorFound = true;
		}
	} */
	if (null == (fpts.getTransactionDate())) {
		 addActionMessage("REQUIRED: Transaction Date");
		errorFound = true;
	
	}
	if ("".equals(fpts.getFptsFrom())) {
		addFieldError("fpts.fptsFrom", "REQUIRED");
		errorFound = true;
	} 
	if ("".equals(fpts.getFptsTo())) {
		addFieldError("fpts.fptsTo", "REQUIRED");
		errorFound = true;
	} 
		return errorFound;
	}
	
	//return slip
	public String loadReturnSlipItem() throws Exception {
		loadLookLists();
		if (pcItr != null) {
			setReturnedItemsList(false);
		}
		Session session = getSession();
		
		//Assign returnslipto from returnSlipToValue
		rs.setReturnSlipTo(returnSlipToValue);
		
		try {
			RawMaterial item = (RawMaterial) (inventoryManager.listInventoryByParameter(
					RawMaterial.class, "itemCode", returnSlipSearchItem.getProductCode(),
					session).get(0));
			returnSlipSearchItem = new Ingredient(item.getItemCode(), item.getDescription(),
					0, item.getUnitOfMeasurement(), item.getItemPricing()
							.getCompanyOwnedStandardPricePerUnit(), item
							.getItemPricing()
							.getCompanyOwnedActualPricePerUnit(), item
							.getItemPricing()
							.getCompanyOwnedTransferPricePerUnit());
		} catch (IndexOutOfBoundsException iobe) {
			try{
				FinishedGood itemFinGood = (FinishedGood) (inventoryManager
					.listInventoryByParameter(FinishedGood.class,
							"productCode", returnSlipSearchItem.getProductCode(), session)
					.get(0));
					returnSlipSearchItem = new Ingredient(itemFinGood.getProductCode(),
					itemFinGood.getDescription(), 0,
					itemFinGood.getUnitOfMeasurement(), itemFinGood
							.getItemPricing()
							.getCompanyOwnedStandardPricePerUnit(), itemFinGood
							.getItemPricing()
							.getCompanyOwnedActualPricePerUnit(), itemFinGood
							.getItemPricing()
							.getCompanyOwnedTransferPricePerUnit());
			} catch (IndexOutOfBoundsException iobe2) {
				try{
					TradedItem tradedItem = (TradedItem) inventoryManager.listInventoryByParameter(TradedItem.class, "itemCode",
							returnSlipSearchItem.getProductCode(),session).get(0);
					
					returnSlipSearchItem = new Ingredient(tradedItem.getItemCode(), tradedItem.getDescription(),
							0, tradedItem.getUnitOfMeasurement(), tradedItem.getItemPricing()
									.getCompanyOwnedStandardPricePerUnit(), tradedItem
									.getItemPricing()
									.getCompanyOwnedActualPricePerUnit(), tradedItem
									.getItemPricing()
									.getCompanyOwnedTransferPricePerUnit());
				} catch (IndexOutOfBoundsException iobe3) {
					addActionError("ITEM NOT IN DATABASE");
					return ERROR;
				}
			}
		}finally {
			if (session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
		return SUCCESS;
	}
	
	public String addReturnSlipItem() {
		loadLookLists();

			if (pcItr != null)
				setReturnedItemsList(false);

			if (pcItr == null) {
				// first entry
				returnSlipItems = new ArrayList<Ingredient>();
				addEntryToReturnSlipItems(returnSlipSearchItem);
			} else {
				// succeeding entries
				setReturnedItemsList(true);
			}
		return SUCCESS;
	}

	private void addEntryToReturnSlipItems(Ingredient in) {
		in.setStandardPricePerUnit(in.getStandardPricePerUnit()
				* in.getQuantity());
		in.setActualPricePerUnit(in.getActualPricePerUnit() * in.getQuantity());
		in.setTransferPricePerUnit(in.getTransferPricePerUnit()
				* in.getQuantity());
		returnSlipItems.add(in);
	}
	public String deleteReturnSlipItem() {
		return deleteIngredient();
	}
	
	
	//GETTERS + SETTERS
	public List getUOMList() {
		return UOMList;
	}

	public void setUOMList(List uOMList) {
		UOMList = uOMList;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public RawMaterial getRm() {
		return rm;
	}

	public void setRm(RawMaterial rm) {
		this.rm = rm;
	}

	
	public TradedItem getTi() {
		return ti;
	}

	public void setTi(TradedItem ti) {
		this.ti = ti;
	}

	public FinishedGood getFg() {
		return fg;
	}

	public void setFg(FinishedGood fg) {
		this.fg = fg;
	}

	public Ingredient getSangkap() {
		return sangkap;
	}

	public void setSangkap(Ingredient ingredient) {
		this.sangkap = ingredient;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public String getPcItr() {
		return pcItr;
	}

	public void setPcItr(String pcItr) {
		this.pcItr = pcItr;
	}

	public String getDescItr() {
		return descItr;
	}

	public void setDescItr(String descItr) {
		this.descItr = descItr;
	}

	public String getUomItr() {
		return uomItr;
	}

	public void setUomItr(String uomItr) {
		this.uomItr = uomItr;
	}

	public String getSppItr() {
		return sppItr;
	}

	public void setSppItr(String sppItr) {
		this.sppItr = sppItr;
	}

	public String getAppItr() {
		return appItr;
	}

	public void setAppItr(String appItr) {
		this.appItr = appItr;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public List getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List itemCodeList) {
		this.itemCodeList = itemCodeList;
	}

	public String getQtyItr() {
		return qtyItr;
	}

	public void setQtyItr(String qtyItr) {
		this.qtyItr = qtyItr;
	}

	public double getStandardTotalOnTable() {
		return standardTotalOnTable;
	}

	public void setStandardTotalOnTable(double standardTotalOnTable) {
		this.standardTotalOnTable = standardTotalOnTable;
	}

	public double getActualTotalsOnTable() {
		return actualTotalsOnTable;
	}

	public void setActualTotalsOnTable(double actualTotalsOnTable) {
		this.actualTotalsOnTable = actualTotalsOnTable;
	}

	public String getForWhatDisplay() {
		return forWhatDisplay;
	}

	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}

	public String getRequestingModule() {
		return requestingModule;
	}

	public void setRequestingModule(String requestingModule) {
		this.requestingModule = requestingModule;
	}

	public FPTS getFpts() {
		return fpts;
	}

	public void setFpts(FPTS fpts) {
		this.fpts = fpts;
	}

	public RequisitionForm getRf() {
		return rf;
	}

	public void setRf(RequisitionForm rf) {
		this.rf = rf;
	}

	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}

	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}

	public List getSupplierAndCustomerList() {
		return supplierAndCustomerList;
	}

	public void setSupplierAndCustomerList(List supplierAndCustomerList) {
		this.supplierAndCustomerList = supplierAndCustomerList;
	}
	public Ingredient getReturnSlipSearchItem() {
		return returnSlipSearchItem;
	}

	public void setReturnSlipSearchItem(Ingredient returnSlipSearchItem) {
		this.returnSlipSearchItem = returnSlipSearchItem;
	}
	public List<Ingredient> getReturnSlipItems() {
		return returnSlipItems;
	}

	public void setReturnSlipItems(List<Ingredient> returnSlipItems) {
		this.returnSlipItems = returnSlipItems;
	}

	public PurchaseOrderDetailHelper getPoDetailsHelperDraft() {
		return poDetailsHelperDraft;
	}

	public void setPoDetailsHelperDraft(
			PurchaseOrderDetailHelper poDetailsHelperDraft) {
		this.poDetailsHelperDraft = poDetailsHelperDraft;
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
	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}

	public void setPoDetailsHelperToCompare(
			PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
	}

	public List getItemSubClassificationList() {
		return itemSubClassificationList;
	}

	public void setItemSubClassificationList(List itemSubClassificationList) {
		this.itemSubClassificationList = itemSubClassificationList;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public List getRfNoList() {
		return rfNoList;
	}

	public void setRfNoList(List rfNoList) {
		this.rfNoList = rfNoList;
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

		public Utensils getU() {
			return u;
		}

		public void setU(Utensils u) {
			this.u = u;
		}

		public UnlistedItem getUnl() {
			return unl;
		}

		public void setUnl(UnlistedItem unl) {
			this.unl = unl;
		}

		public OfficeSupplies getOs() {
			return os;
		}

		public void setOs(OfficeSupplies os) {
			this.os = os;
		}
		
		
}
