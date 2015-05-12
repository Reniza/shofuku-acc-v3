package com.shofuku.accsystem.action.suppliers;


import java.io.InputStream;
import java.util.ArrayList;
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
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.ReportAndSummaryManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.SASConstants;


public class PrintSupplierAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String subModule;
	Supplier supplier;
	SupplierPurchaseOrder po;
	ReceivingReport rr;
	SupplierInvoice invoice;
	private String forWhat;
	
	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	

	//START 2013 - PHASE 3 : PROJECT 1: MARK
		List accountProfileCodeList;
		List<Transaction> transactionList;
		List<Transaction> transactions;
		Iterator itr;
		//END 2013 - PHASE 3 : PROJECT 1: MARK  
		
		//START 2013 - PHASE 3 : PROJECT 1: MARK
		SupplierManager supplierManager = (SupplierManager) actionSession.get("supplierManager");
		AccountEntryManager accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
		TransactionManager transactionManager = (TransactionManager) actionSession.get("transactionManager");
		ReportAndSummaryManager reportAndSummaryManager = (ReportAndSummaryManager) actionSession.get("reportAndSummaryManager");
		//END 2013 - PHASE 3 : PROJECT 1: MARK  
	
	InputStream excelStream;
	String contentDisposition;
	String documentFormat = "xls";
	private Session getSession(){
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		try {
			
			if (getSubModule().equalsIgnoreCase("supplierProfile")){
				
				Supplier supplier = new Supplier();
				supplier = (Supplier) supplierManager.listSuppliersByParameter(Supplier.class, "supplierId", this.getSupId(),session).get(0);
				this.setSupplier(supplier);
				forWhat ="print";
				return "profile";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")){
				SupplierPurchaseOrder po = new SupplierPurchaseOrder();
				po = (SupplierPurchaseOrder) supplierManager.listSuppliersByParameter(SupplierPurchaseOrder.class, "supplierPurchaseOrderId", this.getPoId(),session).get(0);
				poDetailsHelper.generatePODetailsListFromSet(po.getPurchaseOrderDetails());
				poDetailsHelper.generateCommaDelimitedValues();
				this.setSupplier(po.getSupplier());
				this.setPo(po);
				
				forWhat ="print";
				return "purchaseOrder";
				
			}else if (getSubModule().equalsIgnoreCase("receivingReport")){
				ReceivingReport rr = new ReceivingReport();
				rr = (ReceivingReport) supplierManager.listSuppliersByParameter(ReceivingReport.class, "receivingReportNo", this.getRrId(),session).get(0);
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper();
				}
				poDetailsHelperToCompare.generatePODetailsListFromSet(rr.getSupplierPurchaseOrder().getPurchaseOrderDetails());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
				if(null==poDetailsHelper) {
				}else {
					poDetailsHelper.generatePODetailsListFromSet(rr.getPurchaseOrderDetails());
					poDetailsHelper.generateCommaDelimitedValues();
				}
				this.setSupplier(rr.getSupplierPurchaseOrder().getSupplier());
				this.setPo(rr.getSupplierPurchaseOrder());
				this.setRr(rr);
				forWhat ="print";
				return "receivingReport";
			}else {
				SupplierInvoice supInv = new SupplierInvoice();
				supInv = (SupplierInvoice) supplierManager.listSuppliersByParameter(SupplierInvoice.class, "supplierInvoiceNo", this.getInvId(),session).get(0);
				//START Phase 3 - Azhee
				List tempList = transactionManager.listTransactionByParameterLike(Transaction.class, "transactionReferenceNumber", supInv.getSupplierInvoiceNo(), session);
				itr = tempList.iterator();
				transactionList = new ArrayList<Transaction>(); 
				while(itr.hasNext()) {
					Transaction transaction = (Transaction)itr.next();
					if(transaction.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
						transactionList.add(transaction);
					}
				}
				this.setTransactionList(transactionList);
				//END Phase 3 - Azhee
				if(null==poDetailsHelperToCompare) {
					poDetailsHelperToCompare = new PurchaseOrderDetailHelper();
				}
				poDetailsHelperToCompare.generatePODetailsListFromSet(supInv.getReceivingReport().getPurchaseOrderDetails());
				poDetailsHelperToCompare.generateCommaDelimitedValues();
				
				if(null==poDetailsHelper) {
				}else {
					poDetailsHelper.generatePODetailsListFromSet(supInv.getPurchaseOrderDetails());
					poDetailsHelper.generateCommaDelimitedValues();
				}
				
				this.setSupplier(supInv.getReceivingReport().getSupplierPurchaseOrder().getSupplier());
				this.setPo(supInv.getReceivingReport().getSupplierPurchaseOrder());
				this.setRr(supInv.getReceivingReport());
				this.setInvoice(supInv);
				forWhat="print";
				return "invoice";
			}
			
			
			
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("supplierProfile")) {
				return "profile";
			}else if (getSubModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrder";
			}else if (getSubModule().equalsIgnoreCase("receivingReport")) {
				return "receivingReport";
			}else {
				return "invoice";
			}
		}
		finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
	private void inlcudePoDetails() {
		if(poDetailsHelper!=null) {
			poDetailsHelper.prepareSetAndList();
		}if(poDetailsHelperToCompare!=null) {
			poDetailsHelperToCompare.prepareSetAndList();
		}
	}
	
	public String printSupplierPurchaseOrder() {
		Session session = getSession();
		try {
			ServletContext servletContext = ServletActionContext
					.getServletContext();
			
			SupplierPurchaseOrder spo = new SupplierPurchaseOrder();
			spo = (SupplierPurchaseOrder) supplierManager.listByParameter(
					SupplierPurchaseOrder.class, "supplierPurchaseOrderId",
					this.getPoId(),session).get(0);

			excelStream = reportAndSummaryManager.printSupplierPurchaseOrder(spo,subModule,servletContext);
			forWhat="print";
			contentDisposition = "filename=\"supplierPurchaseOrder.xls\"";
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
	
	
	
	//getters and setter
	
	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}

	public SupplierPurchaseOrder getPo() {
		return po;
	}

	public void setPo(SupplierPurchaseOrder po) {
		this.po = po;
	}

	public ReceivingReport getRr() {
		return rr;
	}

	public void setRr(ReceivingReport rr) {
		this.rr = rr;
	}

	public SupplierInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SupplierInvoice invoice) {
		this.invoice = invoice;
	}

	
	private String supId;
	private String poId;
	private String rrId;
	public String getRrId() {
		return rrId;
	}

	public void setRrId(String rrId) {
		this.rrId = rrId;
	}

	private String invId;
	
	
	public String getPoId() {
		return poId;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	
	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	
	public PurchaseOrderDetailHelper getPoDetailsHelper() {
		return poDetailsHelper;
	}
	public void setPoDetailsHelper(PurchaseOrderDetailHelper poDetailsHelper) {
		this.poDetailsHelper = poDetailsHelper;
	}
	
	public PurchaseOrderDetailHelper getPoDetailsHelperToCompare() {
		return poDetailsHelperToCompare;
	}
	public void setPoDetailsHelperToCompare(PurchaseOrderDetailHelper poDetailsHelperToCompare) {
		this.poDetailsHelperToCompare = poDetailsHelperToCompare;
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
	//START 2013 - PHASE 3 : PROJECT 1: MARK 
			public List getAccountProfileCodeList() {
				return accountProfileCodeList;
			}
			public void setAccountProfileCodeList(List accountProfileCodeList) {
				this.accountProfileCodeList = accountProfileCodeList;
			}
			public List<Transaction> getTransactionList() {
				return transactionList;
			}
			
			public void setTransactionList(List<Transaction> transactionList) {
				this.transactionList = transactionList;
			}
			public List<Transaction> getTransactions() {
				return transactions;
			}
			public void setTransactions(List<Transaction> transactions) {
				this.transactions = transactions;
			}
			//END 2013 - PHASE 3 : PROJECT 1: MARK
}
