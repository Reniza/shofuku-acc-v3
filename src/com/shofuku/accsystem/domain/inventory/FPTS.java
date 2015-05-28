package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Transaction;

public class FPTS implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String fptsNo;
	private String fptsNoT;
	private String fptsNoR;
	private String fptsTo;
	private String fptsFrom;
	
	private Timestamp transactionDate;
	private Timestamp approvedDate;
	private Timestamp receivedDate;
	
	RequisitionForm requisitionForm;
	
	private String transferredBy;
	private String receivedBy;
	private String location;
	
	private Set<PurchaseOrderDetails> purchaseOrderDetailsTransferred;
	private Set<PurchaseOrderDetails> purchaseOrderDetailsReceived;
	
	private List<ReturnSlip> returnSlipList;
	
	//2013 - PHASE 3 : PROJECT 1: MARK
		private List<Transaction> transactions;
		public List<Transaction> getTransactions() {
			return transactions;
		}
		
		public void setTransactions(List<Transaction> transactions) {
			this.transactions = transactions;
		}
		//END - 2013 - PHASE 3 : PROJECT 1: MARK
	
	public FPTS(){
		
	}
	public String getFptsNo() {
		return fptsNo;
	}

	public void setFptsNo(String fptsNo) {
		this.fptsNo = fptsNo;
	}

	public String getFptsTo() {
		return fptsTo;
	}

	public void setFptsTo(String fptsTo) {
		this.fptsTo = fptsTo;
	}

	public String getFptsFrom() {
		return fptsFrom;
	}

	public void setFptsFrom(String fptsFrom) {
		this.fptsFrom = fptsFrom;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Timestamp getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Timestamp approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Timestamp getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getTransferredBy() {
		return transferredBy;
	}

	public void setTransferredBy(String transferredBy) {
		this.transferredBy = transferredBy;
	}

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}



	public Set<PurchaseOrderDetails> getPurchaseOrderDetailsTransferred() {
		return purchaseOrderDetailsTransferred;
	}



	public void setPurchaseOrderDetailsTransferred(
			Set<PurchaseOrderDetails> purchaseOrderDetailsTransferred) {
		this.purchaseOrderDetailsTransferred = purchaseOrderDetailsTransferred;
	}



	public Set<PurchaseOrderDetails> getPurchaseOrderDetailsReceived() {
		return purchaseOrderDetailsReceived;
	}



	public void setPurchaseOrderDetailsReceived(
			Set<PurchaseOrderDetails> purchaseOrderDetailsReceived) {
		this.purchaseOrderDetailsReceived = purchaseOrderDetailsReceived;
	}



	public String getFptsNoT() {
		return fptsNoT;
	}



	public void setFptsNoT(String fptsNoT) {
		this.fptsNoT = fptsNoT;
	}



	public String getFptsNoR() {
		return fptsNoR;
	}



	public void setFptsNoR(String fptsNoR) {
		this.fptsNoR = fptsNoR;
	}



	public RequisitionForm getRequisitionForm() {
		return requisitionForm;
	}



	public void setRequisitionForm(RequisitionForm requisitionForm) {
		this.requisitionForm = requisitionForm;
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
