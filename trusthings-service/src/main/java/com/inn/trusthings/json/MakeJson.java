package com.inn.trusthings.json;

/*
 * #%L
 * trusthings-service
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


import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inn.util.tuple.Tuple2;

public class MakeJson {

	public String ofRankingResult(List<Tuple2<URI, Double>> list) {
			
		ObjectMapper jacksonMapper = new ObjectMapper();
		jacksonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		ObjectNode rootNode = jacksonMapper.createObjectNode();
		rootNode.put("success", "true");
		ArrayNode arrayNode = rootNode.putArray("indexes");
		int i = 1;
		for (Tuple2<URI, Double> t : list) {
			ObjectNode node = jacksonMapper.createObjectNode();
			node.put("serviceUri", t.getT1().toASCIIString());
			node.put("index", t.getT2());
			node.put("rank", i++);
			arrayNode.add(node);
		}
		return rootNode.toString();
	}
	
	
	public String ofError(Exception e) {
		ObjectMapper jacksonMapper = new ObjectMapper();
		jacksonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		ObjectNode rootNode = jacksonMapper.createObjectNode();
		rootNode.put("success", "false");
		rootNode.put("message", e.getLocalizedMessage());
		return rootNode.toString();
	}
	
	public String ofErrorSimpleMessage(String message) {
		ObjectMapper jacksonMapper = new ObjectMapper();
		jacksonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		ObjectNode rootNode = jacksonMapper.createObjectNode();
		rootNode.put("success", "false");
		rootNode.put("message", message);
		return rootNode.toString();
	}
	
	public static void main(String[] args) {
//		JsonObject jo = new JsonObject();
//		JsonArray array = new JsonArray();
//		JsonObject e= new JsonObject();
//		e.putString("resourceUri", "http/something");
//		e.putNumber("index", 5);
//		e.putNumber("rank", 1);
//		array.addElement(e);
//		array.addElement(e);
//		jo.putArray("result", array);
//		System.out.println(jo.encode());
//		System.out.println(jo.encodePrettily());
//		List<Tuple2<URI, Double>> l = Lists.newArrayList();
//		l.add(new Tuple2<URI, Double>(URI.create("http://localhost"), 1D));
//	
//		System.out.println(new MakeJson().ofError(new Exception("tes")));
	}

}
