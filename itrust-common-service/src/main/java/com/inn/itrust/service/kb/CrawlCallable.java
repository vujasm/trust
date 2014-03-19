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
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;


public class CrawlCallable implements Callable<Boolean> {

    private static final Logger log = LoggerFactory.getLogger(CrawlCallable.class);

    // The Ont Model Spec to use for obtaining the ontologies
    private final OntModelSpec modelSpec;
    private final String syntax;
    private SparqlGraphStoreManager graphStoreManager;
    private URI modelUri;

    public CrawlCallable(SparqlGraphStoreManager graphStoreManager, OntModelSpec modelSpec, URI modelUri, String syntax) {
        this.graphStoreManager = graphStoreManager;
        this.modelUri = modelUri;
        this.modelSpec = modelSpec;
        this.syntax = syntax;
    }

    /**
     * Given the model uri, this method takes care of retrieving the remote model and any imported documents
     * and uploads them all to the graph store within the same graph. This implementation relies on Jena's embedded
     * support for retrieving ontologies and their imports.
     * <p/>
     * ATTENTION: When deleting models (in cases where several models import a common model) we may remove the
     * first ontology uploaded, with it the imported model will also be deleted and as a result any other model
     * that imported the common one will not have it available any more.
     * TODO: To be fixed
     *
     * @return True if the model was fetched and uploaded or false otherwise
     */
    @Override
    public Boolean call() {

        // If the model has not been uploaded fetch it and upload the graph
        if (!this.graphStoreManager.containsGraph(this.modelUri)) {
            Model model = fetchModel(this.modelUri, this.syntax);
            this.graphStoreManager.putGraph(this.modelUri, model);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    private Model fetchModel(URI modelUri, String syntax) {
    	Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        Model model = new ModelFether().fetch(modelUri,syntax, modelSpec);
        stopwatch.stop();
        log.info("Remote ontology fetched - {} . Time taken: {}", modelUri, stopwatch);
        return model;
    }
}

