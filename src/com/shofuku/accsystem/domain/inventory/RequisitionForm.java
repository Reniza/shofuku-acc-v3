package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Transaction;

public class RequisitionForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5350843972104007198L;

	public RequisitionForm(){
		
	}
	public RequisitionForm(String requisitionNo, String requisitionType,
			String requisitionTo, String requisitionBy,
			Timestamp requisitionDate, String requisitionApprovedBy,
			String requistionReceivedBy,
			Set<PurchaseOrderDetails> purchaseOrderDetailsOrdered,
			Set<PurchaseOrderDetails> purchaseOrderDetailsReceived) {
		this.requisitionNo = requisitionNo;
		this.requisitionType = requisitionType;
		this.requisitionTo = requisitionTo;
		this.requisitionBy = requisitionBy;
		this.requisitionDate = requisitionDate;
		this.requisitionApprovedBy = requisitionApprovedBy;
		this.requistionReceivedBy = requistionReceivedBy;
		this.purchaseOrderDetailsOrdered = purchaseOrderDetailsOrdered;
		this.purchaseOrderDetailsReceived = purchaseOrderDetailsReceived;
	}
	private String requisitionNo;
	private String requisitionType;
	private String requisitionTo;
	private String requisitionBy;
	
	private Timestamp requisitionDate;
	private String requisitionApprovedBy;
	private String requistionReceivedBy;
	
	private List<ReturnSlip> returnSlipList;
	private String location;
	
	private Set<PurchaseOrderDetails> purchaseOrderDetailsOrdered;
	private Set<PurchaseOrderDetails> purchaseOrderDetailsReceived;

	//2013 - PHASE 3 : PROJECT 1: MARK
		private List<Transaction> transactions;
		public List<Transaction> getTransactions() {
			return transactions;
		}
		
		public void setTransactions(List<Transaction> transactions) {
			this.transactions = transactions;
		}
		//END - 2013 - PHASE 3 : PROJECT 1: MARK	
	public String getRequisitionNo() {
		return requisitionNo;
	}

	public void setRequisitionNo(String requisitionNo) {
		this.requisitionNo = requisitionNo;
	}

	public String getRequisitionType() {
		return requisitionType;
	}

	public void setRequisitionType(String requisitionType) {
		this.requisitionType = requisitionType;
	}

	public String getRequisitionTo() {
		return requisitionTo;
	}

	public void setRequisitionTo(String requisitionTo) {
		this.requisitionTo = requisitionTo;
	}

	public String getRequisitionBy() {
		return requisitionBy;
	}

	public void setRequisitionBy(String requisitionBy) {
		this.requisitionBy = requisitionBy;
	}

	public Timestamp getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Timestamp requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public String getRequisitionApprovedBy() {
		return requisitionApprovedBy;
	}

	public void setRequisitionApprovedBy(String requisitionApprovedBy) {
		this.requisitionApprovedBy = requisitionApprovedBy;
	}

	public String getRequistionReceivedBy() {
		return requistionReceivedBy;
	}

	public void setRequistionReceivedBy(String requistionReceivedBy) {
		this.requistionReceivedBy = requistionReceivedBy;
	}


	public Set<PurchaseOrderDetails> getPurchaseOrderDetailsOrdered() {
		return purchaseOrderDetailsOrdered;
	}


	public void setPurchaseOrderDetailsOrdered(
			Set<PurchaseOrderDetails> purchaseOrderDetailsOrdered) {
		this.purchaseOrderDetailsOrdered = purchaseOrderDetailsOrdered;
	}


	public Set<PurchaseOrderDetails> getPurchaseOrderDetailsReceived() {
		return purchaseOrderDetailsReceived;
	}


	public void setPurchaseOrderDetailsReceived(
			Set<PurchaseOrderDetails> purchaseOrderDetailsReceived) {
		this.purchaseOrderDetailsReceived = purchaseOrderDetailsReceived;
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
