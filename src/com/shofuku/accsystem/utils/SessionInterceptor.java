package com.shofuku.accsystem.utils;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.shofuku.accsystem.domain.security.UserAccount;

public class SessionInterceptor extends AbstractInterceptor {
	
  /**
	 * 
	 */
	private static final long serialVersionUID = -4714138482853753102L;

@Override
  public String intercept(ActionInvocation invocation) throws Exception {
      Map<String,Object> session = invocation.getInvocationContext().getSession();
      if(session.isEmpty()) {
    	  return "loginRedirect"; // session is empty/expired
      }
      else {
    	  UserAccount user = (UserAccount) session.get("user");    	  
    	  if(user==null) {
    		  return "loginRedirect"; // session is empty/expired
    	  }else {
    		  invocation.invoke();
    	  }
      }
      
      return invocation.invoke();
}

}
