package com.inn.trusthings.service.interfaces;

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


import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.inn.common.OrderType;
import com.inn.trusthings.kb.KnowledgeBaseManager;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.op.enums.EnumScoreStrategy;
import com.inn.trusthings.service.config.GlobalTrustCriteria;
import com.inn.util.tree.Tree;
import com.inn.util.tuple.Tuple2;


/**
 * Trust Simple Manager Interface. 
 * It exposes methods for getting the trust indexes, trust ranking or filtering for simple services
 * @author markov
 *
 */
public interface TrustSimpleManager extends TrustManager {

	/**
	 * @return KnowledgeBaseManager
	 */
	KnowledgeBaseManager getKnowledgeBaseManager();

	/**
	 * Obtains taxonomy for a given concept
	 * 
	 * @param graphName
	 * @param rootConcept
	 * @return
	 */
	Tree obtainTaxonomy(String graphName, String rootConcept);

	/**
	 * Answers ranking of resources in regards to the trust ranking criteria. To compute the trust indexes and ranking, the default strategy
	 * is used. Resource that does not meet some of particular criterion are not excluded from the ranking. For ranking using other
	 * strategies and control under excluding/including resources that do not meet some of criteria, please @see {@link TrustSimpleManager}
	 * {@link #rankResources(List, TrustCriteria, EnumScoreStrategy, boolean, OrderType)}
	 * 
	 * @param resources A list of resource URI's
	 * @param criteria Trust Ranking criteria as a set of required trust attributes and their values and importance
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @return A ordered list of ranked resources together with their respective trust index
	 */
	List<Tuple2<URI, Double>> rankResources(List<URI> resources, TrustCriteria criteria, OrderType order) throws Exception;

	/**
	 * Answers ranking of resources in regards to the trust ranking criteria.
	 * 
	 * @param resources A list of resources URI's
	 * @param criteria Trust criteria as a set of required trust attributes and their values and importance
	 * @param scoreStrategy A trust index computing and ranking strategy
	 * @param excludeIfAttributeMissing Exclude a resource from ranking in a case it lacks some of requested attributes.
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @return A ordered list of ranked resources together with their respective trust index
	 * @throws Exception
	 */
	List<Tuple2<URI, Double>> rankResources(List<URI> resources, TrustCriteria criteria, EnumScoreStrategy scoreStrategy, boolean excludeIfAttributeMissing,
			OrderType order) throws Exception;

	/**
	 * Filter list of resources in regards to the trust ranking criteria and trust index threshold value. To compute the trust indexes and
	 * ranking, the default strategy is used. Resources that do not meet some of particular criteria are not excluded from the ranking. For
	 * filtering using other strategies and control under excluding/including resource that does not meet some of criterion, please @see
	 * {@link TrustSimpleManager} {@link #filterResources(List, TrustCriteria, EnumScoreStrategy, boolean, boolean, OrderType, Double)}
	 * 
	 * @param resources A list of resources URI's
	 * @param criteria Trust Ranking criteria as a set of required trust attributes and their values and importance
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @param thresholdValue Threshold value
	 * @return A ordered list of filtered resources
	 */
	List<URI> filterResources(List<URI> resources, TrustCriteria criteria, OrderType order, Double thresholdValue) throws Exception;

	/**
	 * Filter list of resources in regards to the trust ranking criteria and trust index threshold value.
	 * 
	 * @param resources A list of resources URI's
	 * @param criteria Trust criteria as a set of required trust attributes and their values and importance
	 * @param scoreStrategy A trust index computing and ranking strategy
	 * @param filterByAttributeMissing Exclude a resources from ranking in a case it lack some of requested attributes.
	 * @param filterByCriteriaNotMet TODO
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @param thresholdValue Threshold value
	 * @return A ordered list of filtered resources
	 * @throws Exception
	 */
	List<URI> filterResources(List<URI> resources, TrustCriteria criteria, EnumScoreStrategy scoreStrategy,
			boolean filterByAttributeMissing, boolean filterByCriteriaNotMet, OrderType order, Double thresholdValue) throws Exception;
	
	/**
	 * 
	 * @param resources resources A list of resources URI's
	 * @param criteria Trust criteria as a set of required trust attributes and their values and importance
	 * @return  A filtered list of filtered resources
	 * @throws Exception
	 */
	public List<URI> filterByCriteriaNotMeet(List<URI> resources, TrustCriteria criteria) throws Exception;

	/**
	 * Answers trust index for a resource with taking into an account user perception of trust (which is expressed using <code>request</code>
	 * )
	 * 
	 * @param resource Resource URI
	 * @param criteria Trust criteria as a set of required trust attributes and their values and importance
	 * @return Computed trust index
	 * @throws Exception
	 */
	Double obtainTrustIndex(URI resource, TrustCriteria criteria) throws Exception;

	/**
	 * Answers trust index for a resource without taking into account user perception of a trust
	 * 
	 * @param resource Resource URI
	 * @return Computed trust index
	 * @throws Exception
	 */
	Double obtainTrustIndex(URI resource) throws Exception;
	
	/**
	 * Answers trust indexes for a resources without taking into account user perception of a trust
	 * @param list
	 * @return
	 */
	List<Tuple2<URI, Double>> obtainTrustIndexes(List<URI> list) throws Exception;
	
	/** 
	 * Answers if two resources can engage into trustworthy relation, 
	 * considering their respective trust requirements and capabilities.
	 * @param resource1URI URI of the resource that request communication with resource <code>resource2URI</code>
	 * @param resource2URI URI of the resource requested
	 * @return true if trustworthy relation
	 * @throws Exception
	 */
	boolean match (URI resource1URI, URI resource2URI) throws Exception;
	

	//TODO consider adding //	 * @param useCache - true if cached trust index should be returned instead of re-computing it
	/**
	 * Answers if resource is trusted in terms of given trust criteria
	 * @param resource Resource URI
	 * @param criteria Trust criteria as a set of required trust attributes and their values and importance
	 * @return true if resource is trusted; otherwise false
	 * @throws Exception
	 */
	boolean isTrusted(URI resourceURI, TrustCriteria criteria) throws Exception;
	
	
	/**
	 * Answers if resource is trusted in terms of global trust criteria
	 * @param resource Resource URI
	 * @return true if resource is trusted; otherwise false
	 * @throws Exception
	 */
	boolean isTrusted(URI resourceURI) throws Exception;
	
	
	/**
	 * 
	 * @param resources
	 * @return
	 * @throws Exception
	 */
	List<URI> filterTrustedByThreshold(List<URI> resources) throws Exception;
	
	
	/**
	 * 
	 * @param resources
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	List<URI> filterTrustedByThreshold(List<URI> resources, TrustCriteria criteria) throws Exception;
	

	/**
	 * Add a resource description from input stream in a resource descriptions base. Please note this method does persist the description.
	 * @param resourceURI Resource URI / identifier
	 * @param inputStream an input stream of a resource description data (e.g. file input stream, byte input stream, string input stream)
	 */
	void addResourceDescription(URI resourceURI, InputStream inputStream);
	
	/**
	 * Add a resource description from a file in a resource descriptions base. Please note this method does persist the description.
	 * @param resourceURI Resource URI / identifier
	 * @param file a file containing resource description
	 */
	void addResourceDescription(URI resourceURI, File file);
	
	
	public List<Model> obtainModels(List<URI> resources, boolean logRequest) ;
	
	public List<Tuple2<URI, Model>> obtainModelsListTuple(List<URI> resources, boolean logRequest);
	
	public List<Model> castListModels(List<Tuple2<URI, Model>> list) ;
	

}
