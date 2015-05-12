package com.shofuku.accsystem.controllers;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.FinancialsDaoImpl;
import com.shofuku.accsystem.domain.financials.Vat;

public class FinancialsManager extends BaseController {

	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	FinancialsDaoImpl dao = new FinancialsDaoImpl();
	
	public boolean insertVatDetails(Vat vat,Session session) {
		return dao.persistingInsert(vat, session);
	}
	public boolean updateVatDetails(Vat vat,Session session) {
		return dao.persistingUpdate(vat, session);
	}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	
	//START: 2013 - PHASE 3 : PROJECT 2: MARK
	public List getSupplierInvoiceFromSupplierIDList(List supplierIdList, Session session) {
		return dao.getSupplierInvoiceFromSupplierIDList(supplierIdList, session);
	}
	
	public List getCustomerInvoiceFromCustomerIDList(List supplierIdList, Session session) {
		return dao.getCustomerInvoiceFromCustomerIDList(supplierIdList, session);
	}
	
	public List getVatDetailsBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session,String orderBy) {
		return dao.getBetweenDates(startDate, endDate, className, field, session);
	}
	
	public List getActiveTransactionsBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session,String orderBy) {
		return dao.getActiveTransactionsBetweenDates(startDate, endDate, className, field, session);
	}
	//END: 2013 - PHASE 3 : PROJECT 2: MARK
	public List getAllTransactionAmountPerAccount(Class clazz, String propertyName, String value, Session session) {
		return dao.getAllTransactionPerAccount(clazz, propertyName, value, session);
	}
	
}
