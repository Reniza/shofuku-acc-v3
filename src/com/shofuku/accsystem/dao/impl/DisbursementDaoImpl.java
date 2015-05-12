package com.shofuku.accsystem.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.security.UserAccount;


public class DisbursementDaoImpl extends BaseHibernateDaoImpl {
	
	// add specific HQL calls here if any
	public List listCheckVoucherWithInvoice(Session session) {
		//dito ang code
		Transaction tx = null;
		try {
			session = super.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from CheckPayments where invoice is not null");
			List list = query.list();
			if(null==list||list.size()<1) {
				return null;
			}else {
				return list;
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			tx.rollback();
			return null;
		}
	}
	public List listCheckVoucherWithoutInvoice(Session session) {
		//dito ang code
		Transaction tx = null;
		try {
			session = super.getSessionFactory().getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("from CheckPayments where invoice is null");
			List list = query.list();
			if(null==list||list.size()<1) {
				return null;
			}else {
				return list;
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			tx.rollback();
			return null;
		}
	}
}
