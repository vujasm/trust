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
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;

public interface SparqlGraphStoreManager {

    URI getSparqlQueryEndpoint();

    URI getSparqlUpdateEndpoint();

    URI getSparqlServiceEndpoint();

    boolean canBeModified();

    void addModelToGraph(URI graphUri, Model data);

    boolean containsGraph(URI graphUri);

    void deleteGraph(URI graphUri);

    void putGraph(Model data);

    void putGraph(URI graphUri, Model data);

    void clearDataset();

    Set<URI> listStoredGraphs();

    Set<URI> listResourcesByQuery(String queryStr, String variableName);
    
    OntModel getGraph(URI graphUri, OntModelSpec modelSpec) ;

    OntModel getGraph(URI graphUri);
    
    OntModel getGraphSparqlQuery(URI graphUri);
    
}
