package com.mPulse.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import java.io.FileInputStream;
//import java.util.Properties;

import com.mPulse.utility.ConfigReader;

public class DatabaseFactory {
	
	private static Connection con;
	
	private DatabaseFactory() {}
	public static synchronized Connection getConnection() {
		if(con == null) {
			try {
				Class.forName("org.postgresql.Driver").newInstance();
			} catch (Exception e) {
				System.out.println("postgresql Driver is not found , Error : - " +e.getMessage());
				e.printStackTrace();
			}
			try {
				String jdbcURL = ConfigReader.getConfig("jdbcURL");
				String jdbcUser = ConfigReader.getConfig("jdbcUser");
				String jdbcPassword = ConfigReader.getConfig("jdbcPassword");
				con = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
				System.out.println("Sucessfully Connected to DB");
			} catch (SQLException e) {
				System.out.println("not able to connect to postgresql , Error : - " +e.getMessage());
				e.printStackTrace();
			}
		}
		return con;
	}
	
	public static void closeConnection() {
		try {
			if (con != null) {
				con.close();
				System.out.println("Closing the DB Connection");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Unable to close the connection", e);
		}
	}
}

