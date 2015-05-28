package com.shofuku.accsystem.domain.customers;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;

public class DeliveryReceipt implements Serializable {

	private static final long serialVersionUID = 2728843801874822600L;

	private String deliveryReceiptNo;
	private Date deliveryReceiptDate;
	private CustomerPurchaseOrder customerPurchaseOrder;

	private String shippingMethod;
	private Date shippingDate;
	private Date dueDate;

	private Set<PurchaseOrderDetails> purchaseOrderDetails;
	private String checkBox;
	private String remarks;
	private String location;
	
	private List<ReturnSlip> returnSlipList;

	//2013 - PHASE 3 : PROJECT 1: MARK
		private List<Transaction> transactions;
		//END - 2013 - PHASE 3 : PROJECT 1: MARK

	private double totalAmount;
	private String preparedBy;
	
		//2013 - PHASE 3 : PROJECT 1: MARK
		public List<Transaction> getTransactions() {
			return transactions;
		}
		
		public void setTransactions(List<Transaction> transactions) {
			this.transactions = transactions;
		}
		//END - 2013 - PHASE 3 : PROJECT 1: MARK
	
	public String getDeliveryReceiptNo() {
		return deliveryReceiptNo;
	}

	public void setDeliveryReceiptNo(String deliveryReceiptNo) {
		this.deliveryReceiptNo = deliveryReceiptNo;
	}

	public Date getDeliveryReceiptDate() {
		return deliveryReceiptDate;
	}

	public void setDeliveryReceiptDate(Date deliveryReceiptDate) {
		this.deliveryReceiptDate = deliveryReceiptDate;
	}

	public CustomerPurchaseOrder getCustomerPurchaseOrder() {
		return customerPurchaseOrder;
	}

	public void setCustomerPurchaseOrder(CustomerPurchaseOrder customerPurchaseOrder) {
		this.customerPurchaseOrder = customerPurchaseOrder;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Set<PurchaseOrderDetails> getPurchaseOrderDetails() {
		return purchaseOrderDetails;
	}

	public void setPurchaseOrderDetails(
			Set<PurchaseOrderDetails> purchaseOrderDetails) {
		this.purchaseOrderDetails = purchaseOrderDetails;
	}

	public String getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(String checkBox) {
		this.checkBox = checkBox;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public List<ReturnSlip> getReturnSlipList() {
		return returnSlipList;
	}

	public void setReturnSlipList(List<ReturnSlip> returnSlipList) {
		this.returnSlipList = returnSlipList;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	

}
