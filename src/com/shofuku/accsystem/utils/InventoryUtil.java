package com.shofuku.accsystem.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.BaseController;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.dao.impl.InventoryDaoImpl;
import com.shofuku.accsystem.domain.inventory.ItemPricing;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;

public class InventoryUtil {
	
	Map<String,Object> actionSession;
	BaseController manager;
	private void initializeController() {
		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
	}
	
	InventoryManager inventoryManager; 
	
	public InventoryUtil(Map<String, Object> actionSession) {
		this.actionSession = actionSession;
	}
	
	public double getItemPricingByCustomerTypeAndParameter(ItemPricing itemPricing, String customerType, String priceType) {
		double price = 0.0;
		System.out.println(priceType.toLowerCase());
		if(customerType.equalsIgnoreCase("F")) {
			switch(priceType.toLowerCase()) {
			case "actual": price = itemPricing.getFranchiseActualPricePerUnit();break;
			case "transfer": price = itemPricing.getFranchiseTransferPricePerUnit();break;
			case "standard": price = itemPricing.getFranchiseStandardPricePerUnit();break;
			}
		}else if(customerType.equalsIgnoreCase("C")) {
			switch(priceType.toLowerCase()) {
			case "actual": price = itemPricing.getCompanyOwnedActualPricePerUnit();break;
			case "transfer": price = itemPricing.getCompanyOwnedTransferPricePerUnit();break;
			case "standard": price = itemPricing.getCompanyOwnedStandardPricePerUnit();break;
			}
		}else {
			//By default we get the franchise price will have to confirm with business
			switch(priceType.toLowerCase()) {
			case "Actual": price = itemPricing.getCompanyOwnedActualPricePerUnit();
			case "Transfer": price = itemPricing.getCompanyOwnedTransferPricePerUnit();
			case "Standard": price = itemPricing.getCompanyOwnedStandardPricePerUnit();
			}
		}
		return price;
	}
	
	public ItemPricing getItemPricing(Session session,String itemCode) {
		initializeController();
		return inventoryManager.getInventoryDao().getItemPricingByItemCodeAndParameter(session, itemCode);
	}
	public String generateNewItemPricingId() {
		String itemCode = "";
		return itemCode;
	}
	public boolean checkCurrentItemPricing(Session session,String itemCode) {
		initializeController();
		ItemPricing itemPricing = inventoryManager.getInventoryDao().getItemPricingByItemCodeAndParameter(session, itemCode);
		if(itemPricing == null) {
			return false;
		}else {
			return true;
		}
	}
	
	//method called from delete
		public PurchaseOrderDetailHelper updateInventoryCountsForDeletion(PurchaseOrderDetailHelper helperItemsForDeletion,String orderType) {
			
			PurchaseOrderDetailHelper helper = new PurchaseOrderDetailHelper(actionSession);
			Set<PurchaseOrderDetails> podetailSet = helper.generatePODetailsSet();
			
			Iterator newHelperIterator = helperItemsForDeletion.getPurchaseOrderDetailsList().iterator();
			while (newHelperIterator.hasNext()) {
				PurchaseOrderDetails newOrderDetail = (PurchaseOrderDetails )newHelperIterator.next();
			
				double difference = 0;
				
				difference = getDifference(0,newOrderDetail.getQuantity());

				
				String changeType = getChangeTypeBasedOnOrderType(orderType, difference);
				
				//this will reverse the logic when adding since we are deleting and returning all changes done on addition
				
				if(changeType.equalsIgnoreCase(SASConstants.CHANGE_TYPE_ADD)) {
					changeType=SASConstants.CHANGE_TYPE_DEDUCT;
				}else {
					changeType=SASConstants.CHANGE_TYPE_ADD;
				}
				
				setChangeInQtyInAndOut(newOrderDetail,difference,changeType);
				
				podetailSet.add(newOrderDetail);
			}

			helper.generatePODetailsSet(podetailSet);
			
			return helper;
		}
	
