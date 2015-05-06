package com.shofuku.accsystem.controllers;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.CustomerDaoImpl;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.suppliers.Supplier;

/*
 * add business side logic in this class
 */
@SuppressWarnings("rawtypes")
public class CustomerManager {

	CustomerDaoImpl dao = new CustomerDaoImpl();

	public Customer loadCustomer(String customerId) {
		Customer customer =(Customer) dao.load(customerId, Customer.class);
		if(customer==null){
			return null;
		}else {
			return customer;
		}
	}
	
	public boolean addCustomerObject(Object CustomerObject,Session session) {
		return dao.save(CustomerObject,session);
	}

	public boolean deleteCustomerByParameter(Object object, Class clazz,Session session) {
		return dao.deleteByParameter(object, clazz,session);
	}

	public boolean updateCustomer(Object persistentObject,Session session) {
		return dao.update(persistentObject,session);
	}

	public List getCustomerElementsBetweenDatesByParameter(Date startDate,
			Date endDate, String className, String parameter,Session session) {
		return dao.getBetweenDates(startDate, endDate, className, parameter,session);
	}

	public List getCustomerElementsByDate(Date date, String className,
			String parameter,Session session) {
		return dao.getBetweenDates(date, date, className, parameter,session);
	}

	public List listByParameter(Class clazz, String parameter, String value,Session session) {
		return dao.listByParameter(clazz, parameter, value,session);
	}

	public List listByName(Class clazz, String parameter, String value,Session session) {
		return dao.listByName(clazz, parameter, value,session);
	}
	public List listByParameterLike(Class clazz, String parameter, String value,Session session) {
		return dao.listByParameterLike(clazz, parameter, value,session);
	}
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
			return dao.listAlphabeticalAscByParameter(clazz, parameter, session);
	}
	
	public List listAllCustomerNo(Session session){
		return listAlphabeticalAscByParameter(Customer.class, "customerNo", session);
	}
	public List listAllCustomerPurchaseOrderNo(Session session) {
		return listAlphabeticalAscByParameter(CustomerPurchaseOrder.class, "customerPurchaseOrderId", session);
	}
	public List listAllCustomerDeliveryReceiptNo(Session session) {
		return listAlphabeticalAscByParameter(DeliveryReceipt.class, "deliveryReceiptNo", session);
	}
	
	public List searchCustomerDeliveryReceiptByCustomerName(Class clazz, String parameter, String value,Session session){
		return dao.searchCustomerDeliveryReceiptByCustomerName(clazz, parameter, value,session);
	}
	public List searchCustomerInvoiceByCustomerName(Class clazz, String parameter, String value,Session session){
		return dao.searchCustomerInvoiceByCustomerName(clazz, parameter, value,session);
	}
	
	public boolean saveOrUpdateCustomer(Object persistentObject,Session session) {
		return dao.saveOrUpdate(persistentObject,session);
	}
	
	public boolean persistingUpdate(Object persistentObject,Session session) {
		return dao.persistingUpdate(persistentObject,session);
	}

}
