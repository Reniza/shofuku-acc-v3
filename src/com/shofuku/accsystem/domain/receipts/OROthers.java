package com.shofuku.accsystem.domain.receipts;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;

public class OROthers implements Serializable {

	private static final long serialVersionUID = 8197988931692300270L;

	private String orNumber;
	private Timestamp orDate;
	private String receivedFrom;
	private String address;
	private String busStyle;
	private String tin;
	private double theAmountOf;
	private String inFullPartialPaymentOf;
	private String receivedBy;
	private String salesInvoiceNumber;
	private double amount;
	private double cash;
	private double check;
	private String bankCheckNo;
	private double total;
	private String amountInWords;
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
	public String getAmountInWords() {
		return amountInWords;
	}
	public void setAmountInWords(String amountInWords) {
		this.amountInWords = amountInWords;
	}
	
	public String getOrNumber() {
		return orNumber;
	}

	public void setOrNumber(String orNumber) {
		this.orNumber = orNumber;
	}

	public Timestamp getOrDate() {
		return orDate;
	}

	public void setOrDate(Timestamp orDate) {
		this.orDate = orDate;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBusStyle() {
		return busStyle;
	}

	public void setBusStyle(String busStyle) {
		this.busStyle = busStyle;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public double getTheAmountOf() {
		return theAmountOf;
	}
	public void setTheAmountOf(double theAmountOf) {
		this.theAmountOf = theAmountOf;
	}
	public String getInFullPartialPaymentOf() {
		return inFullPartialPaymentOf;
	}

	public void setInFullPartialPaymentOf(String inFullPartialPaymentOf) {
		this.inFullPartialPaymentOf = inFullPartialPaymentOf;
	}

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	public String getSalesInvoiceNumber() {
		return salesInvoiceNumber;
	}

	public void setSalesInvoiceNumber(String salesInvoiceNumber) {
		this.salesInvoiceNumber = salesInvoiceNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getCheck() {
		return check;
	}

	public void setCheck(double check) {
		this.check = check;
	}

	public String getBankCheckNo() {
		return bankCheckNo;
	}

	public void setBankCheckNo(String bankCheckNo) {
		this.bankCheckNo = bankCheckNo;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
