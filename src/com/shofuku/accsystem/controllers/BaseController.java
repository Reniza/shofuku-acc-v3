package com.shofuku.accsystem.controllers;

import com.shofuku.accsystem.dao.impl.AccountEntryDaoImpl;
import com.shofuku.accsystem.dao.impl.BaseHibernateDaoImpl;
import com.shofuku.accsystem.dao.impl.CustomerDaoImpl;
import com.shofuku.accsystem.dao.impl.DisbursementDaoImpl;
import com.shofuku.accsystem.dao.impl.FinancialsDaoImpl;
import com.shofuku.accsystem.dao.impl.InventoryDaoImpl;
import com.shofuku.accsystem.dao.impl.LookupDaoImpl;
import com.shofuku.accsystem.dao.impl.ReceiptsDaoImpl;
import com.shofuku.accsystem.dao.impl.SecurityDaoImpl;
import com.shofuku.accsystem.dao.impl.SupplierDaoImpl;
import com.shofuku.accsystem.dao.impl.ToolsDaoImpl;
import com.shofuku.accsystem.dao.impl.TransactionsDaoImpl;
import com.shofuku.accsystem.domain.security.UserAccount;

public class BaseController {

	private UserAccount user;
	
	AccountEntryDaoImpl accountEntryDao =new AccountEntryDaoImpl();
	BaseHibernateDaoImpl baseHibernateDao = new BaseHibernateDaoImpl(); 
	CustomerDaoImpl customerDao =new CustomerDaoImpl();
	DisbursementDaoImpl disbursementDao=new DisbursementDaoImpl();
	FinancialsDaoImpl financialsDao=new FinancialsDaoImpl();
	InventoryDaoImpl inventoryDao=new InventoryDaoImpl();
	LookupDaoImpl lookupDao=new LookupDaoImpl();
	ReceiptsDaoImpl receiptsDao=new ReceiptsDaoImpl();
	SecurityDaoImpl securityDao=new SecurityDaoImpl();
	SupplierDaoImpl supplierDao=new SupplierDaoImpl();
	ToolsDaoImpl toolsDao = new ToolsDaoImpl();
	TransactionsDaoImpl transactionsDao=new TransactionsDaoImpl();
	
	
	public void initializeDaos() {
		accountEntryDao.setUser(user);
		baseHibernateDao.setUser(user);
		customerDao.setUser(user);
		disbursementDao.setUser(user);
		financialsDao.setUser(user);
		inventoryDao.setUser(user);
		lookupDao.setUser(user);
		receiptsDao.setUser(user);
		securityDao.setUser(user);
		supplierDao.setUser(user);
		toolsDao.setUser(user);
		transactionsDao.setUser(user);
	}
	
	public UserAccount getUser() {
		return user;
	}
	public void setUser(UserAccount user) {
		this.user = user;
	}
	
	
	public AccountEntryDaoImpl getAccountEntryDao() {
		return accountEntryDao;
	}

	public BaseHibernateDaoImpl getBaseHibernateDao() {
		return baseHibernateDao;
	}

	public CustomerDaoImpl getCustomerDao() {
		return customerDao;
	}

	public DisbursementDaoImpl getDisbursementDao() {
		return disbursementDao;
	}

	public FinancialsDaoImpl getFinancialsDao() {
		return financialsDao;
	}

	public InventoryDaoImpl getInventoryDao() {
		return inventoryDao;
	}

	public LookupDaoImpl getLookupDao() {
		return lookupDao;
	}

	public ReceiptsDaoImpl getReceiptsDao() {
		return receiptsDao;
	}

	public SecurityDaoImpl getSecurityDao() {
		return securityDao;
	}

	public SupplierDaoImpl getSupplierDao() {
		return supplierDao;
	}

	public ToolsDaoImpl getToolsDao() {
		return toolsDao;
	}

	public TransactionsDaoImpl getTransactionsDao() {
		return transactionsDao;
	}
	
}
