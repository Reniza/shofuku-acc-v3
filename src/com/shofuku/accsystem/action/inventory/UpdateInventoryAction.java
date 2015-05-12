package com.shofuku.accsystem.action.inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.ItemPricing;
import com.shofuku.accsystem.domain.inventory.OfficeSupplies;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.UnlistedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.lookups.InventoryClassification;
import com.shofuku.accsystem.domain.lookups.UnitOfMeasurements;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.DoubleConverter;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.SASConstants;


public class UpdateInventoryAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String forWhat;
	private String forWhatDisplay;
	private String productNo;
	private String itemNo;
	private String subModule;
	
	List<Ingredient> ingredients;
	String pcItr;
	String descItr;
	String qtyItr;
	String uomItr;
	String sppItr;
	String appItr;
	
	RawMaterial rm;
	FinishedGood fg;
	TradedItem ti;
	Utensils u;
	OfficeSupplies os;
	UnlistedItem unl;
	ReturnSlip rs;
	FPTS fpts;
	String fptsId;
	String fptsNo;
	
	RequisitionForm rf;
	String rfNo;
	String rfId;
	String rsIdNo;

	List itemSubClassificationList;
	String classification;
	String tempClassif;

	private boolean otherUOMSelected; 
	PurchaseOrderDetailHelper poDetailsHelperDraft;	

	
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	PurchaseOrderDetailHelper poDetailsHelper;
	//START 2013 - PHASE 3 : PROJECT 1: MARK
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
		AccountEntryProfileUtil apeUtil = new AccountEntryProfileUtil();
	//END 2013 - PHASE 3 : PROJECT 1: MARK  
	
	AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
	TransactionManager transactionMananger = (TransactionManager) actionSession.get("transactionMananger");
	InventoryManager inventoryManager=(InventoryManager) actionSession.get("inventoryManager");
	LookupManager lookupManager = (LookupManager) actionSession.get("lookupManager");
	
	InventoryUtil invUtil = new InventoryUtil();
	DateFormatHelper df = new DateFormatHelper();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
	Session session = getSession();
		
		try {
			forWhatDisplay="edit";
			boolean updateResult=false;
			accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);	
			
			if (getSubModule().equalsIgnoreCase("rawMat")){
				rm.setItemCode(this.getItemNo());
				if (validateRawMat()) {
				}else {
					if(otherUOMSelected){
						if(lookupManager.addNewUOM(new UnitOfMeasurements(rm.getUnitOfMeasurement(),"GENERAL"), session)){
							session = getSession();
							loadLookLists();							
						}
					}
					//Instantiate Item Pricing 
					processItemPricing(session,rm);
					itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
							rm.getClassification(), session);
					updateResult = inventoryManager.updateInventory(rm,session);
					
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}
					forWhat="true";
				}
				
			return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")){
				
				ti.setItemCode(this.getItemNo());
				if (validateTradedItems()) {
					
				}else {
					if(otherUOMSelected){
						if(lookupManager.addNewUOM(new UnitOfMeasurements(ti.getUnitOfMeasurement(),"GENERAL"), session)){
							session = getSession();
							loadLookLists();
						}
					}
					//Instantiate Item Pricing 
					processItemPricing(session,ti);
					itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
							ti.getClassification(), session);
					updateResult = inventoryManager.updateInventory(ti,session);
					
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}
					forWhat="true";
				}
				
			return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems")){
				unl.setItemCode(this.getItemNo());
				if (isValidateUnlistedItems()) {
				}else {
					if(otherUOMSelected){
						if(lookupManager.addNewUOM(new UnitOfMeasurements(unl.getUom(),"GENERAL"), session)){
							session = getSession();
							loadLookLists();
							
						}
					}
					//Instantiate Item Pricing 
					//processItemPricing(session,ti);
					//itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
					//		ti.getClassification(), session);
					updateResult = inventoryManager.updateInventory(unl,session);
					
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}
					forWhat="true";
				}
				
			return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")){
				
				u.setItemCode(this.getItemNo());
				if (validateUtensils()) {
					
				}else {
					if(otherUOMSelected){
						if(lookupManager.addNewUOM(new UnitOfMeasurements(u.getUnitOfMeasurement(),"GENERAL"), session)){
							session = getSession();
							loadLookLists();
						}
					}
					//Instantiate Item Pricing 
					processItemPricing(session,u);
					itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
							u.getClassification(), session);
					updateResult = inventoryManager.updateInventory(u,session);
					
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}
					forWhat="true";
				}
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("ofcSup")){
				return updateOfficeSupplies();
			}else if (getSubModule().equalsIgnoreCase("returnSlip")){
				return updateReturnSlip();
			}else if (getSubModule().equalsIgnoreCase("fpts")){
				
				return updateFPTS();
			}else if (getSubModule().equalsIgnoreCase("rf")){
				
				return updateRF();
			}
			else {
				fg.setProductCode(getProductNo());
				
				if(pcItr==null) {
					ingredients = new ArrayList();
				}
				else{
					setIngredient(false); 
				}
				listToSet();
				fg.setIngredients(inventoryManager.persistsIngredients(finalIngredients,session));
				
				setPrices();
				
				//Instantiate Item Pricing 
				processItemPricing(session,fg);
				
				itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
						fg.getClassification(), session);
				if (validateFinGood()) {
					
				}else {
					if(otherUOMSelected){
						if(lookupManager.addNewUOM(new UnitOfMeasurements(fg.getUnitOfMeasurement(),"GENERAL"), session)){
							session = getSession();
							loadLookLists();							
						}
					}
					updateResult = inventoryManager.updateInventory(fg,session);
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}
				}
				forWhat="true";
				
			return "finGood";
			}
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("rawMat")) {
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")) {
				return "utensils";
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
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}

	private String updateOfficeSupplies(){
		Session session = getSession();
		boolean updateResult=false;
			os.setItemCode(this.getItemNo());
			if (validateOfficeSupplies()) {
			}else {
				if(otherUOMSelected){
					if(lookupManager.addNewUOM(new UnitOfMeasurements(os.getUnitOfMeasurement(),"GENERAL"), session)){
						session = getSession();
						loadLookLists();
					}
				}
				//Instantiate Item Pricing 
				processItemPricing(session,os);
				itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
						os.getClassification(), session);
				updateResult = inventoryManager.updateInventory(os,session);
				
				if (updateResult== true) {
					addActionMessage(SASConstants.UPDATED);
				}else {
					addActionMessage(SASConstants.UPDATE_FAILED);
				}
				forWhat="true";
			}
		return "ofcSup";
	}
	private String updateReturnSlip() {
		//to get if disabled
		rs.setReturnSlipNo(rsIdNo);
		Session session = getSession();
		boolean updateResult=false;
		boolean inventoryUpdateSuccess= false;

		if (validateReturnSlip()) {
			includePoDetails();
			itemCodeList = new ArrayList();
			Iterator itr = poDetailsHelperToCompare.getPurchaseOrderDetailsList().iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr.next();
				itemCodeList.add(tempDetails.getItemCode());
			}
		}else {
			poDetailsHelperDraft.setOrderDate(rs.getReturnDate());
			rs.setPurchaseOrderDetails(poDetailsHelperDraft.persistNewSetElements(session));
			poDetailsHelperDraft.generatePODetailsListFromSet(rs.getPurchaseOrderDetails());
			
			inventoryManager.persistMemo(rs.getMemo(),session);
			
			poDetailsHelperToCompare.prepareSetAndList();
			//2014 - ITEM COLORING
			poDetailsHelperToCompare.generateItemTypesForExistingItems(session);
	
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
			ReturnSlip oldRs = new ReturnSlip();
			oldRs = (ReturnSlip) inventoryManager.listInventoryByParameter(ReturnSlip .class, "returnSlipNo",
					rsIdNo,session).get(0);
			PurchaseOrderDetailHelper helperOld = new PurchaseOrderDetailHelper();
			helperOld.generatePODetailsListFromSet(oldRs.getPurchaseOrderDetails());
			
			PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.getChangeInOrder(helperOld, poDetailsHelperDraft ,rs.getReturnSlipTo());
			
			try {
				inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
				inventoryUpdateSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
				addActionError(e.getMessage());
				inventoryUpdateSuccess=false;
			}
			
			itemCodeList = new ArrayList();
			Iterator itr = poDetailsHelperToCompare.getPurchaseOrderDetailsList().iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr.next();
				itemCodeList.add(tempDetails.getItemCode());
			}
			if(inventoryUpdateSuccess) {
				//START - 2013 - PHASE 3 : PROJECT 1: MARK
				transactionMananger.discontinuePreviousTransactions(rs.getReturnSlipNo(),session);
				//transactionList = new ArrayList();
				transactionList = getTransactionList();
				updateAccountingEntries(rs.getReturnSlipNo(),session,SASConstants.INVENTORY_RETURN_SLIP_FORM);
				this.setTransactionList(transactions);
				rs.setTransactions(transactions);
				//END
				updateResult = inventoryManager.updateInventory(rs,session);
			}else {
				updateResult=false;
			}
			if (updateResult== true) {
				addActionMessage(SASConstants.UPDATED);
			}else {
				addActionMessage(SASConstants.UPDATE_FAILED);
			}
			forWhat="true";
			forWhatDisplay="edit";

		}
		return "returnSlip";
	}
	
	private void updateInventory (
			Set<PurchaseOrderDetails> purchaseOrderDetails) throws Exception{
			Iterator itr = purchaseOrderDetails.iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails poDetails =  (PurchaseOrderDetails)itr.next();
				Session ss = getSession();
				updateInventoryItems(poDetails, ss);
			}
			
	}

	private void updateInventoryItems(PurchaseOrderDetails poDetails,Session session) throws Exception {
			if(rs.getReturnSlipTo().equalsIgnoreCase("CTOW")){
					inventoryManager.addInventoryItem(inventoryManager.determineItemTypeFromPoDetails(poDetails),session);
				}else {
					inventoryManager.deductInventoryItem(inventoryManager.determineItemTypeFromPoDetails(poDetails),session);
			}
	}
	private String updateRF() {
		Session session = getSession();
		rf.setRequisitionNo(rfNo);
		boolean updateResult;
			/*
			 * Checking and fetching existing return slips
			 */
			InventoryManager invManager= new InventoryManager();
			Session rfSession = getSession();
			List returnSlipList = invManager.listInventoryByParameter(ReturnSlip.class, "returnSlipReferenceOrderNo", rfNo, rfSession);
			if (validateRF()) {
				includePoDetails();
			}else {	
				if(returnSlipList.size()>0) {
					rf.setReturnSlipList(returnSlipList);
				}else {
					rf.setReturnSlipList(null);
				}
				if(null==poDetailsHelper) {
					addActionError("ORDER DETAILS IS EMPTY");
				}else {
					poDetailsHelper.setOrderDate(rf.getRequisitionDate());
					Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
					rf.setPurchaseOrderDetailsOrdered(podetailSet);
					poDetailsHelper.generatePODetailsListFromSet(rf.getPurchaseOrderDetailsOrdered());
					
				}
				
				if (rf.getPurchaseOrderDetailsOrdered().size()==0) {
					addActionError(SASConstants.EMPTY_ORDER_DETAILS);
				}else {
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
					RequisitionForm oldRR = 
							(RequisitionForm) inventoryManager.listInventoryByParameter(RequisitionForm.class,"requisitionNo", rfNo, session).get(0);
					PurchaseOrderDetailHelper helperOld = new PurchaseOrderDetailHelper();
					helperOld.generatePODetailsListFromSet(oldRR.getPurchaseOrderDetailsOrdered());
					try {
						PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.getChangeInOrder(helperOld, poDetailsHelper , SASConstants.ORDER_TYPE_ORDER_REQUISITION);
						inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
						inventoryUpdateSuccess = true;
					} catch (Exception e) {
						if(poDetailsHelper.getPurchaseOrderDetailsList()!=null) {
							e.printStackTrace();
							addActionError("INVENTORY MOVEMENT FAILED");
							inventoryUpdateSuccess=false;
						}else {
							//initial create so no details yet / bypass inventory movement
							inventoryUpdateSuccess=true;
						}
					}
					if(inventoryUpdateSuccess) {
						//START - 2013 - PHASE 3 : PROJECT 1: MARK
						transactionMananger.discontinuePreviousTransactions(rf.getRequisitionNo(),session);
						//transactionList = new ArrayList();
						transactionList = getTransactionList();
						updateAccountingEntries(rf.getRequisitionNo(),session,SASConstants.INVENTORY_REQUISITION_FORM);
						this.setTransactionList(transactions);
						rf.setTransactions(transactions);
						//END
						updateResult = inventoryManager.updateInventory(rf,session);
					}else {
						updateResult=false;
					}
					poDetailsHelper.flushUnRelatedOrders(session);
					//poDetailsHelperToCompare.flushUnRelatedOrders(session);
					if (updateResult== true) {
						addActionMessage(SASConstants.UPDATED);
						forWhat="true";
					}else {
						addActionMessage(SASConstants.UPDATE_FAILED);
					}	
				}
			}
		return "rf";
	}
	private String updateFPTS(){
		
		Session session = getSession();
		boolean updateResult;
		fpts.setFptsNo(fptsNo);
		if (validateFPTS()) {
			includePoDetails();
		} else {
			if(null==poDetailsHelper) {
				addActionError("ORDER DETAILS IS EMPTY");
			}else {
				poDetailsHelper.setOrderDate(fpts.getTransactionDate());
				Set<PurchaseOrderDetails> podetailSet = poDetailsHelper.persistNewSetElements(session);
				fpts.setPurchaseOrderDetailsTransferred(podetailSet);
				poDetailsHelper.generatePODetailsListFromSet(fpts.getPurchaseOrderDetailsTransferred());
			}
			if(null==poDetailsHelperToCompare) {
				addActionError("ORDER DETAILS IS EMPTY");
			}else {
				poDetailsHelperToCompare.setOrderDate(fpts.getTransactionDate());
				Set<PurchaseOrderDetails> podetailSet2 = poDetailsHelperToCompare.persistNewSetElements(session);
				fpts.setPurchaseOrderDetailsReceived(podetailSet2);
				poDetailsHelperToCompare.generatePODetailsListFromSet(fpts.getPurchaseOrderDetailsReceived());
				
			}
			
			if (fpts.getPurchaseOrderDetailsTransferred().size()==0) {
				addActionError(SASConstants.EMPTY_ORDER_DETAILS);
			}else {
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
				
				FPTS oldFpts = (FPTS) inventoryManager.listInventoryByParameter(FPTS.class,"fptsNo",fptsNo, session).get(0);
				PurchaseOrderDetailHelper helperOld = new PurchaseOrderDetailHelper();
				helperOld.generatePODetailsListFromSet(oldFpts.getPurchaseOrderDetailsTransferred());
				try {
					PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.getChangeInOrder(helperOld, poDetailsHelper , SASConstants.ORDER_TYPE_FPTS);
					inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
					inventoryUpdateSuccess = true;
				} catch (Exception e) {
					if(poDetailsHelper.getPurchaseOrderDetailsList()!=null) {
						e.printStackTrace();
						addActionError("INVENTORY MOVEMENT FAILED");
						inventoryUpdateSuccess=false;
					}else {
						//initial create so no details yet / bypass inventory movement
						inventoryUpdateSuccess=true;
					}
				}
				
				if(inventoryUpdateSuccess) {
					poDetailsHelper.setOrderDate(fpts.getTransactionDate());
					fpts.setPurchaseOrderDetailsTransferred(poDetailsHelper.persistNewSetElements(session));
					//START - 2013 - PHASE 3 : PROJECT 1: MARK
					transactionMananger.discontinuePreviousTransactions(fpts.getFptsNo(),session);
					//transactionList = new ArrayList();
					transactionList = getTransactionList();
					updateAccountingEntries(fpts.getFptsNo(),session,SASConstants.INVENTORY_FPTS);
					this.setTransactionList(transactions);
					fpts.setTransactions(transactions);
					//END
					updateResult = inventoryManager.updateInventory(fpts,session);
				}else {
					updateResult = false;
				}
				
				poDetailsHelper.flushUnRelatedOrders(session);
				//poDetailsHelperToCompare.flushUnRelatedOrders(session);
				if (updateResult== true) {
					addActionMessage(SASConstants.UPDATED);
					forWhat="true";
				}else {
					addActionMessage(SASConstants.UPDATE_FAILED);
				}	
			}
		}
		return "fpts";
	}
	private void processItemPricing(Session session,Object obj) {
	
	ItemPricing itemPricing = new ItemPricing();
	ItemPricing newItemPricing = new ItemPricing();
	
	if(obj instanceof RawMaterial) {
		itemPricing = invUtil.getItemPricing(session,rm.getItemCode());
	}else if(obj instanceof Utensils) {
		itemPricing = invUtil.getItemPricing(session,u.getItemCode());
	}else if(obj instanceof TradedItem) {
		itemPricing = invUtil.getItemPricing(session,ti.getItemCode());
	}else if(obj instanceof OfficeSupplies) {
		itemPricing = invUtil.getItemPricing(session,os.getItemCode());
	}else if( obj instanceof FinishedGood) {
		itemPricing = invUtil.getItemPricing(session,fg.getProductCode());
	}
	//else if (obj instanceof TradedItem)
	
		if(obj instanceof RawMaterial) {
			newItemPricing = rm.getItemPricing();
		}else if( obj instanceof TradedItem) {
			newItemPricing = ti.getItemPricing();
		}else if( obj instanceof Utensils) {
			newItemPricing = u.getItemPricing();
		}else if( obj instanceof OfficeSupplies) {
			newItemPricing = os.getItemPricing();
		}else if( obj instanceof FinishedGood) {
			newItemPricing = fg.getItemPricing();
		}
		
		itemPricing.setCompanyOwnedActualPricePerUnit(newItemPricing.getCompanyOwnedActualPricePerUnit());
		itemPricing.setCompanyOwnedStandardPricePerUnit(newItemPricing.getCompanyOwnedStandardPricePerUnit());
		itemPricing.setCompanyOwnedTransferPricePerUnit(newItemPricing.getCompanyOwnedTransferPricePerUnit());
		
		itemPricing.setFranchiseActualPricePerUnit(newItemPricing.getFranchiseActualPricePerUnit());
		itemPricing.setFranchiseStandardPricePerUnit(newItemPricing.getFranchiseStandardPricePerUnit());
		itemPricing.setFranchiseTransferPricePerUnit(newItemPricing.getFranchiseTransferPricePerUnit());
		
		inventoryManager.updatePersistingInventoryObject(itemPricing, session);

		if(obj instanceof RawMaterial) {
			rm.setItemPricing(itemPricing);
		}else if( obj instanceof TradedItem) {
			ti.setItemPricing(itemPricing);
		}else if( obj instanceof Utensils) {
			u.setItemPricing(itemPricing);
		}else if( obj instanceof OfficeSupplies) {
			os.setItemPricing(itemPricing);
		}else if( obj instanceof FinishedGood) {
			fg.setItemPricing(itemPricing);
		}
	//else if (obj instanceof TradedItem)
	}
	private void setPrices() {
		ItemPricing itemPricing = fg.getItemPricing();
		
		if(fg.getYields()==0){
			itemPricing.setCompanyOwnedStandardPricePerUnit(0.0);
			itemPricing.setCompanyOwnedActualPricePerUnit(0.0);
			itemPricing.setCompanyOwnedTransferPricePerUnit(0.0);
		}else{
			itemPricing.setCompanyOwnedStandardPricePerUnit(Double.valueOf(fg.getStandardTotalCost() / fg.getYields()));
			itemPricing.setCompanyOwnedActualPricePerUnit(fg.getActualTotalCost()/fg.getYields());
			//Transfer Price Computation
			Double markup = fg.getMarkUp()/100;
			double partialTransferPricePerunit = Double.valueOf((fg.getActualTotalCost() / fg.getYields())*(markup==0.0?1:markup));
			if(markup==0) {
				itemPricing.setCompanyOwnedTransferPricePerUnit(Double.valueOf(partialTransferPricePerunit));
			}else {
				itemPricing.setCompanyOwnedTransferPricePerUnit(Double.valueOf(partialTransferPricePerunit+(fg.getActualTotalCost() / fg.getYields())));
			}
		}
		
	}

