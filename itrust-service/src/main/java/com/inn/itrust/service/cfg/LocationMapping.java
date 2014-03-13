package com.inn.itrust.service.cfg;

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


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.inn.common.Const;
import com.inn.itrust.model.vocabulary.ModelsNSEnum;

public class LocationMapping {

	public static Builder<String, String> getMapping() {
		
		ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
		put(ModelsNSEnum.Trust, map );
		put(ModelsNSEnum.SecuritypolicyVocab, map);
		put(ModelsNSEnum.UsdlSec, map);
		put(ModelsNSEnum.Ssn, map);
		put(ModelsNSEnum.SecurityProfiles, map);
		
		//FIXME - Remove this stuff out of here
		map.put(Const.Ts1, Const.repositoryLocation+"/modelrepo/city_weatherseason_service.ttl");
		map.put(Const.Ts2, Const.repositoryLocation+"/modelrepo/city_weatherseason_service.ttl");
		//
		map.put(Const.Ts3, Const.repositoryLocation+"/modelrepo/city_traffic_service_A.ttl");
		map.put(Const.Ts4, Const.repositoryLocation+"/modelrepo/city_traffic_service_B.ttl");
		map.put(Const.Ts5, Const.repositoryLocation+"/modelrepo/city_traffic_service_C.ttl");
		
        return map;
	}

	private static void put(ModelsNSEnum v, Builder<String, String> map) {
		map.put(v.getNS(), v.getLocation());
	}

}
