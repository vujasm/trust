package com.inn.trusthings.integration.util;

/*
 * #%L
 * plugin-recommender
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

import java.net.URI;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

public class RequestBody {
	

	public String createNew(Set<URI> arg0, String arg1) throws Exception{
		List<URI> resources = Lists.newArrayList(arg0);
		ObjectMapper m = new ObjectMapper();
		ObjectNode rootNode = m.createObjectNode();
		if (arg1 !=null){
			JsonNode criteria = m.readTree(arg1);
			rootNode.put("parameters",criteria);
		}
		
		rootNode.put("strategy", "standard");
		ArrayNode arrayNode = m.createArrayNode();
		for (URI resource : resources) {
			ObjectNode resourceNode = m.createObjectNode();
			resourceNode.put("resourceURI", resource.toASCIIString());
			arrayNode.add(resourceNode);
		}
		rootNode.put("resources",arrayNode);
		return rootNode.toString();
	}

}
