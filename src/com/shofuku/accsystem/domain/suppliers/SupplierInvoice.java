package com.shofuku.accsystem.domain.suppliers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
//import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;

public class SupplierInvoice implements Serializable{

	
	private static final long serialVersionUID = 440785910656946022L;
	private String supplierInvoiceNo;
	private Timestamp supplierInvoiceDate;
	private ReceivingReport receivingReport;
	private Set<PurchaseOrderDetails> purchaseOrderDetails;
	private String checkBox;
	private String remarks;
	//2013 - PHASE 3 : PROJECT 1: MARK
	private List<Transaction> transactions;
	//END - 2013 - PHASE 3 : PROJECT 1: MARK
	private String debit1Title;
	private double debit1Amount;
	private String debit2Title;
	private double debit2Amount;
	private String credit1Title;
	private double credit1Amount;
	private String credit2Title;
	private double credit2Amount;
	private String preparedBy;
	private double purchaseOrderDetailsTotalAmount;
	private double remainingBalance;
	private String location;
	
	
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	private Vat vatDetails;
	
	public Vat getVatDetails() {return vatDetails;}
	public void setVatDetails(Vat vatDetails) {this.vatDetails = vatDetails;}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	
	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public double getRemainingBalance() {
		return remainingBalance;
	}

	public void setRemainingBalance(double remainingBalance) {
		this.remainingBalance = remainingBalance;
	}

	public String getSupplierInvoiceNo() {
		return supplierInvoiceNo;
	}

	public void setSupplierInvoiceNo(String supplierInvoiceNo) {
		this.supplierInvoiceNo = supplierInvoiceNo;
	}

	public Timestamp getSupplierInvoiceDate() {
		return supplierInvoiceDate;
	}

	public void setSupplierInvoiceDate(Timestamp supplierInvoiceDate) {
		this.supplierInvoiceDate = supplierInvoiceDate;
	}

	public ReceivingReport getReceivingReport() {
		return receivingReport;
	}

	public void setReceivingReport(ReceivingReport receivingReport) {
		this.receivingReport = receivingReport;
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

	public String getDebit1Title() {
		return debit1Title;
	}

	public void setDebit1Title(String debit1Title) {
		this.debit1Title = debit1Title;
	}

	public double getDebit1Amount() {
		return debit1Amount;
	}

	public void setDebit1Amount(double debit1Amount) {
		this.debit1Amount = debit1Amount;
	}

	public String getDebit2Title() {
		return debit2Title;
	}

	public void setDebit2Title(String debit2Title) {
		this.debit2Title = debit2Title;
	}

	public double getDebit2Amount() {
		return debit2Amount;
	}

	public void setDebit2Amount(double debit2Amount) {
		this.debit2Amount = debit2Amount;
	}

	public String getCredit1Title() {
		return credit1Title;
	}

	public void setCredit1Title(String credit1Title) {
		this.credit1Title = credit1Title;
	}

	public double getCredit1Amount() {
		return credit1Amount;
	}

	public void setCredit1Amount(double credit1Amount) {
		this.credit1Amount = credit1Amount;
	}

	public String getCredit2Title() {
		return credit2Title;
	}

	public void setCredit2Title(String credit2Title) {
		this.credit2Title = credit2Title;
	}

	public double getCredit2Amount() {
		return credit2Amount;
	}

	public void setCredit2Amount(double credit2Amount) {
		this.credit2Amount = credit2Amount;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}
	public double getPurchaseOrderDetailsTotalAmount() {
		return purchaseOrderDetailsTotalAmount;
	}

	public void setPurchaseOrderDetailsTotalAmount(
			double purchaseOrderDetailsTotalAmount) {
		this.purchaseOrderDetailsTotalAmount = purchaseOrderDetailsTotalAmount;
	}
	//2013 - PHASE 3 : PROJECT 1: MARK
//	public List<Transaction> getTransactions() {
//		return transactions;
//	}
//
//	public void setTransactions(List<Transaction> transactions) {
//		this.transactions = transactions;
//	}
	//END - 2013 - PHASE 3 : PROJECT 1: MARK
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


}
