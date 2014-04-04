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

import uk.ac.open.kmi.sense.evaluation.Scorer;

import com.google.inject.Guice;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.service.interfaces.TrustManager;

/**
 * TrustScorer implements uk.ac.open.kmi.sense.evaluation.Scorer interface 
 * to obtain trust score for COMPOSE service recommendation
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class TrustScorer implements Scorer{
	
	private com.inn.trusthings.service.interfaces.TrustManager trustManager;
	
	public TrustScorer() {
		trustManager =  Guice.createInjector(new TrustModule()).getInstance(TrustManager.class);
	}
	
	public TrustScorer(TrustManager trustManager) {
		this.trustManager = trustManager;
	}

	/**
	 * returns trust index of the resource identified with serviceId URI
	 */
	@Override
	public Double apply(URI serviceId) {
		try {
			return trustManager.obtainTrustIndex(serviceId);
		} catch (Exception e) {
			e.printStackTrace();
			return 0D;
		}
	}

}
