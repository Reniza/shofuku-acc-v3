package com.shofuku.accsystem.action.financials;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;

public class EditFinancialsAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String financialModule;
	private String forWhat;
	private String forWhatDisplay;
	private double tempTotalAmountPerAccount;
	
	List accountCodeList;
	
	private String moduleParameter;
	AccountEntryProfile aep;
	JournalEntryProfile jep;
	
	//Display amount
	Transaction transaction;
	
	AccountEntryManager manager = new AccountEntryManager();

	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception{
		Session session = getSession();
		try {
			forWhatDisplay = "edit";
			accountCodeList = manager.listAlphabeticalAccountEntryProfileAscByParameter(AccountEntryProfile.class, "accountCode", session);
			if (getFinancialModule().equalsIgnoreCase("accountEntryProfile")) {
				
			List<Transaction> transactionPerAccountList = new ArrayList<>();
				transactionPerAccountList = manager.listByParameter(Transaction.class, "accountEntry.accountCode", aep.getAccountCode(), session);
				double totalAmount = 0;
			
				//// total amount per account
				for (Transaction transaction : transactionPerAccountList) {
					if (transaction.getIsInUse().equalsIgnoreCase("IN USE"))
						if (transaction.getTransactionAction().equalsIgnoreCase("DEBIT")) {
							totalAmount = totalAmount + transaction.getAmount();
						}else {
							totalAmount = totalAmount - transaction.getAmount();
						}
				}
				tempTotalAmountPerAccount = totalAmount;
				///
				
				AccountEntryProfile aep = new AccountEntryProfile();
				aep = (AccountEntryProfile) manager.loadAccountEntryProfile(this.aep.getAccountCode());
				setAep(aep);
				return "accountEntryProfile";
			} else {
				JournalEntryProfile jep = new JournalEntryProfile();
				jep = (JournalEntryProfile) manager.listByParameter(JournalEntryProfile.class, "entryNo", this.jep.getEntryNo(), session).get(0);
				setJep(jep);
				
				return "journalEntryProfile";
			}
			
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (getFinancialModule().equalsIgnoreCase("accountEntryProfile")){
				return "accountEntryProfile";
			}else {
				return "journalEntryProfile";
			}
		} finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}
	
	//getter setters
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

	public AccountEntryProfile getAep() {
		return aep;
	}
	public void setAep(AccountEntryProfile aep) {
		this.aep = aep;
	}
	public String getForWhat() {
		return forWhat;
	}

	public void setForWhat(String forWhat) {
		this.forWhat = forWhat;
	}
	public List getAccountCodeList() {
		return accountCodeList;
	}
	public void setAccountCodeList(List accountCodeList) {
		this.accountCodeList = accountCodeList;
	}
	public String getForWhatDisplay() {
		return forWhatDisplay;
	}
	public void setForWhatDisplay(String forWhatDisplay) {
		this.forWhatDisplay = forWhatDisplay;
	}
	public JournalEntryProfile getJep() {
		return jep;
	}
	public void setJep(JournalEntryProfile jep) {
		this.jep = jep;
	}
	public double getTempTotalAmountPerAccount() {
		return tempTotalAmountPerAccount;
	}
	public void setTempTotalAmountPerAccount(double tempTotalAmountPerAccount) {
		this.tempTotalAmountPerAccount = tempTotalAmountPerAccount;
	}
	
	
}
