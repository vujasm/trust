package com.inn.trusthings.service.collectors;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.inn.trusthings.model.factory.TrustModelFactory;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.uti.httpclient.Client;
import com.inn.util.uri.UIDGenerator;

public class ReputationCollector extends AbstractCollector{

	public ReputationCollector(String sourceUri) {
		super(sourceUri);
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		Resource  r = new Agent(URI.create(resourceIdentifier)).asJenaResource();
		Double reputationIndex = obtainReputationIndex(URI.create(resourceIdentifier));
		if (reputationIndex != null){
			OntModel model = ModelFactory.createOntologyModel();
			TrustModelFactory trm = new TrustModelFactory(UIDGenerator.instanceTrust);
			TrustAttribute attribute = trm.createTrustAttibute();
			attribute.addType(URI.create(Trust.Reputation.getURI()));
			attribute.setValue(reputationIndex);
			attribute.setValueDatatype(XSDDouble.XSDdouble);
			model.add(r,Trust.hasAttribute, attribute.asJenaResource());
			return model;
		}
		else{
			return null;	
		}
	}

	private double obtainReputationIndex(URI uri) {
		
		Client client =  new Client();
		JsonNode node = client.getJsonAsJsonNode(getSourceUri()+"/srvcid"+uri.toASCIIString());
		//TODO process obtained reputation bundle
		return 0;
	}

	@Override
	public String getName() {
		return "reputation";
	}
	
}
