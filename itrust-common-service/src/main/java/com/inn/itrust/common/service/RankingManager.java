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
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.inn.common.OrderType;
import com.inn.common.Tuple2;
import com.inn.itrust.common.EnumScoreStrategy;
import com.inn.itrust.model.model.TrustProfile;


/**
 * 
 *@author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
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
	
	public List<Tuple2<URI, Double>> rankServiceModels(List<Model> models, TrustProfile trustProfileRequired, EnumScoreStrategy strategy,
			 boolean excludeIfAttributeMissing, OrderType order) throws Exception ;
	
//	/**
//	 * how close a service matches given criteria
//	 * @param service
//	 * @param trustProfileRequired trust requirements    
//	 * @param useCache if caching is enabled, should cached trust index be returned instead of computing the trust index
//	 * @param updateCache should cached trust index be updated
//	 * @return trust index value [0..1]
//	 */
//	public double computeTrustIndex(URI service, TrustProfile trustProfileRequired, boolean useCache, boolean updateCache);
//	
//	/**
//	 * how close a service matches given criteria
//	 * @param trustProfile a set of trust attributes that will be matched with trust criteria expressed as trustProfileRequired
//	 * @param trustProfileRequired trust requirements    
//	 * @param useCache if caching is enabled, should cached trust index be returned instead of computing the trust index
//	 * @param updateCache should cached trust index be updated
//	 * @return trust index value [0..1]
//	 */
//	public double computeTrustIndex(TrustProfile trustProfile, TrustProfile trustProfileRequired, boolean useCache, boolean updateCache);
	
	
	
}

