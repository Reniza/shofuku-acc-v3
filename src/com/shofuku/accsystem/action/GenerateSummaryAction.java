package com.shofuku.accsystem.action;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.ReceiptsManager;
import com.shofuku.accsystem.controllers.ReportAndSummaryManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.lookups.ExpenseClassification;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.utils.HibernateUtil;

public class GenerateSummaryAction extends ActionSupport {

	private static final long serialVersionUID = 5247219508332414659L;
	
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	InputStream excelStream;
	String contentDisposition;
	String isFormatReport;
	
	
	public String getIsFormatReport() {
		return isFormatReport;
	}

	public void setIsFormatReport(String isFormatReport) {
		this.isFormatReport = isFormatReport;
	}

	String documentFormat = "xls";
	String dateFrom;
	String dateTo;
	String supplierModule;
	String customerModule;
	String inventoryModule;
	String disbursementModule;
	String receiptsModule;
	String subModule;
	List referenceList;
	List customerList;
	List supplierList;
	List pettyList;
	List cashList;
	List checkList;
	List orSalesList;
	List orOthersList;
	List cashCheckList;
	String referenceNo;
	String pettyCashSearchType;
	String searchByStatus;
	

	boolean byRef = false;
	
	boolean itemsSoldReport = false;
	boolean itemsPurchasedReport = false;
	boolean soaReport = false;
	boolean isInventorySummaryReport=false;

	LookupManager lookUpManager = new LookupManager();
	ReportAndSummaryManager reportAndSummaryManager = (ReportAndSummaryManager) actionSession.get("reportAndSummaryManager");
	ReceiptsManager receiptsManager = new ReceiptsManager();
	DisbursementManager disbursementManager = new DisbursementManager();
	CustomerManager customerManager = new CustomerManager();
	SupplierManager supplierManager = new SupplierManager();
	
	

