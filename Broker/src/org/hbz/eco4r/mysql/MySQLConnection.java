package org.hbz.eco4r.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class MySQLConnection {

	private static Logger logger = Logger.getLogger(MySQLConnection.class);
			
	final String DRIVER = "com.mysql.jdbc.Driver";
	private Connection connection;
	private String dbURL;
	private String user;
	private String passwd;
	
	
	public MySQLConnection(String dbURL, String user, String passwd) {
		try {
			this.setDbURL(dbURL);
			this.setUser(user);
			this.setPasswd(passwd);
			Class.forName(DRIVER);
			this.setConnection(DriverManager.getConnection(this.dbURL, this.user, this.passwd));
			logger.info("Connection with MySQL database established.");
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public String getDbURL() {
		return dbURL;
	}
	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