private Set finalIngredients;
private void listToSet(){
	Iterator listitr= ingredients.iterator();
	finalIngredients = new HashSet();
	while(listitr.hasNext()) {
		finalIngredients.add((Ingredient)listitr.next());
	}
	
}
private void setIngredient(boolean includeNewItem) {
	StringTokenizer pcItrTk = new StringTokenizer(pcItr,",");
	StringTokenizer qtyItrTk = new StringTokenizer(qtyItr,",");
	StringTokenizer descItrTk = new StringTokenizer(descItr,",");
	StringTokenizer uomItrTk = new StringTokenizer(uomItr,",");
	ingredients = new ArrayList<Ingredient>();
	DoubleConverter dc = new DoubleConverter();
	Session session = getSession();
	
	try{
	while(pcItrTk.hasMoreElements()) {
			Ingredient in = new Ingredient();
			in.setProductCode(((String) pcItrTk.nextElement()).trim());
			in.setQuantity(Double.valueOf(dc
					.convertFromStringNoContext(((String) qtyItrTk
							.nextElement()).trim())));
			in.setDescription(((String) descItrTk.nextElement()).trim());
			in.setUnitOfMeasurement(((String) uomItrTk.nextElement()).trim());
			in = inventoryManager.loadIngredientPrices(in,session);
			ingredients.add(in);
	}
	}catch(Exception e){
		e.printStackTrace();
	}
	finally{
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
		loadLookLists();
	}
	
}

