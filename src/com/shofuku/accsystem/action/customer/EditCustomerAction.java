package com.shofuku.accsystem.action.customer;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class EditCustomerAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");

	private String customerModule;
	private String forWhat;
	private String forWhatDisplay;
	Customer customer;
	CustomerPurchaseOrder custpo;
	DeliveryReceipt dr;
	CustomerSalesInvoice invoice;
	List customerNoList;
	List purchaseOrderNoList;
	List deliveryReceiptNoList;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
			List accountProfileCodeList;
			List<Transaction> transactionList;
			List<Transaction> transactions;
			Iterator itr;
			//END 2013 - PHASE 3 : PROJECT 1: MARK  
	
	
	PurchaseOrderDetailHelper poDetailsHelper = new PurchaseOrderDetailHelper(actionSession);
	PurchaseOrderDetailHelper poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
	
	AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
	TransactionManager transactionMananger = (TransactionManager) actionSession.get("transactionMananger");
	InventoryManager inventoryManager= (InventoryManager) actionSession.get("inventoryManager");
	CustomerManager customerManager = (CustomerManager) actionSession.get("customerManager");
		
@Deprecated
	public String loadCustomerPO() {
	Session session = getSession();
	try{
		CustomerPurchaseOrder custPO = new CustomerPurchaseOrder();
		custPO = (CustomerPurchaseOrder) customerManager.listByParameter(
				custPO.getClass(), "customerPurchaseOrderId",
				this.getDr().getCustomerPurchaseOrder().getCustomerPurchaseOrderId(),session).get(0);
		
		poDetailsHelper.generatePODetailsListFromSet(custPO.getPurchaseOrderDetails());
		poDetailsHelper.generateCommaDelimitedValues();
		
		if(null==poDetailsHelperToCompare) {
			poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
		}
		poDetailsHelperToCompare.generatePODetailsListFromSet(custPO.getPurchaseOrderDetails());
		poDetailsHelperToCompare.generateCommaDelimitedValues();
		this.getDr().setTotalAmount(poDetailsHelper.getTotalAmount());

		return "dr";
	}catch(Exception e){
		return "invoice";
	}finally{
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
	}
	}
@Deprecated	
	public String loadCustomerDR() {
	Session session = getSession();
	try{	
	DeliveryReceipt custDr = new DeliveryReceipt();
		custDr = (DeliveryReceipt) customerManager.listByParameter(
				custDr.getClass(), "deliveryReceiptNo",
				this.getInvoice().getDeliveryReceipt().getDeliveryReceiptNo(),session).get(0);
		
		poDetailsHelper.generatePODetailsListFromSet(custDr.getPurchaseOrderDetails());
		poDetailsHelper.generateCommaDelimitedValues();
		this.getInvoice().setTotalSales(poDetailsHelper.getTotalAmount());
		return "invoice";
	}catch(Exception e){
		return "invoice";
	}finally{
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
	}
	}
	
