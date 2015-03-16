package com.inn.gui.valuemodel;

/*
 * #%L
 * trusthings-client-simple
 * %%
 * Copyright (C) 2015 COMPOSE project
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


import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.model.vocabulary.NSPrefixes;
import com.inn.trusthings.model.vocabulary.Trust;

public class ValueBeanToJson {
	
	private static JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
	
	public static JsonNode toJson(ValueBean valueModel){
		
		if (isNullAndEmpty(valueModel.getValue1())
				&& isNullAndEmpty(valueModel.getValue2())
				&& isNullAndEmpty(valueModel.getValue3()))
						{
					return null;
						}
		
		ObjectNode node = nodeFactory.objectNode();
		if (valueModel.getType().equals("Quantified")){
			node.put("type", "http://www.compose-project.eu/ns/web-of-things/trust#"+valueModel.getValue1());
			node.put("value", new String((valueModel.getValue2() == null 
								|| valueModel.getValue2().isEmpty())? "0":valueModel.getValue2()));
			node.put("importance", valueModel.getRelevance());
		}
		
		if (valueModel.getType().equals("Certificate")){
			node.put("type", Trust.CertificateAuthorityAttribute.getURI());
			node.put("importance", valueModel.getRelevance());
			
			ObjectNode nodeValue = nodeFactory.objectNode();
			ArrayNode nodeValueArray = nodeFactory.arrayNode();
			ObjectNode typeNode = nodeFactory.objectNode();
			String provider = valueModel.getValue1();
			typeNode.put("type", NSPrefixes.map.get("compose-sec")+provider);
//			String providerCountry = valueModel.getValue2();
			nodeValueArray.add(typeNode);
			nodeValue.put("certificateauthority", nodeValueArray);
			node.put("value",nodeValue);
		}
		
		
		if (valueModel.getType().equals("Security")){
			node.put("type", Trust.SecurityGuarantee.getURI());
			node.put("importance", valueModel.getRelevance());
			
			ObjectNode nodeValue = nodeFactory.objectNode();
			
		
			if (valueModel.getValue1() !=null && valueModel.getValue1().isEmpty() == false){
				String goal = valueModel.getValue1();
				ObjectNode typeNode = nodeFactory.objectNode();
				typeNode.put("type", NSPrefixes.map.get("usdl-sec")+goal);
				ArrayNode nodeValueArrayGoal = nodeFactory.arrayNode();
				nodeValueArrayGoal.add(typeNode);
				nodeValue.put("securitygoal", nodeValueArrayGoal);
			}
			
			
			if (valueModel.getValue2() !=null && valueModel.getValue2().isEmpty() == false){
				String mechanism = valueModel.getValue2();
				ObjectNode typeNode = nodeFactory.objectNode();
				typeNode.put("type", NSPrefixes.map.get("usdl-sec")+mechanism);
				ArrayNode nodeValueArray = nodeFactory.arrayNode();
				nodeValueArray.add(typeNode);
				nodeValue.put("securitymechanism", nodeValueArray);
			}
			
			
			if (valueModel.getValue3() !=null && valueModel.getValue3().isEmpty() == false){
				String technology = valueModel.getValue3();
				ObjectNode typeNode = nodeFactory.objectNode();
				typeNode.put("type", NSPrefixes.map.get("compose-sec")+technology);
				ArrayNode nodeValueArray = nodeFactory.arrayNode();
				nodeValueArray.add(typeNode);
				nodeValue.put("securitytechnology", nodeValueArray);
			}
			
			node.put("value",nodeValue);
		}
	
		return node;
	}
	
	private static boolean isNullAndEmpty(String val) {	
		return (val == null || val.isEmpty());
	}

	public static JsonNode toJson(List<ValueBean> models){
		ObjectNode mainNode = nodeFactory.objectNode();
		ArrayNode nodes = nodeFactory.arrayNode();
		for (ValueBean valueModel : models) {
			JsonNode node = toJson(valueModel);
			if (node != null)
				nodes.add(node);
		}
		if (nodes.size()!=0){
			mainNode.put("attributes", nodes);
			return mainNode;
		}
		else
			return null;
		
	}

	public static String toPrettyJson(JsonNode json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

}
