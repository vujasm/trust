package com.inn.trusthings.service.collectors;

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


import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.inn.trusthings.model.vocabulary.NSPrefixes;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.util.tuple.Tuple2;

public class ReputationResponseBodyResolver {
	
	private static String noValueIndicator = "-1";

	public void reslove(Map<URI, Tuple2<String, String>> mapIds, String responseBody, Map<URI, Model> mapModel)
			throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(responseBody);
		for (JsonNode node : rootNode) {
			String id = node.get("entity_id").textValue();
			String type = node.get("entity_type").textValue();
			if (type !=null && type.equals("") == false){
				URI uri = findURI(mapIds, id);
				populateModel((ArrayNode) node.get("attributes"), mapModel.get(uri), uri);
			}
		}
	}

	private Model populateModel(ArrayNode arrayNode, Model model, URI uri) {
		final Resource trustProfile = model.getResource(uri.toASCIIString()).getPropertyResourceValue(Trust.hasProfile);
		for (JsonNode node : arrayNode) {
			String fieldName = node.fieldNames().next();
			if (fieldName.equalsIgnoreCase("final")) {
				String reputation_Value = node.findValue("reputation").asText();
				addAttributeToProfile(trustProfile, model, UUID.randomUUID().toString(), 
						Trust.Reputation.getLocalName(), reputation_Value);
			}
			if (fieldName.equalsIgnoreCase("feedback")) {
				String userRating_Value = node.findValue("value").asText();
				String userRating_TotalCount = node.findValue("total_count").asText();
				addAttributeToProfile(trustProfile, model, UUID.randomUUID().toString(), 
						Trust.UserRating.getLocalName(), userRating_Value);
				addAttributeToProfile(trustProfile, model, UUID.randomUUID().toString(), 
						Trust.UserRatingCount.getLocalName(), userRating_TotalCount);
			}
			if (fieldName.equalsIgnoreCase("popularity")) {
				String popularity_Value = node.findValue("value").asText();
				String popularity_TotalCount = node.findValue("total_count").asText();
				addAttributeToProfile(trustProfile, model, UUID.randomUUID().toString(), 
						Trust.Popularity.getLocalName(), popularity_Value);
				addAttributeToProfile(trustProfile, model, UUID.randomUUID().toString(), 
						Trust.PopularityCount.getLocalName(), popularity_TotalCount);
			}
			if (fieldName.equalsIgnoreCase("activity")) {
				String activity_Value = node.findValue("value").asText();
				String activity_TotalCount = node.findValue("total_count").asText();
				addAttributeToProfile(trustProfile, model, UUID.randomUUID().toString(), 
						Trust.ContractCompliance.getLocalName(), activity_Value);
				addAttributeToProfile(trustProfile, model, UUID.randomUUID().toString(), 
						Trust.ContractComplianceCount.getLocalName(), activity_TotalCount);
			}
		}
//		RDFDataMgr.write(System.out, model, Lang.TURTLE) ;
		return model;
	}

	private URI findURI(Map<URI, Tuple2<String, String>> mapIds, String id) {
		Set<Entry<URI, Tuple2<String, String>>> entries = mapIds.entrySet();
		for (Entry<URI, Tuple2<String, String>> entry : entries) {
			if (entry.getValue().getT1().equalsIgnoreCase(id))
				return entry.getKey();
		}
		return null;
	}
	
	private Resource addAttributeToProfile(Resource profile, Model model, String attributeId, String attributeType, Object value) {
		if (value == null || value.toString().equalsIgnoreCase(noValueIndicator))
			 return null;
		
		Resource attribute = createJenaResource(NSPrefixes.map.get("db"),"attribute",attributeId);
		model.add(attribute, RDF.type, ResourceFactory.createResource(Trust.NS+attributeType));
		if (value!=null){
			model.add(attribute, Trust.hasValue, value.toString(), XSDDatatype.XSDdecimal);
		}
		model.add(profile, Trust.hasAttribute, attribute);
		return attribute;
	}
	
	private Resource createJenaResource(String namespace, String type, String id){
		String uri = namespace+""+type+"/"+id;
		return ResourceFactory.createResource(uri);
	}

}
