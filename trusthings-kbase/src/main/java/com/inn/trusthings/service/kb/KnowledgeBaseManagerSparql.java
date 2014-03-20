/*
 * Copyright (c) 2013. Knowledge Media Institute - The Open University
 *
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
 */

package com.inn.trusthings.service.kb;

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
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.open.kmi.iserve.commons.io.Syntax;
import uk.ac.open.kmi.iserve.commons.io.util.URIUtil;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.inn.trusthings.Configuration;
import com.inn.trusthings.service.kb.mapping.IgnoredModels;
import com.inn.trusthings.service.kb.mapping.LocationMapping;


public class KnowledgeBaseManagerSparql implements KnowledgeBaseManager {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseManagerSparql.class);

    private SparqlGraphStoreManager graphStoreManager;


    @Inject
    KnowledgeBaseManagerSparql(EventBus eventBus, SparqlGraphStoreFactory graphStoreFactory,
    		@Named(Configuration.SPARQL_ENDPOINT_QUERY_PROP) String queryEndpoint,
			@Named(Configuration.SPARQL_ENDPOINT_UPDATE_PROP) String updateEndpoint,
			@Named(Configuration.SPARQL_ENDPOINT_SERVICE_PROP) String serviceEndpoint) throws Exception {

        Set<URI> defaultModels = ImmutableSet.of();
        ImmutableMap.Builder<String, String> locationMappings =  LocationMapping.getMapping();
        Set<String> ignoredImports = IgnoredModels.getModels();
        this.graphStoreManager = graphStoreFactory.create(queryEndpoint, updateEndpoint, serviceEndpoint,
        		defaultModels, locationMappings.build(), ignoredImports);
    }

  
    @Override
    public boolean containsModel(URI modelUri) {
        return this.graphStoreManager.containsGraph(modelUri);
    }

    /**
     * Obtains a set with all the models loaded  into this Knowledge Base Manager
     *
     * @return the set of loaded models
     */
    @Override
    public Set<URI> getLoadedModels() {
        return this.graphStoreManager.listStoredGraphs();
    }


    @Override
    public void uploadModel(URI modelUri, Model model, boolean forceUpdate, boolean isOntology) {
        if (modelUri != null && model != null && (!this.containsModel(modelUri) || forceUpdate)) {
            this.graphStoreManager.putGraph(modelUri, model);
            this.fetchAndStoreImportedModels(model, isOntology);
        }
    }
    
    @Override
    public void uploadOntology(String modelName, Model model, boolean forceUpdate) {
    	uploadModel(URI.create(modelName), model, forceUpdate, true);
    }

    /**
     * Deletes a model from the Knowledge Base Manager
     *
     * @param modelUri the URI of the model to remove
     * @return true if it was correctly deleted, false otherwise.
     */
    @Override
    public boolean deleteModel(URI modelUri) {
        this.graphStoreManager.deleteGraph(modelUri);
        return true;
    }
    

    
    public boolean fetchAndStoreImportedModels(Model model, boolean isOntology) {
        Set<URI> modelUris = null;
        if (isOntology == false) {
        	modelUris = obtainReferencedModelUris(model);
        }
        else{
        	 OntModelSpec spec = SharedOntModelSpec.getModelSpecShared();
             OntModel om = ModelFactory.createOntologyModel(spec, model);
             Set<String> uriLiteral = om.listImportedOntologyURIs();
             modelUris = Sets.newHashSet();
             for (String uriString : uriLiteral) {
            	 modelUris.add(URI.create(uriString));
     		}
        }
        
        for (URI modelUri : modelUris) {
            // Only fetch those that are not there
            if (!this.graphStoreManager.containsGraph(modelUri)) {
            	   Model m = new JenaModelFether().fetch(modelUri.toASCIIString(), Syntax.RDFXML.getName(), SharedOntModelSpec.getModelSpecShared());
                   this.graphStoreManager.putGraph(m);
            }
        }
        return true;
    }
    
    public SparqlGraphStoreManager getGraphStoreManager() {
		return graphStoreManager;
	}
    
    private Set<URI> obtainReferencedModelUris(Model model) {
        Set<URI> result = new HashSet<URI>();
        if (model != null) {
            RDFNode node;
            NodeIterator modelRefs = model.listObjects();
            while (modelRefs.hasNext()) {
                node = modelRefs.next();
                if (!node.isAnon()) {
                    try {
                        result.add(URIUtil.getNameSpace(node.asResource().getURI()));
                      	log.info("KnowledgeBaseManagerSparql obtainReferencedModelUris " + URIUtil.getNameSpace(node.asResource().getURI()));
                    } catch (URISyntaxException e) {
                        log.error("The namespace from the resource is not a correct URI. Skipping node.", e);
                    }
                }
            }
        }

        return result;
    }
    
    
    /**
     * 
     */
    @Override
    public OntModel getModelByJenaModelFetcher(String modelUri) {
    	return getModelByJenaModelFetcher(modelUri, SharedOntModelSpec.getModelSpecShared());
    }
    
    
    @Override
    public OntModel getModelByJenaModelFetcher(String modelUri, OntModelSpec spec) {
    	spec.setDocumentManager(SharedOntModelSpec.getDocumentManagerShared());
    	Model model =  new JenaModelFether().fetch(URI.create(modelUri),"TURTLE", spec);
    	OntModel oModel = ModelFactory.createOntologyModel(spec, model);
    	return oModel;
    }
    
}
