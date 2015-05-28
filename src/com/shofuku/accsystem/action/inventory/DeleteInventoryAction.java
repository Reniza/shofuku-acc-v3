package com.shofuku.accsystem.action.inventory;

import java.util.List;
import java.util.Map;

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
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class DeleteInventoryAction extends ActionSupport implements Preparable{

	Map actionSession;
	UserAccount user;

	InventoryManager inventoryManager;
	LookupManager lookupManager;
	PurchaseOrderDetailHelper helperItemsToDelete;

	InventoryUtil invUtil;
	
	@Override
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
		lookupManager = (LookupManager) actionSession.get("lookupManager");
		
		invUtil = new InventoryUtil(actionSession);
		
		if(helperItemsToDelete==null) {
			helperItemsToDelete = new PurchaseOrderDetailHelper(actionSession);
		}else {
			helperItemsToDelete.setActionSession(actionSession);
		}
	}
	
	
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
	
	List itemCodeList;
	List UOMList;
	
	private String subModule;
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public String execute() throws Exception{
		Session session = getSession();
		try {
			boolean deleteResult;

			if (getSubModule().equalsIgnoreCase("rawMat")) {
				deleteResult = inventoryManager.deleteInventoryByParameter(getItemNo(), RawMaterial.class,session);
				if (deleteResult == true) {
					rm = new RawMaterial();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				deleteResult = inventoryManager.deleteInventoryByParameter(getItemNo(), TradedItem.class,session);
				if (deleteResult == true) {
					ti = new TradedItem();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")) {
				deleteResult = inventoryManager.deleteInventoryByParameter(getItemNo(), Utensils.class,session);
				if (deleteResult == true) {
					u = new Utensils();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("ofcSup")) {
				deleteResult = inventoryManager.deleteInventoryByParameter(getItemNo(), OfficeSupplies.class,session);
				if (deleteResult == true) {
					os = new OfficeSupplies();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "ofcSup";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems")) {
				UnlistedItem unlistedItem = (UnlistedItem)inventoryManager.listByParameter(UnlistedItem.class, "description", unl.getDescription(), session).get(0);
				deleteResult = inventoryManager.deletePersistingInventoryItem(unlistedItem,session);
				if (deleteResult == true) {
					unl = new UnlistedItem();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("fpts")) {
				updateInventoryCountForDelete(getSubModule(),getFptsNo(),session);
				session = getSession();
				deleteResult = inventoryManager.deleteInventoryByParameter(getFptsNo(), FPTS.class,session);
				if (deleteResult == true) {
					fpts= new FPTS();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "fpts";
			}else if (getSubModule().equalsIgnoreCase("rf")) {
				updateInventoryCountForDelete(getSubModule(),getRfNo(),session);
				session = getSession();
				deleteResult = inventoryManager.deleteInventoryByParameter(getRfNo(), RequisitionForm.class,session);
				if (deleteResult == true) {
					rf= new RequisitionForm();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "rf";
			}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
				updateInventoryCountForDelete(getSubModule(),getRsIdNo(),session);
				session = getSession();
				deleteResult = inventoryManager.deleteInventoryByParameter(getRsIdNo(), ReturnSlip.class,session);
				if (deleteResult == true) {
					rs= new ReturnSlip();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "returnSlip";
			}else  {
				
				deleteResult = inventoryManager.deleteInventoryByParameter(getProductNo(), FinishedGood.class,session);
				if (deleteResult == true) {
					fg = new FinishedGood();
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
	
	private void updateInventoryCountForDelete(String subModule,String id,Session session) {
		String orderType = "";
		
		if (getSubModule().equalsIgnoreCase("fpts")) {
			orderType = SASConstants.ORDER_TYPE_FPTS;
			FPTS oldFpts = (FPTS) inventoryManager.listInventoryByParameter(FPTS.class,"fptsNo",id, session).get(0);
			helperItemsToDelete.generatePODetailsListFromSet(oldFpts.getPurchaseOrderDetailsReceived());
		}else if (getSubModule().equalsIgnoreCase("rf")) {
			orderType = SASConstants.ORDER_TYPE_ORDER_REQUISITION;
			RequisitionForm oldRR = (RequisitionForm) inventoryManager.listInventoryByParameter(RequisitionForm.class,"requisitionNo", id, session).get(0);
			helperItemsToDelete.generatePODetailsListFromSet(oldRR.getPurchaseOrderDetailsReceived());
		}else if (getSubModule().equalsIgnoreCase("returnSlip")) {
			orderType = rs.getReturnSlipTo();
			ReturnSlip oldRs = (ReturnSlip) inventoryManager.listInventoryByParameter(ReturnSlip .class, "returnSlipNo",	id,session).get(0);
			helperItemsToDelete.generatePODetailsListFromSet(oldRs.getPurchaseOrderDetails());
		}
	
		PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.updateInventoryCountsForDeletion(helperItemsToDelete , orderType);
		
		try {
			inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
		}
		
	}

	public String loadLookLists(){
		Session session = getSession();
		try{
		UOMList = lookupManager.getLookupElements(UnitOfMeasurements.class, "GENERAL",session);
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
	public String getRsIdNo() {
		return rsIdNo;
	}
	public void setRsIdNo(String rsIdNo) {
		this.rsIdNo = rsIdNo;
	}
	
		
}
