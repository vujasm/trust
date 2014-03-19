
package com.inn.itrust.common.service;

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

public interface KnowledgeBaseManager extends Component {

    /**
     * Checks whether the model has already been uploaded
     *
     * @param modelUri URI of the model to be checked
     * @return true if the model has already been uploaded, false otherwise.
     */
    public boolean containsModel(URI modelUri);

    /**
     * Obtains a set with all the models loaded into this Knowledge Base Manager
     *
     * @return the set of loaded models
     */
    public Set<URI> getLoadedModels();

    /**
     * Obtains a set with all the models that have not been reachable at some point. These are models that should be
     * loaded but could not possibly due to the model being temporarily unavailable of the link being broken.
     *
     * @return the set of unreachable models
     */
    public Set<URI> getUnreachableModels();

    
    public void uploadModel(URI modelUri, Model model, boolean forceUpdate, boolean isOntology);
    
    
    public void uploadOntology(String modelName, Model model, boolean forceUpdate);

    /**
     * Deletes a model from the Knowledge Base Manager
     * <p/>
     * After successfully deleting a model, implementations of this method should raise a {@code OntologyDeletedEvent}
     *
     * @param modelUri the URI of the model to remove
     * @return true if it was correctly deleted, false otherwise.
     */
    public boolean deleteModel(URI modelUri);

    /**
     * Answers a List all of URIs of the classes that are known to be equivalent to this class. Equivalence may be
     * asserted in the model (using, for example, owl:equivalentClass, or may be inferred by the reasoner attached to
     * the model. Note that the OWL semantics entails that every class is equivalent to itself, so when using a
     * reasoning model clients should expect that this class will appear as a member of its own equivalent classes.
     *
     * @param classUri the URI of the class for which to list the equivalent classes
     * @return the List of equivalent classes
     */
    public Set<URI> listEquivalentClasses(URI classUri);

    /**
     * Answers a List of all the URIs of the classes that are declared to be sub-classes of this class.
     *
     * @param classUri the URI of the class for which to list the subclasses.
     * @param direct   if true only the direct subclasses will be listed.
     * @return the list of subclasses
     */
    public Set<URI> listSubClasses(URI classUri, boolean direct);

    /**
     * Answers a List of all the URIs of the classes that are declared to be super-classes of this class.
     *
     * @param classUri the URI of the class for which to list the super-classes.
     * @param direct   if true only the direct super-classes will be listed.
     * @return the list of super-classes
     */
    public Set<URI> listSuperClasses(URI classUri, boolean direct);

    /**
     * List all concepts managed by the KnowledgeBaseManager.
     *
     * @param graphID Graph URI for filtering the concepts within the specified graphID, null for all concepts.
     * @return List of concepts in the KnowledgeBaseManager
     */
    public Set<URI> listConcepts(URI graphID);
    
    /**
     * 
     * @param model
     * @return
     */
    public boolean ontoFetchAndStoreImportedOntologies(Model model);
    
    /**
     * 
     * @return
     */
    public SparqlGraphStoreManager getGraphStoreManager();

    /**
     * fetches model thru Jena Model/ModelFactory with OWL_MEM OntModelSpec
     * @param modelUri  A model's URI
     * @return OntModel
     */
	public OntModel getModel(String modelUri);
	
	/**
	 * 
	 * @param modelUri A model's URI
	 * @param spec  OntModelSpec
	 * @return OntModel
	 */
	public OntModel getModel(String modelUri, OntModelSpec spec);
    
    
}
