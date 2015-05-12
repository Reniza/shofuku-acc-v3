package com.shofuku.accsystem.action.financials;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.AccountingRules;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;


public class AddFinancialsAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	
	private static final Logger logger = Logger
			.getLogger(AddFinancialsAction.class);

	private static final Logger logger2 = logger.getRootLogger();

	AccountEntryManager aepManager = new AccountEntryManager();
	RecordCountHelper rch = new RecordCountHelper();
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	// beans
		String subModule;
		AccountEntryProfile aep;
		JournalEntryProfile jep;
		private String forWhat;
		private String forWhatDisplay;
		List accountCodeList = null;
	
	public String execute() throws Exception {
		Session session = getSession();
		try {
			
			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
				return addAccountEntryProfile(session);
			}else {
				return addJournalEntryProfile(session);
			}
		
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
				return "accountEntryProfileAdded";
			}else {
				return "journalEntryProfileAdded";
			}
			
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}

	

	public String loadParentCode() {
		Session session = getSession();
		
		
		try {
			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
				accountCodeList = aepManager.listAlphabeticalAccountEntryProfileAscByParameter(AccountEntryProfile.class, "accountCode", session);
				return "accountEntryProfile";
			}else {
				accountCodeList = aepManager.listAlphabeticalAccountEntryProfileAscByParameter(AccountEntryProfile.class, "accountCode", session);
				return "journalEntryProfile";
			}
			
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
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
	private String addAccountEntryProfile(Session session) {
		// TODO Auto-generated method stub
		boolean addResult = false;
			
			if (validateAccountProfileEntry()) {
				loadParentCode();
			
			}else {
				if((aep.getParentCode().equalsIgnoreCase("21010000") || aep.getParentCode().equalsIgnoreCase("21010000")) && aep.getAccountCode().equalsIgnoreCase("")) {
					addActionError(SASConstants.AEP_FAILED);
				}else {
					
				AccountEntryProfile parent = aepManager.loadAccountEntryProfile(aep.getParentCode());
				aep.setHierarchy(getAepLevel(parent.getHierarchy()));
				if(aep.getParentCode().equalsIgnoreCase("none")) {
					aep.setParentCode("0");
					AccountingRules accntgRules = aep.getAccountingRules();
					accntgRules.setRuleId(0);
					aep.setAccountingRules(accntgRules);
				}else {
					if((aep.getParentCode().equalsIgnoreCase("21010000") || aep.getParentCode().equalsIgnoreCase("11020100"))) {
					}else {
						aep.setAccountCode(aepManager.getAccountCode(aep.getParentCode(), session));
					}
					AccountingRules accntgRules = aep.getAccountingRules();
					accntgRules.setAccountCode(aep.getAccountCode());
					accntgRules.setRuleId(rch.getAccountingRulesCount()+1);
					aepManager.addAccountEntryRule(accntgRules,session);
				}
				
				addResult = aepManager.addAccountEntryProfile(aep, session);
				
					if (addResult == true) {
						addActionMessage(SASConstants.ADD_SUCCESS);
						
						forWhat="true";
						forWhatDisplay ="edit";
					} else {
						addActionError(SASConstants.FAILED);
						}
				}
			}
			loadParentCode();
		return "accountProfileEntryAdded";
	}

	private String getAepLevel(String heirarchy) {
		if(heirarchy.equalsIgnoreCase("ACCOUNT")) {
			return "SECTION";
		}else if(heirarchy.equalsIgnoreCase("SECTION")) {
			return "MAIN";
		}else if(heirarchy.equalsIgnoreCase("MAIN")) {
			return "SUB1";
		}else if(heirarchy.equalsIgnoreCase("SUB1")) {
			return "SUB2";
		}else {
			return "";
		}
		
	}



	private String addJournalEntryProfile(Session session) {
		boolean addResult = false;
		
		if (validateJournalProfileEntry()) {
			loadParentCode();
		
		}else {
			AccountEntryProfile aepCredit = aepManager.loadAccountEntryProfile(jep.getAepCredit().getAccountCode());
			AccountEntryProfile aepDebit = aepManager.loadAccountEntryProfile(jep.getAepDebit().getAccountCode());
			
			jep.setAepCredit(aepCredit);
			jep.setAepDebit(aepDebit);
			
			addResult = aepManager.addAccountEntryProfile(jep, session);
			
				if (addResult == true) {
					addActionMessage(SASConstants.ADD_SUCCESS);
					
					forWhat="true";
					forWhatDisplay ="edit";
				} else {
					addActionError(SASConstants.FAILED);
					}
				}
		loadParentCode();
	return "journalProfileEntryAdded";
	}
	
//beans getter/setter from page

	private boolean validateAccountProfileEntry() {
		// TODO Auto-generated method stub
		boolean errorFound = false;
		
		if ("".equals(aep.getName())) {
			 addFieldError("aep.name", "REQUIRED");
			 errorFound = true;
		}if ("".equals(aep.getClassification())) {
			 addFieldError("aep.classification", "REQUIRED");
			 errorFound = true;
		}
		return errorFound;
	}

	private boolean validateJournalProfileEntry() {
		// TODO Auto-generated method stub
		boolean errorFound = false;
		
		if ("".equals(jep.getEntryName())) {
			 addFieldError("jep.entryName", "REQUIRED");
			 errorFound = true;
		}if (jep.getAmount() == 0) {
			 addFieldError("jep.amount", "REQUIRED");
			 errorFound = true;
		}
		return errorFound;
	}

	public AccountEntryProfile getAep() {
		return aep;
	}

	public void setAep(AccountEntryProfile aep) {
		this.aep = aep;
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

	public String getForWhatDisplay() {
		return forWhatDisplay;
	}

	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}

	public List getAccountCodeList() {
		return accountCodeList;
	}

	public void setAccountCodeList(List accountCodeList) {
		this.accountCodeList = accountCodeList;
	}



	public JournalEntryProfile getJep() {
		return jep;
	}



	public void setJep(JournalEntryProfile jep) {
		this.jep = jep;
	}
	
	
}