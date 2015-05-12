package com.shofuku.accsystem.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.shofuku.accsystem.dao.impl.SupplierDaoImpl;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;

/*
 * add business side logic in this class
 */
@SuppressWarnings("rawtypes")
public class SupplierManager extends BaseController{

	SupplierDaoImpl dao = new SupplierDaoImpl();
	InventoryManager invMgr = new InventoryManager();

	public Supplier loadSupplier(String supplierId) {
		Supplier supplier =(Supplier) dao.load(new Supplier(supplierId), Supplier.class);
		if(supplier==null){
			return null;
		}else {
			return supplier;
		}
	}
	
	public boolean addSupplierObject(Object supplierObject,Session session) {
			return dao.save(supplierObject,session);
	}
	
	public boolean saveOrUpdate(Object supplierObject,Session session) {
		return dao.saveOrUpdate(supplierObject,session);
}

	public boolean deleteSupplierByParameter(Object object, Class clazz,Session session) {
		return dao.deleteByParameter(object, clazz,session);
	}

	public boolean updateSupplier(Object persistentObject,Session session) {
		return dao.update(persistentObject,session);
	}

	public List getSupplierElementsBetweenDatesByParameter(Date startDate,
			Date endDate,String className,String parameter,Session session) {
		return  dao.getBetweenDates(startDate, endDate,className, parameter,session);
	}
	
	public List getSupplierElementsByDate(Date date,
			String className,String parameter,Session session) {
		return  dao.getBetweenDates(date, date,className, parameter,session);
	}

	public List listSuppliersByParameter(Class clazz,
			String parameter, String value,Session session) {
		return dao.listByParameter(clazz, parameter, value,session);
	}
	
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		return dao.listAlphabeticalAscByParameter(clazz, parameter,session);
	}
	public List listSupplierByParameterLike(Class clazz, String parameter, String value,Session session) {
		return dao.listByParameterLike(clazz, parameter, value,session);
	}
	public List listByName(Class clazz, String parameter, String value,Session session) {
		return dao.listByName(clazz, parameter, value,session);
	}
	public List getPODetails(Class clazz,String spoid,Session session) {
		return dao.listPurchaseOrderDetails(clazz,spoid,session);
		
	}
	
	public List searchSupplierInvoiceBySupplierName(Class clazz, String parameter, String value,Session session){
		return dao.searchSupplierInvoiceBySupplierName(clazz, parameter, value,session);
	}
	
	public List searchSupplierReceivingReportBySupplierName(Class clazz, String parameter, String value,Session session){
		return dao.searchSupplierRecevingReportBySupplierName(clazz, parameter, value,session);
	}
	
	public void generateSupplierOrderForm(Session session){
		
	}
	public List listByParameter(Class clazz, String parameter, String value,Session session) {
		return dao.listByParameter(clazz, parameter, value,session);
	}
}
