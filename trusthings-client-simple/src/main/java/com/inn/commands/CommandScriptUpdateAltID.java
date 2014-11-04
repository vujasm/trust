package com.inn.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.inn.trusthings.db.d2r.Bridge;


public class CommandScriptUpdateAltID {

	public void execute() throws Exception{
		Connection con = getConn();
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery("select id, url from agent");
		while (rs.next()){
			String iServeId = new Bridge().obtainIDFromiServe(rs.getString(2));
			if (iServeId!=null){
				String s = "update agent set altid = '"+iServeId+"' where id = "+rs.getInt(1)+";";
				System.out.println(s);
			}
		
		}
		rs.close();
		statement.close();
	}

	private static String databaseUrl = "jdbc:mysql://localhost:3306/composetrust";

	public static Connection getConn() throws Exception {
		java.sql.Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(databaseUrl, "root", "");
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void main(String[] args) {
		try {
			new CommandScriptUpdateAltID().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
