package com.inn.trusthings.integration.fat;

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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.service.interfaces.TrustSimpleManager;
import com.inn.util.tuple.Tuple2;

/**
 * TrustScorer implements  uk.ac.open.kmi.iserve.discovery.api.ranking.Scorer interface 
 * to support trust scoring for COMPOSE service recommendation
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class TrustScorer implements uk.ac.open.kmi.iserve.discovery.api.ranking.Scorer{
	
	private com.inn.trusthings.service.interfaces.TrustSimpleManager trustManager;
	
	public TrustScorer() {
		trustManager =  Guice.createInjector(new TrustModule()).getInstance(TrustSimpleManager.class);
	}
	
	public TrustScorer(TrustSimpleManager trustManager) {
		this.trustManager = trustManager;
	}
	
	
	@Override
	public Map<URI, Double> apply(Set<URI> arg0) {
		List<URI> resources = Lists.newArrayList(arg0);
		Map<URI, Double> map = Maps.newHashMap();
		try {
			List<Tuple2<URI, Double>>  list = trustManager.obtainTrustIndexes(resources);
			for (Tuple2<URI, Double> t : list) {
				map.put(t.getT1(), t.getT2());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
		return map;
	}
	
	
	@Override
	public Map<URI, Double> apply(Set<URI> arg0, String arg1) {
		trustManager.setGlobalTrustCriteria(arg1);
		return apply(arg0);
	}

//	/**
//	 * returns trust index of the resource identified with serviceId URI
//	 */
//	@Override
//	public Double apply(URI serviceId) {
//		try {
//			return trustManager.obtainTrustIndex(serviceId);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0D;
//		}
//	}

}
