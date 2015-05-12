package com.shofuku.accsystem.controllers;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.ReceiptsDaoImpl;
/*
 * add business side logic in this class
 */
public class ReceiptsManager extends BaseController{
	
	ReceiptsDaoImpl dao = new ReceiptsDaoImpl();

	public boolean addReceiptsObject(Object receiptsObject,Session session) {
		return dao.save(receiptsObject,session);
	}

	@SuppressWarnings("rawtypes")
	public boolean deleteReceiptsByParameter(Object object, Class clazz,Session session){
		return dao.deleteByParameter(object, clazz,session);
	}

	public boolean updateReceipts(Object persistentObject,Session session){
		return dao.update(persistentObject,session);
	}

	@SuppressWarnings("rawtypes")
	public List listReceiptsByParameter(Class clazz, String parameter,
			String value,Session session){
		return dao.listByParameter(clazz, parameter, value,session);
	}

	@SuppressWarnings("rawtypes")
	public List listReceiptsByParameterLike(Class clazz, String parameter,
			String value,Session session){
		return dao.listByParameter(clazz, parameter, value,session);
	}
	
	@SuppressWarnings("rawtypes")
	public List getReceiptElementsBetweenDatesByParameter(Date startDate,
			Date endDate,String className,String parameter,Session session){
		return  dao.getBetweenDates(startDate, endDate,className, parameter,session);
	}
	
	@SuppressWarnings("rawtypes")
	public List getReceiptElementsByDate(Date date,
			String className,String parameter,Session session){
		return  dao.getBetweenDates(date, date,className, parameter,session);
	}
	
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		return dao.listAlphabeticalAscByParameter(clazz, parameter,session);
	} 
}
