package com.shofuku.accsystem.domain.receipts;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;

public class CashCheckReceipts implements Serializable {

	private static final long serialVersionUID = 6409244884236771482L;

	private String cashReceiptNo;
	private Timestamp cashReceiptDate;
	private String payee;
	private String particulars;
	private double amount;
	private String receivedBy;
	private String checkNo;
	private String bankName;
	private String bankAccountNo;
	private String checkRemarks;
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
	public String getCashReceiptNo() {
		return cashReceiptNo;
	}

	public void setCashReceiptNo(String cashReceiptNo) {
		this.cashReceiptNo = cashReceiptNo;
	}

	public Timestamp getCashReceiptDate() {
		return cashReceiptDate;
	}

	public void setCashReceiptDate(Timestamp cashReceiptDate) {
		this.cashReceiptDate = cashReceiptDate;
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

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getCheckRemarks() {
		return checkRemarks;
	}

	public void setCheckRemarks(String checkRemarks) {
		this.checkRemarks = checkRemarks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
