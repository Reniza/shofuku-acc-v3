package com.shofuku.accsystem.domain.disbursements;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;

public class CheckPayments implements Serializable {
	private static final long serialVersionUID = 5459641638943959354L;
	
	private String checkVoucherNumber;
	private Timestamp checkVoucherDate;
	private String payee;
	private String description;
	private String particulars;
	private double amount;
	private String vat;
	private Double vatAmount;
	private Double finalAmount;
	private String checkNo;
	private Timestamp chequeDate;
	private String bankName;
	private String bankAccountNumber;
	private String releasedBy;
	private String approvedBy;
	private String debitTitle;
	private double debitAmount;
	private String creditTitle;
	private double creditAmount;
	private String amountInWords;
	private Timestamp dueDate;
	private SupplierInvoice invoice;
	private Double totalPurchases;
	private double remainingBalance;
	private double amountToPay;
	private String isEncashed;
	private String isPrinted;
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
	
	public double getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(double amountToPay) {
		this.amountToPay = amountToPay;
	}

	public double getRemainingBalance() {
		return remainingBalance;
	}

	public void setRemainingBalance(double remainingBalance) {
		this.remainingBalance = remainingBalance;
	}

	public Double getTotalPurchases() {
		return totalPurchases;
	}

	public void setTotalPurchases(Double totalPurchases) {
		this.totalPurchases = totalPurchases;
	}

	public Timestamp getDueDate() {
		return dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public SupplierInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SupplierInvoice invoice) {
		this.invoice = invoice;
	}

	public String getAmountInWords() {
		return amountInWords;
	}

	public void setAmountInWords(String amountInWords) {
		this.amountInWords = amountInWords;
	}

	public String getCheckVoucherNumber() {
		return checkVoucherNumber;
	}

	public void setCheckVoucherNumber(String checkVoucherNumber) {
		this.checkVoucherNumber = checkVoucherNumber;
	}

	public Timestamp getCheckVoucherDate() {
		return checkVoucherDate;
	}

	public void setCheckVoucherDate(Timestamp checkVoucherDate) {
		this.checkVoucherDate = checkVoucherDate;
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

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
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

	public Double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getIsEncashed() {
		return isEncashed;
	}
	public void setIsEncashed(String isEncashed) {
		this.isEncashed = isEncashed;
	}
	public String getIsPrinted() {
		return isPrinted;
	}
	public void setIsPrinted(String isPrinted) {
		this.isPrinted = isPrinted;
	}
	public Timestamp getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Timestamp chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
