package com.shofuku.accsystem.domain.suppliers;

import java.io.Serializable;

public class Supplier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3770121423127262940L;

	private String supplierId;
	private String supplierName;
	private String supplierStatus;
	private String contactName;
	private String contactTitle;
	private String companyAddress;
	private String phoneNumber;
	private String faxNumber;
	private String mobileNumber;
	private String emailAddress;
	private String website;
	private String paymentTerm;
	private String tin;
	private String location;
	
	
	public Supplier() {
		
	}
	
	public Supplier(String supplierId) {
		this.supplierId=supplierId;
	}
	public Supplier(String supplierId, String supplierName,
			String supplierStatus, String contactName, String contactTitle,
			String companyAddress, String phoneNumber, String faxNumber,
			String mobileNumber, String emailAddress, String website, String tin) {

		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.supplierStatus = supplierStatus;
		this.contactName = contactName;
		this.contactTitle = contactTitle;
		this.companyAddress = companyAddress;
		this.phoneNumber = phoneNumber;
		this.faxNumber = faxNumber;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
		this.website = website;
		this.tin = tin;
	}
	
	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId.trim();
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierStatus() {
		return supplierStatus;
	}

	public void setSupplierStatus(String supplierStatus) {
		this.supplierStatus = supplierStatus;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	
}
