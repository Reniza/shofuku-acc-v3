package com.shofuku.accsystem.domain.customers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;

public class CustomerSalesInvoice implements Serializable{

	private static final long serialVersionUID = -7656398963006374859L;
	
	private String customerInvoiceNo;
	private Timestamp customerInvoiceDate;
	private String soldTo;
	private String address;
	private String busStyle;
	private String tin;
	private String location;

	private DeliveryReceipt deliveryReceipt;
	private Set<PurchaseOrderDetails> purchaseOrderDetails;
	private Map<String,String>  purchaseOrderGroupPrices;
	public Map<String, String> getPurchaseOrderGroupPrices() {
		return purchaseOrderGroupPrices;
	}

	public void setPurchaseOrderGroupPrices(
			Map<String, String> purchaseOrderGroupPrices) {
		this.purchaseOrderGroupPrices = purchaseOrderGroupPrices;
	}
	
	//START: 2013 - PHASE 3 : PROJECT 4: AZ
		private Vat vatDetails;
		
		public Vat getVatDetails() {return vatDetails;}
		public void setVatDetails(Vat vatDetails) {this.vatDetails = vatDetails;}
		//END: 2013 - PHASE 3 : PROJECT 4: AZ

	private double totalSales;
	private double vat;
	private double totalAmount;
	private String preparedBy;
	private String receivedBy;
	
	//2013 - PHASE 3 : PROJECT 1: MARK
	private List<Transaction> transactions;
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	//END - 2013 - PHASE 3 : PROJECT 1: MARK
	public String getCustomerInvoiceNo() {
		return customerInvoiceNo;
	}

	public void setCustomerInvoiceNo(String customerInvoiceNo) {
		this.customerInvoiceNo = customerInvoiceNo;
	}

	public Timestamp getCustomerInvoiceDate() {
		return customerInvoiceDate;
	}

	public void setCustomerInvoiceDate(Timestamp customerInvoiceDate) {
		this.customerInvoiceDate = customerInvoiceDate;
	}

	public String getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
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

	public DeliveryReceipt getDeliveryReceipt() {
		return deliveryReceipt;
	}

	public void setDeliveryReceipt(DeliveryReceipt deliveryReceipt) {
		this.deliveryReceipt = deliveryReceipt;
	}

	public Set<PurchaseOrderDetails> getPurchaseOrderDetails() {
		return purchaseOrderDetails;
	}

	public void setPurchaseOrderDetails(
			Set<PurchaseOrderDetails> purchaseOrderDetails) {
		this.purchaseOrderDetails = purchaseOrderDetails;
	}

	public double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}

	public double getVat() {
		return vat;
	}

	public void setVat(double vat) {
		this.vat = vat;
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

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
