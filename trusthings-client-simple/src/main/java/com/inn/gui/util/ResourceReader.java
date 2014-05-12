package com.inn.gui.util;

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

