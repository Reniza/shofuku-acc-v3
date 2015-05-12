package com.shofuku.accsystem.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;


@SuppressWarnings("rawtypes")
public class CustomerDaoImpl extends BaseHibernateDaoImpl {

	
	
	//add specific HQL / criteria calls here if any
	public List listByName(Class clazz, String propertyName, String value,Session session) {

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(clazz)
				.add(Restrictions.or(
					    Restrictions.like(propertyName,"%"+ value+"%").ignoreCase(), 
					    Restrictions.or(
							Restrictions.like(propertyName,"%"+ value).ignoreCase(),
							Restrictions.like(propertyName, value+"%").ignoreCase())
							)
							).createAlias("customer", "customer", JoinType.LEFT_OUTER_JOIN);
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;

	}

	public List searchCustomerDeliveryReceiptByCustomerName(Class clazz,
			String propertyName, String value, Session session) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Criteria c = session.createCriteria(DeliveryReceipt.class, "deliveryReceipt");
			c.createAlias("deliveryReceipt.customerPurchaseOrder", "customerPurchaseOrder");
			c.createAlias("customerPurchaseOrder.customer", "customer");
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

	public List searchCustomerInvoiceByCustomerName(Class clazz,
			String propertyName, String value, Session session) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Criteria c = session.createCriteria(CustomerSalesInvoice.class, "customerSalesInvoice");
			c.createAlias("customerSalesInvoice.deliveryReceipt", "deliveryReceipt");
			c.createAlias("deliveryReceipt.customerPurchaseOrder", "customerPurchaseOrder");
			c.createAlias("customerPurchaseOrder.customer", "customer");
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
	
	}
