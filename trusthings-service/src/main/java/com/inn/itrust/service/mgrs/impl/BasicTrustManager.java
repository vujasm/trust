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

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.inn.common.OrderType;
import com.inn.itrust.Configuration;
import com.inn.itrust.config.GlobalTrustRequest;
import com.inn.itrust.model.pojo.TrustRequest;
import com.inn.itrust.model.pojo.Value;
import com.inn.itrust.op.enums.EnumScoreStrategy;
import com.inn.itrust.service.collectors.ActivityCollector;
import com.inn.itrust.service.collectors.Collector;
import com.inn.itrust.service.collectors.FeedbackCollector;
import com.inn.itrust.service.collectors.QoSCollector;
import com.inn.itrust.service.collectors.ReputationCollector;
import com.inn.itrust.service.command.CreateUpdateTrustProfile;
import com.inn.itrust.service.command.FillTaxonomy;
import com.inn.itrust.service.command.CommandSemanticMetadataFetch;
import com.inn.itrust.service.interfaces.RankingManager;
import com.inn.itrust.service.interfaces.TrustManager;
import com.inn.itrust.service.kb.KnowledgeBaseManager;
import com.inn.itrust.service.kb.SparqlGraphStoreFactory;
import com.inn.itrust.service.kb.SparqlGraphStoreManager;
import com.inn.itrust.service.kb.mapping.IgnoredModels;
import com.inn.itrust.service.kb.mapping.LocationMapping;
import com.inn.util.tree.Node;
import com.inn.util.tree.Tree;
import com.inn.util.tuple.ListTuple;
import com.inn.util.tuple.ListTupleConvert;
import com.inn.util.tuple.TFunctor;
import com.inn.util.tuple.Tuple2;

/**
 * Implementation of TrustManager interface
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 * 
 */
public class BasicTrustManager implements TrustManager {

	private static final Logger log = LoggerFactory.getLogger(BasicTrustManager.class);
	private final List<Collector> collectors = Lists.newArrayList();
	private boolean doSaveIntoStore = false;
	private final List<SparqlGraphStoreManager> externalGraphStoreMgrs = Lists.newArrayList();
	private final SparqlGraphStoreManager graphStoreManager;
	private final KnowledgeBaseManager kbManager;
	private final RankingManager rankingManager;

	/**
	 * a default strategy for calculating trust index
	 */
	private EnumScoreStrategy globalStrategy = EnumScoreStrategy.Weighted_sum_model;

	@Inject
	public BasicTrustManager(EventBus eventBus, SparqlGraphStoreFactory graphStoreFactory, RankingManager rankingManager, KnowledgeBaseManager kbManager,
			@Named(Configuration.SPARQL_ENDPOINT_QUERY_PROP) String queryEndpoint, @Named(Configuration.SPARQL_ENDPOINT_UPDATE_PROP) String updateEndpoint,
			@Named(Configuration.SPARQL_ENDPOINT_SERVICE_PROP) String serviceEndpoint) throws Exception {

		Set<String> ignoredImports = IgnoredModels.getModels();
		Set<URI> baseModels = ImmutableSet.of();
		ImmutableMap.Builder<String, String> locationMappings = LocationMapping.getMapping();
		this.graphStoreManager = graphStoreFactory.create(queryEndpoint, updateEndpoint, serviceEndpoint, baseModels, locationMappings.build(), ignoredImports);
		this.rankingManager = rankingManager;
		this.kbManager = kbManager;
		registerExternalGraphStoreManagers(externalGraphStoreMgrs, graphStoreFactory);
		registerCollectors();
	}

	@Override
	public Tree obtainTaxonomy(String graphName, String rootConcept) {
		Tree tree = new Tree();
		OntModel model = graphStoreManager.getGraph(URI.create(graphName), OntModelSpec.OWL_MEM_TRANS_INF);
		OntClass ontClass = model.getOntClass(rootConcept);
		Node root = new Node(ontClass.getLocalName());
		tree.setRoot(root);
		FillTaxonomy.execute(ontClass, root);
		return tree;
	}

