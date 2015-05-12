package com.shofuku.accsystem.action.financials;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.AccountingRules;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class UpdateFinancialsAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger
			.getLogger(AddFinancialsAction.class);

	private static final Logger logger2 = logger.getRootLogger();
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");

	AccountEntryManager aepManager = new AccountEntryManager();
	String parentCode;
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	// beans
		private String subModule;
		private String forWhat;
		private String forWhatDisplay;
		List accountCodeList = null;
		
		AccountEntryProfile aep;
		JournalEntryProfile jep;
	
	public String execute() throws Exception {
		Session session = getSession();
		try {
			
			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
				return updateAccountEntryProfile(session);
			}else {
				return updateJournalEntryProfile(session);
			}
		
		}catch (RuntimeException re) {
			re.printStackTrace();
			if (getSubModule().equalsIgnoreCase("accountEntryProfile")) {
				return "accountEntryProfileUpdated";
			}else {
				return "journalEntryProfileUpdated";
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
	private String updateAccountEntryProfile(Session session) {
		// TODO Auto-generated method stub
		boolean updateResult = false;
		aep.setParentCode(parentCode);
			if (validateAccountProfileEntry()) {
				//aep.setParentCode(parentCode);
				//loadParentCode();
			}else {
				AccountingRules aepRule = aepManager.loadAccountingProfileRuleByAccountCode(aep.getAccountCode(), session);
				if(aepRule==null) {
					AccountingRules newAepRule = aep.getAccountingRules();
					newAepRule.setAccountCode(aep.getAccountCode());
					int totalExistingRules= aepManager.getTotalRecordCount(AccountingRules.class, session).intValue();
					newAepRule.setRuleId(totalExistingRules);
					aepManager.addAccountEntryRule(aep.getAccountingRules(), session);
					aep.setAccountingRules(newAepRule);
				}else {
					AccountingRules updatedAepRule = aep.getAccountingRules();
					updatedAepRule.setRuleId(aepRule.getRuleId());
					aep.setAccountingRules(updatedAepRule);
					updatedAepRule.setAccountCode(aep.getAccountCode());
					aepManager.updateAccountingRule(updatedAepRule, session);
				}
				updateResult = aepManager.updateAccountEntryProfile(aep, session);
				
					if (updateResult == true) {
						addActionMessage(SASConstants.UPDATED);
						loadParentCode();
						forWhat="true";
						forWhatDisplay ="edit";
					} else {
						addActionError(SASConstants.FAILED);
						}
					}
		return "accountEntryProfileUpdated";
	}

	private String updateJournalEntryProfile(Session session) {
		// TODO Auto-generated method stub
		boolean updateResult = false;
		jep.setAccountCode(parentCode);
			if (validateJournalProfileEntry()) {
				loadParentCode();
			}else {
				updateResult = aepManager.updateAccountEntryProfile(jep, session);
					if (updateResult == true) {
						addActionMessage(SASConstants.UPDATED);
						loadParentCode();
						forWhat="true";
						forWhatDisplay ="edit";
					} else {
						addActionError(SASConstants.FAILED);
						}
					}
		return "journalEntryProfileUpdated";
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
		}if (jep.getAmount()==0) {
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

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
}
