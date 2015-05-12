package com.shofuku.accsystem.action.receipts;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.shofuku.accsystem.domain.security.UserAccount;

public class SummaryReceiptAction {

	Map actionSession = ActionContext.getContext().getSession();
	UserAccount user = (UserAccount) actionSession.get("user");
	
}