	/**
	 * For a given resource (identified by uri), retrieves and updates a trust profile. If the trust profile was not
	 * existing for the given resource, this method will create the profile and will collect all profile data (i.e.
	 * trust parameters).
	 * 
	 * @param model
	 * @param uri
	 */
	private OntModel fillTrustProfilForResource(OntModel model, URI uri) {
		return new CreateUpdateTrustProfile().apply(model, uri, collectors);
	}

	/**
	 * 
	 * Loads semantic metadata (semantic annotations) for a resource identified with URI from the registered registers
	 * 
	 * @param uri resource URI
	 * @param fetchFromExternalRegistries true if data has to be retrieved from externally registers; otherwise false
	 * @param useMappedLocations true if mapped location (e.g. local cache) should be used to retrieve data; otherwise
	 *            false
	 * @param fetchFromInternalRegirsty true if data has to be retrieved from internal registry; otherwise false
	 * @return ontModel as a Jena model that contains statements about the resource
	 */
	private OntModel loadSemanticMetadata(URI uri, boolean fetchFromExternalRegistries, boolean useMappedLocations, boolean fetchFromInternalRegirsty) {
		return new CommandSemanticMetadataFetch(graphStoreManager, externalGraphStoreMgrs).apply(uri, fetchFromExternalRegistries, useMappedLocations, fetchFromInternalRegirsty);
	}

	@Override
	public KnowledgeBaseManager getKnowledgeBaseManager() {
		return kbManager;
	}

	@Override
	public String listTrustParameters() {
		//TODO implement me
		return null;
	}

	/**
	 * 
	 */
	@Override
	public List<URI> rankResources(List<URI> resources, TrustRequest trustRequest, OrderType order) throws Exception {
		return rankResources(resources, trustRequest, globalStrategy, true, order);
	}

	/**
	 * 
	 */
	@Override
	public List<URI> rankResources(List<URI> resources, TrustRequest request, EnumScoreStrategy scoreStrategy, boolean excludeIfAttributeMissing, OrderType order) throws Exception {
		final List<Tuple2<URI, Double>> scores = processCall(resources, request, scoreStrategy, excludeIfAttributeMissing, order, false);
		final List<URI> rankedList = ListTupleConvert.toListOfTupleElement(scores, 1);
		return rankedList;
	}

	@Override
	public Double obtainTrustIndex(URI resourceURI) throws Exception {
		final TrustRequest request = newGlobalTrustRequest();
		return obtainTrustIndex(resourceURI, request);
	}

	@Override
	public Double obtainTrustIndex(URI resourceURI, TrustRequest request) throws Exception {
		List<URI> list = Lists.newArrayList();
		list.add(resourceURI);
		final List<Tuple2<URI, Double>> scores = processCall(list, request, globalStrategy, false, OrderType.DESC, false);
		return scores.get(0).getT2();
	}

	@Override
	public List<URI> filterResources(List<URI> resources, TrustRequest request, EnumScoreStrategy scoreStrategy, boolean excludeIfAttributeMissing, OrderType order,
			final Double thresholdValue) throws Exception {
		final List<Tuple2<URI, Double>> scores = processCall(resources, request, scoreStrategy, excludeIfAttributeMissing, order, false);
		Iterable<Tuple2<URI, Double>> filtered = Iterables.filter(scores, new Predicate<Tuple2<URI, Double>>() {
			@Override
			public boolean apply(Tuple2<URI, Double> t) {
				return (Double.valueOf(t.getT2()).compareTo(thresholdValue) >= 0);
			}
		});
		printList(Lists.newArrayList(filtered), " filtered with thresholdValue value of " + thresholdValue);
		final List<URI> filteredList = ListTupleConvert.toListOfTupleElement(Lists.newArrayList(filtered), 1);
		return filteredList;
	}

	private void printList(List<Tuple2<URI, Double>> set, String note) {
		log.info("******** <" + note + "> ************");
		for (Tuple2<URI, Double> t : set) {
			log.info(t.getT1() + " score " + t.getT2());
		}
		log.info("******** </" + note + "> ************");
	}

