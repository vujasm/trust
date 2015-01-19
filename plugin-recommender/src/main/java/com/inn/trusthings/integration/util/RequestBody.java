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


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;

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
	
	public static void main(String[] args) {
		InputStream is = RequestBody.class.getResourceAsStream("/parameters.json");
		String	criteria = null;
		try {
			Set<URI> set = Sets.newHashSet();
			set.add(new URI("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/d905ea82-ae6d-4eb1-9719-0b801e5c758b/google-maps"));
			set.add(new URI("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/bfef4357-0da5-47d7-9beb-54dd95919179/microsoft-bing-maps"));
			criteria = CharStreams.toString(new InputStreamReader(is));
			System.out.println(new RequestBody().createNew(set, criteria));
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
