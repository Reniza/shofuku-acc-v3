package com.shofuku.accsystem.dao.impl;

import org.hibernate.type.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import com.shofuku.accsystem.dao.BaseHibernateDao;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.PurchaseOrder;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

@SuppressWarnings("rawtypes")
public class BaseHibernateDaoImpl extends HibernateUtil implements
		BaseHibernateDao {
	protected Session getSession() {
		return super.getSessionFactory().getCurrentSession();
	}
	public Long recordCount(Class clazz,Session session) {
		Long count=0L;
		
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session.createCriteria(clazz);
			criteria.setProjection(Projections.rowCount());
			count=(Long) criteria.uniqueResult();
			return count;
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		} 
		return count;
	}

	public boolean save(Object item,Session ss) throws ConstraintViolationException{
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(ss);
			ss.save(item);
			tx.commit();
			return true;
		}catch (ConstraintViolationException ex) {
				throw ex;
		}catch (RuntimeException re) {
			if (null != tx) {
				tx.rollback();
			}
			re.printStackTrace();
		} 
		return false;
	}
	
	public boolean saveOrUpdate(Object item,Session ss) throws ConstraintViolationException{
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(ss);
			ss.saveOrUpdate(item);
			tx.commit();
			return true;
		}catch (ConstraintViolationException ex) {
				throw ex;
		}catch (RuntimeException re) {
			if (null != tx) {
				tx.rollback();
			}
			re.printStackTrace();
		} 
		return false;
	}
	
	public boolean update(Object item,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			session.merge(item);
			tx.commit();
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
	}

	public Object load(Object object, Class clazz) {
		Transaction tx = null;
		Session session = getSession();
		try {
			tx=getCurrentTransaction(session);
			Object item = session.get(clazz, (Serializable) object);
			return item;
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}finally {
			if(session.isOpen()) {
				session.close();
				session.getSessionFactory().close();
			}
		}
		return false;
	}

	public boolean deleteByParameter(Object object, Class clazz,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			session.delete(this.load(object, clazz));
			tx.commit();
			return true;
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		} 
		return false;
	}
	
	public boolean mergeByParameter(Object object, Class clazz,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			session.merge(this.load(object, clazz));
			session.delete(this.load(object, clazz));
			tx.commit();
			return true;
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		} 
		return false;
	}

	
	public List getCustomerSalesInvoiceByCustomers(String dateFrom, String dateTo,List customerList,Session session) {
		if(session.isOpen()){
		}else{
			session=HibernateUtil.getSessionFactory().getCurrentSession();
		}

		Transaction tx = getCurrentTransaction(session);
		try {
//				Query query = session.createQuery("from CustomerPurchaseOrder where( customer.customerNo in  (:customerList) )and (between :dateFrom and :dateTo ");
//				query.setParameterList("customerList", customerList);
//				query.setParameter("dateFrom", dateFrom);
//				query.setParameter("dateTo", dateTo);
			DateFormatHelper dfh = new DateFormatHelper();
			Date startDate = dfh.parseStringToTime(dateFrom);
			Date endDate = dfh.parseStringToTime(dateTo);
				Criteria criteria;
					criteria = session
							.createCriteria(CustomerSalesInvoice.class).add(
									Restrictions.between("customerInvoiceDate", startDate, endDate));
					return criteria.list();
		} catch (Exception re) {
			tx.rollback();
			re.printStackTrace();
			return null;
		}
	}
	
	public List getReceivingReportBySupplier(String dateFrom, String dateTo,List customerList,Session session) {
		if(session.isOpen()){
		}else{
			session=HibernateUtil.getSessionFactory().getCurrentSession();
		}

		Transaction tx = getCurrentTransaction(session);
		try {
//				Query query = session.createQuery("from CustomerPurchaseOrder where( customer.customerNo in  (:customerList) )and (between :dateFrom and :dateTo ");
//				query.setParameterList("customerList", customerList);
//				query.setParameter("dateFrom", dateFrom);
//				query.setParameter("dateTo", dateTo);
			DateFormatHelper dfh = new DateFormatHelper();
			Date startDate = dfh.parseStringToTime(dateFrom);
			Date endDate = dfh.parseStringToTime(dateTo);
				Criteria criteria;
					criteria = session
							.createCriteria(ReceivingReport.class).add(
									Restrictions.between("receivingReportDate", startDate, endDate));
					return criteria.list();
		} catch (Exception re) {
			tx.rollback();
			re.printStackTrace();
			return null;
		}
	}
	
	

	public List listByParameterLike(Class clazz, String propertyName,
			String value,Session session) {

		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session.createCriteria(clazz).add(
					Restrictions.or(
							Restrictions.like(propertyName, "%" + value + "%")
									.ignoreCase(), Restrictions.or(Restrictions
									.like(propertyName, "%" + value)
									.ignoreCase(),
									Restrictions
											.like(propertyName, value + "%")
											.ignoreCase())));
					criteria.addOrder(Order.asc(propertyName));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;

	}
	
	public List listDistinctReferenceNo(Class clazz, String propertyName,Session session) {

		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session.createCriteria(clazz).setProjection(Projections.distinct(Projections.property(propertyName)));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;

	}

	private Transaction getCurrentTransaction(Session session){
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
		}catch(RuntimeException runtimeExecption){
			tx = session.getTransaction();
		}		
		return tx;
	}

	public List listByParameter(Class clazz, String propertyName, String value,Session session) {

		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session.createCriteria(clazz).add(
					Restrictions.eq(propertyName, value).ignoreCase());
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}finally{
			
		}
		return null;

	}
	
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session.createCriteria(clazz);
			criteria.addOrder(Order.asc(parameter));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;
	}
	
	public List listByParameters(Criteria criteria,Session session) {

		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}finally{
			
		}
		return null;

	}


	public List getBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session,String orderBy) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session
					.createCriteria(Class.forName(className)).add(
							Restrictions.between(field, startDate, endDate));
			criteria.addOrder(Order.asc(orderBy));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List getBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session
					.createCriteria(Class.forName(className)).add(
							Restrictions.between(field, startDate, endDate));
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
	
	public int getRecordCount(String subModule,Session session){
		int x = 0;
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Query query = session.createSQLQuery("SELECT RECORD_COUNT FROM T901_RECORD_COUNTS WHERE MODULE_NAME = '"+subModule+"'")
			 .addScalar("RECORD_COUNT", IntegerType.INSTANCE);
			return (Integer)query.list().get(0);
		} catch (RuntimeException re) {
			re.printStackTrace();
			return 0;
		} 

	}
	
	public String getLastSupplierByInitialLetter(char lastLetter,Session session){
		int x = 0;
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Query query = session.createSQLQuery("SELECT MAX(SUPPLIER_ID) as SUPPLIER_ID FROM T101_SUPPLIERS WHERE SUPPLIER_ID LIKE 'S"+lastLetter+"%' ORDER BY SUPPLIER_ID ASC")
			 .addScalar("SUPPLIER_ID", StringType.INSTANCE);
			return (String)query.list().get(0);
		} catch (RuntimeException re) {
			re.printStackTrace();
			return "";
		} 

	}
	
	public String getLastCustomerByInitialLetter(char lastLetter,Session session){
		int x = 0;
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Query query = session.createSQLQuery("SELECT MAX(CUSTOMER_ID) as CUSTOMER_ID FROM T201_CUSTOMERS WHERE CUSTOMER_ID LIKE 'C"+lastLetter+"%' ORDER BY CUSTOMER_ID ASC")
			 .addScalar("CUSTOMER_ID", StringType.INSTANCE);
			return (String)query.list().get(0);
		} catch (RuntimeException re) {
			re.printStackTrace();
			return "";
		} 

	}
	
	public void getUpdateCount(String subModule,int count,Session session){
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Query query = session.createSQLQuery("UPDATE T901_RECORD_COUNTS SET RECORD_COUNT= "+count+" WHERE MODULE_NAME = '"+subModule+"'");
			query.executeUpdate();
			tx.commit();
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
	}
	
	public boolean persistingInsert(Object obj, Session ss) {

		Transaction tx = null;
		tx = getCurrentTransaction(ss);
			try {
				ss.save(obj);
				return true;
			} catch (RuntimeException re) {
				if (null != tx) {
					tx.rollback();
				}
				re.printStackTrace();
			} 
		return false;
	}
	
	public boolean persistingUpdate(Object item,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			session.merge(item);
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
	}
	
	

	public int getMaxRows(String tableName, Session session) {
		int x = 0;
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Query query = session.createSQLQuery("SELECT COUNT(*) FROM "+tableName);
			return ((BigInteger)query.list().get(0)).intValue();
			
		} catch (RuntimeException re) {
			re.printStackTrace();
			return 0;
		} 
		
	}
}
