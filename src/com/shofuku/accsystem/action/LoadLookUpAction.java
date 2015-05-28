package com.shofuku.accsystem.action;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.domain.lookups.ExpenseClassification;
import com.shofuku.accsystem.domain.lookups.PaymentClassification;
import com.shofuku.accsystem.domain.lookups.PaymentTerms;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;

public class LoadLookUpAction extends ActionSupport implements Preparable{

	
	private static final long serialVersionUID = 1L;

	Map actionSession;
	UserAccount user;

	LookupManager lookupManager;

	// add other managers for other modules Manager()
	
	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		lookupManager 			= (LookupManager) 		actionSession.get("lookupManager");
		
	}
	
	private String whatLookUp;
	List classifList;
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() throws Exception {
		Session session = getSession();
		try {
			if (getWhatLookUp().equals("pettyCash")) {

				classifList = lookupManager.getLookupElements(
						ExpenseClassification.class, "PETTYCASH",session);
				return "pettyCash";
			} else if (getWhatLookUp().equals("cashPayment")) {

				classifList = lookupManager.getLookupElements(
						PaymentClassification.class, "CASHPAYMENT",session);
				return "cashPayment";
			} else {
				classifList = lookupManager.getLookupElements(PaymentTerms.class,
						"CHECKPAYMENT",session);
			}
			return "checkPayment";
		} catch (Exception e) {
			return SUCCESS;
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
	}

	public String getWhatLookUp() {
		return whatLookUp;
	}

	public void setWhatLookUp(String whatLookUp) {
		this.whatLookUp = whatLookUp;
	}

	public List getClassifList() {
		return classifList;
	}

	public void setClassifList(List classifList) {
		this.classifList = classifList;
	}

}
