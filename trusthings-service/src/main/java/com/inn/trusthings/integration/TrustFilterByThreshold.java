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

import com.google.inject.Guice;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.service.interfaces.TrustManager;

import uk.ac.open.kmi.sense.evaluation.Filter;

/**
 * TrustFilterByThreshold implements uk.ac.open.kmi.sense.evaluation.Filter interface 
 * for Guava-based filtering of services in COMPOSE Service Recommender
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class TrustFilterByThreshold implements Filter {
	
	private com.inn.trusthings.service.interfaces.TrustManager trustManager; 
	public TrustFilterByThreshold() {
		trustManager =  Guice.createInjector(new TrustModule()).getInstance(TrustManager.class);
	}
	
	public TrustFilterByThreshold(TrustManager trustManager) {
		this.trustManager = trustManager;
	}

	
	/**
	 * returns true if resource identified with serviceId URI is evaluated as trusted in respect to the trust threshold value
	 */
	@Override
	public boolean apply(URI serviceId) {
		try {
			return trustManager.isTrusted(serviceId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
