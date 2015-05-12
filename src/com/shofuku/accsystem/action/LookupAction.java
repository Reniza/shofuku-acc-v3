package com.shofuku.accsystem.action;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.domain.lookups.ExpenseClassification;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
 
public class LookupAction extends ActionSupport {
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
    private String data;
    
    private List<String> expenseClassifications;

	private String expenseClassification;
 
	
	// add other managers for other modules Manager()
	LookupManager lookupManager = (LookupManager) actionSession.get("lookupManager");
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		try{
    	expenseClassifications = lookupManager.getLookupElements(ExpenseClassification.class, "PETTYCASH",session);
    	return SUCCESS;
		}catch(Exception e ){
			return SUCCESS;
		}finally{
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
    }
    
    
    public List<String> getExpenseClassifications() {
    	return expenseClassifications;
    }
    
    public void setExpenseClassifications(List<String> expenseClassifications) {
    	this.expenseClassifications = expenseClassifications;
    }
    
    public String getExpenseClassification() {
    	return expenseClassification;
    }
    
    public void setExpenseClassification(String expenseClassification) {
    	this.expenseClassification = expenseClassification;
    }
}