package com.shofuku.accsystem.controllers;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shofuku.accsystem.dao.impl.InventoryDaoImpl;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.ItemPricing;
import com.shofuku.accsystem.domain.inventory.Memo;
import com.shofuku.accsystem.domain.inventory.PurchaseOrder;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.utils.DoubleConverter;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;

/*
 * add business side logic in this class
 */
@SuppressWarnings("rawtypes")
public class InventoryManager extends HibernateUtil {

	InventoryDaoImpl dao = new InventoryDaoImpl();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public boolean addInventoryObject(Object inventoryObject,Session session) {
		return dao.save(inventoryObject,session);
	}

	public boolean addPersistingInventoryObject(Object inventoryObject,Session session) {
		return dao.persistingInsert(inventoryObject,session);
	}
	

	public boolean deleteInventoryByParameter(Object object, Class clazz,Session session) {
		return dao.deleteByParameter(object, clazz,session);
	}
	
	public boolean mergeInventoryByParameter(Object object, Class clazz,Session session) {
		return dao.mergeByParameter(object, clazz,session);
	}

	public boolean updateInventory(Object persistentObject,Session session) {
		return dao.update(persistentObject,session);
	}
	
	public boolean addStockStatus(Object inventoryObject,Session session) {
		return dao.saveStockStatus(inventoryObject,session);
	}
	
	public boolean updateStockStatus(Object persistentObject,Session session) {
		return dao.updateStockStatus(persistentObject,session);
	}
	
	public boolean updatePersistingInventoryObject(Object persistentObject,Session session) {
		return dao.persistingUpdate(persistentObject,session);
	}

	public List listInventoryByParameter(Class clazz, String parameter,
			String value,Session session) {
		return dao.listByParameter(clazz, parameter, value,session);
	}

	public List listInventoryByParameterLike(Class clazz, String parameter,
			String value,Session session) {
		return dao.listByParameterLike(clazz, parameter, value,session);
	}
	
	public List listInventoryByParametersLike(Class clazz, Map<String,Object> parameterMap,List parameterFields, String orderByString,
			Session session) {
		return dao.listByParametersLike(clazz, parameterMap, parameterFields,orderByString, session);
	}
	public List listByParameters(Class clazz, Map<String,Object> parameterMap,List parameterFields, String orderByString,
			Session session) {
		return dao.listByParameters(clazz, parameterMap, parameterFields,orderByString, session);
	}
	public boolean addPurchaseOrderDetails(Object purchaseOrderDetail,Session session) {
		return dao.save(purchaseOrderDetail,session);
	}
	
	public List getInventoryElementsByDate(Date date,
			String className,String parameter,Session session) {
		return  dao.getBetweenDates(date, date,className, parameter,session);
	}

	
	public Set<Ingredient> persistsIngredients(Set<Ingredient> ingredients,Session session) {
		return dao.persistsIngredients(ingredients,session);
	}
	
