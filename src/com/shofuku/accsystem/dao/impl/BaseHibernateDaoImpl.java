package com.shofuku.accsystem.dao.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hibernate.type.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.internal.CriteriaImpl.Subcriteria;

import com.shofuku.accsystem.action.suppliers.AddSupplierAction;
import com.shofuku.accsystem.dao.BaseHibernateDao;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.PurchaseOrder;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.RecordCount;
import com.shofuku.accsystem.utils.SASConstants;

@SuppressWarnings("rawtypes")
public class BaseHibernateDaoImpl extends HibernateUtil implements
		BaseHibernateDao {
	
	private static final Logger logger = Logger
			.getLogger(AddSupplierAction.class);
	
	private UserAccount user;
	public UserAccount getUser() {
		return user;
	}
	public void setUser(UserAccount user) {
		this.user = user;
	}
	
	protected Session getSession() {
		return super.getSessionFactory().getCurrentSession();
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
	
	public Long recordCount(Class clazz,Session session) {
		Long count=0L;
		
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session.createCriteria(clazz);
			criteria.setProjection(Projections.rowCount());
			
			//add location criteria
			if(beanContainsLocationProperty(clazz,"recordCount")) {
				// access individual properties
				criteria.add(addLocationCriteria(clazz));
			}
			
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
			
			//add location property value to object
			if(beanContainsLocationProperty(item.getClass(), "save")) {
				addLocationProperty(item);
			}

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
			
			//add location property value to object
			if(beanContainsLocationProperty(item.getClass(), "saveOrUpdate")) {
				addLocationProperty(item);
			}
			
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
			
			//add location property value to object
			if(beanContainsLocationProperty(item.getClass(), "update")) {
				addLocationProperty(item);
			}
			
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
			
			//add location property value to object
			if(beanContainsLocationProperty(object.getClass(), "mergeByParameter")) {
				addLocationProperty(object);
			}
			
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
					//add location criteria
					if(beanContainsLocationProperty(CustomerSalesInvoice.class,"getCustomerSalesInvoiceByCustomers")) {
						// access individual properties
						criteria.add(addLocationCriteria(CustomerSalesInvoice.class));
					}
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
					//add location criteria
					if(beanContainsLocationProperty(ReceivingReport.class,"getReceivingReportBySupplier")) {
						// access individual properties
						criteria.add(addLocationCriteria(ReceivingReport.class));
					}
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
			//add location criteria
			if(beanContainsLocationProperty(clazz,"listByParameterLike")) {
				// access individual properties
				criteria.add(addLocationCriteria(ReceivingReport.class));
			}
					criteria.addOrder(Order.asc(propertyName));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;

	}
	
	@Deprecated // used by listSummaryByLocation
	private Conjunction limitByLocation(String column,String userLocation, Session session) {
		
		Conjunction conjunction = Restrictions.conjunction();
		
		
		List<String> locationList = listDistinctReferenceNo(UserAccount.class, "location", session);
		for(String location : locationList) {
			if(location.equalsIgnoreCase(userLocation)) {
			}else {
				conjunction.add(Restrictions.not(Restrictions.like(column, location+"-"+"%")));
			}
		}
		
		return conjunction;
	}
	@Deprecated // all querries have location criteria
	public List listSummaryByLocation(Class clazz, String orderBy,String column,Session session) {

		Transaction tx = null;
		tx=getCurrentTransaction(session);
		Criteria criteria = session.createCriteria(clazz);
		
		//for old records that dont contain location prefixes , defaults to MNL
		if (null==user.getLocation() || user.getLocation().equalsIgnoreCase("")) {
		}else {
			if(user.getLocation().equalsIgnoreCase(SASConstants.LOCATION_MANILA)) {
				
				//get all except those with distinct locations
				
			}else {
				criteria.add(Restrictions.like(column,   user.getLocation() +"-"+ "%"));
			}
		}
		criteria.add(limitByLocation(column,user.getLocation(), session));		
		criteria.addOrder(Order.asc(orderBy));
		try {
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
			//add location criteria
			if(beanContainsLocationProperty(clazz,"listDistinctReferenceNo")) {
				// access individual properties
				criteria.add(addLocationCriteria(ReceivingReport.class));
			}
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;

	}

	public List listByParameter(Class clazz, String propertyName, String value,Session session) {

		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session.createCriteria(clazz).add(
					Restrictions.eq(propertyName, value).ignoreCase());
			//add location criteria
			if(beanContainsLocationProperty(clazz,"listByParameter")) {
				// access individual properties
				criteria.add(addLocationCriteria(clazz));
			}
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
			//add location criteria
			if(beanContainsLocationProperty(clazz,"listAlphabeticalAscByParameter")) {
				// access individual properties
				criteria.add(addLocationCriteria(clazz));
			}
			criteria.addOrder(Order.asc(parameter));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
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
			//add location criteria
			if(beanContainsLocationProperty(Class.forName(className),"getBetweenDates")) {
				// access individual properties
				criteria.add(addLocationCriteria(Class.forName(className)));
			}
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
	
	public List getBetweenDatesWithOrderBy(Date startDate, Date endDate, String className,
			String field,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session
					.createCriteria(Class.forName(className)).add(
							Restrictions.between(field, startDate, endDate));
			
			//add location criteria for applicable beans
			if(beanContainsLocationProperty(Class.forName(className),"getBetweenDatesWithOrderBy")) {
				// access individual properties
				criteria.add(addLocationCriteria(Class.forName(className)));
			}
			
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
			
			Criteria cr = session.createCriteria(RecordCount.class)
				    .setProjection(Projections.projectionList()
				      .add(Projections.property("recordCount")));
					cr.add(Restrictions.eq("moduleName", subModule));
					 return  (Integer.valueOf((String)cr.list().get(0)));
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
	
	public void updateRecordCounts(String subModule,int count,Session session){
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
				//add location property value to object
				if(beanContainsLocationProperty(obj.getClass(), "persistingInsert")) {
					addLocationProperty(obj);
				}			
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
			//add location property value to object
			if(beanContainsLocationProperty(item.getClass(), "persistingUpdate")) {
				addLocationProperty(item);
			}	
			session.merge(item);
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
	}
	
	public boolean persistingDelete(Object item,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			//add location property value to object
			if(beanContainsLocationProperty(item.getClass(), "persistingDelete")) {
				addLocationProperty(item);
			}	
			session.delete(item);
			tx.commit();
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.shofuku.accsystem.dao.BaseHibernateDao#getMaxRows(java.lang.String, org.hibernate.Session)
	 * used for accounting profiles t702 table only change this description if used elsewhere
	 */
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

	@Override
	public Object persistingAdd(Object object,Session session) {
		Transaction tx = null;
		tx = getCurrentTransaction(session);
			try {
				//add location property value to object
				if(beanContainsLocationProperty(object.getClass(), "persistingAdd")) {
					addLocationProperty(object);
				}			
				session.save(object);
				return object;
			} catch (RuntimeException re) {
				if (null != tx) {
					tx.rollback();
				}
				re.printStackTrace();
				return null;
			} 
	}

	private Criterion addLocationCriteria(Class clazz) {
			return Restrictions.eq(SASConstants.LOCATION, getUser().getLocation());
	}
	
	private void addLocationProperty(Object obj ) {
		try {
			BeanUtils.setProperty(obj,"location",getUser().getLocation());
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("CANNOT ADD LOCATION PROPERTY TO: " + obj.getClass().getName());
		}
	}
	
	private boolean beanContainsLocationProperty(Class clazz, String methodName) {

		StringBuilder builder = new StringBuilder();
		for (String s : SASConstants.ARRAY_CLASS_WITHOUT_LOCATION) {
			builder.append(s + " ");
		}
		String exemptedClasses = builder.toString();
		
		for (String s : SASConstants.ARRAY_PACKAGES_WITHOUT_LOCATION) {
			builder.append(s + " ");
		}
		String exemptedPackages = builder.toString(); 
				
		if (exemptedClasses.indexOf(clazz.getSimpleName() + ".class") > -1 || exemptedPackages.indexOf(SASConstants.SHOFUKU_PACKAGE_NAME + clazz.getCanonicalName())>-1 ) {
			return false;
		} else {
			try {
				// create a new object based on the clazz parameter through Java
				// Reflection.Constructor
				Object object = null;
				Constructor ctor = clazz.getConstructor();
				object = ctor.newInstance();

				//if below line of code does not give exception then location is a valid property for this object
				String locationName = BeanUtils.getProperty(object, "location");
				return true;
			} catch (InstantiationException | IllegalArgumentException
					| IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				// location code not yet set for this bean
				logger.error("ERROR ON CLASS: " + clazz.getName()
						+ " ON METHOD: " + methodName);
				logger.error("ERROR: LOCATION CODE PROPERTY NOT YET ADDED TO THIS BEAN, and NOT ON EXEMPTED LIST");
				return false;
			}
		}

	}

}
