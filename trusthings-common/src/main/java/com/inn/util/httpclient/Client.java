package com.inn.util.httpclient;

/*
 * #%L
 * trusthings-common
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



import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


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

		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature authenticationFeature = HttpAuthenticationFeature.universal("user", "superSecretPassword");
		client.register(authenticationFeature);

		WebTarget webTarget = client.target(uri);
		
		Invocation.Builder invocationBuilder =
				webTarget.property("Content-Type", "application/json;charset=UTF-8").request();
		
		Response response = invocationBuilder.get();
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.readEntity(String.class);
		return output;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public String getRDFReponse(String uri) {

		javax.ws.rs.client.Client client = ClientBuilder.newClient();		
//		String entity = client.target(uri).request().get(String.class);
		WebTarget webTarget = client.target(uri);
		Invocation.Builder invocationBuilder =
				webTarget.property("Content-Type", "application/x-turtle;charset=UTF-8").request();
		Response response = invocationBuilder.get();
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.readEntity(String.class);
		return output;
	}

}