	public boolean persistMemo(Memo memo,Session session) {
		return dao.persistMemo(memo,session);
	}
	
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		return dao.listAlphabeticalAscByParameter(clazz, parameter,session);
	}
	
	public List searchFPTSByOrderRequisitionNo(Class clazz, String parameter, String value,Session session){
		return dao.searchFPTSByOrderRequisitionNo(clazz, parameter, value, session);
	}
	public List listByParameter(Class clazz, String parameter, String value,Session session) {
		return dao.listByParameter(clazz, parameter, value,session);
	}
	
	/*
	 * <p><b>Description:</b> This adds quantity per record to an item based on the quantityIn value of the incoming object.
	 * Handles RawMaterial,FinishedGood and TradedItem
	 * 
	 * </p>
	 */
	public boolean updateInventoryItemRecordCountFromOrder(Object object,Session session) {
				
		if (object instanceof RawMaterial) {
			RawMaterial incomingItem = (RawMaterial) object;
			RawMaterial originalItem = (RawMaterial)dao.load(incomingItem.getItemCode(),RawMaterial.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(incomingItem.getQuantityIn() + originalItem.getQuantityPerRecord());
				originalItem.setQuantityPerRecord(originalItem.getQuantityPerRecord() - incomingItem.getQuantityOut());
				return dao.updateInventoryPerRecordCount(originalItem, session);
			}			
			
		}else if (object instanceof FinishedGood) {
			FinishedGood incomingItem = (FinishedGood) object;
			FinishedGood originalItem = (FinishedGood)dao.load(incomingItem.getProductCode(),FinishedGood.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(incomingItem.getQuantityIn() + originalItem.getQuantityPerRecord());
				originalItem.setQuantityPerRecord(originalItem.getQuantityPerRecord() - incomingItem.getQuantityOut());
				return dao.updateInventoryPerRecordCount(originalItem, session);
			}
			
		}else if (object instanceof TradedItem) {
			TradedItem incomingItem = (TradedItem) object;
			TradedItem originalItem = (TradedItem)dao.load(incomingItem.getItemCode(),TradedItem.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(incomingItem.getQuantityIn() + originalItem.getQuantityPerRecord());
				originalItem.setQuantityPerRecord(originalItem.getQuantityPerRecord() - incomingItem.getQuantityOut());
				return dao.updateInventoryPerRecordCount(originalItem, session);
			}
		}

		//passed an unknown item type
		return false;

	}
	
	/*
	 * <p><b>Description:</b> This adds quantity per record to an item based on the quantityIn value of the incoming object.
	 * Handles RawMaterial,FinishedGood and TradedItem
	 * 
	 * </p>
	 */
	public boolean addInventoryItem(Object object,Session session) {
				
		if (object instanceof RawMaterial) {
			RawMaterial incomingItem = (RawMaterial) object;
			RawMaterial originalItem = (RawMaterial)dao.load(incomingItem.getItemCode(),RawMaterial.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(incomingItem.getQuantityIn() + originalItem.getQuantityPerRecord());
				 dao.updateInventoryPerRecordCount(originalItem, session);
			}			
			
		}else if (object instanceof FinishedGood) {
			FinishedGood incomingItem = (FinishedGood) object;
			FinishedGood originalItem = (FinishedGood)dao.load(incomingItem.getProductCode(),FinishedGood.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(incomingItem.getQuantityIn() + originalItem.getQuantityPerRecord());
				 dao.updateInventoryPerRecordCount(originalItem, session);
			}
			
		}else if (object instanceof TradedItem) {
			TradedItem incomingItem = (TradedItem) object;
			TradedItem originalItem = (TradedItem)dao.load(incomingItem.getItemCode(),TradedItem.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(incomingItem.getQuantityIn() + originalItem.getQuantityPerRecord());
				 dao.updateInventoryPerRecordCount(originalItem, session);
			}
		}

		//passed an unknown item type
		return false;

	}
	
	/*
	 * <p><b>Description:</b> This deducts quantityOut of the incoming object to the existing quantityPerRecord.
	 * Handles RawMaterial,FinishedGood and TradedItem
	 * 
	 * </p>
	 */
	public boolean deductInventoryItem(Object object,Session session) {
				
		if (object instanceof RawMaterial) {
			RawMaterial incomingItem = (RawMaterial) object;
			RawMaterial originalItem = (RawMaterial)dao.load(incomingItem.getItemCode(),RawMaterial.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(originalItem.getQuantityPerRecord() - incomingItem.getQuantityOut());
				dao.updateInventoryPerRecordCount(originalItem, session);
			}			
			
		}else if (object instanceof FinishedGood) {
			FinishedGood incomingItem = (FinishedGood) object;
			FinishedGood originalItem = (FinishedGood)dao.load(incomingItem.getProductCode(),FinishedGood.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(originalItem.getQuantityPerRecord() - incomingItem.getQuantityOut());
				dao.updateInventoryPerRecordCount(originalItem, session);
			}
			
		}else if (object instanceof TradedItem) {
			TradedItem incomingItem = (TradedItem) object;
			TradedItem originalItem = (TradedItem)dao.load(incomingItem.getItemCode(),TradedItem.class);
			
			if(originalItem != null) {
				originalItem.setQuantityPerRecord(originalItem.getQuantityPerRecord() - incomingItem.getQuantityOut());
				dao.updateInventoryPerRecordCount(originalItem, session);
				
			}
		}
		

		//passed an unknown item type
		return false;

	}
	
	
	
	
	//method called from add
	public void updateInventoryFromOrders(
			PurchaseOrderDetailHelper poDetailsHelper) throws Exception {
			Iterator itr = poDetailsHelper.getPurchaseOrderDetailsSet().iterator();
			
			Session session = getSession();
			
			Object object= null;
			
			List<Item> fullItemList = getAllItemList(session);
			
			while(itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails) itr.next();
				if(poDetailsHelper.getOrderDate()!=null) {
					podetails.setOrderCreatedDate(poDetailsHelper.getOrderDate());
				}
				
				Item item = getInventoryObject(podetails.getItemCode(),fullItemList);
				
				double qtyIn=0;
				double qtyOut=0;
				qtyIn=podetails.getQuantityIn();
				qtyOut=podetails.getQuantityOut();
				try {
				if(item.getItemType().equalsIgnoreCase("rawMat")) {
					object = (RawMaterial)dao.load(podetails.getItemCode(), RawMaterial.class);	
					((RawMaterial)object).setQuantityIn(podetails.getQuantityIn());
					
					((RawMaterial)object).setQuantityOut(podetails.getQuantityOut());
				}else if(item.getItemType().equalsIgnoreCase("tradedItems")) {
					object = (TradedItem)dao.load(podetails.getItemCode(),TradedItem.class);
					((TradedItem)object).setQuantityIn(podetails.getQuantityIn());
					((TradedItem)object).setQuantityOut(podetails.getQuantityOut());
				}else if(item.getItemType().equalsIgnoreCase("finGood")) {
					object = (FinishedGood)dao.load(podetails.getItemCode(),FinishedGood.class);
					((FinishedGood)object).setQuantityIn(podetails.getQuantityIn());
					((FinishedGood)object).setQuantityOut(podetails.getQuantityOut());
				}
				
				if(object!=null) {
					if(qtyIn>0) {
						addInventoryItem(object, session);
					}
					
					if(qtyOut>0) {
						deductInventoryItem(object, session);
					}
				}
				}catch(NullPointerException npe) {
					//catch unlisted Items null pointer on inventory update
					//do nothing
				}
			}
		dao.commitChanges(session);
	}
	
	public void commitChanges(Session session) {
		dao.commitChanges(session);		
	}

	private Item getInventoryObject(String itemCode, List<Item> fullItemList) {
		Item itemFound = new Item();
		for(Item item: fullItemList){
				if(item.getItemCode().equalsIgnoreCase(itemCode)){
					itemFound  = item; 
			}
		}	
		return itemFound;
	}

	public Object determineItemTypeFromPoDetails(PurchaseOrderDetails poDetails) {
		Object object= null;
		object = (RawMaterial)dao.load(poDetails.getItemCode(), RawMaterial.class);
		if(object!=null) {
			((RawMaterial)object).setQuantityIn(poDetails.getQuantity());
			((RawMaterial)object).setQuantityOut(poDetails.getQuantity());
			return object;
		}else {
			object = (TradedItem)dao.load(poDetails.getItemCode(),TradedItem.class);
			if(object!=null) {
				((TradedItem)object).setQuantityIn(poDetails.getQuantity());
				((TradedItem)object).setQuantityOut(poDetails.getQuantity());
				return object;
			}else {
				object = (FinishedGood)dao.load(poDetails.getItemCode(),FinishedGood.class);
				if(object!=null) {
					((FinishedGood)object).setQuantityIn(poDetails.getQuantity());
					((FinishedGood)object).setQuantityOut(poDetails.getQuantity());
					return object;
				}else {
					return null;
				}
			}
		}
	}
