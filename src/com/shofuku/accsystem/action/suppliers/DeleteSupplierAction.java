package com.shofuku.accsystem.action.suppliers;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class DeleteSupplierAction extends ActionSupport implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map actionSession;
	UserAccount user;

	SupplierManager supplierManager;
	InventoryManager inventoryManager;
	RecordCountHelper rch;
	InventoryUtil invUtil;
	
	PurchaseOrderDetailHelper helperItemsToDelete;
	
	@Override
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		supplierManager = (SupplierManager) actionSession.get("supplierManager");
		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
		
		rch = new RecordCountHelper(actionSession);
		invUtil = new InventoryUtil(actionSession);
		
		
		
		if(helperItemsToDelete==null) {
			helperItemsToDelete = new PurchaseOrderDetailHelper(actionSession);
		}else {
			helperItemsToDelete.setActionSession(actionSession);
		}
	}
	
	private String subModule;
	private String supId;
	private String poId;
	private String rrId;
	private String invId;
	

	Supplier supplier;
	SupplierPurchaseOrder po;
	ReceivingReport rr;
	SupplierInvoice invoice;
	
	private Session getSession(){
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() throws Exception {
		Session session = getSession();
		try {
			boolean deleteResult = false;

			if (getSubModule().equalsIgnoreCase("supplierProfile")) {
				deleteResult = supplierManager.deleteSupplierByParameter(getSupId(),
						Supplier.class,session);
				if (deleteResult == true) {
					supplier = new Supplier();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "profileDeleted";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				deleteResult = supplierManager.deleteSupplierByParameter(getPoId(),
						SupplierPurchaseOrder.class,session);
				if (deleteResult == true) {
					po = new SupplierPurchaseOrder();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "poDeleted";
			} else if (getSubModule().equalsIgnoreCase("receivingReport")) {

				ReceivingReport orderToDelete = 
						(ReceivingReport) supplierManager.listSuppliersByParameter(rr.getClass(), "receivingReportNo", 
								rrId,getSession()).get(0);
				
				helperItemsToDelete.generatePODetailsListFromSet(orderToDelete.getPurchaseOrderDetails());
				String orderType =SASConstants.ORDER_TYPE_RR; 
				try {
					PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.updateInventoryCountsForDeletion(helperItemsToDelete , orderType);
					inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
				} catch (Exception e) {
					e.printStackTrace();
					addActionError("FAILED TO UPDATE INVENTORY AFTER DELETE");
				}
				
				deleteResult = supplierManager.deleteSupplierByParameter(getRrId(),ReceivingReport.class,session);
				if (deleteResult == true) {
					rr= new ReceivingReport();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "rrDeleted";
			} else {
				deleteResult = supplierManager.deleteSupplierByParameter(getInvId(),
						SupplierInvoice.class,session);
				if (deleteResult == true) {
					invoice = new SupplierInvoice();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "invoiceDeleted";
			}
		} catch (RuntimeException re) {
			if (getSubModule().equalsIgnoreCase("supplierProfile")) {
				return "profileDeleted";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "poDeleted";
			}else if (getSubModule().equalsIgnoreCase("receivingReport")) {
				return "rrDeleted";
			}else {
				return "invoiceDeleted";
			}
	}
			finally {
				if(session.isOpen()){
					session.close();
					session.getSessionFactory().close();
				}
		}
	}
	
	/*
	 * getters and setters
	 * */
	
	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
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

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public SupplierPurchaseOrder getPo() {
		return po;
	}

	public void setPo(SupplierPurchaseOrder po) {
		this.po = po;
	}

	public ReceivingReport getRr() {
		return rr;
	}

	public void setRr(ReceivingReport rr) {
		this.rr = rr;
	}

	public SupplierInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SupplierInvoice invoice) {
		this.invoice = invoice;
	}
}