	@Override
	public List<URI> filterResources(List<URI> resources, TrustRequest request, OrderType order, Double thresholdValue) throws Exception {
		return filterResources(resources, request, globalStrategy, true, order, thresholdValue);
	}

	/**
	 * Registration of trust information collectors. Typically, the collector grabs data from some source and transforms
	 * data into rdf model.
	 */
	private void registerCollectors() {
		collectors.add(new ReputationCollector());
		collectors.add(new FeedbackCollector());
		collectors.add(new ActivityCollector());
		collectors.add(new QoSCollector());
	}

	/**
	 * Registration of external repositories where resource descriptions might be found
	 */
	private void registerExternalGraphStoreManagers(List<SparqlGraphStoreManager> list, SparqlGraphStoreFactory factory) {
		Set<String> ignoredImports = IgnoredModels.getModels();
		Set<URI> baseModels = ImmutableSet.of();
		ImmutableMap.Builder<String, String> locationMappings = LocationMapping.getMapping();
		SparqlGraphStoreManager manager = factory.create(Configuration.EXT_SPARQL_ENDPOINT_QUERY, Configuration.EXT_SPARQL_ENDPOINT_UPDATE,
				Configuration.EXT_SPARQL_ENDPOINT_SERVICE, baseModels, locationMappings.build(), ignoredImports);
		list.add(manager);
	}

	protected void saveIntoTripleStore(URI uri, Model model) {
		if (doSaveIntoStore)
			graphStoreManager.putGraph(uri, model);
	}

	/**
	 * 
	 * @param tupleModels
	 */
	private void storeModelsIntoStore(List<Tuple2<URI, Model>> tupleModels) {
		for (Tuple2<URI, Model> t : tupleModels) {
			saveIntoTripleStore(t.getT1(), t.getT2());
		}
	}

	/**
	 * 
	 * @param resources
	 * @return
	 */
	private List<Tuple2<URI, Model>> obtainModels(List<URI> resources) {
		List<Tuple2<URI, Model>> listModels = Lists.newArrayList();
		for (URI uri : resources) {
			OntModel model = loadSemanticMetadata(uri, false, true, false);
			model = fillTrustProfilForResource(model, uri);
			listModels.add(new Tuple2<URI, Model>(uri, model));
		}
		return listModels;
	}

	/**
	 * 
	 * @param resources
	 * @param request
	 * @param scoreStrategy
	 * @param excludeIfAttributeMissing
	 * @param order
	 * @param logRequest
	 * @return
	 * @throws Exception
	 */
	private List<Tuple2<URI, Double>> processCall(List<URI> resources, TrustRequest request, EnumScoreStrategy scoreStrategy, boolean excludeIfAttributeMissing, OrderType order,
			boolean logRequest) throws Exception {
		final List<Tuple2<URI, Model>> tupleModels = obtainModels(resources);
		if (logRequest) {
			storeModelsIntoStore(tupleModels);
		}
		List<Model> models = ListTuple.toList(tupleModels, new TFunctor<Model>() {
			@Override
			public Model apply(Tuple2<?, ?> t) {
				return (Model) t.getT2();
			}
		});
		return rankingManager.rankServiceModels(models, request, scoreStrategy, excludeIfAttributeMissing, order);
	}

	@Override
	public boolean isTrusted(URI resourceURI, TrustRequest request, boolean useCache) throws Exception {
		final Double index = obtainTrustIndex(resourceURI, request);
		return new Value(index).isTrustworthy();
	}

	@Override
	public boolean isTrusted(URI resourceURI) throws Exception {
		final Double index = obtainTrustIndex(resourceURI);
		return new Value(index).isTrustworthy();
	}

	@Override
	public boolean match(URI resource1uri, URI resource2uri) throws Exception {
		// TODO Implement S-S match
		// get profiles and trust criteria and do vice-versa matching
		return false;
	}

	/**
	 * Obtains absolute trust request (users' perception of trust is not taken into account)
	 * 
	 * @return
	 */
	private TrustRequest newGlobalTrustRequest() {
		// TODO Define AbsoluteTrustRequest. It should be identified somehow, perhaps thru some survey. For now, we use
		// a test one.
		return GlobalTrustRequest.request();
	}

}
