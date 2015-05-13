package com.Junit.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.hibernate.Session;
import org.junit.Test;

import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.domain.customers.Certificate;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerStockLevel;
import com.shofuku.accsystem.domain.customers.Employee;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.POIUtil;

@SuppressWarnings("rawtypes")
public class CustomerTest {
	CustomerManager cm = new CustomerManager();

	/*
	@Test
	public void insertCustomer(){
		
		Customer customer = new Customer();
		customer.setCustomerNo("Customer-004Test");
		customer.setCustomerName("Test Customer");
		
		CustomerStockLevel csl = new CustomerStockLevel("Customer-003Test","AD11",0);
		
		List cslList = new ArrayList();
		cslList.add(csl);
		
		
		customer.setCustomerStockLevelList(cslList);
		
		cm.addCustomerObject(customer, getSession());
	}
	*/

/*	
	@Test
	public void insertCustomerStockLevel(){
		
				
		CustomerStockLevel csl = new CustomerStockLevel("Customer-004Test","AD13",1);
		
		List cslList = new ArrayList();
		cslList.add(csl);
		
		
		
		cm.addCustomerObject(csl, getSession());
	}
*/	
	/*
	@Test
	public void mappingTesting() {
		
	      
	     
	      Employee employee = new Employee("MARK", "MIRAFLOR", 100);
	      
	      employee = (Employee)cm.listByParameter(Employee.class, "lastName", "MIRAFLOR", getSession()).get(0);
	      Map set = employee.getCertificates();
	      
	      set.put("ComSci2", new Certificate("cs1"));
	      set.put("LegalManagement2", new Certificate("lm"));
	      
	      employee.setCertificates(set);

	      cm.updateCustomer(employee, getSession()); 
//	      cm.addCustomerObject(employee,  getSession());  
	}
	*/
	
	/*
	@Test
	public void getCustomer() {
		
		Customer customer = (Customer)cm.listByParameter(Customer.class, "customerNo", "Customer-004Test", getSession()).get(0);
	
		Iterator itr = customer.getCustomerStockLevelList().iterator();
		
		while(itr.hasNext()) {
			CustomerStockLevel csl = (CustomerStockLevel) itr.next();
			System.out.println(csl.getItemCode());
			System.out.println(csl.getStockLevel());		
			
		}
		
		assertNotNull(customer.getCustomerStockLevelList());
		assertNotNull(cm.listByParameter(Customer.class, "customerNo", "Customer-004Test", getSession()));
	}
	
	*/
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

}



