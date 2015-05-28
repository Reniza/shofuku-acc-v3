package com.shofuku.accsystem.action.disbursement;

import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class DeleteDisbursementAction extends ActionSupport implements Preparable{

	private static final long serialVersionUID = 1L;
	

	Map actionSession;
	UserAccount user;

	DisbursementManager disbursementManager;
	
	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		disbursementManager 	= (DisbursementManager) actionSession.get("disbursementManager");
		
	}
	private String subModule;
	private String pcNo;
	private String cpNo;
	private String chpNo;
	
	PettyCash pc;
	CashPayment cp;
	CheckPayments chp;

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
	try {
		boolean deleteResult;

		if (getSubModule().equalsIgnoreCase("pettyCash")) {
			deleteResult = disbursementManager.deleteDisbursementByParameter(getPcNo(),
					PettyCash.class,session);
			if (deleteResult == true) {
				pc = new PettyCash();
				addActionMessage(SASConstants.DELETED);
			} else {
				addActionError(SASConstants.NON_DELETED);
			}
			return "pettyCashDeleted";
		} else if (getSubModule().equalsIgnoreCase("cashPayment")) {
			deleteResult = disbursementManager.deleteDisbursementByParameter(getCpNo(),
					CashPayment.class,session);
			if (deleteResult == true) {
				cp = new CashPayment();
				addActionMessage(SASConstants.DELETED);
			} else {
				addActionError(SASConstants.NON_DELETED);
			}
			return "cashPaymentDeleted";
		}  else if (getSubModule().equalsIgnoreCase("checkPayment"))  {
			deleteResult = disbursementManager.deleteDisbursementByParameter(getChpNo(), 
					CheckPayments.class,session);
			if (deleteResult == true) {
				addActionMessage(SASConstants.DELETED);
			} else {
				addActionError(SASConstants.NON_DELETED);
			}
		return "checkPaymentDeleted";
		}else  {
			deleteResult = disbursementManager.deleteDisbursementByParameter(getChpNo(), 
					CheckPayments.class,session);
			if (deleteResult == true) {
				addActionMessage(SASConstants.DELETED);
			} else {
				addActionError(SASConstants.NON_DELETED);
			}
		return "checkVoucher";
		}
	} catch (RuntimeException re) {
		if (getSubModule().equals("pettyCash")){
			return "pettyCashDeleted";
		}else if (getSubModule().equals("cashPayment")){
			return "cashPaymentDeleted";
		}else {
			return "checkPaymentDeleted";
		}
	} finally {
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
	}
	}
/*
 * GETTERS AND SETTERS	anong pecha na d pa rin nailipat (3/24)
 */
	
	public String getSubModule() {
		return subModule;
	}
	public void setSubModule(String subModule) {
		this.subModule = subModule;
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
}
	

