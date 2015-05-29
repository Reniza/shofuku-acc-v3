package com.shofuku.accsystem.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class HibernateSessionWatcher {


	Connection conn= null;
	String url;
	String dbName;
	String driver;
	String userName; 
	String password;
	String schema;
	  
	public void killSleepers() {

		  try {
			  getCurrentEnvironmentDBDetails();
			  Class.forName(driver).newInstance();
			  conn = DriverManager.getConnection(url+dbName,userName,password);
			  int numOfConnKilled=0;
			  try{
				  Statement st = conn.createStatement();
				  ResultSet rs= null;
				  rs = st.executeQuery("select * from information_schema.processlist where DB = '"+schema+"' and STATE='' and COMMAND = 'SLEEP' and TIME>1");
				  while (rs.next()) {
					  String i = rs.getString("id");
					  Statement st2 = conn.createStatement();
					  int no = st2.executeUpdate("KILL "+ i + ";");
					  numOfConnKilled++;
				  }	  
			  }catch (SQLException s){
				  s.printStackTrace();
				  System.out.println("SQL statement is not executed!");
			  }
			  conn.close();
			  if(numOfConnKilled>0) {
//				  System.out.println("Kill Sleepers: from database, killed "+ numOfConnKilled+" sleeping sessions.");
			  }
		  }catch (Exception e) {
			  e.printStackTrace();
		  }
	  
	}
	  private static String getToday(){
		  Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(cal.getTime());
	  }
	
	private void getCurrentEnvironmentDBDetails() {
		
			  try {
				  Properties prop = new Properties();
				  prop.load(new FileInputStream(SASConstants.PROPERTY_FILE_PATH));
				  dbName = (String)prop.getProperty("dbName");
				  url = (String)prop.getProperty("mysql_url");
				  driver = (String)prop.getProperty("mysql_driver");
				  userName = (String)prop.getProperty("db_user");
				  password = (String)prop.getProperty("db_pwd");
				  schema = (String)prop.getProperty("schema");
			 } 
			 catch(IOException ioe){
			   ioe.printStackTrace();
			   System.out.println("get properties failed :"+getToday());
			  }
			 
			  catch(Exception e) {
				  System.out.println("get properties failed  : "+getToday());
			 }
	}

}
