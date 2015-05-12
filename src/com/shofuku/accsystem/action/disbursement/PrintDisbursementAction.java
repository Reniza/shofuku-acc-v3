package com.shofuku.accsystem.action.disbursement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.ReportAndSummaryManager;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class PrintDisbursementAction  extends ActionSupport{
private static final long serialVersionUID = 1L;

Map actionSession = ActionContext.getContext().getSession();
UserAccount user = (UserAccount) actionSession.get("user");
	
	private String pcNo;
	private String cpNo;
	private String chpNo;
	private String subModule;
	private String forWhat;
	
	private String chequeDate;
	private String payTo;
	private String amount;
	private String amountInWords;
	List<PurchaseOrderDetails> orderDetails;
	DisbursementManager manager = new DisbursementManager();
	
	PettyCash pc;
	CashPayment cp;
	CheckPayments chp;
	
	InputStream excelStream;
	String contentDisposition;
	String documentFormat = "xls";
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
	
	try {
	
		if (getSubModule().equals("pettyCash")){
			
			PettyCash pc = new PettyCash();
			pc = (PettyCash) manager.listDisbursementsByParameter(
					PettyCash.class, "pcVoucherNumber",
					this.getPcNo(),session).get(0);
			setPc(pc);
			forWhat="print";
			return "pettyCash";
		}else if (getSubModule().equals("cashPayment")){
			CashPayment cp = new CashPayment();
			cp = (CashPayment) manager.listDisbursementsByParameter(
					CashPayment.class, "cashVoucherNumber",
					this.getCpNo(),session).get(0);
			this.setCp(cp);
			forWhat="print";
			return "cashPayment";
		}else if (getSubModule().equals("checkPayment")){
			CheckPayments chp = new CheckPayments();
			chp = (CheckPayments) manager.listDisbursementsByParameter(
					CheckPayments.class, "checkVoucherNumber",
					this.getChpNo(),session).get(0);
			this.setChp(chp);
			forWhat="print";
			return "checkPayment";
		}else {
			CheckPayments chp = new CheckPayments();
			chp = (CheckPayments) manager.listDisbursementsByParameter(
					CheckPayments.class, "checkVoucherNumber",
					this.getChpNo(),session).get(0);
			orderDetails = new ArrayList<PurchaseOrderDetails>();
			Set<PurchaseOrderDetails> invoiceDetailSet = chp.getInvoice().getPurchaseOrderDetails();
			Iterator<PurchaseOrderDetails> itr = invoiceDetailSet.iterator();
				while(itr.hasNext()) {
					PurchaseOrderDetails invoiceDetails = itr.next();
					orderDetails.add(invoiceDetails);
					}
			this.setChp(chp);
			forWhat="print";
			return "checkVoucher";
		}
		
	}catch (RuntimeException re) {
		re.printStackTrace();
		if (getSubModule().equals("pettyCash")){
			return "pettyCash";
		}else if (getSubModule().equals("cashPayment")){
			return "cashPayment";
		}else if (getSubModule().equals("checkPayment")){
			return "checkPayment";
		}else {
			return "checkVoucher";
		}
	}finally {
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
	}
}


	public String printCheckPayments() throws Exception{
		Session session = getSession();
		try {
			ServletContext servletContext = ServletActionContext
					.getServletContext();
			ReportAndSummaryManager reportSummaryMgr = new ReportAndSummaryManager();
			
				CheckPayments chp = new CheckPayments();
				chp = (CheckPayments) manager.listDisbursementsByParameter(
						CheckPayments.class, "checkVoucherNumber",
						this.getChpNo(),session).get(0);

			excelStream = reportSummaryMgr.printCheckPayments(chp,subModule,servletContext);
			forWhat="print";
			contentDisposition = "filename=\"checkpayments.xls\"";
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
	
public String getPcNo() {
	return pcNo;
}
public void setPcNo(String pcNo) {
	this.pcNo = pcNo;
}
public String getCpNo() {
	return cpNo;
}
public void setCpNo(String cpNo) {
	this.cpNo = cpNo;
}
public String getChpNo() {
	return chpNo;
}
public void setChpNo(String chpNo) {
	this.chpNo = chpNo;
}
public String getSubModule() {
	return subModule;
}
public void setSubModule(String subModule) {
	this.subModule = subModule;
}
public PettyCash getPc() {
	return pc;
}
public void setPc(PettyCash pc) {
	this.pc = pc;
}
public CashPayment getCp() {
	return cp;
}
public void setCp(CashPayment cp) {
	this.cp = cp;
}
public CheckPayments getChp() {
	return chp;
}
public void setChp(CheckPayments chp) {
	this.chp = chp;
}

public String getForWhat() {
	return forWhat;
}
public void setForWhat(String forWhat) {
	this.forWhat = forWhat;
}


public String getChequeDate() {
	return chequeDate;
}
public void setChequeDate(String chequeDate) {
	this.chequeDate = chequeDate;
}
public String getPayTo() {
	return payTo;
}
public void setPayTo(String payTo) {
	this.payTo = payTo;
}
public String getAmount() {
	return amount;
}
public void setAmount(String amount) {
	this.amount = amount;
}
public String getAmountInWords() {
	return amountInWords;
}
public void setAmountInWords(String amountInWords) {
	this.amountInWords = amountInWords;
}


public List<PurchaseOrderDetails> getOrderDetails() {
	return orderDetails;
}


public void setOrderDetails(List<PurchaseOrderDetails> orderDetails) {
	this.orderDetails = orderDetails;
}

	
}
