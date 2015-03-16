package com.inn.trusthings.service.command;

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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.inn.trusthings.collector.Collector;
import com.inn.trusthings.kb.SharedOntModelSpec;
import com.inn.trusthings.model.factory.TrustModelFactory;
import com.inn.trusthings.model.io.ToGraphParser;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.util.uri.UIDGenerator;


/**
 * A CreateUpdateTrustProfile command responsible for creation/update of trust profile object
 * for a given resource identified with URI
 * 
 *@author markov
 *
 */
public class CreateUpdateTrustProfile {
	
	 private static final Logger log = LoggerFactory.getLogger(CreateUpdateTrustProfile.class);
	
	public CreateUpdateTrustProfile(){
		
	}
	
	/**
	 * For a given resource (identified by uri), retrieve and update a trust
	 * profile. If trust profile was not existing for the given resource,
	 * this method will create one. Trust profile data are collected by invoking
	 * collectors.
	 * 
	 * @param model as a rdf graph that either contains trust profile resource or has to get one
	 * @param uri resource that needs trust profile
	 * @param collectors a list of trust information collectors
	 * @return model having trust profile data
	 */
	public OntModel apply(OntModel model, URI uri, List<Model> collectedDataList){
		
		if (model.contains(null, Trust.hasProfile)) {
			log.info("Profile for " + uri.toASCIIString() + " exists and has been found");
		} else {
			log.info("No profile exists for " + uri.toASCIIString() + ". Creating the profile.");
			Agent service = new Agent(uri);
			TrustModelFactory trm = new TrustModelFactory(UIDGenerator.instanceTrust);
			service.setHasTrustProfile(trm.createTrustProfile());
			OntModel m = new ToGraphParser().parse(service);
			model = ModelFactory.createOntologyModel(SharedOntModelSpec.getModelSpecShared(), model.union(m));
		}
		for (Model collectedData : collectedDataList) {
			if (collectedData != null) {
				model = (OntModel) model.union(collectedData);
			}
		}
		return model;
	}
}
