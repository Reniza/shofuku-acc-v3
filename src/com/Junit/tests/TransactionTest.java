package com.Junit.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Test;



import com.opensymphony.xwork2.ActionContext;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.financials.AccountingRules;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;

@SuppressWarnings("rawtypes")
public class TransactionTest {
	
	AccountEntryManager aepMgr = new AccountEntryManager();
	TransactionManager trnsMgr = new TransactionManager();
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
//	@Test
//	public void insertTransaction() {
//		AccountEntryProfile aep = aepMgr.loadAccountEntryProfile("10011");
//		aep.setIsActive("INACTIVE");
////		assertTrue(aepMgr.updateAccountEntryProfile(aep, getSession()));
//		
//		Transaction transaction = new Transaction();
//		transaction.setTransactionReferenceNumber("SPO-00002");
//		transaction.setAccountEntry(aep);
//		transaction.setAmount(1234);
//		transaction.setCreatedBy("MARK");
//		transaction.setTransactionType("SUPPLIER");
//		assertTrue(trnsMgr.addTransaction(transaction, getSession()));
//	}
	
//	@Test
//	public void editAccountEntry() {
//		AccountEntryProfile aep = aepMgr.loadAccountEntryProfile(10011);
//		aep.setIsACtive("INACTIVE");
//		assertTrue(aepMgr.updateAccountEntryProfile(aep, getSession()));
//	}
	
	@Test
	public void loadAccountEntry() {

		
		List list = new ArrayList<>();
		
		AccountEntryProfile aep = new AccountEntryProfile();
		Transaction transaction = new Transaction();
		transaction.setTransactionReferenceNumber("SPO-00002");
		transaction.setAccountEntry(aep);
		transaction.setAmount(1234);
		transaction.setCreatedBy("MARK");
		transaction.setTransactionType("SUPPLIER");
		list.add(transaction);
		
		
		List list2 = new ArrayList<>();
		AccountEntryProfile aep2 = new AccountEntryProfile();
		list2.add(aep2);
		
		
		
		
		
//		assertNotNull(aepMgr.loadAccountEntryProfile(10011));
	}
	
//	@Test
//	public void deleteAccountEntry() {
//		AccountEntryProfile aep = new AccountEntryProfile();
//		aep.setAccountCode(10011);
//		assertTrue(aepMgr.deleteAccountEntryProfile(aep.getAccountCode(), AccountEntryProfile.class, getSession()));
//	}
	
//	@Test
//	public void getDefaultAccountEntriesForTransactions() {
//		 List<String> defaultApeList =  trnsMgr.getDefaultAccountEntriesForTransactions("ReceivingReport", getSession());
//		 assertNotNull(defaultApeList);
//	}
	
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
}