private Session getSession() {
	return HibernateUtil.getSessionFactory().getCurrentSession();
}
public String execute() throws Exception{
	Session session = getSession();
	try{
		forWhatDisplay="edit";
		accountProfileCodeList = accountEntryManager.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
			if (getCustomerModule().equals("profile")) {
				
				Customer profile = new Customer();
				profile = (Customer) customerManager.listByParameter(
						profile.getClass(), "customerNo",
						this.getCustomer().getCustomerNo(),session).get(0);
				this.setCustomer(profile);
				return "profile";
			} else if (getCustomerModule().equals("purchaseOrder")) {
				CustomerPurchaseOrder custPO = new CustomerPurchaseOrder();
				customerNoList = customerManager.listAlphabeticalAscByParameter(Customer.class, "customerNo", session);
				
				custPO = (CustomerPurchaseOrder) customerManager.listByParameter(
						custPO.getClass(), "customerPurchaseOrderId",
						this.getCustpo().getCustomerPurchaseOrderId(),session).get(0);
				poDetailsHelper.generatePODetailsListFromSet(custPO.getPurchaseOrderDetails());
				poDetailsHelper.generateCommaDelimitedValues();
				this.setCustomer(custPO.getCustomer());
				this.setCustpo(custPO);
				return "purchaseOrder";
			} else if (getCustomerModule().equals("deliveryReceipt")) {
				DeliveryReceipt custDr = new DeliveryReceipt();
				purchaseOrderNoList = customerManager.listAlphabeticalAscByParameter(CustomerPurchaseOrder.class, "customerPurchaseOrderId", session);
				
				custDr = (DeliveryReceipt) customerManager.listByParameter(
						custDr.getClass(), "deliveryReceiptNo",
						this.getDr().getDeliveryReceiptNo(),session).get(0);

				//START Phase 3 - Azhee
				List tempList = transactionMananger.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", dr.getDeliveryReceiptNo(), session);
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
				//END Phase 3 - Azhee
				/*
				 * Checking and fetching existing return slips
				 */
				
				Session drSession = getSession();
				List returnSlipList = inventoryManager.listInventoryByParameter(ReturnSlip.class, "returnSlipReferenceOrderNo", dr.getDeliveryReceiptNo(), drSession);
				
				if(returnSlipList.size()>0) {
					custDr.setReturnSlipList(returnSlipList);
				}else {
					custDr.setReturnSlipList(null);
				}
				
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
				}
				poDetailsHelperToCompare.generatePODetailsListFromSet(custDr.getCustomerPurchaseOrder().getPurchaseOrderDetails());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
				if(null==poDetailsHelper) {
				}else {
					poDetailsHelper.generatePODetailsListFromSet(custDr.getPurchaseOrderDetails());
					poDetailsHelper.generateCommaDelimitedValues();
					//2014 - ITEM COLORING
 					poDetailsHelper.generateItemTypesForExistingItems(session);
				}
				this.setCustomer(custDr.getCustomerPurchaseOrder()
						.getCustomer());
				this.setCustpo(custDr.getCustomerPurchaseOrder());
				//START Phase 3 - Project 1 - Mark
				this.dr.setTransactions(transactionList);
				//END Phase 3 - Project 1 - Mark
				this.setDr(custDr);
				return "deliveryReceipt";
			} else if (getCustomerModule().equals("invoice")) {
				CustomerSalesInvoice custInv = new CustomerSalesInvoice();
				deliveryReceiptNoList = customerManager.listAlphabeticalAscByParameter(DeliveryReceipt.class, "deliveryReceiptNo", session);
				
				custInv = (CustomerSalesInvoice) customerManager.listByParameter(
						custInv.getClass(), "customerInvoiceNo",
						this.getInvoice().getCustomerInvoiceNo(),session).get(0);
				//START Phase 3 - Azhee
				List tempList = transactionMananger.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber",invoice.getCustomerInvoiceNo() , session);
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
				//END Phase 3 - Azhee
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
				}
				poDetailsHelperToCompare.generatePODetailsListFromSet(custInv.getDeliveryReceipt().getPurchaseOrderDetails());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
				if(null==poDetailsHelper) {
				}else {
					poDetailsHelper.generatePODetailsListFromSet(custInv.getPurchaseOrderDetails());
					poDetailsHelper.generateCommaDelimitedValues();
					//2014 - ITEM COLORING
 					poDetailsHelper.generateItemTypesForExistingItems(session);
				}
				this.setCustomer(custInv.getDeliveryReceipt()
						.getCustomerPurchaseOrder().getCustomer());
				this.setCustpo(custInv.getDeliveryReceipt()
						.getCustomerPurchaseOrder());
				this.setDr(custInv.getDeliveryReceipt());
				//START Phase 3 - Azhee
				this.invoice.setTransactions(transactionList);
				//END Phase 3 - Azhee
				this.setInvoice(custInv);
			}
			return "invoice";
		} catch (RuntimeException re) {
			if (getCustomerModule().equalsIgnoreCase("profile")) {
				return "profile";
			}else if (getCustomerModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrder";
			}else if (getCustomerModule().equalsIgnoreCase("deliveryReceipt")) {
				return "deliveryReceipt";
			}else {
				return "invoice";
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
	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}
	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}
	
	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}
	public void setPoDetailsHelperToCompare(PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
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

	public void setCustpo(CustomerPurchaseOrder custpo) {
		this.custpo = custpo;
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

	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}


	public String getCustomerModule() {
		return customerModule;
	}

	public void setCustomerModule(String customerModule) {
		this.customerModule = customerModule;
	}
	public List getCustomerNoList() {
		return customerNoList;
	}
	public void setCustomerNoList(List customerNoList) {
		this.customerNoList = customerNoList;
	}
	public List getPurchaseOrderNoList() {
		return purchaseOrderNoList;
	}
	public void setPurchaseOrderNoList(List purchaseOrderNoList) {
		this.purchaseOrderNoList = purchaseOrderNoList;
	}
	public List getDeliveryReceiptNoList() {
		return deliveryReceiptNoList;
	}
	public void setDeliveryReceiptNoList(List deliveryReceiptNoList) {
		this.deliveryReceiptNoList = deliveryReceiptNoList;
	}
	public String getForWhatDisplay() {
		return forWhatDisplay;
	}
	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}
	//START 2013 - PHASE 3 : PROJECT 1: Azhee
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
			
			//END 2013 - PHASE 3 : PROJECT 1: Azhee
}