List itemCodeList;
List UOMList;

		private String requestingModule;
		private List generateUOMStrings(List uomList){
			List uomStringsList=new ArrayList();
			Iterator iterator = uomList.iterator();
			while(iterator.hasNext()){
				String uom = ((UnitOfMeasurements)iterator.next()).getValue();
				uomStringsList.add(uom);
			}
			return uomStringsList;
				}
		
		public String loadLookLists(){
			Session session = getSession();
			try{
			UOMList = generateUOMStrings(lookupManager.getLookupElements(UnitOfMeasurements.class, "GENERAL",session));
			itemCodeList = inventoryManager.loadItemListFromRawAndFin(session);
			}catch(Exception e){
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
				}else if (getSubModule().equalsIgnoreCase("rawMat")) {
					return "rawMat";
				}else {
					return "finGood";
				}
			}finally{
				if(session.isOpen()){
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
			
			if(requestingModule!=null && requestingModule.equalsIgnoreCase("rawMaterial")){
				return "rawMat";
			}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("tradedItems")){
				return "tradedItems";
			}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("utensils")){
				return "utensils";
			}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("ofcSup")){
				return "ofcSup";
			}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("unlistedItems")){
				return "unlistedItems";
			}else{
				return "finGood";
			}
		}
		
		private boolean validateRawMat (){
			loadLookLists();
			boolean errorFound = false;
			if ("".equals(getRm().getItemCode())){
				addFieldError("rm.itemCode","REQUIRED");
				errorFound= true;
			}else{
				if(getRm().getItemCode().length()>45){
					addFieldError("rm.itemCode","item code too long");
					errorFound= true;
				}
			}
			if ("".equals(getRm().getDescription())){
				addFieldError("rm.description","REQUIRED");
				errorFound= true;
			}else {
				if (getRm().getDescription().trim().length()>200){
				addFieldError("rm.description","MAXIMUM LENGTH: 200 characters");
				}
			}	
			if(rm.getUnitOfMeasurement()==null){
			}else{
				if(rm.getUnitOfMeasurement().indexOf(", ")>-1){
					if("".equals(rm.getUnitOfMeasurement().substring(rm.getUnitOfMeasurement().indexOf(", ")))){
						addFieldError("rm.unitOfMeasurement","REQUIRED");
						errorFound= true;
					}else{
						otherUOMSelected=true;
						rm.setUnitOfMeasurement(rm.getUnitOfMeasurement().substring(rm.getUnitOfMeasurement().indexOf(", ")+2));
					}
				}
			}
			return errorFound;	
		}
			
		private boolean validateFinGood (){
			loadLookLists();
			boolean errorFound = false;
			if ("".equals(getFg().getProductCode())){
				addFieldError("fg.productCode","REQUIRED");
				errorFound= true;
			}
			if ("".equals(getFg().getDescription())){
				addFieldError("fg.description","REQUIRED");
				errorFound= true;
			}else if (getFg().getDescription().trim().length()>200){
				addFieldError("fg.description","MAXIMUM LENGTH: 200 characters");
			}
			if(fg.getUnitOfMeasurement()==null){
			}else{
				if(fg.getUnitOfMeasurement().indexOf(", ")>-1){
					if("".equals(fg.getUnitOfMeasurement().substring(fg.getUnitOfMeasurement().indexOf(", ")))){
						addFieldError("fg.unitOfMeasurement","REQUIRED");
						errorFound= true;
					}else{
						otherUOMSelected=true;
						fg.setUnitOfMeasurement(fg.getUnitOfMeasurement().substring(fg.getUnitOfMeasurement().indexOf(", ")+2));
					}
				}
			}
		//	if (getFg().getStandardPricePerUnit()==0){
		//		addFieldError("fg.standardPricePerUnit","REQUIRED");
		//		errorFound= true;
		//	}
		//	if (getFg().getActualPricePerUnit()==0){
		//		addFieldError("fg.actualPricePerUnit","REQUIRED");
		//		errorFound= true;
		//	}	
		
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
		
		private boolean isValidateUnlistedItems() {
			loadLookLists();
			boolean errorFound = false;
		/*	if ("".equals(getUnl().getItemCode())) {
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
				addFieldError("ofcSup.unitOfMeasurementText", "REQUIRED");
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
		
		private boolean validateFPTS() {
			
			boolean errorFound = false;
			
			if ("".equals(fpts.getFptsNo())) {
				addFieldError("fpts.fptsNo", "REQUIRED");
				errorFound = true;
			} else {
				if (fpts.getFptsNo().length() > 45) {
					addFieldError("fpts.fptsNo", "FPTS No. too long");
					errorFound = true;
				}
			}
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
			 if ((getTransactionList().get(0).getAmount() == 0 )) {
					addActionMessage("REQUIRED: Accounting Entries Details");
					errorFound = true;
				}
				return errorFound;
			}
private boolean validateRF() 	 {
	
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
	}  if ((getTransactionList().get(0).getAmount() == 0 )) {
		addActionMessage("REQUIRED: Accounting Entries Details");
		errorFound = true;
	}
		return errorFound;
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
	}  if ((getTransactionList().get(0).getAmount() == 0 )) {
		addActionMessage("REQUIRED: Accounting Entries Details");
		errorFound = true;
	}
		return errorFound;
	
}
		private void includePoDetails() {
			if(poDetailsHelper!=null) {
				poDetailsHelper.prepareSetAndList();
			}
			if(poDetailsHelperToCompare!=null) {
				poDetailsHelperToCompare.prepareSetAndList();
			}if(poDetailsHelperDraft!=null) {
				poDetailsHelperDraft.prepareSetAndList();
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
				transactionMananger.addTransactionsList(transactions,session);
			}
			//END - 2013 - PHASE 3 : PROJECT 1: MARK
			//return transactions;
		}

	public List getUOMList() {
		return UOMList;
	}
	
	public void setUOMList(List uOMList) {
		UOMList = uOMList;
	}
	
	public List getItemCodeList() {
		return itemCodeList;
	}
	
	public void setItemCodeList(List itemCodeList) {
		this.itemCodeList = itemCodeList;
	}
	
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
	
	public RawMaterial getRm() {
		return rm;
	}
	
	public void setRm(RawMaterial rm) {
		this.rm = rm;
	}
	
	public FinishedGood getFg() {
		return fg;
	}
	
	public void setFg(FinishedGood fg) {
		this.fg = fg;
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
	
	public Set getFinalIngredients() {
		return finalIngredients;
	}
	
	public void setFinalIngredients(Set finalIngredients) {
		this.finalIngredients = finalIngredients;
	}
	public String getProductNo() {
		return productNo;
	}
	
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	
	public String getItemNo() {
		return itemNo;
	}
	
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	
	public String getQtyItr() {
		return qtyItr;
	}
	
	public void setQtyItr(String qtyItr) {
		this.qtyItr = qtyItr;
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
	public TradedItem getTi() {
		return ti;
	}
	public void setTi(TradedItem ti) {
		this.ti = ti;
	}
	public ReturnSlip getRs() {
		return rs;
	}
	public void setRs(ReturnSlip rs) {
		this.rs = rs;
	}
	public PurchaseOrderDetailHelper getPoDetailsHelperDraft() {
		return poDetailsHelperDraft;
	}
	public void setPoDetailsHelperDraft(
			PurchaseOrderDetailHelper poDetailsHelperDraft) {
		this.poDetailsHelperDraft = poDetailsHelperDraft;
	}
	public String getRsIdNo() {
		return rsIdNo;
	}
	public void setRsIdNo(String rsIdNo) {
		this.rsIdNo = rsIdNo;
	}
	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}
	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}
	public FPTS getFpts() {
		return fpts;
	}
	public void setFpts(FPTS fpts) {
		this.fpts = fpts;
	}
	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}
	public void setPoDetailsHelperToCompare(
			PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
	}
	public String getFptsId() {
		return fptsId;
	}
	public void setFptsId(String fptsId) {
		this.fptsId = fptsId;
	}
	public String getFptsNo() {
		return fptsNo;
	}
	public void setFptsNo(String fptsNo) {
		this.fptsNo = fptsNo;
	}
	public RequisitionForm getRf() {
		return rf;
	}
	public void setRf(RequisitionForm rf) {
		this.rf = rf;
	}
	public String getRfNo() {
		return rfNo;
	}
	public void setRfNo(String rfNo) {
		this.rfNo = rfNo;
	}
	public String getRfId() {
		return rfId;
	}
	public void setRfId(String rfId) {
		this.rfId = rfId;
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
	public String getTempClassif() {
		return tempClassif;
	}
	public void setTempClassif(String tempClassif) {
		this.tempClassif = tempClassif;
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
