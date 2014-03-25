package com.inn.uti.httpclient;


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

		String output = getJSONReponse(uri);
		System.out.println(output);
		ObjectMapper m = new ObjectMapper();
		JsonNode node = null;
		try {
			node = m.readTree(output);

			// System.out.println(node.get("indexes").get(0).get("rank"));

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