	public PurchaseOrderDetailHelper  getChangeInOrder(PurchaseOrderDetailHelper currentOrderHelper, PurchaseOrderDetailHelper newOrderHelper,String orderType){
		
		PurchaseOrderDetailHelper helper = new PurchaseOrderDetailHelper(actionSession);
		
		Set<PurchaseOrderDetails> podetailSet = helper.generatePODetailsSet();
		
		HashMap<String, PurchaseOrderDetails> currentHelperMap = new HashMap<String, PurchaseOrderDetails>();
		
		if(currentOrderHelper.getPurchaseOrderDetailsList()!=null) {
			Iterator currentHelperIterator = currentOrderHelper.getPurchaseOrderDetailsList().iterator();
			while(currentHelperIterator.hasNext()) {
				PurchaseOrderDetails orderDetail = (PurchaseOrderDetails )currentHelperIterator.next();
				currentHelperMap.put(orderDetail.getItemCode(), orderDetail);
			}
		}else {
			// this is a new entry no comparisson needed
		}
		
		Iterator newHelperIterator = newOrderHelper.getPurchaseOrderDetailsList().iterator();
		
		
		
		while (newHelperIterator.hasNext()) {
			PurchaseOrderDetails newOrderDetail = (PurchaseOrderDetails )newHelperIterator.next();
			PurchaseOrderDetails oldOrderDetail = (PurchaseOrderDetails ) currentHelperMap.get(newOrderDetail.getItemCode());
		
			double difference = 0;
			
			if(oldOrderDetail !=null) {
				difference = getDifference(oldOrderDetail.getQuantity(),newOrderDetail.getQuantity());
			}else {
				difference = getDifference(0,newOrderDetail.getQuantity());
				// this is a new entry no comparisson needed
			}
			
			String changeType = getChangeTypeBasedOnOrderType(orderType, difference);
			
			setChangeInQtyInAndOut(newOrderDetail,difference,changeType);
			
			podetailSet.add(newOrderDetail);
		}

		helper.generatePODetailsSet(podetailSet);
		
		return helper;
		
	}
	private void setChangeInQtyInAndOut(PurchaseOrderDetails newOrderDetail,
			Double difference, String changeType) {
		
		if(changeType.equalsIgnoreCase(SASConstants.CHANGE_TYPE_ADD)) {
			newOrderDetail.setQuantityIn(Math.abs(difference));	
		}else if(changeType.equalsIgnoreCase(SASConstants.CHANGE_TYPE_DEDUCT)) {
			newOrderDetail.setQuantityOut(Math.abs(difference));
		}
		
	}
	/*
	 * ADD VALUES IN SASCONSTANTS and IN THE CONDITIONS BELOW IF THERE IS A NEWLY INTRODUCED TYPE TO KEEP INVENTORY ITEMS MOVING
	 */
	private String getChangeTypeBasedOnOrderType(String orderType,double difference) {
		String changeType="";
		if(orderType.equalsIgnoreCase(SASConstants.ORDER_TYPE_DR)||orderType.equalsIgnoreCase(SASConstants.ORDER_TYPE_ORDER_REQUISITION)||
				   orderType.equalsIgnoreCase(SASConstants.ORDER_TYPE_WAREHOUSE_TO_SUPPLIER)||orderType.equalsIgnoreCase(SASConstants.RS_WAREHOUSE_TO_PRODUCTION)) {
			if(difference<0) {
				changeType=SASConstants.CHANGE_TYPE_DEDUCT;
			}else {
				changeType=SASConstants.CHANGE_TYPE_ADD;
			}
		}else if (orderType.equalsIgnoreCase(SASConstants.ORDER_TYPE_RR)||orderType.equalsIgnoreCase(SASConstants.ORDER_TYPE_FPTS)||
				  orderType.equalsIgnoreCase(SASConstants.ORDER_TYPE_CUSTOMER_TO_WAREHOUSE)||
				  orderType.equalsIgnoreCase(SASConstants.ORDER_TYPE_PRODUCTION_TO_WAREHOUSE_INPUT)){
			if(difference<0) {
				changeType=SASConstants.CHANGE_TYPE_ADD;
			}else {
				changeType=SASConstants.CHANGE_TYPE_DEDUCT;
			}
		}
		return changeType;
	}
	private double getDifference(double oldQuantity, double newQuantity) {
		return oldQuantity - newQuantity;
	}
	
}
