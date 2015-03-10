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


import java.net.URI;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.inn.common.ValuesHolder;

/**
 * 
 * Note: KnowledgeBaseManager is taken from iServe library https://github.com/kmi/iserve. 
 * It has been modified (most of methods from original version are removed bcs not needed at this point). 
 * The idea was to use same store for services and their security/trust NFPs 
 * Depending on further design decision in regards to the store of trust annotations / indexes this class will be kept or removed.  
 *
 */

public interface KnowledgeBaseManager {

    boolean containsModel(URI modelUri);

    boolean deleteModel(URI modelUri);

    SparqlGraphStoreManager getGraphStoreManager();
    
    Set<URI> getLoadedModels();

    OntModel getModel(String modelUri);

    OntModel getModel(String modelUri, OntModelSpec spec);

	void uploadModel(URI modelUri, Model model, boolean forceUpdate, boolean isOntology);

	void uploadOntology(String modelName, Model model, boolean forceUpdate);
}
