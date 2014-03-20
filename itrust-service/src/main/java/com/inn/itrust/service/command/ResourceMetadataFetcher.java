package com.inn.itrust.service.command;

/*
 * #%L
 * itrust-service
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
import java.util.List;

import org.apache.jena.atlas.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.inn.common.Syntax;
import com.inn.itrust.service.kb.ModelFether;
import com.inn.itrust.service.kb.SharedOntModelSpec;
import com.inn.itrust.service.kb.SparqlGraphStoreManager;
import com.inn.itrust.service.mgrs.impl.BasicTrustManager;


/**
 * TODO
 *@author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class ResourceMetadataFetcher {
	
	private final SparqlGraphStoreManager graphStoreManager;
	private final List<SparqlGraphStoreManager>   externalGraphStoreMgrs;
	private static final Logger log = LoggerFactory.getLogger(ResourceMetadataFetcher.class);
	
	public ResourceMetadataFetcher(final SparqlGraphStoreManager graphStoreManager, final List<SparqlGraphStoreManager> externalGraphStoreMgrs) {
		this.graphStoreManager = graphStoreManager;
		this.externalGraphStoreMgrs = externalGraphStoreMgrs;
	}

	
	/**
	 * 
	 * @param uri
	 * @param fetchFromExternalRegistries
	 * @param useMappedLocations
	 * @param fetchFromInternalRegirsty
	 * @return
	 */
	public OntModel apply(URI uri, boolean fetchFromExternalRegistries, boolean useMappedLocations, boolean fetchFromInternalRegirsty) {

		Model externalModel = null;
		// Try to get the service descr from the external registries.
		try {
			if (fetchFromExternalRegistries) {
				log.info("obtaining model from external registries using sparqlEndpoint");
				externalModel = fetchServiceFromExternalRegistry(uri);
			}
		} catch (Exception e) {
			// Not found. It is safe not to handle the exception.
		}
		// try to find it on the web or via location mapping
		if (externalModel == null && useMappedLocations) {
			log.info( "obtaining model from external source using  Jena's embedded support for retrieving models");
			try {
				externalModel = new ModelFether().fetch(uri.toASCIIString(), Syntax.TTL.getName(), SharedOntModelSpec.getModelSpecShared());
			} catch (org.apache.jena.atlas.web.HttpException e) {
				log.info(" There was model retrival failure. Failed to retrive model because "+e.getMessage());
			}
		}
		// try to find it in internal registry

		Model internalModel = null;
		try {
			if (fetchFromInternalRegirsty) {
				Log.info(this, "obtaining model from internal registry using sparqlEndpoint");
				internalModel = graphStoreManager.getGraph(uri);
			}
		} catch (Exception e) {
			if (e instanceof org.apache.jena.atlas.web.HttpException) {
				log.info("internal registry using sparqlEndpoint connection refused - sparqendpoint is not running //"+e.getMessage());
			} else {
				e.printStackTrace();
			}
		}
		Model modelUnion;
		if (internalModel != null) {
			modelUnion = internalModel;
			if (externalModel != null)
				modelUnion = internalModel.union(externalModel);
		} else {
			modelUnion = externalModel;
		}
		OntModel model = ModelFactory.createOntologyModel(SharedOntModelSpec.getModelSpecShared(), modelUnion);

		return model;
	}
	
	/**
	 * goes thru a list of spaqrqlEndpoints and tries to find any metadata for
	 * the service
	 * 
	 * @param uri
	 *            - service URI.
	 * @return
	 */
	private Model fetchServiceFromExternalRegistry(URI uri) {
		Model model = null;
		for (SparqlGraphStoreManager storeManager : externalGraphStoreMgrs) {
			model = storeManager.getGraph(uri);
			if (model.isEmpty() == false) // stop when model is found
				continue;
		}
		return model;
	}


}
