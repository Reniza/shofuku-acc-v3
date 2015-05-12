package com.shofuku.accsystem.action.customer;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
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

public class DeleteCustomerAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");

	CustomerManager manager = new CustomerManager();
	InventoryManager inventoryManager = new InventoryManager();

	RecordCountHelper rch = new RecordCountHelper();

	private String subModule;

	private String custpoid;

	private String invId;
	private String cusId;
	private String drId;

	Customer customer;
	CustomerPurchaseOrder custpo;
	DeliveryReceipt dr;
	CustomerSalesInvoice invoice;

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		boolean deleteResult;
		try {
			if (getSubModule().equalsIgnoreCase("profile")) {
				deleteResult = manager.deleteCustomerByParameter(getCusId(),
						Customer.class,session);
				if (deleteResult == true) {
//					rch.updateCount(SASConstants.CUSTOMER, "delete");
					customer = new Customer();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "profileDeleted";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				deleteResult = manager.deleteCustomerByParameter(getCustpoid(),
						CustomerPurchaseOrder.class,session);
				if (deleteResult == true) {
//					rch.updateCount(SASConstants.CUSTOMERPO, "delete");
					custpo = new CustomerPurchaseOrder();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "poDeleted";
			} else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {

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
				InventoryUtil invUtil = new InventoryUtil();
				
				DeliveryReceipt orderToDelete = (DeliveryReceipt) manager.listByParameter(
						DeliveryReceipt.class, "deliveryReceiptNo",drId,session).get(0);
				
				PurchaseOrderDetailHelper helperItemsToDelete = new PurchaseOrderDetailHelper();
				helperItemsToDelete.generatePODetailsListFromSet(orderToDelete.getPurchaseOrderDetails());
				
				String orderType =SASConstants.ORDER_TYPE_DR; 
				try {
					PurchaseOrderDetailHelper inventoryUpdateRequest = invUtil.updateInventoryCountsForDeletion(helperItemsToDelete , orderType);
					inventoryManager.updateInventoryFromOrders(inventoryUpdateRequest);
				} catch (Exception e) {
					e.printStackTrace();
					addActionError("FAILED TO UPDATE INVENTORY AFTER DELETE");
				}
				
				deleteResult = manager.deleteCustomerByParameter(getDrId(),
						DeliveryReceipt.class,session);
				if (deleteResult == true) {
//					rch.updateCount(SASConstants.DELIVERYREPORT, "delete");
					dr = new DeliveryReceipt();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "drDeleted";
			} else {
				deleteResult = manager.deleteCustomerByParameter(getInvId(),
						CustomerSalesInvoice.class,session);
				if (deleteResult == true) {
//					rch.updateCount(SASConstants.CUSTOMERINVOICE, "delete");
					invoice = new CustomerSalesInvoice();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "invoiceDeleted";
			}
		} catch (RuntimeException re) {
			if (getSubModule().equalsIgnoreCase("profile")) {
				return "profileDeleted";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "poDeleted";
			} else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				return "drDeleted";
			} else {
				return "invoiceDeleted";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}

	/*
	 * GETTERS AND SETTERS
	 */

	public String getCustpoid() {
		return custpoid;
	}

	public void setCustpoid(String poId) {
		this.custpoid = poId;
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

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getDrId() {
		return drId;
	}

	public void setDrId(String drId) {
		this.drId = drId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerPurchaseOrder getCustpo() {
		return custpo;
	}

	public void setCustpo(CustomerPurchaseOrder po) {
		this.custpo = po;
	}

	public DeliveryReceipt getDr() {
		return dr;
	}

	public void setDr(DeliveryReceipt dr) {
		this.dr = dr;
	}

	public CustomerSalesInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(CustomerSalesInvoice invoice) {
		this.invoice = invoice;
	}

}
