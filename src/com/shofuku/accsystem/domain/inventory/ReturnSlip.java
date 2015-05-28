package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Transaction;

public class ReturnSlip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 958669583004140734L;


	private String returnSlipNo;
	private String returnSlipFrom;
	private String returnSlipTo;
	private String returnSlipReferenceOrderNo;
	private String preparedBy;
	private String approvedBy;
	private Memo memo;
	private Timestamp returnDate;
	private Set<PurchaseOrderDetails> purchaseOrderDetails;
	private String location;
	
	//2013 - PHASE 3 : PROJECT 1: MARK
		private List<Transaction> transactions;
		public List<Transaction> getTransactions() {
			return transactions;
		}
		
		public void setTransactions(List<Transaction> transactions) {
			this.transactions = transactions;
		}
		//END - 2013 - PHASE 3 : PROJECT 1: MARK
	
	public String getReturnSlipNo() {
		return returnSlipNo;
	}
	public void setReturnSlipNo(String returnSlipNo) {
		this.returnSlipNo = returnSlipNo;
	}
	public String getReturnSlipFrom() {
		return returnSlipFrom;
	}
	public void setReturnSlipFrom(String returnSlipFrom) {
		this.returnSlipFrom = returnSlipFrom;
	}
	public String getReturnSlipTo() {
		return returnSlipTo;
	}
	public void setReturnSlipTo(String returnSlipTo) {
		this.returnSlipTo = returnSlipTo;
	}
	public String getReturnSlipReferenceOrderNo() {
		return returnSlipReferenceOrderNo;
	}
	public void setReturnSlipReferenceOrderNo(String returnSlipReferenceOrderNo) {
		this.returnSlipReferenceOrderNo = returnSlipReferenceOrderNo;
	}
	public String getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Timestamp getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Timestamp returnDate) {
		this.returnDate = returnDate;
	}
	public Memo getMemo() {
		return memo;
	}
	public void setMemo(Memo memo) {
		this.memo = memo;
	}
	public Set<PurchaseOrderDetails> getPurchaseOrderDetails() {
		return purchaseOrderDetails;
	}
	public void setPurchaseOrderDetails(
			Set<PurchaseOrderDetails> purchaseOrderDetails) {
		this.purchaseOrderDetails = purchaseOrderDetails;
	}
	public ReturnSlip(String returnSlipNo, String returnSlipFrom,
			String returnSlipTo, String returnSlipReferenceOrderNo,
			String preparedBy, String approvedBy, Memo memo,
			Timestamp returnDate, Set<PurchaseOrderDetails> purchaseOrderDetails) {
		super();
		this.returnSlipNo = returnSlipNo;
		this.returnSlipFrom = returnSlipFrom;
		this.returnSlipTo = returnSlipTo;
		this.returnSlipReferenceOrderNo = returnSlipReferenceOrderNo;
		this.preparedBy = preparedBy;
		this.approvedBy = approvedBy;
		this.memo = memo;
		this.returnDate = returnDate;
		this.purchaseOrderDetails = purchaseOrderDetails;
	}
	
	public ReturnSlip() {
		
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
