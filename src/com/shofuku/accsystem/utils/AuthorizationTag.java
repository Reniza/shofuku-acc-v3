package com.shofuku.accsystem.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import javax.servlet.jsp.tagext.Tag;

import com.opensymphony.xwork2.ActionContext;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.helpers.UserRoleHelper;

public class AuthorizationTag extends BodyTagSupport  {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3858218270545685201L;

	UserRoleHelper roleHelper = new UserRoleHelper();
	
	private String role;
	
	private String alternateMessage;
	
	public int  doStartTag() throws JspException {
		    
		    Map actionSession = ActionContext.getContext().getSession();
			UserAccount user = (UserAccount) actionSession.get("user");
			
			List modulesList = (List) actionSession.get("rolesList");
			
			String bodyText = bodyContent.getString();
			
			 try {
			       	pageContext.getOut().print(bodyText);
			       }
			       catch (Exception e) {
			       	throw new JspTagException(e.getMessage());
			       }
			 
			 StringBuffer tableOut = new StringBuffer(); 
			 tableOut.append(alternateMessage); 
			
			if(user==null) {
			}else {
				
				Map allowedRoles = roleHelper.parseModulesListToMap(roleHelper.addRolesAccessStringToGrantedList(user.getRole(), roleHelper.parseModulesListToMap(modulesList)));
				if(allowedRoles.get(Integer.valueOf(role))==null) {
					 return Tag.SKIP_BODY;
				}else {
					 return Tag.EVAL_BODY_INCLUDE;
				}
			}
			
			 return Tag.SKIP_BODY;
		  }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAlternateMessage() {
		return alternateMessage;
	}

	public void setAlternateMessage(String alternateMessage) {
		this.alternateMessage = alternateMessage;
	}
}
