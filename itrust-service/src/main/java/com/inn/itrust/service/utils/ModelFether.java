package com.inn.itrust.service.utils;

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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;
import com.inn.common.Syntax;

public class ModelFether {

	public OntModel fetch(URI modelUri, String syntax, OntModelSpec modelSpec) {
	    OntModel model = ModelFactory.createOntologyModel(modelSpec);
        // Fetch the model
        if (syntax != null){
        	model.read(modelUri.toASCIIString(), syntax);
        }
        else{
        	String lang = FileUtils.guessLang(modelUri.toASCIIString());
        	String altMapping = model.getDocumentManager().doAltURLMapping(modelUri.toASCIIString());
        	if (altMapping !=null){
        		
        		lang = FileUtils.guessLang(altMapping);
        	}
        	if (lang == null){
        		lang = Syntax.RDFXML.getName();
        	}
            model.read(modelUri.toASCIIString(), lang);
        }
        return model;
	}

}
