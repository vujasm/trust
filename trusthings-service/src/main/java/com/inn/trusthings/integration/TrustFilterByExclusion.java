package com.inn.trusthings.integration;

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
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.inn.trusthings.json.TrustPOJOFactory;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.service.interfaces.TrustManager;


/**
 * TrustFilterByExclusion implements iServe Recommender Filter interface 
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class TrustFilterByExclusion implements uk.ac.open.kmi.iserve.discovery.api.ranking.Filter {
	
	private com.inn.trusthings.service.interfaces.TrustManager trustManager; 
	public TrustFilterByExclusion() {
		trustManager = Factory.createInstance(TrustManager.class);
	}
	
	public TrustFilterByExclusion(TrustManager trustManager) {
		this.trustManager = trustManager;
	}

	@Override
	public Set<URI> apply(Set<URI> arg0) {
		List<URI> resources = Lists.newArrayList(arg0);
		TrustCriteria criteria = trustManager.getGlobalTrustCriteria();
		List<URI> filtered ;
		try {
			filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return Sets.newHashSet(filtered);
	}
	

	@Override
	public Set<URI> apply(Set<URI> arg0, String arg1) {
		List<URI> resources = Lists.newArrayList(arg0);
		TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(arg1);
		List<URI> filtered ;
		try {
			filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return Sets.newHashSet(filtered);
	}
	
	
	
//	public boolean apply(URI serviceId) {
//		boolean inList = false;
//		try {
//			List<URI> resources = Lists.newArrayList();
//			resources.add(serviceId);
//			TrustCriteria criteria = trustManager.getGlobalTrustCriteria();
//			List<URI> filtered = trustManager.filterByCriteriaNotMeet(resources, criteria);
//			for (URI uri : filtered) {
//				if (uri.compareTo(serviceId)==0){
//					inList = true;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return inList;
//	}

}
