package com.inn.trusthings.integration;

/*
 * #%L
 * plugin-recommender
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


public abstract class TrustClientHTTPLite {
	
//	private static String restServiceHostName ="localhost";
//	private static String restServicePort ="9998";
	
	private static String restServiceHostName ="trusthings.147.83.30.133.xip.io";
	private static String restServicePort ="80";
	
	protected String obtainEndpointBase(){
		
		String host  = System.getProperty("iserve.filter.trust.host", restServiceHostName);
		String port = System.getProperty("iserve.filter.trust.port", restServicePort);
		return "http://"+host+":"+port;
		
//		return "http://"+restServiceHostName+":"+restServicePort+"/trusthings-webservice-1.0.1.v20012015";

	}

}
