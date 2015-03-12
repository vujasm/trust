package com.inn.trusthings.collector.reputation;

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


import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inn.util.tuple.Tuple2;

public class ReputationAPIRequestBodyBuilder {

	public String build(Collection<Tuple2<String, String> > resources) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ArrayNode entities = mapper.createArrayNode();
		rootNode.put("entities", entities);
		for (Tuple2<String, String> t : resources) {
			ObjectNode entityNode = mapper.createObjectNode();
			entityNode.put("entity_id",t.getT1());
			entityNode.put("entity_type", t.getT2());
			entities.add(entityNode);
		}
		ArrayNode attributes = mapper.createArrayNode();
		rootNode.put("attributes", attributes);
		attributes.add(newAttribute("final", new String[]{"reputation"}, mapper));
		attributes.add(newAttribute("feedback", new String[]{"value", "total_count"}, mapper));
		attributes.add(newAttribute("popularity", new String[]{"value", "total_count"}, mapper));
		attributes.add(newAttribute("activity", new String[]{"value", "total_count"}, mapper));
		String jsonString = rootNode.toString();
		return jsonString;
	}
	
	private JsonNode newAttribute(String type, String[] values, ObjectMapper mapper) {
		ObjectNode attr = mapper.createObjectNode();
		attr.put("reputation_type",type);
		ArrayNode valuesNode = mapper.createArrayNode();
		attr.put("values",valuesNode);
		for (String v : values) {
			valuesNode.add(v);	
		}
//	  "reputation_type": "feedback",
//  "values": [
//      "value","total_count"
//  ]	
		return attr;
	}
	
}
