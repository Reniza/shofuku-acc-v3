package com.shofuku.accsystem.controllers;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.DisbursementDaoImpl;
import com.shofuku.accsystem.utils.DoubleConverter;
import com.shofuku.accsystem.utils.SASConstants;
/*
 * add business side logic in this class
 */
public class DisbursementManager extends BaseController{
	
	DisbursementDaoImpl dao = new DisbursementDaoImpl();
	DoubleConverter dc = new DoubleConverter();

	public boolean addDisbursementObject(Object disbursementObject,Session session) {
			return dao.save(disbursementObject,session);
	}

	@SuppressWarnings("rawtypes")
	public boolean deleteDisbursementByParameter(Object object, Class clazz,Session session) {
		return dao.deleteByParameter(object, clazz,session);
	}

	public boolean updateDisbursement(Object persistentObject,Session session) {
		return dao.update(persistentObject,session);
	}

	@SuppressWarnings("rawtypes")
	public List listDisbursementsByParameter(Class clazz,
			String parameter, String value,Session session) {
		return dao.listByParameter(clazz, parameter, value,session);
	}
	
	@SuppressWarnings("rawtypes")
	public List listDisbursementsByParameterLike(Class clazz,
			String parameter, String value,Session session) {
		return dao.listByParameterLike(clazz, parameter, value,session);
	}
	public List listDisbursementsWithInvoice(Session session) {
		return dao.listCheckVoucherWithInvoice(session);
	}
	public List listDisbursementsCheckWithoutInvoice(Session session) {
		return dao.listCheckVoucherWithoutInvoice(session);
	}
	
	@SuppressWarnings("rawtypes")
	public List getDisbursementElementsBetweenDatesByParameter(Date startDate,
			Date endDate,String className,String parameter,Session session) {
		return  dao.getBetweenDates(startDate, endDate,className, parameter,session);
	}
	
	@SuppressWarnings("rawtypes")
	public List getDisbursementElementsByDate(Date date,
			String className,String parameter,Session session) {
		return  dao.getBetweenDates(date, date,className, parameter,session);
	}
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
			return  dao.listAlphabeticalAscByParameter(clazz, parameter, session);
	}
	public double computeVat(double amount){
		double vattedAmount = 0;
		double vatAmount = 0;
		
		vattedAmount = amount / SASConstants.VAT_PERCENT;
		vatAmount = amount - vattedAmount;
		return dc.formatDoubleToCurrency(vattedAmount);
	}
	public double computeVatAmount(double vattedAmount){
		//double vattedAmount = 0;
		double vatAmount = 0;
		
		//vattedAmount = amount / SASConstants.VAT_PERCENT;
		vatAmount = vattedAmount * SASConstants.VAT_AMOUNT_PERCENT;
		return dc.formatDoubleToCurrency(vatAmount);
	}
	public List listDistinctAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		return  dao.listDistinctReferenceNo(clazz, parameter, session);
}
		
}
