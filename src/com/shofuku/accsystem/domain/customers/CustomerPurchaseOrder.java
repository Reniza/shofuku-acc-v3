package com.shofuku.accsystem.domain.customers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Set;

import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;

public class CustomerPurchaseOrder implements Serializable{

	private static final long serialVersionUID = -3843824112436865501L;
	
	private String customerPurchaseOrderId;
	private Timestamp purchaseOrderDate;
	private Customer customer;
	private String paymentTerm;
	private Timestamp paymentDate;
	private Timestamp dateOfDelivery;
	private Set<PurchaseOrderDetails> purchaseOrderDetails;
	private double totalAmount;
	private String orderedBy;
	private String approvedBy;
	private String location;

	public String getCustomerPurchaseOrderId() {
		return customerPurchaseOrderId;
	}

	public void setCustomerPurchaseOrderId(String customerPurchaseOrderId) {
		this.customerPurchaseOrderId = customerPurchaseOrderId;
	}

	public Timestamp getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	public void setPurchaseOrderDate(Timestamp purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public Timestamp getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Timestamp getDateOfDelivery() {
		return dateOfDelivery;
	}

	public void setDateOfDelivery(Timestamp dateOfDelivery) {
		this.dateOfDelivery = dateOfDelivery;
	}

	public Set<PurchaseOrderDetails> getPurchaseOrderDetails() {
		return purchaseOrderDetails;
	}

	public void setPurchaseOrderDetails(
			Set<PurchaseOrderDetails> purchaseOrderDetails) {
		this.purchaseOrderDetails = purchaseOrderDetails;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
