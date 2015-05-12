package com.shofuku.accsystem.action.receipts;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.ReceiptsManager;
import com.shofuku.accsystem.controllers.ReportAndSummaryManager;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class PrintReceiptAction extends ActionSupport{
private static final long serialVersionUID = 1L;

	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	InputStream excelStream;
	String contentDisposition;
	String documentFormat = "xls";
	
	private String forWhat;
	private String orSNo;
	private String orONo;
	private String crNo;
	private String subModule;
	
	ORSales orSales;
	OROthers orOthers;
	CashCheckReceipts ccReceipts;
	ReceiptsManager manager = new ReceiptsManager();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String printReceipt() throws Exception{
		Session session = getSession();
		try {
			ServletContext servletContext = ServletActionContext
					.getServletContext();
			Map receiptMap = new HashMap();
			ReportAndSummaryManager reportSummaryMgr = new ReportAndSummaryManager();
			if (getSubModule().equalsIgnoreCase("orSales")){
				ORSales orSales = new ORSales();
				orSales = (ORSales) manager.listReceiptsByParameter(ORSales.class, "orNumber",getOrSNo(),session).get(0);
				receiptMap =createReceiptMap(orSales);
			}
			else if (getSubModule().equalsIgnoreCase("orOthers")){
				OROthers orOthers= new OROthers();
				orOthers = (OROthers) manager.listReceiptsByParameter(OROthers.class, "orNumber",getOrONo(),session).get(0);
				receiptMap =createReceiptMap(orOthers);
			}

			excelStream = reportSummaryMgr.printReceipt(receiptMap,subModule,servletContext);
			forWhat="print";
			contentDisposition = "filename=\"receipt.xls\"";

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	String getExcelContentType() {
		return documentFormat == "xlsx" ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
				: "application/vnd.ms-excel";
	}
	
	public String execute() throws Exception{
		Session session = getSession();
		try {
		
			if (getSubModule().equalsIgnoreCase("orSales")){
				
				ORSales orSales = new ORSales();
				orSales = (ORSales) manager.listReceiptsByParameter(
						ORSales.class, "orNumber",
						getOrSNo(),session).get(0);
				this.setOrSales(orSales);
				forWhat="print";
					return "orSales";
			}else if (getSubModule().equalsIgnoreCase("orOthers")){
				OROthers orOthers= new OROthers();
				orOthers = (OROthers) manager.listReceiptsByParameter(
						OROthers.class, "orNumber",
						this.getOrONo(),session).get(0);
				setOrOthers(orOthers);
				forWhat="print";
				return "orOthers";
			}else {
				CashCheckReceipts cashCheckReceipts = new CashCheckReceipts();
				cashCheckReceipts = (CashCheckReceipts) manager.listReceiptsByParameter(
						CashCheckReceipts.class, "cashReceiptNo",
						this.getCrNo(),session).get(0);
				this.setCcReceipts(cashCheckReceipts);
				forWhat="print";
				return "cashCheckReceipts";
			}
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("orSales")) {
				return "orSales";
			}else if (getSubModule().equalsIgnoreCase("orOthers")) { 
				return "orOthers";
			}else { 
				return "cashCheckReceipts";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}
	
	public Map<String,Object> createReceiptMap(Object obj) {
		Map<String, Object> receiptMap = new HashMap<String,Object>();
		receiptMap.put("orNumber",obj.getClass().equals(ORSales.class)?((ORSales)obj).getOrNumber():((OROthers)obj).getOrNumber());
		receiptMap.put("orDate",obj.getClass().equals(ORSales.class)?((ORSales)obj).getOrDate():((OROthers)obj).getOrDate());
		receiptMap.put("receivedFrom",obj.getClass().equals(ORSales.class)?((ORSales)obj).getReceivedFrom():((OROthers)obj).getReceivedFrom());
		receiptMap.put("address",obj.getClass().equals(ORSales.class)?((ORSales)obj).getAddress():((OROthers)obj).getAddress());
		receiptMap.put("busStyle",obj.getClass().equals(ORSales.class)?((ORSales)obj).getBusStyle():((OROthers)obj).getBusStyle());
		receiptMap.put("tin",obj.getClass().equals(ORSales.class)?((ORSales)obj).getTin():((OROthers)obj).getTin());
		receiptMap.put("theAmountOf",obj.getClass().equals(ORSales.class)?((ORSales)obj).getTheAmountOf():((OROthers)obj).getTheAmountOf());
		receiptMap.put("inFullPartialPaymentOf",obj.getClass().equals(ORSales.class)?((ORSales)obj).getInFullPartialPaymentOf():((OROthers)obj).getInFullPartialPaymentOf());
		receiptMap.put("salesInvoiceNumber",obj.getClass().equals(ORSales.class)?((ORSales)obj).getSalesInvoiceNumber():((OROthers)obj).getSalesInvoiceNumber());
		receiptMap.put("amount",obj.getClass().equals(ORSales.class)?((ORSales)obj).getAmount():((OROthers)obj).getAmount());
		receiptMap.put("cash",obj.getClass().equals(ORSales.class)?((ORSales)obj).getCash():((OROthers)obj).getCash());
		receiptMap.put("check",obj.getClass().equals(ORSales.class)?((ORSales)obj).getCheck():((OROthers)obj).getCheck());
		receiptMap.put("bankCheckNo",obj.getClass().equals(ORSales.class)?((ORSales)obj).getBankCheckNo():((OROthers)obj).getBankCheckNo());
		receiptMap.put("total",obj.getClass().equals(ORSales.class)?((ORSales)obj).getTotal():((OROthers)obj).getTotal());
		receiptMap.put("amountInWords",obj.getClass().equals(ORSales.class)?((ORSales)obj).getAmountInWords():((OROthers)obj).getAmountInWords());
		return receiptMap;
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


public String getForWhat() {
	return forWhat;
}
public void setForWhat(String forWhat) {
	this.forWhat = forWhat;
}
public String getOrSNo() {
	return orSNo;
}
public void setOrSNo(String orSNo) {
	this.orSNo = orSNo;
}
public String getOrONo() {
	return orONo;
}
public void setOrONo(String orONo) {
	this.orONo = orONo;
}
public String getCrNo() {
	return crNo;
}
public void setCrNo(String crNo) {
	this.crNo = crNo;
}
public String getSubModule() {
	return subModule;
}
public void setSubModule(String subModule) {
	this.subModule = subModule;
}
public ORSales getOrSales() {
	return orSales;
}
public void setOrSales(ORSales orSales) {
	this.orSales = orSales;
}
public OROthers getOrOthers() {
	return orOthers;
}
public void setOrOthers(OROthers orOthers) {
	this.orOthers = orOthers;
}
public CashCheckReceipts getCcReceipts() {
	return ccReceipts;
}
public void setCcReceipts(CashCheckReceipts ccReceipts) {
	this.ccReceipts = ccReceipts;
}

}