package com.shofuku.accsystem.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.BaseHibernateDao;
import com.shofuku.accsystem.dao.impl.BaseHibernateDaoImpl;
//import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;

public class AccountEntryProfileUtil{

	BaseHibernateDao baseHibernateDao = new BaseHibernateDaoImpl();
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public AccountEntryProfile getAccountEntryByAccountEntryName(List<AccountEntryProfile> accountEntries, String key) {
		Iterator<AccountEntryProfile> iterator = accountEntries.iterator();
		while(iterator.hasNext()) {
			AccountEntryProfile accountEntry = (AccountEntryProfile)iterator.next();
			if(key.equalsIgnoreCase(accountEntry.getName())) {;
				return accountEntry;
			}
		}
		return null;
	}

	
//	public Transaction getTransactionByAccountEntryName(List<Transaction> transactions, String key) {
//		Iterator<Transaction> iterator = transactions.iterator();
//		while(iterator.hasNext()) {
//			Transaction transaction = (Transaction)iterator.next();
//			if(key.equalsIgnoreCase(transaction.getAccountEntry().getName())) {;
//				return transaction;
//			}
//		}
//		return null;
//	}
	
	
	public AccountEntryProfile createAccountEntryProfileByCode(String accountEntryProfileCode) {
		AccountEntryProfile accountEntryProfile = new AccountEntryProfile();
		
		return accountEntryProfile;
	}
	
	
	public AccountEntryProfile getAccountEntryProfile(String accountEntryProfileCode) {
		
			Session session = getSession();
			try{
			
			char firstLetter = 'a';
			String lastSupplier = baseHibernateDao.getLastSupplierByInitialLetter(firstLetter ,session);
			int maxCount=0;
			try{
			maxCount = Integer.valueOf(lastSupplier.substring(2,lastSupplier.length()))+1;
			}catch(Exception e){
				maxCount=1;
			}
			
			AccountEntryProfile ape = new AccountEntryProfile();
//			ape.setAccountCode("S"+String.valueOf(firstLetter)+maxCount);
			return ape;
			} catch (Exception e) {
				return null;
			} finally {
				if(session.isOpen()){
					session.close();
					session.getSessionFactory().close();
				}
			}
	}
	
	// 2013 - PHASE 3 : PROJECT 1: MARK

	public String getActionBasedOnType(AccountEntryProfile accountEntry,
			String type) {

		String action = "";

		if (type.equalsIgnoreCase(SASConstants.RECEIVINGREPORT)) {
			action = accountEntry.getAccountingRules().getSupplierReceivingReport();
		} else if (type.equalsIgnoreCase(SASConstants.SUPPLIERINVOICE)) {
			action = accountEntry.getAccountingRules().getSupplierInvoice();
		} else if (type.equalsIgnoreCase(SASConstants.DELIVERYREPORT)) {
			action = accountEntry.getAccountingRules().getCustomerDeliveryReceipt();
		} else if (type.equalsIgnoreCase(SASConstants.CUSTOMERINVOICE)) {
			action = accountEntry.getAccountingRules().getCustomerInvoice();
		} else if (type.equalsIgnoreCase(SASConstants.PETTYCASH)) {
			action = accountEntry.getAccountingRules().getDisbursementPettyCash();
		} else if (type.equalsIgnoreCase(SASConstants.CASHPAYMENT)) {
			action = accountEntry.getAccountingRules().getDisbursementCashPayment();
		} else if (type.equalsIgnoreCase(SASConstants.CHECK_VOUCHER)) {
			action = accountEntry.getAccountingRules().getDisbursementCheckVoucher();
		} else if (type.equalsIgnoreCase(SASConstants.CHECKPAYMENT)) {
			action = accountEntry.getAccountingRules().getDisbursementCheckPayment();
		} else if (type.equalsIgnoreCase(SASConstants.INVENTORY_RETURN_SLIP_FORM)) {
			action = accountEntry.getAccountingRules().getInventoryRS();
		} else if (type.equalsIgnoreCase(SASConstants.INVENTORY_REQUISITION_FORM)) {
			action = accountEntry.getAccountingRules().getInventoryOR();
		} else if (type.equalsIgnoreCase(SASConstants.INVENTORY_FPTS)) {
			action = accountEntry.getAccountingRules().getInventoryFPTS();
		} else if (type.equalsIgnoreCase(SASConstants.ORSALES)) {
			action = accountEntry.getAccountingRules().getReceiptsOrSales();
		} else if (type.equalsIgnoreCase(SASConstants.OROTHERS)) {
			action = accountEntry.getAccountingRules().getReceiptsOrOther();
		} else if (type.equalsIgnoreCase(SASConstants.CASHCHECKRECEIPTS)) {
			action = accountEntry.getAccountingRules().getReceiptsCheck();
		}
		return action;
	}

	// END - 2013 - PHASE 3 : PROJECT 1: MARK
}
