package com.inn.trusthings.integration;

/*
 * #%L
 * trusthings-service
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


import java.net.URI;
import java.util.Set;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jersey.repackaged.com.google.common.collect.Sets;

import org.glassfish.jersey.client.ClientProperties;

import com.inn.trusthings.integration.util.ConvertTo;
import com.inn.trusthings.integration.util.RequestBody;



/**
 * TrustFilterByThreshold implements implements iServe Recommender Filter interface 
 * 
 * @author markov
 *
 */
public class TrustFilterByThreshold extends TrustClientHTTPLite implements uk.ac.open.kmi.iserve.discovery.api.ranking.Filter {
	
	
	public TrustFilterByThreshold() {
	}

	
	/**
	 * returns true if resource identified with serviceId URI is evaluated as trusted in respect to the trust threshold value
	 */
	@Override
	public Set<URI> apply(Set<URI> arg0) {
		return apply(arg0, null);
	}
	
	@Override
	public Set<URI> apply(Set<URI> arg0, String arg1) {
		
		if (arg0 == null || arg0.isEmpty())
			return Sets.newHashSet();

		try {
			String requestBody = new RequestBody().createNew(arg0, arg1);
			javax.ws.rs.client.Client client = ClientBuilder.newClient();
			client.property(ClientProperties.CONNECT_TIMEOUT, 0);
			String url = obtainEndpointBase()+"/trust/filter/threshold";
			Response response = client.target(url)
					 .request().accept(MediaType.APPLICATION_JSON)
					 .post(Entity.entity(requestBody, MediaType.APPLICATION_JSON), Response.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String output = response.readEntity(String.class);
			return new ConvertTo().toSet(output);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
