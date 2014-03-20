package com.inn.itrust.service.kb;

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

import org.apache.jena.riot.adapters.AdapterFileManager;
import org.apache.jena.riot.stream.StreamManager;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.inn.itrust.service.kb.mapping.LocationMapping;

public class JenaModelFether {

	
	public OntModel fetch(URI uri, String syntax, OntModelSpec modelSpec) {
		return fetch(uri.toASCIIString(), syntax, modelSpec);
	}
	
	/**
	 * 
	 * @param url
	 * @param syntax
	 * @param modelSpec
	 * @return
	 */
	public OntModel fetch(String url, String syntax, OntModelSpec modelSpec) {
		StreamManager streamManager = StreamManager.makeDefaultStreamManager();
		streamManager.setLocationMapper(LocationMapping.obtainLocationMapper());
		Model m = new AdapterFileManager(streamManager, LocationMapping.obtainLocationMapper()).loadModel(url);
	    OntModel model = ModelFactory.createOntologyModel(modelSpec, m);
	    return model;

//        // Fetch the model
//        if (syntax != null){
//        	model.read(url, syntax);
//        }
//        else{
//        	String lang = FileUtils.guessLang(url);
//        	String altMapping = model.getDocumentManager().doAltURLMapping(url);
//        	if (altMapping !=null){
//        		lang = FileUtils.guessLang(altMapping);
//        	}
//        	if (lang == null){
//        		lang = Syntax.RDFXML.getName();
//        	}
//            model.read(url, lang);
//        }
//        return model;
	}

}