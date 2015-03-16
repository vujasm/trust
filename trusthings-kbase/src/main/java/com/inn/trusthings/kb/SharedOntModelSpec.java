package com.inn.trusthings.kb;

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


import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.inn.trusthings.kb.config.IgnoredModels;
import com.inn.trusthings.kb.config.LocationMapping;

public class SharedOntModelSpec {
	
	
	private static OntModelSpec modelSpecShared ;
	
	public static OntModelSpec getModelSpecShared() {
		if (modelSpecShared == null) {
	        ImmutableMap.Builder<String, String> locationMappings =  LocationMapping.getMapping();
	        Set<String> ignoredImports = IgnoredModels.getModels();
			modelSpecShared = createModelSpecification(locationMappings.build(), ignoredImports);
		}
		return modelSpecShared;
	}
	
	
	 
	 /**
     * Creates and sets up a the OntModelSpec to be used when parsing models
     * In particular, ignored imports and location mapping / redirecting.
     *
     * @param locationMappings
     * @param ignoredImports
     * @return
     */
    public static OntModelSpec createModelSpecification(Map<String, String> locationMappings, Set<String> ignoredImports) {

        OntDocumentManager documentManager = new OntDocumentManager();


        for (Map.Entry<String, String> mapping : locationMappings.entrySet()) {
            documentManager.addAltEntry(mapping.getKey(), mapping.getValue());
        }

        // Ignore the imports indicated
        for (String ignoreUri : ignoredImports) {
            documentManager.addIgnoreImport(ignoreUri);
        }

        // follow imports for now..
        documentManager.setProcessImports(false);

        OntModelSpec ontModelSpec =  new OntModelSpec(OntModelSpec.OWL_MEM);
        ontModelSpec.setDocumentManager(documentManager);
        return ontModelSpec;
    }



	@SuppressWarnings("unused")
	private static OntModelSpec getModelSpecShared(Map<String, String> locationMappings, Set<String> ignoredImports) {
		if (modelSpecShared == null) {
			modelSpecShared = createModelSpecification(locationMappings, ignoredImports);
		}
		return modelSpecShared;
		
	}
	
	public static OntDocumentManager getDocumentManagerShared(){
		if (modelSpecShared == null){
			getModelSpecShared();
		}
		return modelSpecShared.getDocumentManager();
	}


}
