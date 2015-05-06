package com.shofuku.accsystem.action.inventory;

import java.util.List;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;

import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.OfficeSupplies;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.UnlistedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.lookups.UnitOfMeasurements;

import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class DeleteInventoryAction extends ActionSupport{

	InventoryManager manager = new InventoryManager();
	private static final long serialVersionUID = 1L;
	RawMaterial rm;
	FinishedGood fg;
	TradedItem ti;
	Utensils u;
	OfficeSupplies os;
	UnlistedItem unl;
	FPTS fpts;
	RequisitionForm rf;
	ReturnSlip rs;
	
	private String productNo;
	private String itemNo;
	private String fptsNo;
	private String rfNo;
	private String rsIdNo;
	
	
	public String getRsIdNo() {
		return rsIdNo;
	}
	public void setRsIdNo(String rsIdNo) {
		this.rsIdNo = rsIdNo;
	}

	private String subModule;
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		try {
			boolean deleteResult;

			if (getSubModule().equalsIgnoreCase("rawMat")) {
				deleteResult = manager.deleteInventoryByParameter(getItemNo(), RawMaterial.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				deleteResult = manager.deleteInventoryByParameter(getItemNo(), TradedItem.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")) {
				deleteResult = manager.deleteInventoryByParameter(getItemNo(), Utensils.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("ofcSup")) {
				deleteResult = manager.deleteInventoryByParameter(getItemNo(), OfficeSupplies.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "ofcSup";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems")) {
				UnlistedItem unlistedItem = (UnlistedItem)manager.listByParameter(UnlistedItem.class, "description", unl.getDescription(), session).get(0);
				deleteResult = manager.deletePersistingInventoryItem(unlistedItem,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("fpts")) {
				updateInventoryCountForDelete(getSubModule(),getFptsNo(),session);
				session = getSession();
				deleteResult = manager.deleteInventoryByParameter(getFptsNo(), FPTS.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "fpts";
			}else if (getSubModule().equalsIgnoreCase("rf")) {
				updateInventoryCountForDelete(getSubModule(),getRfNo(),session);
				session = getSession();
				deleteResult = manager.deleteInventoryByParameter(getRfNo(), RequisitionForm.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "rf";
			}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
				updateInventoryCountForDelete(getSubModule(),getRsIdNo(),session);
				session = getSession();
				deleteResult = manager.deleteInventoryByParameter(getRsIdNo(), ReturnSlip.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "returnSlip";
			}else  {
				
				deleteResult = manager.deleteInventoryByParameter(getProductNo(), FinishedGood.class,session);
				if (deleteResult == true) {
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				loadLookLists();
				return "finGood";
			}
		} catch (RuntimeException re) {
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
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
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
	private void updateInventoryCountForDelete(String subModule,String id,Session session) {
		String orderType = "";
		PurchaseOrderDetailHelper helperItemsToDelete = new PurchaseOrderDetailHelper();
		
		if (getSubModule().equalsIgnoreCase("fpts")) {
			orderType = SASConstants.ORDER_TYPE_FPTS;
			FPTS oldFpts = (FPTS) manager.listInventoryByParameter(FPTS.class,"fptsNo",id, session).get(0);
			helperItemsToDelete.generatePODetailsListFromSet(oldFpts.getPurchaseOrderDetailsReceived());
		}else if (getSubModule().equalsIgnoreCase("rf")) {
			orderType = SASConstants.ORDER_TYPE_ORDER_REQUISITION;
			RequisitionForm oldRR = (RequisitionForm) manager.listInventoryByParameter(RequisitionForm.class,"requisitionNo", id, session).get(0);
			helperItemsToDelete.generatePODetailsListFromSet(oldRR.getPurchaseOrderDetailsReceived());
		}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
			orderType = rs.getReturnSlipTo();
			ReturnSlip oldRs = (ReturnSlip) manager.listInventoryByParameter(ReturnSlip .class, "returnSlipNo",	id,session).get(0);
			helperItemsToDelete.generatePODetailsListFromSet(oldRs.getPurchaseOrderDetails());
		}
		InventoryUtil invUtil = new InventoryUtil();
		PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.updateInventoryCountsForDeletion(helperItemsToDelete , orderType);
		
		try {
			manager.updateInventoryFromOrders(inventoryUpdateRequest);
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
		}
		
	}

	List itemCodeList;
	List UOMList;

	LookupManager lookupManager = new LookupManager();

	public String loadLookLists(){
		Session session = getSession();
		try{
		UOMList = lookupManager.getLookupElements(UnitOfMeasurements.class, "GENERAL",session);
		itemCodeList = manager.loadItemListFromRawAndFin(session);
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
			}else {
				return "finGood";
			}
		}finally{
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		return "finGood";
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
	
	public List getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List itemCodeList) {
		this.itemCodeList = itemCodeList;
	}
	public List getUOMList() {
		return UOMList;
	}

	public void setUOMList(List uOMList) {
		UOMList = uOMList;
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
	public Utensils getU() {
		return u;
	}
	public void setU(Utensils u) {
		this.u = u;
	}
	public OfficeSupplies getOs() {
		return os;
	}
	public void setOs(OfficeSupplies os) {
		this.os = os;
	}
	public UnlistedItem getUnl() {
		return unl;
	}
	public void setUnl(UnlistedItem unl) {
		this.unl = unl;
	}
	
		
}
