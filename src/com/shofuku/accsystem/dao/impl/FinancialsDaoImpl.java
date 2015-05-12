package com.shofuku.accsystem.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StringType;

import com.shofuku.accsystem.dao.BaseHibernateDao;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;

public class FinancialsDaoImpl extends BaseHibernateDaoImpl{

	
	public String getAccountEntryProfileByCode(char lastLetter,Session session){
//		int x = 0;
//		Transaction tx = null;
//		try {
//			tx=getCurrentTransaction(session);
//			Query query = session.createSQLQuery("SELECT MAX(SUPPLIER_ID) as SUPPLIER_ID FROM T101_SUPPLIERS WHERE SUPPLIER_ID LIKE 'S"+lastLetter+"%' ORDER BY SUPPLIER_ID ASC")
//			 .addScalar("SUPPLIER_ID", StringType.INSTANCE);
//			return (String)query.list().get(0);
//		} catch (RuntimeException re) {
//			re.printStackTrace();
			return "";
//		} 
//
	}
	
	public List getSupplierInvoiceFromSupplierIDList(List supplierList,Session session) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(SupplierInvoice.class)
				.createCriteria("receivingReport","receivingReport")
				.createCriteria("receivingReport.supplierPurchaseOrder","supplierPO")
				.createCriteria("supplierPO.supplier","supplier")
				.add(Restrictions.in("supplier.supplierId",supplierList));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;
	}
	
	public List getCustomerInvoiceFromCustomerIDList(List customerList,Session session) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CustomerSalesInvoice.class)
				.createCriteria("deliveryReceipt","deliveryReceipt")
				.createCriteria("deliveryReceipt.customerPurchaseOrder","cpo")
				.createCriteria("cpo.customer","customer")
				.add(Restrictions.in("customer.customerNo",customerList));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;
	}
	
	public List getAllTransactionPerAccount(Class clazz, String propertyName, String value,Session session){
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Criteria c = session.createCriteria(Transaction.class, "transaction");
			c.createAlias("transaction.accountEntry", "aep");
			c.add(Restrictions.or(
					Restrictions.like(propertyName, "%" + value + "%")
							.ignoreCase(), Restrictions.or(Restrictions
							.like(propertyName, "%" + value)
							.ignoreCase(),
							Restrictions
									.like(propertyName, value + "%")
									.ignoreCase())));
			return c.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}finally{
			
		}
		return null;
	}
	
	
	public List getActiveTransactionsBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session
					.createCriteria(Class.forName(className)).add(
							Restrictions.between(field, startDate, endDate));
			criteria.add(Restrictions.eq("isInUse", "IN USE"));
			criteria.addOrder(Order.asc(field));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
