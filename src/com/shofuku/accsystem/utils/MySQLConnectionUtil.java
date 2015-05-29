package com.shofuku.accsystem.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.shofuku.accsystem.utils.SASConstants;

public class MySQLConnectionUtil {
	private static final Logger logger = Logger
			.getLogger(MySQLConnectionUtil.class);

	Connection con = null;

	public Connection getConnection() {
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(SASConstants.PROPERTY_FILE_PATH));
		} catch (FileNotFoundException e1) {
			logger.error("Property File Not Found");
			logger.error(e1.getStackTrace());
		} catch (IOException e1) {
			logger.error("IO Exception");
			logger.error(e1.getStackTrace());
		}
		
		String url = (String)prop.getProperty("mysql_home");
		String db = (String)prop.getProperty("database");
		String user = (String)prop.getProperty("db_user");
		String password = (String)prop.getProperty("db_pwd");
		
		db = createDBURL(url,db);
		
		System.out.println("Connecting to : "+ db);
		try {
			con = DriverManager.getConnection(db, user, password);
		} catch (Exception e) {
			logger.debug("getConnection() :: Error in connection" + e);
		}
		return con;
	}


	private String createDBURL(String url,String dbName) {
		return url+dbName;
	}

}
