package com.shofuku.accsystem.domain.financials;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.security.UserAccount;

public class Transaction implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private int id;
	private String transactionReferenceNumber;
	private String transactionType;
	private String transactionAction;
	private AccountEntryProfile accountEntry;
	private double amount;
	private Timestamp transactionDate;
	private String createdBy;
	private String isInUse;
	
	public String getTransactionReferenceNumber() {
		return transactionReferenceNumber;
	}
	public void setTransactionReferenceNumber(String transactionReferenceNumber) {
		this.transactionReferenceNumber = transactionReferenceNumber;
	}
	public AccountEntryProfile getAccountEntry() {
		return accountEntry;
	}
	public void setAccountEntry(AccountEntryProfile accountEntry) {
		this.accountEntry = accountEntry;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Timestamp getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getIsInUse() {
		return isInUse;
	}
	public void setIsInUse(String isInUse) {
		this.isInUse = isInUse;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTransactionAction() {
		return transactionAction;
	}
	public void setTransactionAction(String transactionAction) {
		this.transactionAction = transactionAction;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
