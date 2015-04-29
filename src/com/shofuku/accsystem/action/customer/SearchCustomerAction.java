package com.shofuku.accsystem.action.customer;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchCustomerAction extends ActionSupport {

	CustomerManager manager = new CustomerManager();

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger
			.getLogger(SearchCustomerAction.class);
	private String moduleParameter;
	private String moduleParameterValue;

	List customerList;
	Customer customer;
	CustomerPurchaseOrder custpo;
	DeliveryReceipt dr;
	CustomerSalesInvoice invoice;
	private String clicked;
	private String customerModule;
	
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();

		try {
			if (getClicked().equals("true")) {
				if (null != getModuleParameter()
						&& getCustomerModule().equalsIgnoreCase("profile")) {
					Customer cust = new Customer();
					if (moduleParameter.equals("customerName")) {
						customerList = manager.listByParameterLike(
								Customer.class, "customerName",
								moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						customerList = manager.listAlphabeticalAscByParameter(Customer.class, "customerNo",session);
						moduleParameterValue="all";
						
					}  else {
						customerList = manager.listByParameter(Customer.class,
								moduleParameter, moduleParameterValue,session);
					}
					if (customerList == null || 0 == customerList.size()) {
						addActionMessage(SASConstants.NO_LIST);
					}
					
					return "profile";
				} else if (null != getModuleParameter()
						&& getCustomerModule().equalsIgnoreCase("purchaseOrder")) {
					CustomerPurchaseOrder custPO = new CustomerPurchaseOrder();

					if (moduleParameter.equals("customerName")) {
						customerList = manager.listByName(CustomerPurchaseOrder.class,
								"customer.customerName", moduleParameterValue,session);
					} else if (moduleParameter.equalsIgnoreCase("ALL")) {
						customerList = manager.listAlphabeticalAscByParameter(CustomerPurchaseOrder.class, "customerPurchaseOrderId",session);
						moduleParameterValue="all";
						
					} else if (moduleParameter.endsWith("Date")) {
						customerList = manager
								.getCustomerElementsByDate(
										new DateFormatHelper()
												.parseStringToTime(moduleParameterValue),
												CustomerPurchaseOrder.class.getName(),
										moduleParameter,session);
					} else {
						customerList = manager.listByParameter(
								CustomerPurchaseOrder.class, moduleParameter,
								moduleParameterValue,session);
					}
					if (customerList == null || 0 == customerList.size()) {
						addActionMessage(SASConstants.NO_LIST);
					}
					
					return "purchaseOrder";
				} else if (null != getModuleParameter()
						&& getCustomerModule().equalsIgnoreCase("deliveryReceipt")) {
					DeliveryReceipt custDR = new DeliveryReceipt();
					if ("customerPurchaseOrderId".equals(moduleParameter)) {
						customerList = manager
								.listByParameter(
										DeliveryReceipt.class,
										"customerPurchaseOrder.customerPurchaseOrderId",
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						customerList = manager.listAlphabeticalAscByParameter(DeliveryReceipt.class, "deliveryReceiptNo",session);
						moduleParameterValue="all";
					}else if (null != getModuleParameter() && moduleParameter.equalsIgnoreCase("customerName")) {
						customerList = manager.searchCustomerDeliveryReceiptByCustomerName(DeliveryReceipt.class,
								"customer.customerName", moduleParameterValue,session);	
					}  else if (moduleParameter.endsWith("Date")) {
						customerList = manager
								.getCustomerElementsByDate(
										new DateFormatHelper()
												.parseStringToTime(moduleParameterValue),
												DeliveryReceipt.class.getName(),
										moduleParameter,session);
					} else {
						customerList = manager.listByParameter(
								DeliveryReceipt.class, moduleParameter,
								moduleParameterValue,session);
					}
					if (customerList == null || 0 == customerList.size()) {
						addActionMessage(SASConstants.NO_LIST);
					}
					
					return "deliveryReceipt";
				} else if (null != getModuleParameter()
						&& getCustomerModule().equalsIgnoreCase("invoice")) {
					CustomerSalesInvoice custInv = new CustomerSalesInvoice();
					if ("deliveryReceiptNo".equals(moduleParameter)) {
						customerList = manager.listByParameter(
								CustomerSalesInvoice.class,
								"deliveryReceipt.deliveryReceiptNo",
								moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						customerList = manager.listAlphabeticalAscByParameter(CustomerSalesInvoice.class, "customerInvoiceNo",session);
						moduleParameterValue="all";
						
					} else if (null != getModuleParameter() && moduleParameter.equalsIgnoreCase("customerName")) {
						customerList = manager.searchCustomerInvoiceByCustomerName(CustomerSalesInvoice.class,
								"customer.customerName", moduleParameterValue,session);	
					}
					else if (moduleParameter.endsWith("Date")) {
						customerList = manager
								.getCustomerElementsByDate(
										new DateFormatHelper()
												.parseStringToTime(moduleParameterValue),
												CustomerSalesInvoice.class.getName(),
										moduleParameter,session);
					} else {
						customerList = manager.listByParameter(
								CustomerSalesInvoice.class, moduleParameter,
								moduleParameterValue,session);
					}
					if (customerList == null || 0 == customerList.size()) {
						addActionMessage(SASConstants.NO_LIST);
					}
					
					return "invoice";
				}
			}
			moduleParameterValue="";
			return "populate";
		} catch (RuntimeException re) {
			if (getCustomerModule().equalsIgnoreCase("profile")) {
				return "profile";
			}else if (getCustomerModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrder";
			}else if (getCustomerModule().equalsIgnoreCase("deliveryReceipt")) {
				return "deliveryReceipt";
			}else if (getCustomerModule().equalsIgnoreCase("invoice")) {
				return "invoice";
			}else {
				return "populate";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}
	public List getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List customerList) {
		this.customerList = customerList;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public DeliveryReceipt getDr() {
		return dr;
	}

	public void setDr(DeliveryReceipt dr) {
		this.dr = dr;
	}

	public CustomerPurchaseOrder getPo() {
		return custpo;
	}

	public void setPo(CustomerPurchaseOrder po) {
		this.custpo = po;
	}

	public CustomerSalesInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(CustomerSalesInvoice invoice) {
		this.invoice = invoice;
	}

	public String getCustomerModule() {
		return customerModule;
	}

	public void setCustomerModule(String customerModule) {
		this.customerModule = customerModule;
	}

	public String getClicked() {
		return clicked;
	}

	public void setClicked(String clicked) {
		this.clicked = clicked;
	}

	public String getModuleParameter() {
		return moduleParameter;
	}

	public void setModuleParameter(String moduleParameter) {
		this.moduleParameter = moduleParameter;
	}

	public String getModuleParameterValue() {
		return moduleParameterValue;
	}

	public void setModuleParameterValue(String moduleParameterValue) {
		this.moduleParameterValue = moduleParameterValue;
	}

}
