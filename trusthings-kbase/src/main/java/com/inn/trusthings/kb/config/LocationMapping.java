package com.inn.trusthings.kb.config;

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


import java.util.Map;

import org.apache.jena.riot.stream.LocationMapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.inn.common.Const;
import com.inn.trusthings.model.vocabulary.ModelEnum;

public class LocationMapping {

	public static Builder<String, String> getMapping() {
		
		ImmutableMap.Builder<String, String> map = ImmutableMap.builder();

		map.put(ModelEnum.Trust.getURI(),  					Const.repoOntologies+"trustontology.ttl");
		map.put(ModelEnum.SecuritypolicyVocab.getURI(),		Const.repoOntologies+"securitypolicyvocab.ttl");
		map.put(ModelEnum.UsdlSec.getURI(),					Const.repoOntologies+"usdl-sec.ttl");
		map.put(ModelEnum.Ssn.getURI(),						Const.repoOntologies+"ssn.owl");
		map.put(ModelEnum.SecurityProfiles.getURI(), 		Const.repoOntologies+"securityprofiles.ttl");
		map.put(ModelEnum.Dul.getURI(), 					"http://www.loa-cnr.it/ontologies/DUL.owl");
		
        return map;
	}
	
	
	public static synchronized String resolveLocation(String modelUri){
		return LocationMapping.getMapping().build().get(modelUri);
	}
	
	public static synchronized LocationMapper obtainLocationMapper(){
		LocationMapper lm = new LocationMapper();
		Map<String, String> locationMappings = LocationMapping.getMapping().build();
		  for (Map.Entry<String, String> mapping : locationMappings.entrySet()) {
	            lm.addAltEntry(mapping.getKey(), mapping.getValue());
	        }

		return lm;
	}

}
