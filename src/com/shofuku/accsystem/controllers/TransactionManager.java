package com.shofuku.accsystem.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;

import com.shofuku.accsystem.dao.impl.TransactionsDaoImpl;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.utils.SASConstants;

public class TransactionManager extends BaseController {
	
	
/*	public Transaction loadTransaction(String referenceId) {
		Transaction transaction = (Transaction) transactionsDao.load(referenceId, Transaction.class);
		if(transaction==null){
			return null;
		}else {
			return transaction;
		}
	}
	*/
	public boolean addTransaction(Object object,Session session) {
			return transactionsDao.save(object,session);
	}

	public boolean addTransactionsList(List<Transaction> transactionsList,Session session) {
		return transactionsDao.addTransactionList(transactionsList, session);
	}

	public boolean deleteTransaction(Object object, Class clazz,Session session) {
		return transactionsDao.deleteByParameter(object, clazz,session);
	}

	public boolean updateTransaction(Object persistentObject,Session session) {
		return transactionsDao.persistingUpdate(persistentObject,session);
	}

	public List listTransactionByName(Class clazz,
			String parameter, String value,Session session) {
		return transactionsDao.listByParameter(clazz, parameter, value,session);
	}
	
	public List listAlphabeticalTransactionAscByParameter(Class clazz, String parameter,Session session) {
		return transactionsDao.listAlphabeticalAscByParameter(clazz, parameter,session);
	}
	
	public List listTransactionByParameterLike(Class clazz, String parameter, String value,Session session) {
		return transactionsDao.listByParameterLike(clazz, parameter, value,session);
	}
	public List<String> getDefaultAccountEntriesForTransactions(String transactionType,Session session){
		return transactionsDao.getDefaultAccountEntriesForTransactions(transactionType, session);
	}

	public void discontinuePreviousTransactions(String referenceNo,Session session) {
		List<Transaction> tempList = transactionsDao.listByParameterLike(Transaction.class, "transactionReferenceNumber", referenceNo ,session);
		if (tempList != null) {
		Iterator itr = tempList.iterator();
		while(itr.hasNext()) {
			Transaction transaction = (Transaction)itr.next();
			transaction.setIsInUse(SASConstants.TRANSACTION_NOT_IN_USE);
			updateTransaction(transaction, session);
			}
		}
	}
}
