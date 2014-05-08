package com.inn.gui.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;

public class ResourceReader {
	
	
	public static String getTrustDescr(String agentid) {
		String	result=  "resource not found"; 
		try {
			InputStream is = ResourceReader.class.getResourceAsStream("/modelrepo/modelmap");
			String s = CharStreams.toString(new InputStreamReader(is));
			ObjectMapper m = new ObjectMapper();
			JsonNode modelmap = m.readTree(s);
			JsonNode node =  modelmap.get(agentid);
			is.close();
			InputStream is2 =  ResourceReader.class.getResourceAsStream("/"+node.textValue());
			 result =  CharStreams.toString(new InputStreamReader(is2));
			is2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static String getResourceAsStringForClasspath(String resourcePathOnClassPath) {
		String	result=  "resource not found";
		InputStream is = null;
	try {
		is = ResourceReader.class.getResourceAsStream(resourcePathOnClassPath);
		result = CharStreams.toString(new InputStreamReader(is));
		is.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	finally{
	}
	return result;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getResourceAsStringForClasspath("/criteria/demo/criteria_sc_a.json"));
	}
}

