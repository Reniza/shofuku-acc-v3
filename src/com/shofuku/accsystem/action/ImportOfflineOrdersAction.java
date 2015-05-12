package com.shofuku.accsystem.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.ImportOfflineOrdersUtil;

public class ImportOfflineOrdersAction extends ActionSupport {
	
	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
	private String fileUpload;
	List errorStringList;
	ImportOfflineOrdersUtil util = new ImportOfflineOrdersUtil();
	
	Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public String execute(){
		Session session = getSession();
		errorStringList = new ArrayList();
		
		//util.readImportFile(fileUpload, session);
		errorStringList = util.getErrorStrings();
		return "imported";
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public List getErrorStringList() {
		return errorStringList;
	}

	public void setErrorStringList(List errorStringList) {
		this.errorStringList = errorStringList;
	}
}
