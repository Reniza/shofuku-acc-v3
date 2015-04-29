package com.shofuku.accsystem.action.financials;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;

import com.shofuku.accsystem.domain.disbursements.CashPayment;

import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchFinancialsAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private String financialModule;
	private String moduleParameter;
	private String moduleParameterValue;
	
	private String clicked;
	List financialsList;

	AccountEntryManager manager = new AccountEntryManager();

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		try {
			if (getClicked().equals("true")) {

				if (null != getModuleParameter()&& getFinancialModule().equalsIgnoreCase("accountEntryProfile")) {
					
					if (getModuleParameter().equalsIgnoreCase("name")) {
						financialsList = manager.listAccountEntryProfileByParameterLike(
										AccountEntryProfile.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						financialsList = manager.listAlphabeticalAccountEntryProfileAscByParameter(AccountEntryProfile.class, "name",session);
						moduleParameterValue="all";
						
					} else {
						AccountEntryProfile aep = manager.loadAccountEntryProfile(moduleParameterValue);
						financialsList = new ArrayList<>();
						financialsList.add(aep);
					}
						if (financialsList == null || financialsList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
						
					return "accountEntryProfile";
				} else if (null != getModuleParameter()&& getFinancialModule().equalsIgnoreCase("journalEntryProfile")) {
					
					if (getModuleParameter().equalsIgnoreCase("entryName")) {
						financialsList = manager.listAccountEntryProfileByParameterLike(
										JournalEntryProfile.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						financialsList = manager.listAlphabeticalAccountEntryProfileAscByParameter(JournalEntryProfile.class,"entryNo",session);
						moduleParameterValue="all";
						
					} else {
						JournalEntryProfile jep = (JournalEntryProfile) manager.listByParameter(JournalEntryProfile.class, moduleParameter, moduleParameterValue, session).get(0);
						financialsList = new ArrayList<>();
						financialsList.add(jep);
					}
						if (financialsList == null || financialsList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
						
					return "journalEntryProfile";
				} 
			}
		moduleParameterValue="";
		return "populateList";
		
		} catch (RuntimeException re) {
			if (getFinancialModule().equals("accountEntryProfile")){
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
	
	public String getClicked() {
		return clicked;
	}

	public void setClicked(String clicked) {
		this.clicked = clicked;
	}

	public String getFinancialModule() {
		return financialModule;
	}

	public void setFinancialModule(String financialModule) {
		this.financialModule = financialModule;
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
	public List getFinancialsList() {
		return financialsList;
	}
	public void setFinancialsList(List financialsList) {
		this.financialsList = financialsList;
	}

	

}
