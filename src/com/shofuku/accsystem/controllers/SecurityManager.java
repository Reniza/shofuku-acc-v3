package com.shofuku.accsystem.controllers;

import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.DisbursementDaoImpl;
import com.shofuku.accsystem.dao.impl.SecurityDaoImpl;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.utils.DoubleConverter;

public class SecurityManager extends BaseController{
	
	DoubleConverter dc = new DoubleConverter();
	
	public List listSecurityByParameter(Class clazz,
			String parameter, String value,Session session) {
		return securityDao.listByParameter(clazz, parameter, value,session);
	}
	
	public boolean insertToolsDetails(Object obj,Session session) {
		return securityDao.save(obj, session);
	}
	@SuppressWarnings("rawtypes")
	public List listToolsByParameterLike(Class clazz,
			String parameter, String value,Session session) {
		return securityDao.listByParameterLike(clazz, parameter, value,session);
	}
	
	public List listAlphabeticalAscByParameter(Class clazz, String parameter,Session session) {
		return  securityDao.listAlphabeticalAscByParameter(clazz, parameter, session);
	}
	public boolean updateSecurity(Object persistentObject,Session session){
		return securityDao.update(persistentObject,session);
	}
}
