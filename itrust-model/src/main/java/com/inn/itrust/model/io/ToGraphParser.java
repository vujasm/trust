package com.inn.itrust.model.io;

/*
 * #%L
 * itrust-model
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


import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.inn.itrust.model.model.Agent;
import com.inn.itrust.model.model.TResource;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.model.TrustProfile;
import com.inn.itrust.model.vocabulary.NSPrefixes;
import com.inn.itrust.model.vocabulary.Trust;

//TODO check if this class implemented 
public class ToGraphParser {

	private static final Logger log = LoggerFactory.getLogger(ToGraphParser.class);

	public ToGraphParser() {

	}
	
	public Model parse(Agent  agent){
		log.debug("transforming "+agent.getUri().toASCIIString()+" into Triples");
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefixes(NSPrefixes.map);
		model.add(agent.asJenaResource(), RDF.type, Trust.Agent);
		addProfile(agent, model);
		return model;
	}

	private void addProfile(Agent agent, Model model) {
		TrustProfile profile = agent.getHasTrustProfile();
		if (profile != null){
			model.add(profile.asJenaResource(), RDF.type, Trust.TrustProfile);
			model.add(agent.asJenaResource(), Trust.hasTrustProfile, profile.asJenaResource());
			addParameters(profile, model);
		}	
	}

	private void addParameters(TrustProfile profile, Model model) {
		Collection<TrustAttribute> list = profile.getAttributes();
		for (TrustAttribute attribute : list) {
			//u type stavljamo type parametra. ovo se desava pri kolektovanju podataka
			List<TResource> types = attribute.getTypesAll();
			for (TResource type : types) {
				model.add(attribute.asJenaResource(), RDF.type, ResourceFactory.createResource(type.getUri().toASCIIString()));
			}
			
			model.add(profile.asJenaResource(),Trust.hasAttribute, attribute.asJenaResource());
		}
	}

}
