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


import java.net.URI;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.inn.common.OrderType;
import com.inn.trusthings.model.expression.SingleElement;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.model.pojo.TrustProfile;
import com.inn.trusthings.op.enums.EnumScoreStrategy;
import com.inn.util.tuple.Tuple2;


/**
 * 
 *@author markov
 *
 */
public interface RankingManager {
	
	
	
	/**
	 * Ranking of Services by given TrustProfileRequired
	 * @param models 
	 * @param trustProfileRequired
	 * @param strategy 
	 * @param order OrderType ASC / DESC
	 * @return
	 */	
	
	public List<Tuple2<URI, Double>> rankServiceModels(List<Model> models, TrustCriteria trustProfileRequired, EnumScoreStrategy strategy,
			 boolean excludeIfAttributeMissing, boolean filterByCriteriaNotMet, OrderType order) throws Exception ;
	
	/**
	 * 	Prepares data set. It may exclude agents that have no requested attribute if filterByAttributeMissing true, \
	 * or evaluate as zero if rigorous is true and attributed evaluated lower than expected 
	 * @param models
	 * @param trustProfileRequired
	 * @param filterByAttributeMissing
	 * @param rigorous
	 * @return
	 */
	public List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> prepareDataset(List<Model> models, List<SingleElement> listCriteria, boolean filterByAttributeMissing, boolean rigorous) ;
	
}