	private void getModuleAndSubmodule() {

		if (null != supplierModule) {
			if (supplierModule.equalsIgnoreCase("ItemPurchasedFromSupplier")){
				itemsPurchasedReport = true;
			}
			subModule = supplierModule;
			
		} else if (null != customerModule) {
			if (customerModule.equalsIgnoreCase("ItemSoldToCustomers")) {
						itemsSoldReport = true;
			}else if (customerModule.equalsIgnoreCase("StatementOfAccount")){
				soaReport=true;
			}
			subModule = customerModule;
		} else if (null != inventoryModule) {
			subModule = inventoryModule;
			isInventorySummaryReport=true;
		} else if (null != disbursementModule) {
			if (disbursementModule.equalsIgnoreCase("PettyCash")) {
				if (null != pettyCashSearchType) {
					if (pettyCashSearchType.equalsIgnoreCase("ByRef")) {
						byRef = true;
					}
				}
			}
			subModule = disbursementModule;
		} else if (null != receiptsModule) {
			subModule = receiptsModule;
		} else {
		}
	}

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String loadReferenceNo() {
		Session session = getSession();
		try {
			lookUpManager.setUser(user);
			disbursementManager.setUser(user);
			referenceList = lookUpManager.getDistinctReferenceNo(
					PettyCash.class, session);
				pettyList = disbursementManager.listDistinctAlphabeticalAscByParameter(PettyCash.class, "payee", session);
				checkList = disbursementManager.listDistinctAlphabeticalAscByParameter(CheckPayments.class, "payee", session);
				cashList = disbursementManager.listDistinctAlphabeticalAscByParameter(CashPayment.class, "payee", session);
			
			return "summaryDisbursementForm";
		} catch (Exception e) {
			return SUCCESS;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
	public String loadCustomers() {
		Session session = getSession();
		try {
			customerList = customerManager.listAlphabeticalAscByParameter(Customer.class, "customerNo", session);
			return "summaryCustomerForm";
		} catch (Exception e) {
			return SUCCESS;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
	public String loadSuppliers() {
		Session session = getSession();
		try {
			supplierList = supplierManager.listAlphabeticalAscByParameter(Supplier.class, "supplierId", session);
			return "summarySupplierForm";
		} catch (Exception e) {
			return SUCCESS;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}
	
	public String loadReceipts() {
		Session session = getSession();
		try {
			receiptsManager.setUser(user);
			orSalesList = receiptsManager.listAlphabeticalAscByParameter(ORSales.class, "receivedFrom", session);
			orOthersList = receiptsManager.listAlphabeticalAscByParameter(OROthers.class, "receivedFrom", session);
			cashCheckList = receiptsManager.listAlphabeticalAscByParameter(CashCheckReceipts.class, "cashReceiptNo", session);
			
			return "summaryReceiptForm";
		} catch (Exception e) {
			return SUCCESS;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	public String execute() throws Exception {
		Session session = getSession();
		try{

			ServletContext servletContext = ServletActionContext
					.getServletContext();

			getModuleAndSubmodule();

			if (byRef) {
				excelStream = reportAndSummaryManager.generateSummary(servletContext,
						dateFrom, dateTo, subModule, referenceNo,session);
			} else {
				if(itemsSoldReport){
					if(validateFields()){
					}else{
						excelStream = reportAndSummaryManager.generateSummary(servletContext,
								dateFrom, dateTo, subModule, customerList,isFormatReport,session);
					}
				}else if(itemsPurchasedReport){
					if(validateFields()){
					}else{
						excelStream = reportAndSummaryManager.generateSummary(servletContext,
								dateFrom, dateTo, subModule, supplierList,isFormatReport,session);
					}
				}else if(soaReport){
					if(validateFields()){
					}else{
						excelStream = reportAndSummaryManager.generateSoaSummary(servletContext,
								dateFrom, dateTo, subModule, customerList,session);
					}
				}else{
					if(supplierList!=null) {
						excelStream = reportAndSummaryManager.generateSummary(servletContext,
								dateFrom, dateTo,subModule, supplierList,isFormatReport,session);
					}else if(customerList!=null) {
						excelStream = reportAndSummaryManager.generateSummary(servletContext,
								dateFrom, dateTo,subModule, customerList,isFormatReport,session);
					}else if(pettyList!=null || cashList!=null || checkList!=null) {
						if (isFormatReport.equalsIgnoreCase("true")) {
							if (disbursementModule.equalsIgnoreCase("PettyCash")) {
								excelStream = reportAndSummaryManager.generateSummary(servletContext,
										dateFrom, dateTo,subModule, pettyList,isFormatReport,session);
							}else if (disbursementModule.equalsIgnoreCase("CashPayment")) {
								excelStream = reportAndSummaryManager.generateSummary(servletContext,
										dateFrom, dateTo,subModule, cashList,isFormatReport,session);
							}else {
							
							excelStream = reportAndSummaryManager.generateSummary(servletContext,
									dateFrom, dateTo,subModule, checkList,isFormatReport,session);
							}
						}else {
							
							excelStream = reportAndSummaryManager.generateSummary(servletContext,
									dateFrom, dateTo, subModule,session);
						}
					}else if(orSalesList!=null || orOthersList!=null || cashCheckList!=null) {
						if (isFormatReport.equalsIgnoreCase("true")) {
							if (receiptsModule.equalsIgnoreCase("ORSales")) {
								excelStream = reportAndSummaryManager.generateSummary(servletContext,
										dateFrom, dateTo,subModule, orSalesList,isFormatReport,session);
							}else if (receiptsModule.equalsIgnoreCase("OROthers")) {
								excelStream = reportAndSummaryManager.generateSummary(servletContext,
										dateFrom, dateTo,subModule, orOthersList,isFormatReport,session);
							}else {
								excelStream = reportAndSummaryManager.generateSummary(servletContext,
									dateFrom, dateTo,subModule, cashCheckList,isFormatReport,session);
							}
						}else {
							excelStream = reportAndSummaryManager.generateSummary(servletContext,
									dateFrom, dateTo, subModule,session);
						}
					}else if(isInventorySummaryReport) {
						excelStream = reportAndSummaryManager.generateInventorySummaryByStatus(servletContext,
								dateFrom, dateTo, subModule,searchByStatus,session);
					}else {
						excelStream = reportAndSummaryManager.generateSummary(servletContext,
								dateFrom, dateTo, subModule,session);
					}
					addActionError("Summary Report already created...");	
				}
				
			}
			// TODO: get from manager
			contentDisposition = "filename=\""+subModule+"_summary.xls\"";
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Error on generating summary with the supplied parameters");
			if (null != supplierModule) {
				return "supplier";
			} else if (null != customerModule) {
				return "customer";
			} else if (null != inventoryModule) {
				return "inventory";
			} else if (null != disbursementModule) {
				return "disbursement";
			} else{
				return "receipts";
			}
			
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}

	private boolean validateFields() {
		boolean emptyFound = false;
		if (getDateFrom().equalsIgnoreCase("")) {
			addActionError("REQUIRED: Date From");
			emptyFound = true;
		}
		if (getDateTo().equalsIgnoreCase("")) {
			addActionError("REQUIRED: Date To");
			emptyFound = true;
		}
		
		if (subModule.equalsIgnoreCase("ItemSoldToCustomers")||subModule.equalsIgnoreCase("StatementOfAccount")){
			
			if(customerList==null || customerList.isEmpty()){
				addActionError("Select at least one customer");
				emptyFound = true;
			}
		}else{
			if(supplierList==null || supplierList.isEmpty()){
				addActionError("Select at least one Supplier");
				emptyFound = true;
			}
		}
		
		return emptyFound;
	}

	String getExcelContentType() {
		return documentFormat == "xlsx" ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
				: "application/vnd.ms-excel";
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

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		dateFrom = null == dateFrom ? "" : dateFrom;
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		dateTo = null == dateTo ? "" : dateTo;
		this.dateTo = dateTo;
	}

	public String getSupplierModule() {
		return supplierModule;
	}

	public void setSupplierModule(String supplierModule) {
		this.supplierModule = supplierModule;
	}

	public String getDocumentFormat() {
		return documentFormat;
	}

	public void setDocumentFormat(String documentFormat) {
		this.documentFormat = documentFormat;
	}

	public String getCustomerModule() {
		return customerModule;
	}

	public void setCustomerModule(String customerModule) {
		this.customerModule = customerModule;
	}

	public String getInventoryModule() {
		return inventoryModule;
	}

	public void setInventoryModule(String inventoryModule) {
		this.inventoryModule = inventoryModule;
	}

	public String getDisbursementModule() {
		return disbursementModule;
	}

	public void setDisbursementModule(String disbursementModule) {
		this.disbursementModule = disbursementModule;
	}

	public String getReceiptsModule() {
		return receiptsModule;
	}

	public void setReceiptsModule(String receiptsModule) {
		this.receiptsModule = receiptsModule;
	}

	public List getReferenceList() {
		return referenceList;
	}

	public void setReferenceList(List referenceList) {
		this.referenceList = referenceList;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	
	public List getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List customerList) {
		this.customerList = customerList;
	}

	public List getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List supplierList) {
		this.supplierList = supplierList;
	}

	public List getPettyList() {
		return pettyList;
	}

	public void setPettyList(List pettyList) {
		this.pettyList = pettyList;
	}

	public List getCashList() {
		return cashList;
	}

	public void setCashList(List cashList) {
		this.cashList = cashList;
	}

	public List getCheckList() {
		return checkList;
	}

	public void setCheckList(List checkList) {
		this.checkList = checkList;
	}

	public List getOrSalesList() {
		return orSalesList;
	}

	public void setOrSalesList(List orSalesList) {
		this.orSalesList = orSalesList;
	}

	public List getOrOthersList() {
		return orOthersList;
	}

	public void setOrOthersList(List orOthersList) {
		this.orOthersList = orOthersList;
	}

	public List getCashCheckList() {
		return cashCheckList;
	}

	public void setCashCheckList(List cashCheckList) {
		this.cashCheckList = cashCheckList;
	}

	public String getSearchByStatus() {
		return searchByStatus;
	}

	public void setSearchByStatus(String searchByStatus) {
		this.searchByStatus = searchByStatus;
	}

	public String getPettyCashSearchType() {
		return pettyCashSearchType;
	}

	public void setPettyCashSearchType(String pettyCashSearchType) {
		this.pettyCashSearchType = pettyCashSearchType;
	}
	
	
}
