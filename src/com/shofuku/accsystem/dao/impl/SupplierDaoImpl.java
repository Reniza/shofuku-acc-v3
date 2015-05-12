package com.shofuku.accsystem.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;

@SuppressWarnings("rawtypes")
public class SupplierDaoImpl extends BaseHibernateDaoImpl {
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
								).createAlias("supplier", "supplier", JoinType.LEFT_OUTER_JOIN);
				return criteria.list();
			} catch (RuntimeException re) {
				tx.rollback();
				re.printStackTrace();
			}
			return null;

		}
		public List searchSupplierInvoiceBySupplierName(Class clazz, String propertyName, String value,Session session){
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				
				Criteria c = session.createCriteria(SupplierInvoice.class, "supplierInvoice");
				c.createAlias("supplierInvoice.receivingReport", "receivingReport");
				c.createAlias("receivingReport.supplierPurchaseOrder", "supplierPurchaseOrder");
				c.createAlias("supplierPurchaseOrder.supplier", "supplier");
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
		
		public List searchSupplierRecevingReportBySupplierName(Class clazz, String propertyName, String value,Session session){
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				
				Criteria c = session.createCriteria(ReceivingReport.class, "receivingReport");
				c.createAlias("receivingReport.supplierPurchaseOrder", "supplierPurchaseOrder");
				c.createAlias("supplierPurchaseOrder.supplier", "supplier");
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
		
		
		public List listPurchaseOrderDetails(Class clazz,String spoid,Session session) {
			return null;
		}
		
}
