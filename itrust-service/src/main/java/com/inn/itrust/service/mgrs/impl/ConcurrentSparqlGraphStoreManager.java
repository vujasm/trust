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

package com.inn.itrust.service.mgrs.impl;

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

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.modify.request.Target;
import com.hp.hpl.jena.sparql.modify.request.UpdateCreate;
import com.hp.hpl.jena.sparql.modify.request.UpdateDrop;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.inn.common.Syntax;
import com.inn.itrust.common.service.SparqlGraphStoreManager;
import com.inn.itrust.model.vocabulary.NSPrefixes;
import com.inn.itrust.service.cfg.Configuration;
import com.inn.itrust.service.utils.MyOntModelSpecFactory;

public class ConcurrentSparqlGraphStoreManager implements SparqlGraphStoreManager {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentSparqlGraphStoreManager.class);

    // Default Models that every Graph Store should have in iServe
    private static final URI RDF_URI = URI.create(RDF.getURI());
    private static final URI RDFS_URI = URI.create(RDFS.getURI());
    private static final URI OWL_URI = URI.create(OWL.NS);
    private static final Set<URI> CORE_MODELS = ImmutableSet.of(RDF_URI, RDFS_URI, OWL_URI);

    private static final String JAVA_PROXY_HOST_PROP = "http.proxyHost";
    private static final String JAVA_PROXY_PORT_PROP = "http.proxyPort";

    // Set backed by a ConcurrentHashMap to avoid race conditions
    private Set<URI> loadedModels;

    // Default model that this Graph Store should have even when it is empty
    // You can see this as default initialisation graphs that should be pre-loaded
    private Set<URI> baseModels;

    // Executor for doing the fetching of remote models
    private ExecutorService executor;

    private DatasetAccessor datasetAccessor;

    private URI sparqlQueryEndpoint;
    // To use SPARQL Update for modification
    private URI sparqlUpdateEndpoint;
    // To use SPARQL HTTP protocol for graph modification
    private URI sparqlServiceEndpoint;

    // Keeps track of currently ongoing fetching tasks
    private Map<URI, Future<Boolean>> fetchingMap;

	private OntModelSpec modelSpec;
	


    static class ProxyConfiguration {
        @Inject(optional = true)
        @Named(Configuration.PROXY_HOST_NAME_PROP)
        private String proxyHost = null;
        @Inject(optional = true)
        @Named(Configuration.PROXY_PORT_PROP)
        private String proxyPort = null;
    }
    
    @Inject
    ConcurrentSparqlGraphStoreManager(
    		@Assisted("sparqlQueryEndpoint") String sparqlQueryEndpoint,
    		@Assisted("sparqlUpdateEndpoint") String sparqlUpdateEndpoint,
    		@Assisted("sparqlServiceEndpoint") String sparqlServiceEndpoint,
            @Assisted("baseModels") Set<URI> baseModels,
            @Assisted("locationMappings") Map<String, String> locationMappings,
            @Assisted("ignoredImports") Set<String> ignoredImports,
            ProxyConfiguration proxyCfg) throws Exception {

        this.loadedModels = Collections.newSetFromMap(new ConcurrentHashMap<URI, Boolean>());
        this.loadedModels.addAll(CORE_MODELS);

        this.baseModels = baseModels;

        this.fetchingMap = new ConcurrentHashMap<URI, Future<Boolean>>();

        // Configure proxy if necessary
        configureProxy(proxyCfg);

        modelSpec =  MyOntModelSpecFactory.createModelSpecification(locationMappings, ignoredImports);

        // set the executor
//        executor = Executors.newFixedThreadPool(NUM_THREADS);
        executor = Executors.newSingleThreadExecutor();

        if (sparqlQueryEndpoint == null) {
            log.error(ConcurrentSparqlGraphStoreManager.class.getSimpleName() + " requires a SPARQL Query endpoint.");
            throw new Exception(ConcurrentSparqlGraphStoreManager.class.getSimpleName() + " requires a SPARQL Query endpoint.");
        }

        if (sparqlUpdateEndpoint == null && sparqlServiceEndpoint == null) {
            log.warn(ConcurrentSparqlGraphStoreManager.class.getSimpleName() + " requires a SPARQL Update endpoint to modify data.");
        }

        try {
            this.sparqlQueryEndpoint = new URI(sparqlQueryEndpoint);
            this.sparqlUpdateEndpoint = new URI(sparqlUpdateEndpoint);
            this.sparqlServiceEndpoint = new URI(sparqlServiceEndpoint);

            if (this.sparqlServiceEndpoint != null) {
                this.datasetAccessor = DatasetAccessorFactory.createHTTP(this.sparqlServiceEndpoint.toASCIIString());
            }
        } catch (URISyntaxException e) {
            log.error("URI error configuring SPARQL Graph Store Manager", e);
        }
    }

    private void configureProxy(ProxyConfiguration proxyCfg) {
        Properties prop = System.getProperties();
        if (proxyCfg != null && proxyCfg.proxyHost != null && proxyCfg.proxyPort != null) {
            log.info("Configuring proxy: Host - {} - Port {} .", proxyCfg.proxyHost, proxyCfg.proxyPort);
            prop.put(JAVA_PROXY_HOST_PROP, proxyCfg.proxyHost);
            prop.put(JAVA_PROXY_PORT_PROP, proxyCfg.proxyPort);
        } else {
            prop.remove(JAVA_PROXY_HOST_PROP);
            prop.remove(JAVA_PROXY_PORT_PROP);
        }
    }

  
    /**
     * This method will be called when the server is initialised.
     * If necessary it should take care of updating any indexes on boot time.
     */
    @Override
    public void initialise() {
    	
        //this.loadedModels.addAll(listStoredGraphs());
        //loadDefaultModels();
        
    }

    /**
     * This method will be called when the server is being shutdown.
     * Ensure a clean shutdown.
     */
    @Override
    public void shutdown() {
        log.info("Shutting down Ontology crawlers.");
        this.executor.shutdown();
        // waiting 2 seconds
        try {
            this.executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for threads to conclude crawling.", e);
        }
    }

    /**
     * Loads the default models for this Graph Store and verifies they were correctly retrieved
     * NOTE: For now models with local mappings cannot be TTL for Jena does not guess the format properly in that case.
     * We need to specify the format while loading in this case.
     *
     * @return True if they were all added correctly. False otherwise.
     */

    @SuppressWarnings("unused")
	private boolean loadDefaultModels() {

        Map<URI, Future<Boolean>> concurrentTasks = new HashMap<URI, Future<Boolean>>();
        for (URI model : baseModels) {
            concurrentTasks.put(model, this.asyncFetchModel(model, Syntax.N3.getName()));
        }

        boolean result = true;
        for (URI modelUri : concurrentTasks.keySet()) {
            Future<Boolean> f = concurrentTasks.get(modelUri);
            try {
                boolean fetched = f.get();
                result = result && fetched;

                if (!fetched) {
                    log.error("Cannot load default model - {} - The software may not behave correctly. ");
                }
            } catch (Exception e) {
                // Mark as invalid
                log.error("There was an error while trying to fetch a remote model - {}", modelUri, e);
                result = false;
            }
        }
        return result;
    }


    /**
     * @return the sparqlQueryEndpoint
     */
    @Override
    public URI getSparqlQueryEndpoint() {
        return this.sparqlQueryEndpoint;
    }

    /**
     * @return the SPARQL update endpoint
     */
    @Override
    public URI getSparqlUpdateEndpoint() {
        return sparqlUpdateEndpoint;
    }

    /**
     * @return the URI of the SPARQL Service
     */
    @Override
    public URI getSparqlServiceEndpoint() {
        return sparqlServiceEndpoint;
    }

    @Override
    public boolean canBeModified() {
        return (sparqlServiceEndpoint != null || sparqlUpdateEndpoint != null);
    }

    /**
     * Add statements to a named model
     *
     * @param graphUri
     * @param data
     */
    @Override
    public void addModelToGraph(URI graphUri, Model data) {
        if (graphUri == null || data == null)
            return;

        // Use HTTP protocol if possible
        if (this.sparqlServiceEndpoint != null) {
            datasetAccessor.add(graphUri.toASCIIString(), data);
        } else {
        	log.warn("sparqlServiceEndpoint is null");
//            this.addModelToGraphSparqlQuery(graphUri, data);
        }
    }


    /**
     * Does the Dataset contain a named graph?
     *
     * @param graphUri
     * @return
     */
    @Override
    public boolean containsGraph(URI graphUri) {

        if (graphUri == null)
            return false;

        return this.loadedModels.contains(graphUri);
    }

    
    public boolean containsGraphSparqlQuery(URI graphUri) {

        if (graphUri == null)
            return true; // Default graph always exists

        StringBuilder queryStr = new StringBuilder("ASK { GRAPH <").append(graphUri).append("> {?s a ?o} } \n");

        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.sparqlService(this.getSparqlQueryEndpoint().toASCIIString(), query);
        try {
            return qexec.execAsk();
        } finally {
            qexec.close();
        }
    }

    /**
     * Delete a named model of a Dataset
     *
     * @param graphUri
     */
    @Override
    public void deleteGraph(URI graphUri) {

        if (graphUri == null || !this.canBeModified())
            return;

        // Use HTTP protocol if possible
//        if (this.sparqlServiceEndpoint != null) {
//            this.datasetAccessor.deleteModel(graphUri.toASCIIString());
//        } else {
            deleteGraphSparqlUpdate(graphUri);
//        }

        this.loadedModels.remove(graphUri);
        log.debug("Graph deleted: {}", graphUri.toASCIIString());
    }

    /**
     * Delete Graph using SPARQL Update
     *
     * @param graphUri
     */
    private void deleteGraphSparqlUpdate(URI graphUri) {
        UpdateRequest request = UpdateFactory.create();
        request.setPrefixMapping(PrefixMapping.Factory.create().setNsPrefixes(NSPrefixes.map));
        request.add(new UpdateDrop(graphUri.toASCIIString()));

        // Use create form for Sesame-based engines. TODO: Generalise and push to config.
        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request, this.getSparqlUpdateEndpoint().toASCIIString());
        processor.execute(); // TODO: anyway to know if things went ok?
    }

    /**
     * Gets the default model of a Dataset
     *
     * @return
     */
    protected OntModel getGraph() {

        // Use HTTP protocol if possible
        if (this.sparqlServiceEndpoint != null) {
            return ModelFactory.createOntologyModel(this.modelSpec, datasetAccessor.getModel());
        } else {
            return this.getGraphSparqlQuery();
        }
    }

    private OntModel getGraphSparqlQuery() {
        return getGraphSparqlQuery(null);
    }

    /**
     * Get a named model of a Dataset
     *
     * @param graphUri
     * @return the Ontology Model
     */
    @Override
    public OntModel getGraph(URI graphUri) {
        log.info("Obtaining graph: {}", graphUri==null ?"graphUri is null":graphUri.toString());
        if (graphUri == null)
            return null;
        if (this.sparqlServiceEndpoint != null) {
            Model model = datasetAccessor.getModel(graphUri.toASCIIString());
            OntModel ontModel =  ModelFactory.createOntologyModel(modelSpec, model);
            return ontModel;
        } else {
        	return this.getGraphSparqlQuery(graphUri);
        }
    }
    
    /**
     * Get a named model of a Dataset thru reasoner
     *
     * @param graphUri
     * @param reasoner
     * @return the Ontology Model
     */
    @Override
    public OntModel getGraph(URI graphUri, OntModelSpec modelSpecification) {
        log.debug("Obtaining graph: {}", graphUri==null ?"graphUri is null":graphUri.toString());
        if (graphUri == null)
            return null;
        Model model = datasetAccessor.getModel(graphUri.toASCIIString());
        modelSpecification.setDocumentManager(this.modelSpec.getDocumentManager());
        OntModel ontModel =  ModelFactory.createOntologyModel(modelSpec, model);
        return ontModel;
    }

    /**
     * Obtains the OntModel within a named graph or the default graph if no graphUri is provided
     *
     * @param graphUri
     * @return
     */
    @Override
    public OntModel getGraphSparqlQuery(URI graphUri) {
    	log.debug("Obtaining graph via sparql query: {}", graphUri==null ?"graphUri is null":graphUri.toString());

        StringBuilder queryStr = new StringBuilder("CONSTRUCT { ?s ?p ?o } \n")
                .append("WHERE {\n");

        // Add named graph if necessary
        if (graphUri != null) {
            queryStr.append("GRAPH <").append(graphUri.toASCIIString()).append("> ");
        }
        queryStr.append("{ ?s ?p ?o } } \n");

        log.debug("Querying graph store: {}", queryStr.toString());

        Query query = QueryFactory.create(queryStr.toString());
        QueryExecution qexec = QueryExecutionFactory.sparqlService(this.getSparqlQueryEndpoint().toASCIIString(), query);
        // Note that we are not using the store specification here to avoid retrieving remote models
        OntModel resultModel = ModelFactory.createOntologyModel();
        try {
            qexec.execConstruct(resultModel);
            return resultModel;
        } finally {
            qexec.close();
        }
    }

    /**
     * Put (create/replace) the default graph of a Dataset
     *
     * @param data
     */
    @Override
    public void putGraph(Model data) {

        if (data == null)
            return;

        // Use HTTP protocol if possible
        if (this.sparqlServiceEndpoint != null) {
            datasetAccessor.putModel(data);
        } else {
            this.putGraphSparqlQuery(data);
        }

    }

    private void putGraphSparqlQuery(Model data) {
        this.putGraphSparqlQuery(null, data);
    }

    /**
     * Put (create/replace) a named graph of a Dataset
     *
     * @param graphUri
     * @param data
     */
    @Override
    public void putGraph(URI graphUri, Model data) {
        if (graphUri == null || data == null)
            return;

        // Use HTTP protocol if possible
        if (this.sparqlServiceEndpoint != null) {
            datasetAccessor.putModel(graphUri.toASCIIString(), data);
        } else {
            this.putGraphSparqlQuery(graphUri, data);
        }

        this.loadedModels.add(graphUri);
        log.info("Graph added to store: {}", graphUri.toASCIIString());
    }

    private void putGraphSparqlQuery(URI graphUri, Model data) {

        UpdateRequest request = UpdateFactory.create();
        request.setPrefixMapping(PrefixMapping.Factory.create().setNsPrefixes(NSPrefixes.map));
        request.add(new UpdateCreate(graphUri.toASCIIString()));
        request.add(generateInsertRequest(graphUri, data));
        log.debug("Sparql Update Query issued: {}", request.toString());

        // Use create form for Sesame-based engines. TODO: Generalise and push to config.
        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request, this.getSparqlUpdateEndpoint().toASCIIString());
        processor.execute(); // TODO: anyway to know if things went ok?
    }

    private String generateInsertRequest(URI graphUri, Model data) {

        StringWriter out = new StringWriter();
        data.write(out, Syntax.TTL.getName());
        StringBuilder updateSB = new StringBuilder();
        updateSB.append("INSERT DATA { \n");
        // If a graph is given use it, otherwise insert in the default graph.
        if (graphUri != null) {
            updateSB.append("GRAPH <").append(graphUri.toASCIIString()).append(">");
        }
        updateSB.append(" { \n");
        updateSB.append(out.getBuffer());
        updateSB.append("}\n");
        updateSB.append("}\n");

        String result = updateSB.toString();

        return result;
    }

    /**
     * Empties the entire Dataset
     */
    @Override
    public void clearDataset() {

        // Deleting via graph store protocol does not work apparently.
        // Use HTTP protocol if possible
//        if (this.sparqlServiceEndpoint != null) {
//            datasetAccessor.deleteDefault();
//        } else {
        clearDatasetSparqlUpdate();
//        }
        initialise();
    }

    private void clearDatasetSparqlUpdate() {
        UpdateRequest request = UpdateFactory.create();
        request.add(new UpdateDrop(Target.ALL));
        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request,
                this.getSparqlUpdateEndpoint().toASCIIString());
        processor.execute(); // TODO: any way to know if things went ok?
        log.debug("Dataset cleared.");
    }

    /**
     * Figure out the models that are already in the Knowledge Base
     *
     * @return the Set of URIs of the models loaded
     */
    @Override
    public Set<URI> listStoredGraphs() {

        StringBuilder strBuilder = new StringBuilder()
                .append("SELECT DISTINCT ?graph WHERE {").append("\n")
                .append("GRAPH ?graph {").append("?s ?p ?o").append(" }").append("\n")
                .append("}").append("\n");
        return listResourcesByQuery(strBuilder.toString(), "graph");
    }

    /**
     * executes queryStr and lists resources placeholded with variableName 
     */
    @Override
    public Set<URI> listResourcesByQuery(String queryStr, String variableName) {

        ImmutableSet.Builder<URI> result = ImmutableSet.builder();
        // If the SPARQL endpoint does not exist return immediately.
        if (this.getSparqlQueryEndpoint() == null) {
            return result.build();
        }
        // Query the engine
        log.debug("Evaluating SPARQL query in Knowledge Base: \n {}", queryStr);
        Query query = QueryFactory.create(queryStr);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(this.getSparqlQueryEndpoint().toASCIIString(), query);
        
      //  System.out.println(this.getSparqlQueryEndpoint().toASCIIString());

        try {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();

            ResultSet qResults = qexec.execSelect();

            stopwatch.stop();
            log.info("Time taken for querying the registry: {}", stopwatch);

            Resource resource;
            URI matchUri;
            // Iterate over the results obtained
            while (qResults.hasNext()) {
                QuerySolution soln = qResults.nextSolution();

                // Get the match URL
                resource = soln.getResource(variableName);

                if (resource != null && resource.isURIResource()) {
                    matchUri = new URI(resource.getURI());
                    result.add(matchUri);
                } else {
                    log.warn("Skipping result as the URL is null");
                    break;
                }
            }
        } catch (URISyntaxException e) {
            log.error("Error obtaining match result. Expected a correct URI", e);
        } finally {
            qexec.close();
        }
        return result.build();
    }

    /**
     * fetches an external RDFXML resource and stores it into dataset.
     */
    @Override
    public boolean fetchAndStore(URI modelUri) {
        return fetchAndStore(modelUri, Syntax.RDFXML.getName());
    }

    
    /**
     * fetches an external resource and stores it into dataset. 
     */
    public boolean fetchAndStore(URI modelUri, String syntax) {
        boolean fetched;
        Future<Boolean> future = asyncFetchModel(modelUri, syntax);

        try {
            fetched = future.get();
            // remove from the fetching map
            this.fetchingMap.remove(modelUri);
            if (!fetched) {
                log.warn("Could not store model - {}", modelUri);
            }

        } catch (InterruptedException e) {
            log.error("The system was interrupted while trying to fetch and store a remote model", e);
            return false;
        } catch (ExecutionException e) {
            log.error("There was an error while trying to fetch and store a remote model", e);
            return false;
        }
        return fetched;
    }

    private Future<Boolean> asyncFetchModel(URI modelUri, String syntax) {

        // Check we are not downloading it already
        if (this.fetchingMap.containsKey(modelUri)) {
            return this.fetchingMap.get(modelUri);
        }

        Callable<Boolean> task = new CrawlCallable(this, modelSpec, modelUri, syntax);
        log.debug("Fetching model - {}", modelUri);
        return this.executor.submit(task);
    }
    
}
