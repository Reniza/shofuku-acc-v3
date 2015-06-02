package com.shofuku.accsystem.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;

public class PurchaseOrderDetailHelper {

	Map<String,Object> actionSession;
	
	public Map<String, Object> getActionSession() {
		return actionSession;
	}
	public void setActionSession(Map<String, Object> actionSession) {
		this.actionSession = actionSession;
	}
	private void initializeController() {
		accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
		
	}
	
	AccountEntryManager accountEntryManager;
	InventoryManager inventoryManager;
	
	public PurchaseOrderDetailHelper(Map<String, Object> actionSession) {
		this.actionSession = actionSession;
	}
	public PurchaseOrderDetailHelper() {
	}

	private Timestamp orderDate;

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	String hiddenDelimetedOrderDetailsItemCode;
	String hiddenDelimetedOrderDetailsDescription;
	String hiddenDelimetedOrderDetailsQuantity;
	String hiddenDelimetedOrderDetailsUOM;
	String hiddenDelimetedOrderDetailsCost;
	String hiddenDelimetedOrderDetailsAmount;
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	String hiddenDelimetedOrderDetailsIsVattable;
	String hiddenDelimetedOrderDetailsVatAmount;
	String hiddenDelimetedOrderDetailsVattableAmount;
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	String[] splitStringItemCode;
	String[] splitStringDescription;
	String[] splitStringQuantity;
	String[] splitStringUOM;
	String[] splitStringCost;
	String[] splitStringAmount;
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	String[] splitStringIsVattable;
	String[] splitStringVatAmount;
	String[] splitStringVattableAmount;
	//END: 2013 - PHASE 3 : PROJECT 4: MARK

	List<PurchaseOrderDetails> purchaseOrderDetailsList;
	Set<PurchaseOrderDetails> purchaseOrderDetailsSet;

	public List<PurchaseOrderDetails> getPurchaseOrderDetailsList() {
		return purchaseOrderDetailsList;
	}

	public void setPurchaseOrderDetailsList(
			List<PurchaseOrderDetails> purchaseOrderDetailsList) {
		this.purchaseOrderDetailsList = purchaseOrderDetailsList;
	}

	public Set<PurchaseOrderDetails> getPurchaseOrderDetailsSet() {
		return purchaseOrderDetailsSet;
	}

	public void setPurchaseOrderDetailsSet(
			Set<PurchaseOrderDetails> purchaseOrderDetailsSet) {
		this.purchaseOrderDetailsSet = purchaseOrderDetailsSet;
	}

	public String getHiddenDelimetedOrderDetailsItemCode() {
		return hiddenDelimetedOrderDetailsItemCode;
	}

	public void setHiddenDelimetedOrderDetailsItemCode(
			String hiddenDelimetedOrderDetailsItemCode) {
		this.hiddenDelimetedOrderDetailsItemCode = hiddenDelimetedOrderDetailsItemCode;
	}

	public String getHiddenDelimetedOrderDetailsDescription() {
		return hiddenDelimetedOrderDetailsDescription;
	}

	public void setHiddenDelimetedOrderDetailsDescription(
			String hiddenDelimetedOrderDetailsDescription) {
		this.hiddenDelimetedOrderDetailsDescription = hiddenDelimetedOrderDetailsDescription;
	}

	public String getHiddenDelimetedOrderDetailsQuantity() {
		return hiddenDelimetedOrderDetailsQuantity;
	}

	public void setHiddenDelimetedOrderDetailsQuantity(
			String hiddenDelimetedOrderDetailsQuantity) {
		this.hiddenDelimetedOrderDetailsQuantity = hiddenDelimetedOrderDetailsQuantity;
	}

	public String getHiddenDelimetedOrderDetailsUOM() {
		return hiddenDelimetedOrderDetailsUOM;
	}

	public void setHiddenDelimetedOrderDetailsUOM(
			String hiddenDelimetedOrderDetailsUOM) {
		this.hiddenDelimetedOrderDetailsUOM = hiddenDelimetedOrderDetailsUOM;
	}

