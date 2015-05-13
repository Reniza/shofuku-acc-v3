package com.shofuku.accsystem.action.customer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.ReportAndSummaryManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;

public class PrintCustomerAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String subModule;
	private String cusId;
	private String custpoid;
	private String drId;
	private String invId;
	private String forWhat;
	private String forWhatPrint;

	Customer customer;
	CustomerPurchaseOrder custpo;
	DeliveryReceipt dr;
	CustomerSalesInvoice invoice;
	
	CustomerManager customerManager = (CustomerManager) actionSession.get("customerManager");
	ReportAndSummaryManager reportAndSummaryManager = (ReportAndSummaryManager) actionSession.get("reportAndSummaryManager");
	
	List<PurchaseOrderDetails> poDetailList;
	
	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperToCompare ;
	
	InputStream excelStream;
	String contentDisposition;
	String documentFormat = "xls";
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		try {
			if (getSubModule().equalsIgnoreCase("profile")) {

				Customer profile = new Customer();
				profile = (Customer) customerManager.listByParameter(
						Customer.class, "customerNo",
						this.cusId,session).get(0);
				if (profile.getCustomerType().equalsIgnoreCase("C")){
					profile.setCustomerType("Company Owned");
				}else{
					profile.setCustomerType("Franchise");
				}
				this.setCustomer(profile);
				forWhat = "print";
				return "profile";
			} else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				CustomerPurchaseOrder custPO = new CustomerPurchaseOrder();
				custPO = (CustomerPurchaseOrder) customerManager.listByParameter(
						CustomerPurchaseOrder.class, "customerPurchaseOrderId",
						this.custpoid,session).get(0);
				poDetailsHelper.generatePODetailsListFromSet(custPO.getPurchaseOrderDetails());
				poDetailsHelper.generateCommaDelimitedValues();
				
				if (custPO.getCustomer().getCustomerType().equalsIgnoreCase("C")){
					custPO.getCustomer().setCustomerType("Company Owned");
				}else if (custPO.getCustomer().getCustomerType().equalsIgnoreCase("CC")){
					custPO.getCustomer().setCustomerType("Commissary");
				}else{
					custPO.getCustomer().setCustomerType("Franchise");
				}
				this.setCustomer(custPO.getCustomer());
				this.setCustpo(custPO);
				forWhat = "print";
				return "purchaseOrder";
			} else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				DeliveryReceipt custDr = new DeliveryReceipt();
				custDr = (DeliveryReceipt) customerManager.listByParameter(DeliveryReceipt.class, "deliveryReceiptNo",this.getDrId(),session).get(0);
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
				}
				poDetailsHelperToCompare.generatePODetailsListFromSet(custDr.getCustomerPurchaseOrder().getPurchaseOrderDetails());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
				if(null==poDetailsHelper) {
				}else {
					poDetailsHelper.generatePODetailsListFromSet(custDr.getPurchaseOrderDetails());
					poDetailsHelper.generateCommaDelimitedValues();
				}
				
				if (custDr.getCustomerPurchaseOrder().getCustomer().getCustomerType().equalsIgnoreCase("C")){
					custDr.getCustomerPurchaseOrder().getCustomer().setCustomerType("Company Owned");
				}else if (custDr.getCustomerPurchaseOrder().getCustomer().getCustomerType().equalsIgnoreCase("CC")){
					custDr.getCustomerPurchaseOrder().getCustomer().setCustomerType("Commissary");
				}else{
					custDr.getCustomerPurchaseOrder().getCustomer().setCustomerType("Franchise");
				}
				this.setCustomer(custDr.getCustomerPurchaseOrder().getCustomer());
				this.setCustpo(custDr.getCustomerPurchaseOrder());
				this.setDr(custDr);
				forWhat = "print";
				return "deliveryReceipt";
			} else {
				CustomerSalesInvoice custInv = new CustomerSalesInvoice();
				custInv = (CustomerSalesInvoice) customerManager.listByParameter(
						CustomerSalesInvoice.class, "customerInvoiceNo",
						this.getInvId(),session).get(0);

				poDetailList = new ArrayList<PurchaseOrderDetails>();
				Set<PurchaseOrderDetails> poDetailsSet = custInv.getPurchaseOrderDetails();
				Iterator<PurchaseOrderDetails> itr = poDetailsSet.iterator();
				while(itr.hasNext()) {
					PurchaseOrderDetails poDetails = itr.next();
					poDetailList.add(poDetails);
				}
				sortListsAlphabetically();
				
				if (custInv.getDeliveryReceipt()
						.getCustomerPurchaseOrder().getCustomer().getCustomerType().equalsIgnoreCase("C")){
					custInv.getDeliveryReceipt()
					.getCustomerPurchaseOrder().getCustomer().setCustomerType("Company Owned");
				}else if (custInv.getDeliveryReceipt()
						.getCustomerPurchaseOrder().getCustomer().getCustomerType().equalsIgnoreCase("CC")){
					custInv.getDeliveryReceipt()
					.getCustomerPurchaseOrder().getCustomer().setCustomerType("Commissary");
				}else{
					custInv.getDeliveryReceipt()
					.getCustomerPurchaseOrder().getCustomer().setCustomerType("Franchise");
				}
				this.setCustomer(custInv.getDeliveryReceipt()
						.getCustomerPurchaseOrder().getCustomer());
				this.setCustpo(custInv.getDeliveryReceipt()
						.getCustomerPurchaseOrder());
				this.setDr(custInv.getDeliveryReceipt());
				this.setInvoice(custInv);
		
					forWhat = "print";
				return "invoice";
				}
			
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("profile")) {
				return "profile";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrder";
			}else if (getSubModule().equalsIgnoreCase("deliveryReceipt")) {
				return "deliveryReceipt";
			}else  {
				return "invoice";	
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}
	
	//TODO: LINISIN MO TO
	private void sortListsAlphabetically(){
		
		List purchaseOrderDetailsList= this.poDetailList;
		List sortedPurchaseOrderDetailsList= new ArrayList();
		
		HashMap<String,PurchaseOrderDetails> map = new HashMap<String,PurchaseOrderDetails>();
		Set<PurchaseOrderDetails> sortedMap = new HashSet<PurchaseOrderDetails>();
		List itemCodeList = new ArrayList();
		try {
			Iterator<PurchaseOrderDetails> itr =purchaseOrderDetailsList.iterator();
			while(itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails)itr.next();
				map.put(podetails.getItemCode(),podetails);
				itemCodeList.add(podetails.getItemCode());
			}
			
			Collections.sort(itemCodeList);
			
			Iterator<String> iteratorSorted =itemCodeList.iterator();
			while(iteratorSorted.hasNext()) {
				String  code = (String)iteratorSorted.next();
				sortedPurchaseOrderDetailsList.add(map.get(code));
				sortedMap.add(map.get(code));
			}
			
			this.poDetailList = sortedPurchaseOrderDetailsList ;
		}catch(NullPointerException nfe) {
			nfe.printStackTrace();
		}
		
				
	}

	public String printInvoice() {
		Session session = getSession();
		try {
			ServletContext servletContext = ServletActionContext
					.getServletContext();
			
			CustomerSalesInvoice custInv = new CustomerSalesInvoice();
			custInv = (CustomerSalesInvoice) customerManager.listByParameter(
					CustomerSalesInvoice.class, "customerInvoiceNo",
					this.getInvId(),session).get(0);

			excelStream = reportAndSummaryManager.printCustomerInvoice(custInv,subModule,servletContext);
			forWhat="print";
			contentDisposition = "filename=\"customerInvoice.xls\"";
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
			
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}	
	public String getCustpoid() {
		return custpoid;
	}

	public void setCustpoid(String custpoid) {
		this.custpoid = custpoid;
	}

	public String getForWhatPrint() {
		return forWhatPrint;
	}

	public void setForWhatPrint(String forWhatPrint) {
		this.forWhatPrint = forWhatPrint;
	}

	public CustomerPurchaseOrder getCustpo() {
		return custpo;
	}

	public void setCustpo(CustomerPurchaseOrder custpo) {
		this.custpo = custpo;
	}

	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getPoId() {
		return custpoid;
	}

	public void setPoId(String poId) {
		this.custpoid = poId;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getDrId() {
		return drId;
	}

	public void setDrId(String drId) {
		this.drId = drId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerPurchaseOrder getPo() {
		return custpo;
	}

	public void setPo(CustomerPurchaseOrder po) {
		this.custpo = po;
	}

	public DeliveryReceipt getDr() {
		return dr;
	}

	public void setDr(DeliveryReceipt dr) {
		this.dr = dr;
	}

	public CustomerSalesInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(CustomerSalesInvoice invoice) {
		this.invoice = invoice;
	}


	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}


	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}


	public List<PurchaseOrderDetails> getPoDetailList() {
		return poDetailList;
	}


	public void setPoDetailList(List<PurchaseOrderDetails> poDetailList) {
		this.poDetailList = poDetailList;
	}

	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}
	public void setPoDetailsHelperToCompare(PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
	}

}
