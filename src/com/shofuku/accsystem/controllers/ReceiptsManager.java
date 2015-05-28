package com.shofuku.accsystem.controllers;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.ReceiptsDaoImpl;
/*
 * add business side logic in this class
 */
public class ReceiptsManager extends BaseController{
	
	public boolean addReceiptsObject(Object receiptsObject,Session session) {
		return receiptsDao.save(receiptsObject,session);
	}

	@SuppressWarnings("rawtypes")
	public boolean deleteReceiptsByParameter(Object object, Class clazz,Session session){
		return receiptsDao.deleteByParameter(object, clazz,session);
	}

	public boolean updateReceipts(Object persistentObject,Session session){
		return receiptsDao.update(persistentObject,session);
	}

	@SuppressWarnings("rawtypes")
	public List listReceiptsByParameter(Class clazz, String parameter,
			String value,Session session){
		return receiptsDao.listByParameter(clazz, parameter, value,session);
	}

	@SuppressWarnings("rawtypes")
	public List listReceiptsByParameterLike(Class clazz, String parameter,
			String value,Session session){
		return receiptsDao.listByParameter(clazz, parameter, value,session);
	}
	
	@SuppressWarnings("rawtypes")
	public List getReceiptElementsBetweenDatesByParameter(Date startDate,
			Date endDate,String className,String parameter,Session session){
		return  receiptsDao.getBetweenDatesWithOrderBy(startDate, endDate,className, parameter,session);
	}
	
	@SuppressWarnings("rawtypes")
	public List getReceiptElementsByDate(Date date,
			String className,String parameter,Session session){
		return  receiptsDao.getBetweenDatesWithOrderBy(date, date,className, parameter,session);
	}
	
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		return receiptsDao.listAlphabeticalAscByParameter(clazz, parameter,session);
	} 
}
