package com.shofuku.accsystem.domain.disbursements;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;

public class PettyCash implements Serializable {
	private static final long serialVersionUID = -8488504647526662032L;
	String pcVoucherNumber;
	Timestamp pcVoucherDate;
	String payee;
	String description;
	String particulars;
	double amount;
	String releasedBy;
	String approvedBy;
	String debitTitle;
	double debitAmount;
	String creditTitle;
	double creditAmount;
	String referenceNo;
	boolean isByRef;
	
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
	
	//START: 2013 - PHASE 3 : PROJECT 4: AZ
		private Vat vatDetails;
		
		public Vat getVatDetails() {return vatDetails;}
		public void setVatDetails(Vat vatDetails) {this.vatDetails = vatDetails;}
		//END: 2013 - PHASE 3 : PROJECT 4: AZ
	public boolean isByRef() {
		return isByRef;
	}
	public void setByRef(boolean isByRef) {
		this.isByRef = isByRef;
	}
	
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getPcVoucherNumber() {
		return pcVoucherNumber;
	}
	public void setPcVoucherNumber(String pcVoucherNumber) {
		this.pcVoucherNumber = pcVoucherNumber;
	}
	public Timestamp getPcVoucherDate() {
		return pcVoucherDate;
	}
	public void setPcVoucherDate(Timestamp pcVoucherDate) {
		this.pcVoucherDate = pcVoucherDate;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getReleasedBy() {
		return releasedBy;
	}
	public void setReleasedBy(String releasedBy) {
		this.releasedBy = releasedBy;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getDebitTitle() {
		return debitTitle;
	}
	public void setDebitTitle(String debitTitle) {
		this.debitTitle = debitTitle;
	}
	public double getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(double debitAmount) {
		this.debitAmount = debitAmount;
	}
	public String getCreditTitle() {
		return creditTitle;
	}
	public void setCreditTitle(String creditTitle) {
		this.creditTitle = creditTitle;
	}
	public double getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


}
