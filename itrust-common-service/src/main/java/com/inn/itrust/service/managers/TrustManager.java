package com.inn.itrust.service.managers;

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

import com.inn.common.OrderType;
import com.inn.common.structure.tree.Tree;
import com.inn.itrust.model.model.TrustRequest;
import com.inn.itrust.service.enums.EnumScoreStrategy;
import com.inn.itrust.service.component.Component;


/**
 * TODO describe me
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public interface TrustManager extends Component {

	/**
	 * retrives TrustAttribute taxonomy from data store/
	 * 
	 * @return
	 */
	String listTrustParameters();

	// /**
	// * retrives all named graphs from the Trust triple store
	// *
	// * @return
	// */
	// Set<URI> getLoadedModels();

	/**
	 * puts an ontology in the Trust triple store
	 * 
	 * @param ontologyUri
	 * @param graphName
	 */
	void uploadOntology(URI ontologyUri, String graphName);

	/**
	 * @return KnowledgeBaseManager
	 */
	KnowledgeBaseManager getKnowledgeBaseManager();

	/**
	 * removes ontology from the Trust triple store
	 * 
	 * @param graphName
	 */
	void removeOntology(String graphName);

	/**
	 * returns taxonomy for a given concept
	 * 
	 * @param graphName
	 * @param rootConcept
	 * @return
	 */
	Tree computeTaxonomy(String graphName, String rootConcept);

	/**
	 * Answers ranking of resources in regards to the trust ranking criteria. To compute the trust indexes and ranking, the default strategy
	 * is used. Resource that does not meet some of particular criterion are not excluded from the ranking. For ranking using other
	 * strategies and control under excluding/including resources that do not meet some of criteria, please @see {@link TrustManager}
	 * {@link #rankResources(List, TrustRequest, EnumScoreStrategy, boolean, OrderType)}
	 * 
	 * @param resources A list of resource URI's
	 * @param request Trust Ranking criteria as a set of required trust attributes and their values and importance
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @return A ordered list of ranked resources
	 */
	List<URI> rankResources(List<URI> resources, TrustRequest request, OrderType order) throws Exception;

	/**
	 * Answers ranking of resources in regards to the trust ranking criteria.
	 * 
	 * @param resources A list of resources URI's
	 * @param request Trust criteria as a set of required trust attributes and their values and importance
	 * @param scoreStrategy A trust index computing and ranking strategy
	 * @param excludeIfAttributeMissing Exclude a resource from ranking in a case it lacks some of requested attributes.
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @return A ordered list of ranked resources.
	 * @throws Exception
	 */
	List<URI> rankResources(List<URI> resources, TrustRequest request, EnumScoreStrategy scoreStrategy, boolean excludeIfAttributeMissing,
			OrderType order) throws Exception;

	/**
	 * Filter list of resources in regards to the trust ranking criteria and trust index threshold value. To compute the trust indexes and
	 * ranking, the default strategy is used. Resources that do not meet some of particular criteria are not excluded from the ranking. For
	 * filtering using other strategies and control under excluding/including resource that does not meet some of criterion, please @see
	 * {@link TrustManager} {@link #filterResources(List, TrustRequest, EnumScoreStrategy, boolean, OrderType, Double)}
	 * 
	 * @param resources A list of resources URI's
	 * @param request Trust Ranking criteria as a set of required trust attributes and their values and importance
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @param thresholdValue Threshold value
	 * @return A ordered list of filtered resources
	 */
	List<URI> filterResources(List<URI> resources, TrustRequest request, OrderType order, Double thresholdValue) throws Exception;

	/**
	 * Filter list of resources in regards to the trust ranking criteria and trust index threshold value.
	 * 
	 * @param resources A list of resources URI's
	 * @param request Trust criteria as a set of required trust attributes and their values and importance
	 * @param scoreStrategy A trust index computing and ranking strategy
	 * @param excludeIfAttributeMissing Exclude a resources from ranking in a case it lack some of requested attributes.
	 * @param order OrderType ASC for ascending. DESC for descending.
	 * @param thresholdValue Threshold value
	 * @return A ordered list of filtered resources
	 * @throws Exception
	 */
	List<URI> filterResources(List<URI> resources, TrustRequest request, EnumScoreStrategy scoreStrategy,
			boolean excludeIfAttributeMissing, OrderType order, Double thresholdValue) throws Exception;

	/**
	 * Answers trust index for a resource with taking into an account user perception of trust (which is expressed using <code>request</code>
	 * )
	 * 
	 * @param resource Resource URI
	 * @param request Trust criteria as a set of required trust attributes and their values and importance
	 * @return Computed trust index
	 * @throws Exception
	 */
	Double obtainTrustIndex(URI resource, TrustRequest request) throws Exception;

	/**
	 * Answers trust index for a resource without taking into account user perception of a trust
	 * 
	 * @param resource Resource URI
	 * @return Computed trust index
	 * @throws Exception
	 */
	Double obtainTrustIndex(URI resource) throws Exception;

}