	public String getHiddenDelimetedOrderDetailsCost() {
		return hiddenDelimetedOrderDetailsCost;
	}

	public void setHiddenDelimetedOrderDetailsCost(
			String hiddenDelimetedOrderDetailsCost) {
		this.hiddenDelimetedOrderDetailsCost = hiddenDelimetedOrderDetailsCost;
	}

	public String getHiddenDelimetedOrderDetailsAmount() {
		return hiddenDelimetedOrderDetailsAmount;
	}

	public void setHiddenDelimetedOrderDetailsAmount(
			String hiddenDelimetedOrderDetailsAmount) {
		this.hiddenDelimetedOrderDetailsAmount = hiddenDelimetedOrderDetailsAmount;
	}
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	public String getHiddenDelimetedOrderDetailsVatAmount() {
		return hiddenDelimetedOrderDetailsVatAmount;
	}

	public void setHiddenDelimetedOrderDetailsVatAmount(
			String hiddenDelimetedOrderDetailsVatAmount) {
		this.hiddenDelimetedOrderDetailsVatAmount = hiddenDelimetedOrderDetailsVatAmount;
	}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	public String[] getSplitStringItemCode() {
		return splitStringItemCode;
	}

	public void setSplitStringItemCode(String[] splitStringItemCode) {
		this.splitStringItemCode = splitStringItemCode;
	}

	public String[] getSplitStringDescription() {
		return splitStringDescription;
	}

	public void setSplitStringDescription(String[] splitStringDescription) {
		this.splitStringDescription = splitStringDescription;
	}

	public String[] getSplitStringQuantity() {
		return splitStringQuantity;
	}

	public void setSplitStringQuantity(String[] splitStringQuantity) {
		this.splitStringQuantity = splitStringQuantity;
	}

	public String[] getSplitStringUOM() {
		return splitStringUOM;
	}

	public void setSplitStringUOM(String[] splitStringUOM) {
		this.splitStringUOM = splitStringUOM;
	}

	public String[] getSplitStringCost() {
		return splitStringCost;
	}

	public void setSplitStringCost(String[] splitStringCost) {
		this.splitStringCost = splitStringCost;
	}

	public String[] getSplitStringAmount() {
		return splitStringAmount;
	}

	public void setSplitStringAmount(String[] splitStringAmount) {
		this.splitStringAmount = splitStringAmount;
	}
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	public String getHiddenDelimetedOrderDetailsIsVattable() {
		return hiddenDelimetedOrderDetailsIsVattable;
	}

	public void setHiddenDelimetedOrderDetailsIsVattable(
			String hiddenDelimetedOrderDetailsIsVattable) {
		this.hiddenDelimetedOrderDetailsIsVattable = hiddenDelimetedOrderDetailsIsVattable;
	}

	public String[] getSplitStringIsVattable() {
		return splitStringIsVattable;
	}

	public void setSplitStringIsVattable(String[] splitStringIsVattable) {
		this.splitStringIsVattable = splitStringIsVattable;
	}

	public String[] getSplitStringVatAmount() {
		return splitStringVatAmount;
	}

	public void setSplitStringVatAmount(String[] splitStringVatAmount) {
		this.splitStringVatAmount = splitStringVatAmount;
	}
	public String getHiddenDelimetedOrderDetailsVattableAmount() {
		return hiddenDelimetedOrderDetailsVattableAmount;
	}

	public void setHiddenDelimetedOrderDetailsVattableAmount(
			String hiddenDelimetedOrderDetailsVattableAmount) {
		this.hiddenDelimetedOrderDetailsVattableAmount = hiddenDelimetedOrderDetailsVattableAmount;
	}

	public String[] getSplitStringVattableAmount() {
		return splitStringVattableAmount;
	}

	public void setSplitStringVattableAmount(String[] splitStringVattableAmount) {
		this.splitStringVattableAmount = splitStringVattableAmount;
	}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	
	// Helper methods start heres

