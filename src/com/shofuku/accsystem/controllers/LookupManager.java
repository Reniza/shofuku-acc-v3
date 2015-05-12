package com.shofuku.accsystem.controllers;

import java.util.List;

import org.hibernate.Session;

import com.shofuku.accsystem.dao.impl.LookupDaoImpl;
import com.shofuku.accsystem.domain.lookups.UnitOfMeasurements;
import com.shofuku.accsystem.utils.SASConstants;

public class LookupManager extends BaseController {
	
	LookupDaoImpl lookupDao = new LookupDaoImpl();
	
	@SuppressWarnings("rawtypes")
	public List getLookupElements(Class clazz, String module,Session session) {
		return lookupDao.listAlphabeticalAscByParameter(clazz, "value",session);
	}
	public List listItemByClassification(Class clazz, String parameter,
			String value,Session session) {
		return lookupDao.listByParameter(clazz, parameter, value,session);
	}
	
	public List getDistinctReferenceNo(Class clazz,Session session){
		return lookupDao.listDistinctReferenceNo(clazz, SASConstants.REFERENCE_NO,session);
	}
	
	public boolean addNewUOM(UnitOfMeasurements uom, Session session){
		return lookupDao.save(uom, session);
	}
	 
}
