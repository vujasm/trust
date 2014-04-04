package com.inn.trusthings.kb;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

import org.apache.jena.riot.adapters.AdapterFileManager;
import org.apache.jena.riot.stream.StreamManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.inn.trusthings.kb.config.LocationMapping;

public class RDFModelsHandler {

	boolean useCache = true;
	Map<String, OntModel> modelCache = Maps.newConcurrentMap();
	
	private static RDFModelsHandler globalInstance  = null;
	//private static final Logger log = LoggerFactory.getLogger(RDFModelsHandler.class);

	private JsonNode modelmap = null;
	
	private RDFModelsHandler() {

		try {
			InputStream is = this.getClass().getResourceAsStream("/modelrepo/modelmap.json");
			String s = CharStreams.toString(new InputStreamReader(is));
			ObjectMapper m = new ObjectMapper();
			modelmap = m.readTree(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized OntModel fetch(URI uri, String syntax, OntModelSpec modelSpec) {
		return fetch(uri.toASCIIString(), syntax, modelSpec);
	}

	/**
	 * 
	 * @param url
	 * @param syntax
	 * @param modelSpec
	 * @return
	 */
	public synchronized OntModel fetch(String filenameOrURI, String syntax, OntModelSpec modelSpec) {
		
		if (hasCachedModel(filenameOrURI))
			return getFromCache(filenameOrURI);
		
		StreamManager streamManager = StreamManager.makeDefaultStreamManager();
		streamManager.setLocationMapper(LocationMapping.obtainLocationMapper());
		Model m = new AdapterFileManager(streamManager).loadModel(filenameOrURI);
		OntModel model = ModelFactory.createOntologyModel(modelSpec, m);
		
		if (isCachingModels())
			addCacheModel(filenameOrURI, model);

		
		return model;
	}
	
	
	public synchronized OntModel fetchDescriptionFromFileSystem(String uri, String syntax, OntModelSpec modelSpec) {
		JsonNode node =  modelmap.get(uri);
		if (node == null)
			return null;
		InputStream is = getClass().getResourceAsStream("/modelrepo/"+node.textValue());
		return fetch(uri, is, modelSpec);
		
	}

	/**
	 * 
	 * @param url
	 * @param syntax
	 * @param modelSpec
	 * @return
	 */
	public synchronized OntModel fetch(String uri, InputStream inputStream, OntModelSpec modelSpec) {

		if (hasCachedModel(uri))
			return getFromCache(uri);

		OntModel m = ModelFactory.createOntologyModel(modelSpec);
		m.read(inputStream, null, "TURTLE");

		if (isCachingModels())
			addCacheModel(uri, m);

		return m;
	}

	private void addCacheModel(String filenameOrURI, OntModel m) {
		modelCache.put(filenameOrURI, m);
	}

	public OntModel getFromCache(String filenameOrURI) {
		return modelCache.get(filenameOrURI);
	}

	public boolean hasCachedModel(String filenameOrURI) {
		return modelCache.containsKey(filenameOrURI);
	}

	public boolean isCachingModels() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	

	public static RDFModelsHandler getGlobalInstance() {
		 if(globalInstance == null) {
			 globalInstance = new RDFModelsHandler();
	      }
	      return globalInstance;
	}

}