	/*
	 * returns 1 if size is more than 1
	 */
	private int checkDelimitedCount(String[] toCheck) {
		if(toCheck.length>0) {
			return 1;
		}else {
			return 0;
		}
	}
	

	

	
	private void parseCommaDelimetedFields() {
		if(null==hiddenDelimetedOrderDetailsItemCode||"".equalsIgnoreCase(hiddenDelimetedOrderDetailsItemCode)) {
		}else {
			splitStringItemCode = hiddenDelimetedOrderDetailsItemCode.split(",");
			splitStringDescription = hiddenDelimetedOrderDetailsDescription.split(",");
			splitStringQuantity = hiddenDelimetedOrderDetailsQuantity.split(",");
			splitStringUOM = hiddenDelimetedOrderDetailsUOM.split(",");
			splitStringCost = hiddenDelimetedOrderDetailsCost.split(",");
			splitStringAmount = hiddenDelimetedOrderDetailsAmount.split(",");
			//START: 2013 - PHASE 3 : PROJECT 4: MARK
			splitStringVatAmount = hiddenDelimetedOrderDetailsVatAmount.split(",");
			splitStringIsVattable = hiddenDelimetedOrderDetailsIsVattable.split(",");
			splitStringVattableAmount = hiddenDelimetedOrderDetailsVattableAmount.split(",");
			//END: 2013 - PHASE 3 : PROJECT 4: MARK
			if (checkDelimitedCount(splitStringItemCode)>0 ) {
				for (int x = 0; x < splitStringItemCode.length; x++) {
					populatePOSet(x);
				}
			}
		}
	}
	
	public void clearCommaDelimitedValues() {
		setHiddenDelimetedOrderDetailsItemCode("");
		setHiddenDelimetedOrderDetailsDescription("");
		setHiddenDelimetedOrderDetailsQuantity("");
		setHiddenDelimetedOrderDetailsUOM("");
		setHiddenDelimetedOrderDetailsCost("");
		setHiddenDelimetedOrderDetailsAmount("");
		//START: 2013 - PHASE 3 : PROJECT 4: MARK
		setHiddenDelimetedOrderDetailsIsVattable("");
		setHiddenDelimetedOrderDetailsVatAmount("");
		setHiddenDelimetedOrderDetailsVattableAmount("");
		//END: 2013 - PHASE 3 : PROJECT 4: MARK
	}
	
