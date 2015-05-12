package com.shofuku.accsystem.action.financials;


import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;

public class PrintFinancialsAction extends ActionSupport{

private static final long serialVersionUID = 1L;

Map actionSession = ActionContext.getContext().getSession();
UserAccount user = (UserAccount) actionSession.get("user");
	
	private String accId;
	private String subModule;
	private String forWhat;
	private String forWhatDisplay;
	
	AccountEntryProfile aep;
	JournalEntryProfile jep;
	
	AccountEntryManager manager = new AccountEntryManager();
	
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
	
	try {
		if (subModule.equals("accountEntryProfile")){
			this.setAep(manager.loadAccountEntryProfile(getAep().getAccountCode()));
			forWhat= "true";
		return "accountEntryProfile";
		}else {
			jep = (JournalEntryProfile) manager.listByParameter(JournalEntryProfile.class, "entryNo", this.jep.getEntryNo(), session).get(0);
			this.setJep(jep);
			forWhat= "print";
			return "journalEntryProfile";
		}
	}catch (RuntimeException re) {
		re.printStackTrace();
		if (getSubModule().equals("accountEntryProfile")){
			return "accountEntryProfile";
		}else {
			return "journalEntryProfile";
		}
	}finally {
		if(session.isOpen()){
			session.close();
			session.getSessionFactory().close();
		}
	}
}
	
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getSubModule() {
		return subModule;
	}
	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}
	public String getForWhat() {
		return forWhat;
	}
	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
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
	public String getForWhatDisplay() {
		return forWhatDisplay;
	}
	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}
	
	
}
