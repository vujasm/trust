package com.inn.util.httpclient;

/*
 * #%L
 * trusthings-common
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



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class Client {

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public JsonNode getJsonAsJsonNode(String uri) {

		final String output = getJSONReponse(uri);
		ObjectMapper m = new ObjectMapper();
		JsonNode node = null;
		try {
			node = m.readTree(output);
			// to parse -> node.get("indexes").get(0).get("rank"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed JSON parsing: error code : " + e.getMessage());
		}
		return node;
	}

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public String getJSONReponse(String uri) {

		//FIXME pass username/password as parameters
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create();
		client.addFilter(new HTTPBasicAuthFilter("username", "password"));
		WebResource webResource = client
				.resource(uri);
		ClientResponse response = webResource.header("Content-Type", "application/json;charset=UTF-8").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.getEntity(String.class);
		return output;
	}

}
