package com.inn.trusthings.collector;

/*
 * #%L
 * trusthings-service
 * %%
 * Copyright (C) 2014 - 2015 INNOVA S.p.A
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
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.common.Const;
import com.inn.common.ValuesHolder;
import com.mv.util.vcap.VCAPParser;

public class ValuesHolderLoader {

	private static final Logger log = LoggerFactory.getLogger(ValuesHolderLoader.class);
	
	private  String jdbc_url = "jdbc:mysql://localhost/composetrust?user=root&password=";
	
	private Connection conn = null;
	
	private java.sql.PreparedStatement ps = null;
	
	private  String sqlString = "select max(value), t.name from attribute a, attributetype t"
			+ " where t.id = a.typeid"
			+ " group by t.name";
	
	public ValuesHolderLoader() {
		try {
			if (VCAPParser.parseVcap_Services() != null) {
				System.out.println("trust service - vcap service env exists");
				String username = VCAPParser.obtainDBServiceUsername(VCAPParser.parseVcap_Services());
				String password = VCAPParser.obtainDBServicePassword(VCAPParser.parseVcap_Services());
				jdbc_url = VCAPParser.obtainDBServiceJDBCURI(VCAPParser.parseVcap_Services()) + "?user=" + username
						+ "&password=" + password;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("trust service aims to connect to: " + jdbc_url);
	}
	
	public  ValuesHolder loadValues() {
		ValuesHolder v = new ValuesHolder();
		ResultSet rs = null;
		try {
			rs = executeSelect();
			while (rs.next()) {
				Integer maxVal = rs.getInt(1);
				String attributeName = rs.getString(2);
				v.setValue(Const.MAX+attributeName, maxVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				rs.close();
				ps.close();
//				getConnection().close();
			} catch (Exception e) {
				//
			}
		}
	
		return v;
	}
	
	private Connection  getConnection() throws Exception{
		if (conn == null || conn.isClosed() == true || conn.isValid(0) == false){
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
			 conn = DriverManager.getConnection(jdbc_url);
		}
		return conn;
	}
	
	private ResultSet executeSelect() throws Exception{
		if (ps == null || ps.isClosed()){ 
			ps = getConnection().prepareStatement(sqlString);
		}
		return ps.executeQuery();
	}

}