/*
	//method called from update
	public void updateInventoryFromOrders(
			Set<PurchaseOrderDetails> purchaseOrderDetails,
			PurchaseOrderDetailHelper poDetailsHelper, String string,Session session) {
		try {
			Map<String,Double> mapChangesInQty = new HashMap<String,Double>();
			List<PurchaseOrderDetails> listToUpdate = new ArrayList<PurchaseOrderDetails>();
			
			//assign order record to a map
			Iterator drItr = purchaseOrderDetails.iterator();
			while(drItr.hasNext()) {
				PurchaseOrderDetails drOrder = (PurchaseOrderDetails) drItr.next();
				mapChangesInQty.put(drOrder.getItemCode(), drOrder.getQuantity());
			}
			
			//identifies which entries have changes by comparing order record map to the new orders
			drItr = poDetailsHelper.getPurchaseOrderDetailsSet().iterator();
			while(drItr.hasNext()) {
				PurchaseOrderDetails drOrder = (PurchaseOrderDetails) drItr.next();
				if(mapChangesInQty.containsKey(drOrder.getItemCode())) {
					double oldValue = mapChangesInQty.get(drOrder.getItemCode());
					double newValue = drOrder.getQuantity();
					mapChangesInQty.put(drOrder.getItemCode(), oldValue-newValue);
					listToUpdate.add(drOrder);
					//mapChangesInQty now has the difference in qty of the old and new orders
				}
			}
			
			
			//apply the change in qty to the appropriate item may it be fin/raw
			Iterator itr = listToUpdate.iterator();
				while(itr.hasNext()) {
					PurchaseOrderDetails podetails = (PurchaseOrderDetails) itr.next();
					double multiplier=-1;
					if(string.equalsIgnoreCase("rr")) {
						RawMaterial rawMaterial = (RawMaterial)dao.load(podetails.getItemCode(), RawMaterial.class);
						if(rawMaterial!=null) {
							rawMaterial.setQuantityPerRecord(rawMaterial.getQuantityPerRecord() + (mapChangesInQty.get(rawMaterial.getItemCode())*multiplier));
							dao.update(rawMaterial,session);
						}
					}
					 if(string.equalsIgnoreCase("dr")) {
						 FinishedGood finGood = (FinishedGood)dao.load(podetails.getItemCode(), FinishedGood.class);
						 if(finGood!=null) {
								finGood.setQuantityPerRecord(finGood.getQuantityPerRecord()+mapChangesInQty.get(finGood.getProductCode()));
								dao.update(finGood,session);
							}
					 }
				}
		}catch(Exception e) {
				e.printStackTrace();
	}
	}
	//method called from delete
	public void updateInventoryFromOrders(
			Set<PurchaseOrderDetails> purchaseOrderDetails, String string,Session session) {
		try {
			Iterator itr = purchaseOrderDetails.iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails) itr.next();

				if(string.equalsIgnoreCase("rr")) {
					RawMaterial rawMaterial = (RawMaterial)dao.load(podetails.getItemCode(), RawMaterial.class);
					if(rawMaterial!=null) {
						rawMaterial.setQuantityPerRecord(rawMaterial.getQuantityPerRecord()-podetails.getQuantity());
						dao.update(rawMaterial,session);
					}
				}
				if(string.equalsIgnoreCase("dr")) {
					FinishedGood finGood = (FinishedGood)dao.load(podetails.getItemCode(), FinishedGood.class);
					if(finGood!=null) {
						finGood.setQuantityPerRecord(finGood.getQuantityPerRecord()+podetails.getQuantity());
						dao.update(finGood,session);
					}
				}
			}
		}catch(Exception e) {
				e.printStackTrace();
		}
	}
	*/
	
	
	//loadIngredients
	public Ingredient loadIngredientPrices(Ingredient originalIngredient,Session session){
		Ingredient ingredient = null;
		try{
		List list = dao.listByParameter(RawMaterial.class, "itemCode",
				originalIngredient.getProductCode(), session);
		RawMaterial item = (RawMaterial) list.get(0);
		ingredient = new Ingredient(
				item.getItemCode(),
				item.getDescription(),
				originalIngredient.getQuantity(),
				item.getUnitOfMeasurement(),
				item.getItemPricing().getCompanyOwnedStandardPricePerUnit()
						* originalIngredient.getQuantity(),
				item.getItemPricing().getCompanyOwnedActualPricePerUnit() * originalIngredient.getQuantity(),
				item.getItemPricing().getCompanyOwnedTransferPricePerUnit()
						* originalIngredient.getQuantity());
		}catch (IndexOutOfBoundsException iobe) {
			try{
				List list = dao.listByParameter(FinishedGood.class, "productCode",
					originalIngredient.getProductCode(), session);
			FinishedGood item = (FinishedGood) list.get(0);
			ingredient = new Ingredient(
					item.getProductCode(),
					item.getDescription(),
					originalIngredient.getQuantity(),
					item.getUnitOfMeasurement(),
					item.getItemPricing().getCompanyOwnedStandardPricePerUnit()
							* originalIngredient.getQuantity(),
					item.getItemPricing().getCompanyOwnedActualPricePerUnit() * originalIngredient.getQuantity(),
					item.getItemPricing().getCompanyOwnedTransferPricePerUnit()
							* originalIngredient.getQuantity());
			}catch(IndexOutOfBoundsException ioeb2) {
				List list = dao.listByParameter(TradedItem.class, "itemCode",
						originalIngredient.getProductCode(), session);
				TradedItem item = (TradedItem) list.get(0);
				ingredient = new Ingredient(
						item.getItemCode(),
						item.getDescription(),
						originalIngredient.getQuantity(),
						item.getUnitOfMeasurement(),
						item.getItemPricing().getCompanyOwnedStandardPricePerUnit()
								* originalIngredient.getQuantity(),
						item.getItemPricing().getCompanyOwnedActualPricePerUnit() * originalIngredient.getQuantity(),
						item.getItemPricing().getCompanyOwnedTransferPricePerUnit()
								* originalIngredient.getQuantity());
			}
		}
		return ingredient;
	}
	
	public List loadItemListFromRawAndFin(Session session){
		
		List rawAndFinList = dao.listAlphabeticalAscByParameter(RawMaterial.class, "itemCode",session);
		List finList = dao.listAlphabeticalAscByParameter(FinishedGood.class, "productCode", session);
		List tradedItemList = dao.listAlphabeticalAscByParameter(TradedItem.class, "itemCode", session);
		
		Iterator finListItr = finList.iterator();
		Iterator tradedListItr = tradedItemList.iterator();
		while(finListItr.hasNext()){
			FinishedGood finGood = (FinishedGood) finListItr.next();
			
			rawAndFinList.add(new RawMaterial(finGood.getProductCode(), finGood.getDescription(), finGood.getUnitOfMeasurement(), finGood.getItemPricing()));
			//rawAndFinList.add(new RawMaterial(ti.getItemCode(), ti.getDescription(), ti.getUnitOfMeasurement(), ti.getItemPricing()));
		}
		while(tradedListItr.hasNext()){
			TradedItem ti = (TradedItem) tradedListItr.next();
			rawAndFinList.add(new RawMaterial(ti.getItemCode(), ti.getDescription(), ti.getUnitOfMeasurement(), ti.getItemPricing()));
		}
		
		return rawAndFinList;  
	}
	
	//used to generate new Supplier Order Form
	public Map getLatestInventoryItemUom(Session session){
		Map<String, String> itemsWithUom = new HashMap();
		
		List rawAndFinList = loadItemListFromRawAndFin(session);
		
		Iterator itemIterator = rawAndFinList.iterator();
		while(itemIterator.hasNext()){
			RawMaterial rawMaterial = (RawMaterial)itemIterator.next();
			itemsWithUom.put(rawMaterial.getItemCode(), rawMaterial.getUnitOfMeasurement());
		}
		
		return itemsWithUom;
	}
	
	public double getItemPricingByItemCodeAndParameter(Session session, String itemCode, String customerType, String priceType) {
		ItemPricing itemPricing = dao.getItemPricingByItemCodeAndParameter(session, itemCode);
		double price = 0.0;
		if(itemCode.equalsIgnoreCase("FL09")) {
			System.out.println(priceType.toLowerCase());
		}
		try {
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
		}else if(customerType.equalsIgnoreCase("CC")) {
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
		}catch(Exception e){
			return formatPrice(0.0);
		}
		
		return formatPrice(price);
	}
	
	public double formatPrice(double value) {
		DecimalFormat df = new DecimalFormat("########0.00##");
		return Double.valueOf(df.format(value));
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Item> getAllItemList(Session session) {
		Iterator iterator = null;
		
		List<RawMaterial> rawMatList =listAlphabeticalAscByParameter(RawMaterial.class, "subClassification",session);
		List<TradedItem> tradedItemList =listAlphabeticalAscByParameter(TradedItem.class, "subClassification",session);
		List<FinishedGood> finList = listAlphabeticalAscByParameter(FinishedGood.class, "subClassification", session);
		
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
		
		
		iterator = finList.iterator();
		while(iterator.hasNext()){
			FinishedGood finGood = (FinishedGood) iterator.next();
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				Item item = new Item(finGood.getProductCode(), finGood.getDescription(), finGood.getUnitOfMeasurement(),finGood.getClassification(),finGood.getSubClassification(),finGood.getIsVattable());
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				item.setItemType("finGood");
				tempList.add(item);
		}
		
		return tempList;
	}
	
	public List getStockStatusInBetweenMonthAndYear(String dateFrom, String dateTo, String className,
			Session session) {
		return dao.getStockStatusInBetweenMonthAndYear(dateFrom, dateTo, className, session);
	}

	public List<PurchaseOrderDetails> getRelatedOrders(String itemCode,List<String> ordersRelated) {
		// TODO Auto-generated method stub
		return dao.getRelatedOrders(itemCode,ordersRelated);
	}
	
	public Map<String, String> getRelatedReturnSlipIds(String itemCode,List<String> ordersRelated) {
		// TODO Auto-generated method stub
		return dao.getRelatedReturnSlipIds(itemCode,ordersRelated);
	}

}
