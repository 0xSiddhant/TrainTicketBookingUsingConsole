package com.trainTicket.booking;
import java.sql.*;

public class Connector {

	static Connector connector;
	static Statement stmt;
	static Connection con;
	private Connector() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");	
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");
			stmt = con.createStatement();
			
		}catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static Connector getConnector() {
		if (connector == null) {
			return new Connector();
		}else {
			return connector;
		}
	}
}
