package com.Junit.tests;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.junit.Test;

import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerStockLevel;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.ImportOfflineOrdersUtil;
import com.shofuku.accsystem.utils.POIUtil;

public class ImportOfflineOrdersTest {
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	
	@Test
	public void readStockLevelForm() {

		POIUtil util = new POIUtil();
		
		String fileName="C:\\Dev/customerPurchasesImportTemplate.xls";
		Session session = getSession();
	
		ImportOfflineOrdersUtil ioo = new ImportOfflineOrdersUtil();
		ioo.setImportType("CUSTOMER");
		ioo.readImportFile(fileName,"Customer", session);
		
		Iterator itr = ioo.getErrorStrings().iterator();
		
		while(itr.hasNext()) {
			String errorMsg = (String)itr.next();
			System.out.println(errorMsg);
		}
		
		
	}
}
