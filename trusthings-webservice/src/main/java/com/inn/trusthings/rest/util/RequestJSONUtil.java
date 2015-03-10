package com.inn.trusthings.rest.util;

/*
 * #%L
 * trusthings-webservice
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


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.inn.common.CompositeServiceWrapper;
import com.inn.trusthings.json.TrustPOJOFactory;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.op.enums.EnumScoreStrategy;

public class RequestJSONUtil {

	public static TrustCriteria getCriteria(String request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		JsonNode parameters = rootNode.get("parameters");
		if (parameters == null || parameters.size() == 0){
			return null;
		}
		return new TrustPOJOFactory().ofTrustCriteria(parameters.toString());
	}

	public static List<URI> getResourceList(String request) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		JsonNode resources = rootNode.path("resources");
		if (resources == null || resources.size() == 0){
			throw new Exception ("No list of resource URIs in a request.");
		}
		List<URI> list = Lists.newArrayList();
		for (JsonNode node : resources) {
			list.add((URI.create(node.get("resourceURI").textValue())));
		}
		return list;
	}
	
	public static EnumScoreStrategy getScoreStrategy(String request) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(request);
		JsonNode strategy = rootNode.path("strategy");
		if (strategy !=null && strategy.textValue().equalsIgnoreCase("topis")){
		  return EnumScoreStrategy.TOPSIS;
		}
		if (strategy !=null && strategy.textValue().equalsIgnoreCase("standard")){
			  return EnumScoreStrategy.Weighted_sum_model;

		}
		return EnumScoreStrategy.Weighted_sum_model;
	}	
	
	public static void main(String[] args) {
	
		String request = "";
		InputStream is = RequestJSONUtil.class.getResourceAsStream("/request.json");
		try {
			request = CharStreams.toString(new InputStreamReader(is));
			is.close();
			System.out.println(RequestJSONUtil.getScoreStrategy(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//FIXME  - implementiraj me
	public static List<CompositeServiceWrapper> getCompositeServiceWrapperList(String request) {
		List<CompositeServiceWrapper> list = Lists.newArrayList();
		
		return list;
	}

	

}
