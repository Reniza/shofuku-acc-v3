package com.shofuku.accsystem.action.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

//import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.TradedItem;

import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;

public class PrintInventoryAction {
	InventoryManager manager = new InventoryManager();
	private static final long serialVersionUID = 1L;
	RawMaterial rm;
	FinishedGood fg;
	TradedItem ti;
	FPTS fpts;
	RequisitionForm rf;
	ReturnSlip rs;
	
	
	private String productNo;
	private String itemNo;
	private String fptsNo;
	private String rfNo;
	private String rsNo;
	
	private String subModule;
	private String forWhat;
	List<Ingredient> ingredients;
	List itemCodeList;
	
	
	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	PurchaseOrderDetailHelper poDetailsHelperDraft;
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
public String execute() throws Exception{
	Session session = getSession();
		try {
		
			if (getSubModule().equalsIgnoreCase("rawMat")){
				
				RawMaterial rm = new RawMaterial();
				rm = (RawMaterial) manager.listInventoryByParameter(
						RawMaterial.class, "itemCode",
						getItemNo(),session).get(0);
				this.setRm(rm);
				forWhat="print";
					return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")){
				
				TradedItem ti = new TradedItem();
				ti = (TradedItem) manager.listInventoryByParameter(
						TradedItem.class, "itemCode",
						getItemNo(),session).get(0);
				this.setTi(ti);
				forWhat="print";
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("fpts")){
				
				FPTS fpts = new FPTS();
				fpts = (FPTS) manager.listInventoryByParameter(
						FPTS.class, "fptsNo",
						getFptsNo(),session).get(0);
				
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
				
				this.setFpts(fpts);
				this.setRf(fpts.getRequisitionForm());
				forWhat="print";
				return "fpts";
			}else if (getSubModule().equalsIgnoreCase("rf")){
				
				RequisitionForm rf = new RequisitionForm();
				rf = (RequisitionForm) manager.listInventoryByParameter(
						RequisitionForm.class, "requisitionNo",
						getRfNo(),session).get(0);
				
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
				
				this.setRf(rf);
				forWhat="print";
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
				this.setRs(rs);
				forWhat = "print";
				return "returnSlip";
			
			}else {
				FinishedGood fg = new FinishedGood();
				fg = (FinishedGood) manager.listInventoryByParameter(
						FinishedGood.class, "productCode",
						this.getProductNo(),session).get(0);
				
				ingredients = new ArrayList<Ingredient>();
				Set<Ingredient>	ingSet = fg.getIngredients();
				Iterator<Ingredient> itr = ingSet.iterator();
				while(itr.hasNext()) {
					Ingredient ingredient = itr.next();
					ingredients.add(ingredient);
				}
				this.setFg(fg);
				forWhat="print";
				return "finGood";
			}
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("rawMat")) {
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("fpts")) {
				return "fpts";
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

public List<Ingredient> getIngredients() {
	return ingredients;
}

public void setIngredients(List<Ingredient> ingredients) {
	this.ingredients = ingredients;
}
public TradedItem getTi() {
	return ti;
}
public void setTi(TradedItem ti) {
	this.ti = ti;
}
public FPTS getFpts() {
	return fpts;
}
public void setFpts(FPTS fpts) {
	this.fpts = fpts;
}
public String getFptsNo() {
	return fptsNo;
}
public void setFptsNo(String fptsNo) {
	this.fptsNo = fptsNo;
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
public String getRfNo() {
	return rfNo;
}
public void setRfNo(String rfNo) {
	this.rfNo = rfNo;
}
public ReturnSlip getRs() {
	return rs;
}
public void setRs(ReturnSlip rs) {
	this.rs = rs;
}
public String getRsNo() {
	return rsNo;
}
public void setRsNo(String rsNo) {
	this.rsNo = rsNo;
}
public PurchaseOrderDetailHelper getPoDetailsHelperDraft() {
	return poDetailsHelperDraft;
}
public void setPoDetailsHelperDraft(
		PurchaseOrderDetailHelper poDetailsHelperDraft) {
	this.poDetailsHelperDraft = poDetailsHelperDraft;
}
public List getItemCodeList() {
	return itemCodeList;
}
public void setItemCodeList(List itemCodeList) {
	this.itemCodeList = itemCodeList;
}


}
