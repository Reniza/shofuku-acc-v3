package com.shofuku.accsystem.domain.suppliers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;

public class SupplierPurchaseOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7013302443679910895L;
	private String supplierPurchaseOrderId;
	private Timestamp purchaseOrderDate;
	private Supplier supplier;
	private String paymentTerm;
	private Timestamp paymentDate;
	private Timestamp dateOfDelivery;
	private Set<PurchaseOrderDetails> purchaseOrderDetails;
	private double totalAmount;
	private String orderedBy;
	private String approvedBy;
	private String location;
	
	
	public String getSupplierPurchaseOrderId() {
		return supplierPurchaseOrderId;
	}

	public void setSupplierPurchaseOrderId(String supplierPurchaseOrderId) {
		this.supplierPurchaseOrderId = supplierPurchaseOrderId;
	}

	public Timestamp getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	public void setPurchaseOrderDate(Timestamp purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
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
