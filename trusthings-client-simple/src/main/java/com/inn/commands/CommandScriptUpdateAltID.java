package com.inn.commands;

/*
 * #%L
 * trusthings-client-simple
 * %%
 * Copyright (C) 2014 INNOVA S.p.A
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
