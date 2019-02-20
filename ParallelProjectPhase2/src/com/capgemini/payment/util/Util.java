package com.capgemini.payment.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
	
	public static Connection getConnection()
	{
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con;
				con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "Capgemini123");
				return con;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

	}

}
