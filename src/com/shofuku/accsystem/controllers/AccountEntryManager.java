package com.shofuku.accsystem.controllers;

import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.AccountEntryDaoImpl;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.AccountingRules;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.domain.financials.Vat;

public class AccountEntryManager extends BaseController{
	
	AccountEntryDaoImpl accountEntryDao = new AccountEntryDaoImpl();
	
	
	public AccountEntryProfile loadAccountEntryProfile(String accountCode) {
		AccountEntryProfile accountEntry =(AccountEntryProfile) accountEntryDao.load(accountCode, AccountEntryProfile.class);
		if(accountEntry==null){
			return null;
		}else {
			return accountEntry;
		}
	}
	
	public JournalEntryProfile loadJournalEntryProfile(String accountCode, Session session) {
		JournalEntryProfile journalEntry =(JournalEntryProfile) accountEntryDao.listByParameter(JournalEntryProfile.class, 
				"entryNo", accountCode, session);
		if(journalEntry==null){
			return null;
		}else {
			return journalEntry;
		}
	}
	
	public boolean addAccountEntryProfile(Object aepObject,Session session) {
			return accountEntryDao.save(aepObject,session);
	}
	
	public boolean addAccountEntryRule(Object accountingRule,Session session) {
			return accountEntryDao.persistingInsert(accountingRule,session);
	}
	
	public boolean updateAccountingRule(AccountingRules rules,Session session) {
		return accountEntryDao.persistingUpdate(rules, session);
	}
	
	public AccountingRules loadAccountingProfileRuleByAccountCode(String accountCode, Session session) {
		List list = accountEntryDao.listByParameter(AccountingRules.class,"accountCode", accountCode, session);
		if(list==null){
			return null;
		}else {
			if(list.size()>0) {
				return (AccountingRules)list.get(0);
			}
		}
		return null;
	}
	

	public boolean deleteAccountEntryProfile(Object object, Class clazz,Session session) {
		return accountEntryDao.deleteByParameter(object, clazz,session);
	}

	public boolean updateAccountEntryProfile(Object persistentObject,Session session) {
		return accountEntryDao.update(persistentObject,session);
	}

	public List listAccountEntryProfileByName(Class clazz,
			String parameter, String value,Session session) {
		return accountEntryDao.listByParameter(clazz, parameter, value,session);
	}
	
	public List listAlphabeticalAccountEntryProfileAscByParameter(Class clazz, String parameter,Session session) {
		return accountEntryDao.listAlphabeticalAscByParameter(clazz, parameter,session);
	}
	
	public List listAlphabeticalAccountEntryProfileChildrenAscByParameter(Session session) {
		return accountEntryDao.listAlphabeticalAccountEntryProfileChildrenAscByParameter(session);
	}
	public List listAccountEntryProfileByParameterLike(Class clazz, String parameter, String value,Session session) {
		return accountEntryDao.listByParameterLike(clazz, parameter, value,session);
	}
	public List listByParameter(Class clazz, String parameter, String value,Session session) {
		return accountEntryDao.listByParameter(clazz, parameter, value,session);
	}
	
	public String getAccountCode(String parentCode,Session session) {
		int latestAvailableChildSlot = accountEntryDao.getChildrenCountByParentCode(parentCode, session)+1;
		return String.valueOf(Integer.valueOf(parentCode)*10+latestAvailableChildSlot);
		
	}
	
	public List<AccountEntryProfile> getAccountEntriesFromList(List accountCodes,Session session){
		return accountEntryDao.listAccountEntriesFromList(accountCodes,session);
	}
	
	public Long getTotalRecordCount(Class clazz,Session session) {
		return accountEntryDao.recordCount(clazz, session);
	}
	
}
