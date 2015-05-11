package com.shofuku.accsystem.action.receipts;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.ReceiptsManager;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class DeleteReceiptAction extends ActionSupport{

	ReceiptsManager manager = new ReceiptsManager();
	private static final long serialVersionUID = 1L;
	ORSales orSales;
	OROthers orOthers;
	CashCheckReceipts ccReceipts;
	private String orSNo;
	private String orONo;
	private String crNo;
	private String subModule;
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() throws Exception{
		Session session = getSession();
		try {
			boolean deleteResult;

			if (getSubModule().equalsIgnoreCase("orSales")) {
				deleteResult = manager.deleteReceiptsByParameter(getOrSNo(), ORSales.class,session);
				if (deleteResult == true) {
					orSales = new ORSales();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "orSalesDeleted";
			} else if (getSubModule().equalsIgnoreCase("orOthers")) {
				deleteResult = manager.deleteReceiptsByParameter(getOrONo(),
						OROthers.class,session);
				if (deleteResult == true) {
					orOthers = new OROthers();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "orOthersDeleted";
			} else  {
				deleteResult = manager.deleteReceiptsByParameter(getCrNo(), CashCheckReceipts.class,session);
				if (deleteResult == true) {
					ccReceipts = new CashCheckReceipts();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "cashCheckReceiptsDeleted";
			}
		} catch (RuntimeException re) {
			if (getSubModule().equalsIgnoreCase("orSales")) {
				return "orSalesDeleted";
			}else if (getSubModule().equalsIgnoreCase("orOthers")) { 
				return "orOthersDeleted";
			}else {
				return "cashCheckReceiptsDeleted";
			}
		}  finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
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
	public CashCheckReceipts getCashCheckReceipt() {
		return ccReceipts;
	}
	public void setCashCheckReceipt(CashCheckReceipts cashCheckReceipt) {
		this.ccReceipts = cashCheckReceipt;
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
	
		
	
}
