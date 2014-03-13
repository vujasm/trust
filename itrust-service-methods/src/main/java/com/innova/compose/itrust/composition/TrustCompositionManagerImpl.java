package com.innova.compose.itrust.composition;


/*
 * #%L
 * itrust-methods
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



public class TrustCompositionManagerImpl implements TrustCompositionManager{

//	
//	/**
//	 * 
//	 * @param serviceURI URI of a service that looks to be composed
//	 * @param serviceCandidatesURIs A list of uris of service that matched functionally what serviceURI needs.
//	 * @return
//	 */
//	public List<URI> filterBySecurityRequrment(URI serviceURI, List<URI> serviceCandidatesURIs){
//		
//		List<URI> filtered = new List();
//		
//		ServiceMetadata metaData = obtainServiceMetadata(serviceURI);
//		SecurityPolicy policy = obtainSecurityPolicy(serviceURI);
//		//for each candidate service
//		for (URI serviceCandidateURI : serviceCandidatesURIs) {
//			SecurityPolicy policyOfCandidateService = obtainSecurityPolicy(serviceCandidateURI);
//			ServiceMetadata metaDataOfCandidateService = obtainServiceMetadata(serviceCandidateURI);
//			boolean compliesAB = evaluate (policyOfCandidateService, metadata);
//			boolean compliesBA = evaluate (policy, metaDataOfCandidateService);
//			if (compliesAB && compliesBA){
//				list.add serviceCandidateURI)
//			}
//		}
//		
//		return list;
//		
//	}
//	
//	
//	private ServiceMetadata obtainServiceMetadata(URI serviceCandidateURI) {
//		//TODO this method grabs (collects) metadata about the service
//		//e.g. ownership, isProvenanaceEnabled, inputs / outputs annotations, etc
//		return null;
//	}
//
//
//	private boolean evaluate(SecurityPolicy policy, ServiceMetadata data){
//		List<Rule> rules = policy.getRules;
//		// feed the data and policy rules into the rule engine and execute the rules
//		//if paralocks are open return true, otherwise false
//		return someRule.apply(data, rules)l
//	}
//	
	
}
