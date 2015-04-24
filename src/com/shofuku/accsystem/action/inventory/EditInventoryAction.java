package com.shofuku.accsystem.action.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.action.AddOrderDetailsAction;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.UnlistedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.lookups.InventoryClassification;
import com.shofuku.accsystem.domain.lookups.UnitOfMeasurements;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class EditInventoryAction extends AddOrderDetailsAction{

	private static final long serialVersionUID = 1L;
	private String subModule;
	private String forWhat;
	private String forWhatDisplay;
	
	RawMaterial rm;
	FinishedGood fg;
	TradedItem ti;
	Utensils u;
	UnlistedItem unl;
	FPTS fpts;
	RequisitionForm rf;
	ReturnSlip rs;
	InventoryManager manager=new InventoryManager();
	List<Ingredient> ingredients;
	List itemCodeList;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
			List accountProfileCodeList;
			List<Transaction> transactionList;
			List<Transaction> transactions;
			Iterator itr;
			AccountEntryManager accountEntryManager = new AccountEntryManager();
			TransactionManager transactionMananger = new TransactionManager();
			
			//END 2013 - PHASE 3 : PROJECT 1: MARK 

	String itemCode;
	String isGeneralSearch;
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getIsGeneralSearch() {
		return isGeneralSearch;
	}
	public void setIsGeneralSearch(String isGeneralSearch) {
		this.isGeneralSearch = isGeneralSearch;
	}
	private String requestingModule;
	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	
	List UOMList;
	
	List itemSubClassificationList;
	
	PurchaseOrderDetailHelper poDetailsHelperDraft;
	private String parentPage;
	
	LookupManager lookupManager = new LookupManager();
	
	public List getUOMList() {
		return UOMList;
	}
	public void setUOMList(List uOMList) {
		UOMList = uOMList;
	}
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
		itemCodeList = manager.loadItemListFromRawAndFin(session);
		accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
		
		}catch(Exception e){
			if (getSubModule().equalsIgnoreCase("rawMat")) {
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")) {
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems")) {
				return "unlistedItems";
			}
			else {
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
		}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("unlistedItems")){
			return "unlistedItems";
		}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("fpts")){
			return "fpts";
		}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("rf")){
			return "rf";
		}else if(requestingModule!=null && requestingModule.equalsIgnoreCase("returnSlip")){
			return "returnSlip";
		}else{
			return "finGood";
		}
	}
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception {
		Session session = getSession();
		loadLookLists();
		
		    if(isGeneralSearch!=null){
				if(isGeneralSearch.equalsIgnoreCase("true")){
					if(subModule.equalsIgnoreCase("rawMat")){
						//di ako sure
						this.setRm(new RawMaterial());
						this.getRm().setItemCode(itemCode);
					}else if(subModule.equalsIgnoreCase("tradedItems")){
						//di ako sure
						this.setTi(new TradedItem());
						this.getTi().setItemCode(itemCode);
					}else if(subModule.equalsIgnoreCase("utensils")){
						//di ako sure
						this.setU(new Utensils());
						this.getU().setItemCode(itemCode);
					}else if(subModule.equalsIgnoreCase("unlistedItems")){
						//di ako sure
						this.setUnl(new UnlistedItem());
						this.getUnl().setItemCode(itemCode);
					}else if(subModule.equalsIgnoreCase("finGood")){
						//di ako sure
						this.setFg(new FinishedGood());
						this.getFg().setProductCode(itemCode);
					}
				}
			}
			
			try {
				forWhat = "true";
				forWhatDisplay="edit";
				accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
				
			if (getSubModule().equalsIgnoreCase("rawMat")) {
				RawMaterial rawMat = new RawMaterial();
				rawMat = (RawMaterial) manager.listInventoryByParameter(RawMaterial.class, "itemCode",
						this.getRm().getItemCode(),session).get(0);
				
				this.setRm(rawMat);
				
				itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
						rm.getClassification(), session);
				
				return "rawMat";
			
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				TradedItem ti = new TradedItem();
				ti = (TradedItem) manager.listInventoryByParameter(TradedItem.class, "itemCode",
						this.getTi().getItemCode(),session).get(0);
				
				this.setTi(ti);
				itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
						ti.getClassification(), session);
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")) {
				Utensils ti = new Utensils();
				u = (Utensils) manager.listInventoryByParameter(Utensils.class, "itemCode",
						this.getU().getItemCode(),session).get(0);
				
				this.setU(u);
				itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
						u.getClassification(), session);
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems")) {
				UnlistedItem unl = new UnlistedItem();
				unl = (UnlistedItem) manager.listInventoryByParameterLike(UnlistedItem.class, "description",
						this.getUnl().getDescription(),session).get(0);
				
				this.setUnl(unl);
				//itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
				//		ti.getClassification(), session);
				return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("fpts")) {
				
				FPTS fpts = new FPTS();
				fpts = (FPTS) manager.listInventoryByParameter(FPTS.class,"fptsNo", this.getFpts().getFptsNo(), session).get(0);
				
				InventoryManager invManager= new InventoryManager();
				Session fptsSession = getSession();
				List returnSlipList = invManager.listInventoryByParameter(ReturnSlip.class, "returnSlipReferenceOrderNo", fpts.getFptsNo(), fptsSession);
				
				if(returnSlipList.size()>0) {
					fpts.setReturnSlipList(returnSlipList);
				}else {
					fpts.setReturnSlipList(null);
				}
				
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper();
				}
				poDetailsHelperToCompare.generatePODetailsListFromSet(fpts.getPurchaseOrderDetailsReceived());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
				if(null==poDetailsHelper) {
					poDetailsHelper = new PurchaseOrderDetailHelper();
				}
				poDetailsHelper.generatePODetailsListFromSet(fpts.getPurchaseOrderDetailsTransferred());
				poDetailsHelper.generateCommaDelimitedValues();
			
				//START Phase 3 - Azhee
				List tempList = transactionMananger.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", fpts.getFptsNo(), session);
				if(tempList.size()>0) {
					itr = tempList.iterator();
					transactionList = new ArrayList<Transaction>(); 
					while(itr.hasNext()) {
						Transaction transaction = (Transaction)itr.next();
						if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
							transactionList.add(transaction);
						}
					}
				}else {
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transactionList.add(transaction);
				}
				this.setTransactionList(transactionList);
				this.fpts.setTransactions(transactionList);
				//END Phase 3 - Azhee
				this.setFpts(fpts);
				
				return "fpts";
			
			}else if (getSubModule().equalsIgnoreCase("rf")) {
				
				RequisitionForm rf = new RequisitionForm();
				rf = (RequisitionForm) manager.listInventoryByParameter(RequisitionForm.class,"requisitionNo", this.getRf().getRequisitionNo(), session).get(0);
				
				/*
				 * Checking and fetching existing return slips
				 */
				
				InventoryManager invManager= new InventoryManager();
				Session rfSession = getSession();
				List returnSlipList = invManager.listInventoryByParameter(ReturnSlip.class, "returnSlipReferenceOrderNo", rf.getRequisitionNo(), rfSession);
				
				if(returnSlipList.size()>0) {
					rf.setReturnSlipList(returnSlipList);
				}else {
					rf.setReturnSlipList(null);
				}
				
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper();
				}
				poDetailsHelperToCompare.generatePODetailsListFromSet(rf.getPurchaseOrderDetailsReceived());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
				if(null==poDetailsHelper) {
					poDetailsHelper = new PurchaseOrderDetailHelper();
				}
				poDetailsHelper.generatePODetailsListFromSet(rf.getPurchaseOrderDetailsOrdered());
				poDetailsHelper.generateCommaDelimitedValues();
			
				//START Phase 3 - Azhee
				List tempList = transactionMananger.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", rf.getRequisitionNo(), session);
				if(tempList.size()>0) {
					itr = tempList.iterator();
					transactionList = new ArrayList<Transaction>(); 
					while(itr.hasNext()) {
						Transaction transaction = (Transaction)itr.next();
						if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
							transactionList.add(transaction);
						}
					}
				}else {
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transactionList.add(transaction);
				}
				this.setTransactionList(transactionList);
				this.rf.setTransactions(transactionList);
				//END Phase 3 - Azhee
				this.setRf(rf);
				
				return "rf";
			
			}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
				ReturnSlip rs = new ReturnSlip();
				rs = (ReturnSlip) manager.listInventoryByParameter(ReturnSlip .class, "returnSlipNo",
						this.getRs().getReturnSlipNo(),session).get(0);
				
				poDetailsHelperDraft = new PurchaseOrderDetailHelper();
				poDetailsHelperDraft.generatePODetailsListFromSet(rs.getPurchaseOrderDetails());
				poDetailsHelperDraft.generateCommaDelimitedValues();
				
				Iterator itr = poDetailsHelperDraft.getPurchaseOrderDetailsList().iterator();
				itemCodeList = new ArrayList();
				while(itr.hasNext()) {
					PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr.next();
					this.itemCodeList.add(tempDetails.getItemCode());
				}
				
				poDetailsHelperToCompare = new PurchaseOrderDetailHelper();
				poDetailsHelperToCompare.generatePODetailsListFromSet(loadOrdersByReferenceNo(rs));
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				//2014 - ITEM COLORING
				poDetailsHelperToCompare.generateItemTypesForExistingItems(session);

				
				Iterator itr2 = poDetailsHelperToCompare.getPurchaseOrderDetailsList().iterator();
				itemCodeList = new ArrayList();
				while(itr2.hasNext()) {
					PurchaseOrderDetails tempDetails = (PurchaseOrderDetails) itr2.next();
					this.itemCodeList.add(tempDetails.getItemCode());
				}
				
				//START Phase 3 - Azhee
				List tempList = transactionMananger.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", rs.getReturnSlipNo(), session);
				if(tempList.size()>0) {
				itr = tempList.iterator();
				transactionList = new ArrayList<Transaction>(); 
				while(itr.hasNext()) {
					Transaction transaction = (Transaction)itr.next();
					if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
						transactionList.add(transaction);
					}
				}
				}else {
					transactionList = new ArrayList();
					Transaction transaction = new Transaction();
					transactionList.add(transaction);
				}
				this.setTransactionList(transactionList);
				this.rs.setTransactions(transactionList);
				//END Phase 3 - Azhee
				this.setRs(rs);
				forWhat = "true";
				forWhatDisplay="edit";
				return "returnSlip";
			
			}else {
				FinishedGood finGood = new FinishedGood();
				finGood = (FinishedGood) manager.listInventoryByParameter(FinishedGood.class, "productCode",
						this.getFg().getProductCode(),session).get(0);
				ingredients = new ArrayList<Ingredient>();
				Set<Ingredient>	ingSet = finGood.getIngredients();
				Iterator<Ingredient> itr = ingSet.iterator();
				while(itr.hasNext()) {
					Ingredient ingredient = itr.next();
					ingredients.add(ingredient);
				}
				itemCodeList = manager.loadItemListFromRawAndFin(session);
				this.setFg(finGood);
				itemSubClassificationList = lookupManager.listItemByClassification(InventoryClassification.class, "classification", 
						fg.getClassification(), session);
				return "finGood";
			}		

		} catch (RuntimeException re) {
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
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

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
	public FinishedGood getFg() {
		return fg;
	}

	public void setFg(FinishedGood fg) {
		this.fg = fg;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public List getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List itemCodeList) {
		this.itemCodeList = itemCodeList;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
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
	public String getParentPage() {
		return parentPage;
	}
	public void setParentPage(String parentPage) {
		this.parentPage = parentPage;
	}
	public FPTS getFpts() {
		return fpts;
	}
	public void setFpts(FPTS fpts) {
		this.fpts = fpts;
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
	public RequisitionForm getRf() {
		return rf;
	}
	public void setRf(RequisitionForm rf) {
		this.rf = rf;
	}
	public List getItemSubClassificationList() {
		return itemSubClassificationList;
	}
	public void setItemSubClassificationList(List itemSubClassificationList) {
		this.itemSubClassificationList = itemSubClassificationList;
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
			

}
