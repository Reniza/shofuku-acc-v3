package com.shofuku.accsystem.domain.financials;

import java.io.Serializable;
import java.sql.Timestamp;

import com.shofuku.accsystem.utils.TransactionUtil;

public class Vat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -454117541349622210L;
	
	private String vatReferenceNo;
	private String payee;
	private String tinNumber;
	private String address;
	private double amount;
	private double vatAmount;
	private double vattableAmount;
	private String orNo;
	private Timestamp orDate;
	
	
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Timestamp getOrDate() {
		return orDate;
	}
	public void setOrDate(Timestamp orDate) {
		this.orDate = orDate;
	}
	public String getOrNo() {
		return orNo;
	}
	public void setOrNo(String orNo) {
		this.orNo = orNo;
	}
	public String getTinNumber() {
		return tinNumber;
	}
	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getVatAmount() {
		TransactionUtil util = new TransactionUtil();
		return util.computeForInputTaxAmount(vatAmount);
	}
	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}
	public double getVattableAmount() {
		return vattableAmount;
	}
	public void setVattableAmount(double vattableAmount) {
		this.vattableAmount = vattableAmount;
	}
	public String getVatReferenceNo() {
		return vatReferenceNo;
	}
	public void setVatReferenceNo(String vatReferenceNo) {
		this.vatReferenceNo = vatReferenceNo;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	
}
