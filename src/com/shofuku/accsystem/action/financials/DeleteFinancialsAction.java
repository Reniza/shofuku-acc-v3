package com.shofuku.accsystem.action.financials;

import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class DeleteFinancialsAction extends ActionSupport implements Preparable{

	private static final long serialVersionUID = 1L;
	

	Map actionSession;
	UserAccount user;

	AccountEntryManager accountEntryManager;
	
	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		accountEntryManager		= (AccountEntryManager) actionSession.get("accountEntryManager");
	}
	
	private String subModule;
	AccountEntryProfile aep;
	JournalEntryProfile jep;
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String execute() throws Exception{
		Session session = getSession();
		try {
			boolean deleteResult;

			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
				deleteResult = accountEntryManager.deleteAccountEntryProfile(this.aep.getAccountCode(), AccountEntryProfile.class, session);
				if (deleteResult == true) {
					aep = new AccountEntryProfile();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "accountEntryProfileDeleted";
			} else  {
				deleteResult = accountEntryManager.deleteAccountEntryProfile(this.jep.getAccountCode(), JournalEntryProfile.class, session);
				if (deleteResult == true) {
					jep = new JournalEntryProfile();
					addActionMessage(SASConstants.DELETED);
				} else {
					addActionError(SASConstants.NON_DELETED);
				}
				return "journalEntryProfileDeleted";
			}
		} catch (RuntimeException re) {
			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
				return "accountEntryProfileDeleted";
			}else {
				return "journalEntryProfileDeleted";
			}
		}  finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public AccountEntryProfile getAep() {
		return aep;
	}

	public void setAep(AccountEntryProfile aep) {
		this.aep = aep;
	}

	public JournalEntryProfile getJep() {
		return jep;
	}

	public void setJep(JournalEntryProfile jep) {
		this.jep = jep;
	}
	
	
	
}
