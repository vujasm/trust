package com.inn.trusthings.db;

/*
 * #%L
 * trusthings-kbase
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


import com.hp.hpl.jena.rdf.model.Model;

public abstract class ABridge {

	
	public abstract Model obtainTrustProfile(String serviceId) ;
	
	public synchronized Model obtainTrustProfileFixID(String serviceId){
		return obtainTrustProfile(fixServiceID(serviceId));
	}
	
	private String fixServiceID(String serviceId) {
		String lastPart = serviceId.substring(serviceId.lastIndexOf('/') + 1);
		return "http://www.programmableweb.com/api/"+lastPart;
	}

	public abstract  void stop();
	
}
