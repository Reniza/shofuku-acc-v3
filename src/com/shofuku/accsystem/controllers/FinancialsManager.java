package com.shofuku.accsystem.controllers;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.FinancialsDaoImpl;
import com.shofuku.accsystem.domain.financials.Vat;

public class FinancialsManager extends BaseController {

	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	public boolean insertVatDetails(Vat vat,Session session) {
		return financialsDao.persistingInsert(vat, session);
	}
	public boolean updateVatDetails(Vat vat,Session session) {
		return financialsDao.persistingUpdate(vat, session);
	}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	
	//START: 2013 - PHASE 3 : PROJECT 2: MARK
	public List getSupplierInvoiceFromSupplierIDList(List supplierIdList, Session session) {
		return financialsDao.getSupplierInvoiceFromSupplierIDList(supplierIdList, session);
	}
	
	public List getCustomerInvoiceFromCustomerIDList(List supplierIdList, Session session) {
		return financialsDao.getCustomerInvoiceFromCustomerIDList(supplierIdList, session);
	}
	
	public List getVatDetailsBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session,String orderBy) {
		return financialsDao.getBetweenDates(startDate, endDate, className, field, session);
	}
	
	public List getActiveTransactionsBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session,String orderBy) {
		return financialsDao.getActiveTransactionsBetweenDates(startDate, endDate, className, field, session);
	}
	//END: 2013 - PHASE 3 : PROJECT 2: MARK
	public List getAllTransactionAmountPerAccount(Class clazz, String propertyName, String value, Session session) {
		return financialsDao.getAllTransactionPerAccount(clazz, propertyName, value, session);
	}
	
}
