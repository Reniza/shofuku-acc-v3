package com.shofuku.accsystem.dao.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.loader.JoinWalker;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;

import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.ItemPricing;
import com.shofuku.accsystem.domain.inventory.Memo;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;

public class InventoryDaoImpl extends BaseHibernateDaoImpl {

	// add specific HQL calls here if any

	private Transaction getCurrentTransaction(Session session){
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
		}catch(RuntimeException runtimeExecption){
			tx = session.getTransaction();
		}		
		return tx;
	}
	
	public boolean updateInventoryPerRecordCount(Object object, Session session) {
		Transaction tx = getCurrentTransaction(session);
		try {
			tx=getCurrentTransaction(session);
//			session.save(object);
			session.merge(object);
//			tx.commit();
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
	}
	
	public boolean commitChanges(Session session) {
		Transaction tx = getCurrentTransaction(session);
		try {
			tx=getCurrentTransaction(session);
			tx.commit();
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
	}
	
	public boolean persistMemo(Memo memo, Session session) {
		
		Transaction tx = getCurrentTransaction(session);
		try {
			tx=getCurrentTransaction(session);
			session.save(memo);
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
		
	}
	
	public Set<Ingredient> persistsIngredients(Set<Ingredient> ingredients,Session ss) {
		try {
			Set<Ingredient> ingredientSet = ingredients;
			Iterator<Ingredient> itr = ingredientSet.iterator();

			Set<Ingredient> persistedSet = new HashSet<Ingredient>();

			Transaction tx = null;
			tx = getCurrentTransaction(ss);
			while (itr.hasNext()) {
				Ingredient ingredient = (Ingredient) itr.next();
				try {
					ss.save(ingredient);
					persistedSet.add(ingredient);
				} catch (RuntimeException re) {
					if (null != tx) {
						tx.rollback();
					}
					re.printStackTrace();
				}
			}
			return persistedSet;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List searchFPTSByOrderRequisitionNo(Class clazz, String propertyName, String value,Session session){
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Criteria c = session.createCriteria(FPTS.class, "fpts");
			c.createAlias("fpts.requisitionForm", "requisitionForm");
			
			c.add(Restrictions.or(
					Restrictions.like(propertyName, "%" + value + "%")
							.ignoreCase(), Restrictions.or(Restrictions
							.like(propertyName, "%" + value)
							.ignoreCase(),
							Restrictions
									.like(propertyName, value + "%")
									.ignoreCase())));
			return c.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}finally{
			
		}
		return null;
	}
	public ItemPricing getItemPricingByItemCodeAndParameter(Session session,String itemCode) {
		Transaction tx = null;
		try {
			tx = getCurrentTransaction(session);
			Query query = session.createQuery("from ItemPricing where  lower(itemCode) =:itemCode");
			query.setParameter("itemCode", itemCode.toLowerCase());
			List list = query.list();
			if(null==list||list.size()<1) {
				return null;
			}else {
				ItemPricing itemPricing = (ItemPricing)list.get(0);
				return itemPricing;
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			tx.rollback();
			return null;
		}
	}
	
	public List getStockStatusInBetweenMonthAndYear(String dateFrom, String dateTo, String className,
			Session session) {
		
		DateFormatHelper dfh = new DateFormatHelper();
		String monthFrom ="";
		String yearFrom = "";
		String monthTo ="";
		String yearTo = "";
		
		if(dateFrom!=null && !dateFrom.equalsIgnoreCase("")&&dateTo!=null && !dateTo.equalsIgnoreCase("")){
			monthFrom = dfh.getMonthFromDateString(dateFrom);
			yearFrom = dfh.getYearFromDateString(dateFrom);
			monthTo = dfh.getMonthFromDateString(dateTo);
			yearTo = dfh.getYearFromDateString(dateTo);
		}
		
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			Criteria criteria = session
					.createCriteria(Class.forName(className)).add(
							Restrictions.between("month", monthFrom, monthTo));
			session.createCriteria(Class.forName(className)).add(
					Restrictions.between("year", yearFrom, yearTo));
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List listByParameters(Class clazz,
			Map<String, Object> parameterMap,List parameterFields, String orderByString , Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			
			Criteria criteria = session.createCriteria(clazz);
			
			Iterator itr = parameterFields.iterator();
			while(itr.hasNext()) {
				String field = (String)itr.next();
				
				if(parameterMap.get(field) instanceof String) {
					criteria.add(
							Restrictions.eq(field, parameterMap.get(field)).ignoreCase());
				}else {
					criteria.add(
							Restrictions.eq(field, parameterMap.get(field)));
				}

			} String sql ="";
			criteria.addOrder(Order.asc(orderByString));
			try {
				 CriteriaImpl c = (CriteriaImpl) criteria;
				  SessionImpl s = (SessionImpl) c.getSession();
				  SessionFactoryImplementor factory = (SessionFactoryImplementor) s.getSessionFactory();
				  String[] implementors = factory.getImplementors(c.getEntityOrClassName());
				  LoadQueryInfluencers lqis = new LoadQueryInfluencers();
				  CriteriaLoader loader = new CriteriaLoader((OuterJoinLoadable) factory.getEntityPersister(implementors[0]), factory, c, implementors[0], lqis);
				  Field f = OuterJoinLoader.class.getDeclaredField("sql");
				  f.setAccessible(true);
				   sql = (String) f.get(loader);
			}catch(Exception e) {
				
			}
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;
	}
	
	public List listByParametersLike(Class clazz,
			Map<String, Object> parameterMap,List parameterFields, String orderByString , Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			
			Criteria criteria = session.createCriteria(clazz);
			
			Iterator itr = parameterFields.iterator();
			while(itr.hasNext()) {
				String field = (String)itr.next();
				
				if(parameterMap.get(field) instanceof String) {
					criteria.add(
							Restrictions.or(
									Restrictions.like(field, "%" + parameterMap.get(field) + "%")
									.ignoreCase(), Restrictions.or(Restrictions
											.like(field, "%" + parameterMap.get(field))
											.ignoreCase(),
											Restrictions
											.like(field, parameterMap.get(field) + "%")
											.ignoreCase())));
				}else {
					criteria.add(
							Restrictions.eq(field, parameterMap.get(field)));
				}

			} String sql ="";
			criteria.addOrder(Order.asc(orderByString));
			try {
				 CriteriaImpl c = (CriteriaImpl) criteria;
				  SessionImpl s = (SessionImpl) c.getSession();
				  SessionFactoryImplementor factory = (SessionFactoryImplementor) s.getSessionFactory();
				  String[] implementors = factory.getImplementors(c.getEntityOrClassName());
				  LoadQueryInfluencers lqis = new LoadQueryInfluencers();
				  CriteriaLoader loader = new CriteriaLoader((OuterJoinLoadable) factory.getEntityPersister(implementors[0]), factory, c, implementors[0], lqis);
				  Field f = OuterJoinLoader.class.getDeclaredField("sql");
				  f.setAccessible(true);
				   sql = (String) f.get(loader);
			}catch(Exception e) {
				
			}
			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		}
		return null;
	}
	
	

	public List<PurchaseOrderDetails> getPurchaseOrderDetailsContainingItemCode(String itemCode) {
	
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(
					"c://ShofukuAccountingSystemDatabase.properties"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String mysqlUrl = (String) prop.getProperty("mysql_url");
		String driver = (String) prop.getProperty("mysql_driver");
		String userName = (String) prop.getProperty("db_user");
		String password = (String) prop.getProperty("db_pwd");
		String dbName = (String) prop.getProperty("dbName");
		String schema = (String) prop.getProperty("schema");
		

		Connection conn = null;
		List orderDetailsList = new ArrayList();
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(mysqlUrl + dbName, userName,
					password);
//			System.out.println("Connected to the database");
			try {
				Statement st = conn.createStatement();
				ResultSet rs = null;

				rs = st.executeQuery("SELECT `t601_purchase_order_details`.`id`,`t601_purchase_order_details`.`ITEM_CODE`,`t601_purchase_order_details`.`QUANTITY`,`t601_purchase_order_details`.`AMOUNT`,`t601_purchase_order_details`.`ORDER_DATE` "
						+ "FROM `"+schema+"`.`t601_purchase_order_details`"
						+ "WHERE ITEM_CODE ='" + itemCode + "'");

				while (rs.next()) {
					PurchaseOrderDetails poDetails = new PurchaseOrderDetails();
					poDetails.setId(rs.getInt("ID"));
					poDetails.setItemCode(rs.getString("ITEM_CODE"));
					poDetails.setQuantity(rs.getDouble("QUANTITY"));
					poDetails.setAmount(rs.getDouble("AMOUNT"));
					poDetails.setOrderCreatedDate(rs.getTimestamp("ORDER_DATE"));
					orderDetailsList.add(poDetails);
				}
				
				conn.close();
//				System.out.println("Disconnected from database");
				return orderDetailsList;
			} catch (SQLException s) {
				s.printStackTrace();
				System.out.println("SQL statement is not executed!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<PurchaseOrderDetails> getRelatedOrders(String itemCode,List<String> ordersRelated) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(
					"c://ShofukuAccountingSystemDatabase.properties"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String mysqlUrl = (String) prop.getProperty("mysql_url");
		String driver = (String) prop.getProperty("mysql_driver");
		String userName = (String) prop.getProperty("db_user");
		String password = (String) prop.getProperty("db_pwd");
		String dbName = (String) prop.getProperty("dbName");
		String schema = (String) prop.getProperty("schema");
		
		Map<String,String> ordersList = new HashMap<String,String>();
		List orderDetailsList = getPurchaseOrderDetailsContainingItemCode(itemCode) ;
		List finalList = new ArrayList();
		Connection conn = null;
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(mysqlUrl + dbName, userName,
					password);
//			System.out.println("Connected to the database");
			try {
				Statement st = conn.createStatement();
				ResultSet rs = null;

				StringBuilder sb = new StringBuilder();
				
				Iterator itr = orderDetailsList.iterator();
				while(itr.hasNext()) {
					PurchaseOrderDetails poDetails = (PurchaseOrderDetails) itr.next();
					sb.append(poDetails.getId()+",");
				}
				
				String ids="";
				if(sb.length()>0) {
					ids = sb.substring(0, sb.length()-1);
					
					Iterator qryDtls = ordersRelated.iterator();
					StringBuilder qryBldr = new StringBuilder();
					qryBldr.append("SELECT `t602_orders`.`id` FROM `"+schema+"`.`t602_orders` WHERE ID IN (" + ids + ") AND (");
					while(qryDtls.hasNext()) {
						String stringTemp = (String)qryDtls.next();
						qryBldr.append(stringTemp +" is not null OR ");
					}
					
					
					String qryString = qryBldr.substring(0, qryBldr.length()-3);
					qryBldr = new StringBuilder();
					qryBldr.append(qryString);
					qryBldr.append(")");
					
					rs = st.executeQuery(qryBldr.toString());
					if(rs!=null) {
						while (rs.next()) {
							String id = rs.getString("ID");
							ordersList.put(id,id);
						}
					}
					
					Iterator itr2 = orderDetailsList.iterator();
					while(itr2.hasNext()) {
						PurchaseOrderDetails poDetails = (PurchaseOrderDetails) itr2.next();
						if(ordersList.get(String.valueOf(poDetails.getId()))!=null){
							finalList.add(poDetails);
						}
					}
				}
			} catch (SQLException s) {
				s.printStackTrace();
				System.out.println("SQL statement is not executed!");
			}
			conn.close();
//			System.out.println("Disconnected from database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return finalList;
	}
	
	
	
	public Map<String, String> getRelatedReturnSlipIds(String itemCode,List<String> ordersRelated) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(
					"c://ShofukuAccountingSystemDatabase.properties"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String mysqlUrl = (String) prop.getProperty("mysql_url");
		String driver = (String) prop.getProperty("mysql_driver");
		String userName = (String) prop.getProperty("db_user");
		String password = (String) prop.getProperty("db_pwd");
		String dbName = (String) prop.getProperty("dbName");
		String schema = (String) prop.getProperty("schema");
		
		Map<String,String> ordersList = new HashMap<String,String>();
		List orderDetailsList = getPurchaseOrderDetailsContainingItemCode(itemCode) ;
		List finalList = new ArrayList();
		Connection conn = null;
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(mysqlUrl + dbName, userName,
					password);
//			System.out.println("Connected to the database");
			try {
				Statement st = conn.createStatement();
				ResultSet rs = null;

				StringBuilder sb = new StringBuilder();
				
				Iterator itr = orderDetailsList.iterator();
				while(itr.hasNext()) {
					PurchaseOrderDetails poDetails = (PurchaseOrderDetails) itr.next();
					sb.append(poDetails.getId()+",");
				}
				
				String ids="";
				if(sb.length()>0) {
					ids = sb.substring(0, sb.length()-1);
					
					Iterator qryDtls = ordersRelated.iterator();
					StringBuilder qryBldr = new StringBuilder();
					qryBldr.append("SELECT `t602_orders`.`ID`,`t602_orders`.`return_slip_no` FROM `"+schema+"`.`t602_orders` WHERE ID IN (" + ids + ") AND (");
					while(qryDtls.hasNext()) {
						String stringTemp = (String)qryDtls.next();
						qryBldr.append(stringTemp +" is not null OR ");
					}
					
					
					String qryString = qryBldr.substring(0, qryBldr.length()-3);
					qryBldr = new StringBuilder();
					qryBldr.append(qryString);
					qryBldr.append(")");
					
					rs = st.executeQuery(qryBldr.toString());
					if(rs!=null) {
						while (rs.next()) {
							String id = rs.getString("ID");
							String rsNum = rs.getString("RETURN_SLIP_NO");
							ordersList.put(id,rsNum);
						}
					}
				}
			} catch (SQLException s) {
				s.printStackTrace();
				System.out.println("SQL statement is not executed!");
			}
			conn.close();
//			System.out.println("Disconnected from database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ordersList;
	}
	
	public boolean updateStockStatus(Object item,Session session) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(session);
			session.merge(item);
			return true;
		} catch (RuntimeException re) {
			if (null != tx)
				tx.rollback();
			re.printStackTrace();
		}
		return false;
	}
	
	public boolean saveStockStatus(Object item,Session ss) {
		Transaction tx = null;
		try {
			tx=getCurrentTransaction(ss);
			ss.save(item);
			tx.commit();
			return true;
		} catch (RuntimeException re) {
			if (null != tx) {
				tx.rollback();
			}
			re.printStackTrace();
		} 
		return false;
	}

	public List listInventoryItemsByStatus(String subModule,
			String searchByStatus, Session session) {
		Transaction tx = null;
		try {
			tx = getCurrentTransaction(session);

			List list = new ArrayList();
			String orderBy = "";

			Class clazz = null;

			if (subModule.equalsIgnoreCase("RawMaterials")) {
				clazz = RawMaterial.class;
				orderBy = "itemCode";
			} else if (subModule.equalsIgnoreCase("TradedItems")) {
				clazz = TradedItem.class;
				orderBy = "itemCode";
			} else if (subModule.equalsIgnoreCase("FinishedGoods")) {
				clazz = FinishedGood.class;
				orderBy = "productCode";
			}

			Criteria criteria = session.createCriteria(clazz);
			criteria.addOrder(Order.asc(orderBy));

			switch (searchByStatus) {
			case "A":
				searchByStatus = "B";
				criteria.add(Restrictions.or(Restrictions.or(
						Restrictions.eq("template", "S"),
						Restrictions.eq("template", "B")), Restrictions.eq(
						"template", "C")));
				break;
			case "I":
				searchByStatus = "N";
				criteria.add(Restrictions.eq("template", "N"));
				break;
			case "B":
				searchByStatus = "";
				break;

			}

			return criteria.list();
		} catch (RuntimeException re) {
			tx.rollback();
			re.printStackTrace();
		} finally {

		}
		return null;

	}
	

}
