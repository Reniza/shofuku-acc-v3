package com.shofuku.accsystem.controllers;

import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.DisbursementDaoImpl;
import com.shofuku.accsystem.dao.impl.SecurityDaoImpl;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.utils.DoubleConverter;

public class SecurityManager extends BaseController{
	
	SecurityDaoImpl dao = new SecurityDaoImpl();
	DoubleConverter dc = new DoubleConverter();
	
	public List listSecurityByParameter(Class clazz,
			String parameter, String value,Session session) {
		return dao.listByParameter(clazz, parameter, value,session);
	}
	
	public boolean insertToolsDetails(Object obj,Session session) {
		return dao.save(obj, session);
	}
	@SuppressWarnings("rawtypes")
	public List listToolsByParameterLike(Class clazz,
			String parameter, String value,Session session) {
		return dao.listByParameterLike(clazz, parameter, value,session);
	}
	
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		return  dao.listAlphabeticalAscByParameter(clazz, parameter, session);
	}
	public boolean updateSecurity(Object persistentObject,Session session){
		return dao.update(persistentObject,session);
	}
}