	public void generateCommaDelimitedValues(){
		Iterator<PurchaseOrderDetails> itr =purchaseOrderDetailsSet.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails)itr.next();
			addHiddenDelimetedOrderDetails(poDetails);
		}
	}
	
	private Transaction getCurrentTransaction(Session session){
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
		}catch(RuntimeException runtimeExecption){
			tx = session.getTransaction();
		}		
		return tx;
	}
	public Set<PurchaseOrderDetails> persistNewSetElements(Session ss) {
		Set<PurchaseOrderDetails> poset = generatePODetailsSet();
		Iterator<PurchaseOrderDetails> itr = poset.iterator();
		Set<PurchaseOrderDetails> persistedSet = new HashSet<PurchaseOrderDetails>();
		Transaction tx = null;
		tx = getCurrentTransaction(ss);
		while(itr.hasNext()) {
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails)itr.next();
			if(orderDate!=null)
			poDetails.setOrderCreatedDate(orderDate);
			try {
				ss.save(poDetails);
				persistedSet.add(poDetails);
			} catch (RuntimeException re) {
				if (null != tx) {
					tx.rollback();
				}
				re.printStackTrace();
			} 
		}
		return persistedSet;
	}
	
	
	void populatePOSet(int x){
		DoubleConverter dc = new DoubleConverter();
		//START: 2013 - PHASE 3 : PROJECT 4: MARK
		String itemCode= checkDelimitedCount(splitStringItemCode) > 0 ? null == splitStringItemCode[x] ? ""	: "" == splitStringItemCode[x] ? ""	: splitStringItemCode[x]: "";
		String description = checkDelimitedCount(splitStringDescription) > 0 ? null == splitStringDescription[x] ? "": "" == splitStringDescription[x] ? ""	: splitStringDescription[x]	: "" ;
		double quantity = checkDelimitedCount(splitStringQuantity) > 0 ? null == splitStringQuantity[x] ? 0.0: "" == splitStringQuantity[x] ? 0.0 : Double.valueOf(splitStringQuantity[x]): 0.0;
		String unitOfMeasurement = checkDelimitedCount(splitStringUOM) > 0 ? null == splitStringUOM[x] ? ""	: "" == splitStringUOM[x] ? ""	: splitStringUOM[x]	: "";
		double unitCost = checkDelimitedCount(splitStringCost) > 0 ? null == splitStringCost[x] ? 0.0: "" == splitStringCost[x] ? 0.0: Double.valueOf(dc.convertFromStringNoContextForDetailsTable(splitStringCost[x])): 0.0;
		double amount = 	checkDelimitedCount(splitStringAmount) > 0 ? null == splitStringAmount[x] ? 0.0: "" == splitStringAmount[x] ? 0.0: Double.valueOf(dc.convertFromStringNoContextForDetailsTable(splitStringAmount[x])): 0.0;
		String isVattable = checkDelimitedCount(splitStringIsVattable) > 0 ? null == splitStringIsVattable[x] ? ""	: "" == splitStringIsVattable[x] ? ""	: splitStringIsVattable[x]	: "";
		double vatAmount = checkDelimitedCount     (splitStringVatAmount)      > 0 ? null == splitStringVatAmount[x]      ? 0.0: "" == splitStringVatAmount[x]      ? 0.0: Double.valueOf(dc.convertFromStringNoContextForDetailsTable(splitStringVatAmount[x]     )): 0.0;
		double vattableAmount = checkDelimitedCount(splitStringVattableAmount) > 0 ? null == splitStringVattableAmount[x] ? 0.0: "" == splitStringVattableAmount[x] ? 0.0: Double.valueOf(dc.convertFromStringNoContextForDetailsTable(splitStringVattableAmount[x])): 0.0;
		
		if(isVattable.equalsIgnoreCase("Y")) {
			vattableAmount = amount / 1.12;
			vatAmount = vattableAmount *.12;
		}
			
		
		purchaseOrderDetailsSet.add(new PurchaseOrderDetails(itemCode,description,quantity,unitOfMeasurement,unitCost,amount,isVattable,vatAmount,vattableAmount));
		//END: 2013 - PHASE 3 : PROJECT 4: MARK
	}
	
	
	
	public Set<PurchaseOrderDetails> generatePODetailsSet(){
		purchaseOrderDetailsSet = new HashSet<PurchaseOrderDetails>();
		parseCommaDelimetedFields();
		return purchaseOrderDetailsSet;
	}
	
	public Set<PurchaseOrderDetails> generatePODetailsSet(Set<PurchaseOrderDetails> detailsSet){
		purchaseOrderDetailsSet = detailsSet;
		
		parseCommaDelimetedFields();
		return purchaseOrderDetailsSet;
	}

	
	private boolean isExistingInSet(String itemCode) {
		purchaseOrderDetailsSet = new HashSet<PurchaseOrderDetails>();
		parseCommaDelimetedFields();
		Iterator<PurchaseOrderDetails> itr =purchaseOrderDetailsSet.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
			if(podetails.getItemCode().toLowerCase().equals(itemCode.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	private PurchaseOrderDetails getOrderOnCurrentOrderList(String itemCode) {
		purchaseOrderDetailsSet = new HashSet<PurchaseOrderDetails>();
		parseCommaDelimetedFields();
		Iterator<PurchaseOrderDetails> itr =purchaseOrderDetailsSet.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
			if(podetails.getItemCode().toLowerCase().equals(itemCode.toLowerCase())) {
				return podetails;
			}
		}
		return null;
	}
	
	public void modifyPurchaseOrderDetail(PurchaseOrderDetails purchaseOrderDetail) {
		//check if existing
		PurchaseOrderDetails currentOrder = new PurchaseOrderDetails();
		if(isExistingInSet(purchaseOrderDetail.getItemCode())) {
				currentOrder = getOrderOnCurrentOrderList(purchaseOrderDetail.getItemCode());
				removePurchaseOrderDetail(purchaseOrderDetail.getItemCode());
		}else {
			currentOrder = purchaseOrderDetail;
		}
		purchaseOrderDetail.setQuantity((currentOrder.getQuantity()+purchaseOrderDetail.getQuantityIn())-purchaseOrderDetail.getQuantityOut());
		if(purchaseOrderDetail.getQuantity()<0) {
			//meaning qty out deducted more than existing order and therefore leaving a negative number of orders
			purchaseOrderDetail.setQuantity(0);
			purchaseOrderDetail.setAmount(0);
			purchaseOrderDetail.setUnitCost(0);
			//removePurchaseOrderDetail(purchaseOrderDetail.getItemCode());
		}else {
			if(purchaseOrderDetail.getAmount()==0) {
				purchaseOrderDetail.setAmount(purchaseOrderDetail.getQuantity()*purchaseOrderDetail.getUnitCost());
			}else {
				purchaseOrderDetail.setAmount(purchaseOrderDetail.getQuantity()*purchaseOrderDetail.getUnitCost());
			}
			//add to the comma delimited string
			addHiddenDelimetedOrderDetails(purchaseOrderDetail);
			//add to the set and list
			generatePODetailsSet();
			generatePODetailsListFromSet(purchaseOrderDetailsSet);
		}
		
		
	}
	
	public void removePurchaseOrderDetail(String  itemCode) {
		purchaseOrderDetailsSet =  new HashSet<PurchaseOrderDetails>();
		parseCommaDelimetedFields();
		purchaseOrderDetailsList = new ArrayList<PurchaseOrderDetails>();
		Iterator<PurchaseOrderDetails> itr =purchaseOrderDetailsSet.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
			if(podetails.getItemCode().toLowerCase().equals(itemCode.toLowerCase())) {
				itr.remove();
			}else {
				purchaseOrderDetailsList.add(podetails);
			}
		}
		clearCommaDelimitedValues();
		generateCommaDelimitedValues();
	}
	
	
	public void generateItemTypesForExistingItems(Session session) {
		initializeController();
		List<Item> allItemList = new ArrayList<Item>();
		allItemList = inventoryManager.getAllItemList(session);
		Iterator itr  =allItemList.iterator();
		
		Map<String, String> itemsSet = new HashMap();
		while(itr.hasNext()) {
			Item item = (Item)itr.next();
			itemsSet.put(item.getItemCode(), item.getItemType());
		}
		
		itr  =purchaseOrderDetailsList.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
			podetails.setItemType(itemsSet.get(podetails.getItemCode()));
		}
	}
	
	public List<PurchaseOrderDetails> generatePODetailsListFromSet(Set<PurchaseOrderDetails> poSet){
		purchaseOrderDetailsList = new ArrayList<PurchaseOrderDetails>();
		purchaseOrderDetailsSet = poSet;
		
		try {
		Iterator<PurchaseOrderDetails> itr =poSet.iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
				//TODO: ADD ISVATTABLE FROM DATABASE
				purchaseOrderDetailsList.add(podetails);
			}
			
			sortListsAlphabetically();
			
		}catch(NullPointerException nfe) {
			purchaseOrderDetailsSet = new HashSet<PurchaseOrderDetails>();
			nfe.printStackTrace();
			
		}
		
		return purchaseOrderDetailsList;
	}
	
	private void sortListsAlphabetically(){
		
		List purchaseOrderDetailsList= this.purchaseOrderDetailsList;
		List sortedPurchaseOrderDetailsList= new ArrayList();
		
		HashMap<String,PurchaseOrderDetails> map = new HashMap<String,PurchaseOrderDetails>();
		Set<PurchaseOrderDetails> sortedMap = new HashSet<PurchaseOrderDetails>();
		List itemCodeList = new ArrayList();
		List unlistedItemsList= new ArrayList();
		HashMap<String,PurchaseOrderDetails> unlistedItemsMap = new HashMap<String,PurchaseOrderDetails>();
		
		try {
			Iterator<PurchaseOrderDetails> itr =purchaseOrderDetailsList.iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
				if(null==podetails.getItemCode() || podetails.getItemCode().trim().equalsIgnoreCase("")) {
					unlistedItemsMap.put(SASConstants.NOT_APPLICABLE,podetails);
					// YOU LEFT HERE PROBLEM: PODETAILS CANT SHOW FOR SAME ITEM CODES WHICH IS BLANK FOR UNLISTED ITEMS
					unlistedItemsList.add(podetails);
					
				}else {
					map.put(podetails.getItemCode(),podetails);
					itemCodeList.add(podetails.getItemCode());
				}
			}
			
			Collections.sort(itemCodeList);
			
			Iterator<String> iteratorSorted =itemCodeList.iterator();
			while(iteratorSorted.hasNext()) {
				String  code = (String)iteratorSorted.next();
				sortedPurchaseOrderDetailsList.add(map.get(code));
				sortedMap.add(map.get(code));
			}
			
			//include unlisted items
			Iterator unlistedItemsMapItr = unlistedItemsList.iterator();
			while(unlistedItemsMapItr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails)unlistedItemsMapItr.next();
				sortedMap.add(podetails);
			}
			sortedPurchaseOrderDetailsList.addAll(unlistedItemsList);
			//end include unlisted items
			
			
			this.setPurchaseOrderDetailsSet(sortedMap);
			this.setPurchaseOrderDetailsList(sortedPurchaseOrderDetailsList);
		}catch(NullPointerException nfe) {
			nfe.printStackTrace();
		}
		
				
	}
	
	
	public void prepareSetAndList(){
		if(null==purchaseOrderDetailsSet) {
			purchaseOrderDetailsSet =  new HashSet<PurchaseOrderDetails>();
		}
		if(null==purchaseOrderDetailsList) {
			purchaseOrderDetailsList = new ArrayList<PurchaseOrderDetails>();
			purchaseOrderDetailsSet =  new HashSet<PurchaseOrderDetails>();
		}
		parseCommaDelimetedFields();
		generatePODetailsListFromSet(purchaseOrderDetailsSet);
	}
	
	void addHiddenDelimetedOrderDetails(PurchaseOrderDetails orderDetails) {
		//used item code since this cant be null
		try{splitStringItemCode = hiddenDelimetedOrderDetailsItemCode.split(",");
		if(checkDelimitedCount(splitStringItemCode)==0) {
			setHiddenDelimetedOrderDetailsItemCode(orderDetails.getItemCode() + ",");
			setHiddenDelimetedOrderDetailsDescription(orderDetails.getDescription() + ",");
			setHiddenDelimetedOrderDetailsQuantity(orderDetails.getQuantity() + ",");
			setHiddenDelimetedOrderDetailsUOM(orderDetails.getUnitOfMeasurement() + ",");
			setHiddenDelimetedOrderDetailsCost(orderDetails.getUnitCost() + ",");
			setHiddenDelimetedOrderDetailsAmount(orderDetails.getAmount() + ",");
			//START: 2013 - PHASE 3 : PROJECT 4: MARK
			setHiddenDelimetedOrderDetailsIsVattable(orderDetails.getIsVattable() + ",");
			setHiddenDelimetedOrderDetailsVatAmount(orderDetails.getVatAmount() + ",");
			setHiddenDelimetedOrderDetailsVattableAmount(orderDetails.getVattableAmount() + ",");
			//END: 2013 - PHASE 3 : PROJECT 4: MARK
		}else {
			setHiddenDelimetedOrderDetailsItemCode(hiddenDelimetedOrderDetailsItemCode + ("".equalsIgnoreCase(orderDetails.getItemCode())?" ":orderDetails.getItemCode()) + ",");
			setHiddenDelimetedOrderDetailsDescription(hiddenDelimetedOrderDetailsDescription+ ("".equalsIgnoreCase(orderDetails.getDescription())?" ":orderDetails.getDescription()) + ",");
			setHiddenDelimetedOrderDetailsQuantity(hiddenDelimetedOrderDetailsQuantity + ("".equalsIgnoreCase(String.valueOf(orderDetails.getQuantity()))?0.0:orderDetails.getQuantity())+ ",");
			setHiddenDelimetedOrderDetailsUOM(hiddenDelimetedOrderDetailsUOM + ("".equalsIgnoreCase(orderDetails.getUnitOfMeasurement())?" ":orderDetails.getUnitOfMeasurement())+ ",");
			setHiddenDelimetedOrderDetailsCost(hiddenDelimetedOrderDetailsCost+ ("".equalsIgnoreCase(String.valueOf(orderDetails.getUnitCost()))?0.0:orderDetails.getUnitCost()) + ",");
			setHiddenDelimetedOrderDetailsAmount(hiddenDelimetedOrderDetailsAmount + ("".equalsIgnoreCase(String.valueOf(orderDetails.getAmount()))?0.0:orderDetails.getAmount()) + ",");
			//START: 2013 - PHASE 3 : PROJECT 4: MARK
			setHiddenDelimetedOrderDetailsIsVattable(hiddenDelimetedOrderDetailsIsVattable +  ("".equalsIgnoreCase(String.valueOf(orderDetails.getIsVattable()))?0.0:orderDetails.getIsVattable()) + ",");
			setHiddenDelimetedOrderDetailsVatAmount(hiddenDelimetedOrderDetailsVatAmount +  ("".equalsIgnoreCase(String.valueOf(orderDetails.getVatAmount()))?0.0:orderDetails.getVatAmount()) + ",");
			setHiddenDelimetedOrderDetailsVattableAmount(hiddenDelimetedOrderDetailsVattableAmount +  ("".equalsIgnoreCase(String.valueOf(orderDetails.getVattableAmount()))?0.0:orderDetails.getVattableAmount()) + ",");
			//END: 2013 - PHASE 3 : PROJECT 4: MARK
		}
		}catch(NullPointerException nfe) {
			setHiddenDelimetedOrderDetailsItemCode(orderDetails.getItemCode() + ",");
			setHiddenDelimetedOrderDetailsDescription(orderDetails.getDescription() + ",");
			setHiddenDelimetedOrderDetailsQuantity(orderDetails.getQuantity() + ",");
			setHiddenDelimetedOrderDetailsUOM(orderDetails.getUnitOfMeasurement() + ",");
			setHiddenDelimetedOrderDetailsCost(orderDetails.getUnitCost() + ",");
			setHiddenDelimetedOrderDetailsAmount(orderDetails.getAmount() + ",");
			//START: 2013 - PHASE 3 : PROJECT 4: MARK
			setHiddenDelimetedOrderDetailsIsVattable(orderDetails.getIsVattable() + ",");
			setHiddenDelimetedOrderDetailsVatAmount(orderDetails.getVatAmount() + ",");
			setHiddenDelimetedOrderDetailsVattableAmount(orderDetails.getVattableAmount() + ",");
			//END: 2013 - PHASE 3 : PROJECT 4: MARK
		}
	}

	public double getTotalAmount() {
		double total = 0;
		for(int x =0 ; x < purchaseOrderDetailsList.size();x++) {
			total+=((PurchaseOrderDetails)purchaseOrderDetailsList.get(x)).getAmount();
		}
		return total;
	}
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	public double getTotalVatAmount() {
		double total = 0;
		for(int x =0 ; x < purchaseOrderDetailsList.size();x++) {
			total+=((PurchaseOrderDetails)purchaseOrderDetailsList.get(x)).getVatAmount();
		}
		return total;
	}
	public double getTotalVattableAmount() {
		double total = 0;
		for(int x =0 ; x < purchaseOrderDetailsList.size();x++) {
			total+=((PurchaseOrderDetails)purchaseOrderDetailsList.get(x)).getVattableAmount();
		}
		return total;
	}
	public double getTotalNonVattableAmount() {
		double total = 0;
		total = getTotalAmount() - (getTotalVattableAmount() + getTotalVatAmount());
		return total;
	}
	public double getTotalVatedAmount() {
		double total = 0;
		total = getTotalVattableAmount() + getTotalVatAmount();
		return total;
	}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
}
