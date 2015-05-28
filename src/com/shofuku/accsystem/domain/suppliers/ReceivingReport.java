package com.shofuku.accsystem.domain.suppliers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;

public class ReceivingReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364793314073475147L;
	
	
	private String receivingReportNo;
	private Date receivingReportDate;
	private Timestamp receivingReportPaymentDate;
	private SupplierPurchaseOrder supplierPurchaseOrder;
	private Set<PurchaseOrderDetails> purchaseOrderDetails;
	private String checkBox;
	private String remarks;
	private List<ReturnSlip> returnSlipList;
	private List<Transaction> transactions;

	private double totalAmount;
	private String location;
	
	
	
	private String preparedBy;
	
	
	public String getReceivingReportNo() {
		return receivingReportNo;
	}

	public void setReceivingReportNo(String receivingReportNo) {
		this.receivingReportNo = receivingReportNo;
	}

	public Date getReceivingReportDate() {
		return receivingReportDate;
	}

	public void setReceivingReportDate(Date receivingReportDate) {
		this.receivingReportDate = receivingReportDate;
	}

	
	public Timestamp getReceivingReportPaymentDate() {
		return receivingReportPaymentDate;
	}

	public void setReceivingReportPaymentDate(Timestamp receivingReportPaymentDate) {
		this.receivingReportPaymentDate = receivingReportPaymentDate;
	}

	public SupplierPurchaseOrder getSupplierPurchaseOrder() {
		return supplierPurchaseOrder;
	}

	public void setSupplierPurchaseOrder(SupplierPurchaseOrder supplierPurchaseOrder) {
		this.supplierPurchaseOrder = supplierPurchaseOrder;
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

	public List<ReturnSlip> getReturnSlipList() {
		return returnSlipList;
	}

	public void setReturnSlipList(List<ReturnSlip> returnSlipList) {
		this.returnSlipList = returnSlipList;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
