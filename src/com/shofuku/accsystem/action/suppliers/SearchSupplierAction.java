package com.shofuku.accsystem.action.suppliers;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.ExportSearchResultsHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchSupplierAction extends ActionSupport implements Preparable {

	
	Map actionSession;
	UserAccount user;

	SupplierManager supplierManager;
	
	@Override
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		supplierManager = (SupplierManager) actionSession.get("supplierManager");
		
	}
	

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger
			.getLogger(SearchSupplierAction.class);
	List supplierList;
	private String clicked;

	
	private String supplierModule;
	private String moduleParameter;
	private String moduleParameterValue;

	InputStream excelStream;
	String contentDisposition;
	
	private Session getSession(){
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	//
	
	public String exportResults() throws Exception{
		ExportSearchResultsHelper exporter = new ExportSearchResultsHelper(actionSession);
		Session session = getSession();
		if (null != getModuleParameter()
					&& getSupplierModule().equalsIgnoreCase("receivingReport")) {
				ReceivingReport supRR = new ReceivingReport();
				
				searchRecevingReport(supRR,session);
		}
		exporter.setSearchResults(supplierList);
		exporter.setSearchType(supplierModule);
		excelStream = exporter.generateExportedXls();
		
		return SUCCESS;
	}
	
	public String execute() throws Exception {
		Session session = getSession();
		try {
			if (getClicked().equals("true")) {

				if (getSupplierModule().equalsIgnoreCase("profile")) {
				
					if (moduleParameter.equalsIgnoreCase("ALL")) {
						supplierList = supplierManager.listAlphabeticalAscByParameter(Supplier.class, "supplierName",session);
						moduleParameterValue="all";
					}else if (null != getModuleParameter() && moduleParameter.equalsIgnoreCase("supplierId")) {
						Supplier sup = new Supplier();
						supplierList = supplierManager.listSuppliersByParameter(sup.getClass(),
							moduleParameter, moduleParameterValue,session);
					}else{
						if (null != getModuleParameter()) {
						Supplier sup = new Supplier();
						supplierList = supplierManager.listSupplierByParameterLike(sup.getClass(),
							moduleParameter, moduleParameterValue,session);
						}
					}
							if (supplierList == null || 0 == supplierList.size()) {
								addActionMessage(SASConstants.NO_LIST);
							}
					
					return "profile";
				} else if (null != getModuleParameter()
						&& getSupplierModule().equalsIgnoreCase("purchaseOrder")) {
					SupplierPurchaseOrder supPO = new SupplierPurchaseOrder();
					if (moduleParameter.equals("supplierName")) {
						supplierList = supplierManager.listByName(supPO.getClass(),
								"supplier.supplierName", moduleParameterValue,session);
					}else if (moduleParameter.endsWith("Date")) {
						supplierList = supplierManager.getSupplierElementsByDate(
								new DateFormatHelper()
										.parseStringToTime(moduleParameterValue),
								supPO.getClass().getName(), moduleParameter,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
							supplierList = supplierManager.listAlphabeticalAscByParameter(SupplierPurchaseOrder.class, "supplierPurchaseOrderId",session);
							moduleParameterValue="all";
					}
					 else {
						supplierList = supplierManager.listSuppliersByParameter(
								supPO.getClass(), moduleParameter,
								moduleParameterValue,session);
					}
						if (supplierList == null || 0 == supplierList.size()) {
							addActionMessage(SASConstants.NO_LIST);
						}
						
						return "purchaseOrder";
				} else if (null != getModuleParameter()
						&& getSupplierModule().equalsIgnoreCase("receivingReport")) {
					ReceivingReport supRR = new ReceivingReport();
					
					searchRecevingReport(supRR,session);
					
					return "receivingReport";
				} else if (null != getModuleParameter()
						&& getSupplierModule().equalsIgnoreCase("invoice")) {
					SupplierInvoice supInv = new SupplierInvoice();
					if ("receivingReportNo".equals(moduleParameter)) {
						supplierList = supplierManager.listSuppliersByParameter(
								supInv.getClass(),
								"receivingReport.receivingReportNo",
								moduleParameterValue,session);
					} else if (moduleParameter.endsWith("Date")) {
						supplierList = supplierManager.getSupplierElementsByDate(
								new DateFormatHelper()
										.parseStringToTime(moduleParameterValue),
								supInv.getClass().getName(), moduleParameter,session);
					
					} else if (null != getModuleParameter() && moduleParameter.equalsIgnoreCase("supplierName")) {
						supplierList = supplierManager.searchSupplierInvoiceBySupplierName(SupplierInvoice.class,
							"supplier.supplierName", moduleParameterValue,session);
					
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						supplierList = supplierManager.listAlphabeticalAscByParameter(SupplierInvoice.class, "supplierInvoiceNo",session);
						moduleParameterValue="all";
					
					}else {
						supplierList = supplierManager.listSuppliersByParameter(
								supInv.getClass(), moduleParameter,
								moduleParameterValue,session);
					}
					if (supplierList == null || 0 == supplierList.size()) {
						addActionMessage(SASConstants.NO_LIST);
					}
					
					return "invoice";
				}
			}
			moduleParameterValue="";
			return "populate";
		}catch (RuntimeException re) {
			if (getSupplierModule().equalsIgnoreCase("profile")) {
				return "profile";
			}else if (getSupplierModule().equalsIgnoreCase("purchaseOrder")) {
				return "purchaseOrder";
			}else if (getSupplierModule().equalsIgnoreCase("receivingReport")) {
				return "receivingReport";
			}else if (getSupplierModule().equalsIgnoreCase("invoice"))  {
				return "invoice";
			}else {
				return "populate";
			}
		}
		finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
	}
	}

	private void searchRecevingReport(ReceivingReport supRR,Session session ) {
		if ("supplierPurchaseOrderId".equals(moduleParameter)) {
			supplierList = supplierManager.listSuppliersByParameter(
					supRR.getClass(),
					"supplierPurchaseOrder.supplierPurchaseOrderId",
					moduleParameterValue,session);
		} else if (moduleParameter.endsWith("Date")) {
			supplierList = supplierManager.getSupplierElementsByDate(
					new DateFormatHelper()
							.parseStringToTime(moduleParameterValue),
					supRR.getClass().getName(), moduleParameter,session);
		}else if (moduleParameter.equalsIgnoreCase("ALL")) {
			supplierList = supplierManager.listAlphabeticalAscByParameter(ReceivingReport.class, "receivingReportNo",session);
			moduleParameterValue="all";
		}else if (null != getModuleParameter() && moduleParameter.equalsIgnoreCase("supplierName")) {
			supplierList = supplierManager.searchSupplierReceivingReportBySupplierName(ReceivingReport.class,
					"supplier.supplierName", moduleParameterValue,session);
			
		}else {
			supplierList = supplierManager.listSuppliersByParameter(
					supRR.getClass(), moduleParameter,
					moduleParameterValue,session);
		}
		if (0 == supplierList.size()) {
			addActionMessage(SASConstants.NO_LIST);
		}
	}

	public String getSupplierModule() {
		return supplierModule;
	}

	public void setSupplierModule(String supplierModule) {
		this.supplierModule = supplierModule;
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

	public List getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List supplierList) {
		this.supplierList = supplierList;
	}

	Supplier supplier;

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	SupplierPurchaseOrder po;

	public SupplierPurchaseOrder getPo() {
		return po;
	}

	public void setPo(SupplierPurchaseOrder po) {
		this.po = po;
	}

	ReceivingReport rr;

	public ReceivingReport getRr() {
		return rr;
	}

	public void setRr(ReceivingReport rr) {
		this.rr = rr;
	}

	SupplierInvoice invoice;

	public SupplierInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SupplierInvoice invoice) {
		this.invoice = invoice;
	}

	public String getClicked() {
		return clicked;
	}

	public void setClicked(String clicked) {
		this.clicked = clicked;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getContentDisposition() {
		
		return "filename=\""+ supplierModule +"-searchResults.xls\"";
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	

}
