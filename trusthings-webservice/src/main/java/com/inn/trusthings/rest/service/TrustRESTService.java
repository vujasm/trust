package com.inn.trusthings.rest.service;

/*
 * #%L
 * trusthings-webservice
 * %%
 * Copyright (C) 2014 - 2015 INNOVA S.p.A
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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.inn.common.CompositeServiceWrapper;
import com.inn.common.CompositionIdentifier;
import com.inn.common.OrderType;
import com.inn.trusthings.json.ProduceJSON;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.op.enums.EnumScoreStrategy;
import com.inn.trusthings.rest.exception.TrustRestException;
import com.inn.trusthings.rest.util.RequestJSONUtil;
import com.inn.trusthings.service.interfaces.TrustCompositionManager;
import com.inn.trusthings.service.interfaces.TrustSimpleManager;
import com.inn.util.tuple.Tuple2;

@Path("/trust")
public class TrustRESTService {
	

	@GET
	@Path("/hello")
	@Produces("text/plain")
	public String getHello() {
		return "This is COMPOSE Trust Score/Filter API";
	}

	@POST
	@Path("/score")
	@Consumes(MediaType.APPLICATION_JSON)
	public String scoring(final String request) {
		try {
					
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> list = RequestJSONUtil.getResourceList(request);
			EnumScoreStrategy strategy = RequestJSONUtil.getScoreStrategy(request);
			List<Tuple2<URI, Double>> result = null;
			if (strategy == EnumScoreStrategy.TOPSIS) {
				result = trustManager.rankResources(list, criteria, EnumScoreStrategy.TOPSIS, false,OrderType.DESC);
			} else {
				trustManager.setGlobalTrustCriteria(criteria);
				result = trustManager.obtainTrustIndexes(list);
			}
			return new ProduceJSON().ofRankingResult(result);
		} catch (Exception e) {
			 e.printStackTrace();
			 throw new TrustRestException(new ProduceJSON().ofError(e));
		}
	}

	@POST
	@Path("/filter/threshold")
	@Consumes(MediaType.APPLICATION_JSON)
	public String filteringThreshold(final String request) {
		try {
			List<URI> filtered ;
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> resources = RequestJSONUtil.getResourceList(request);
			filtered = trustManager.filterTrustedByThreshold(resources, criteria);
			return new ProduceJSON().ofFilteringResult(filtered);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TrustRestException(new ProduceJSON().ofError(e));
		}
	}
	
	@POST
	@Path("/filter/exclusion")
	@Consumes(MediaType.APPLICATION_JSON)
	public String filteringExclusion(final String request) {
		try {
			List<URI> filtered ;
			final TrustSimpleManager trustManager = Factory.createInstance(TrustSimpleManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<URI> resources = RequestJSONUtil.getResourceList(request);
			filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
			return new ProduceJSON().ofFilteringResult(filtered);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TrustRestException(new ProduceJSON().ofError(e));
		}
	}
	
	@POST
	@Path("/filter/composite/threshold")
	@Consumes(MediaType.APPLICATION_JSON)
	public String filteringCompositionThreshold(final String request) {
		try {
			final TrustCompositionManager trustManager = Factory.createInstance(TrustCompositionManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			String level = RequestJSONUtil.getLevelFromJsonComposite(criteria);
			String strategy = RequestJSONUtil.getStrategyFromJsonComposite(criteria);
			final List<CompositeServiceWrapper> compositeServiceList = RequestJSONUtil.getCompositeServiceWrapperList(request);
			List<CompositionIdentifier> filtered = trustManager.filterTrustedByThreshold(compositeServiceList, criteria, level, strategy);
			return new ProduceJSON().ofFilteringCompositionsResult(filtered);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TrustRestException(new ProduceJSON().ofError(e));
		}
	}
	
	@POST
	@Path("/scoring/composite")
	@Consumes(MediaType.APPLICATION_JSON)
	public String scoringCompositions(final String request) {
		try {
			TrustCompositionManager trustManager = Factory.createInstance(TrustCompositionManager.class);
			TrustCriteria criteria = RequestJSONUtil.getCriteria(request);
			if (criteria == null) 
				criteria = trustManager.getGlobalTrustCriteria();
			final List<CompositeServiceWrapper> compositeServiceList = RequestJSONUtil.getCompositeServiceWrapperList(request);
			trustManager.setGlobalTrustCriteria(criteria);
			String level = RequestJSONUtil.getLevelFromJsonComposite(criteria);
			String strategy = RequestJSONUtil.getStrategyFromJsonComposite(criteria);
			List<Tuple2<CompositionIdentifier, Double>> scored = trustManager.obtainTrustIndexes(compositeServiceList, criteria, level, strategy);
			return new ProduceJSON().ofRankingCompositionsResult(scored);
		} catch (Exception e) {
			 e.printStackTrace();
			 throw new TrustRestException(new ProduceJSON().ofError(e));
		}
	}
}
