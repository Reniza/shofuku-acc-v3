package com.shofuku.accsystem.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

public interface BaseHibernateDao {
	
	public boolean save(Object item,Session session);

	@SuppressWarnings("rawtypes")
	public Object load(Object object, Class clazz);

	@SuppressWarnings("rawtypes")
	public boolean deleteByParameter(Object object, Class clazz,Session session);

	public boolean update(Object item,Session session);

	@SuppressWarnings("rawtypes")
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session);

	@SuppressWarnings("rawtypes")
	public List listByParameterLike(Class clazz, String propertyName,
			String value,Session session);

	@SuppressWarnings("rawtypes")
	public List listByParameter(Class clazz, String propertyName, String value,Session session);

	@SuppressWarnings("rawtypes")
	public List getBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session,String orderBy);

	@SuppressWarnings("rawtypes")
	public List getBetweenDates(Date startDate, Date endDate, String className,
			String field,Session session);
	
	public Object persistingAdd(Object object,Session session);
	
	
	public int getRecordCount(String subModule,Session session);
	public String getLastSupplierByInitialLetter(char lastLetter,Session session);
	public String getLastCustomerByInitialLetter(char lastLetter,Session session);
	

	public void getUpdateCount(String subModule,int count,Session session);

	public int getMaxRows(String tableName, Session session);
}
