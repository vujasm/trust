package com.inn.testtemp;

/*
 * #%L
 * trusthings-common
 * %%
 * Copyright (C) 2015 COMPOSE project
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


import com.inn.common.Const;

public enum DescriptionsEnum {
	
	
	TSA("http://localhost/services/CITY_TRAFFIC_SERVICE_A", Const.repoModels+"city_traffic_service_A.ttl"),
	TSB("http://localhost/services/CITY_TRAFFIC_SERVICE_B", Const.repoModels+"city_traffic_service_B.ttl"),
	TSC("http://localhost/services/CITY_TRAFFIC_SERVICE_C", Const.repoModels+"city_traffic_service_C.ttl"),
	
	Flickr("http://www.programmableweb.com/api/flickr", Const.repoModels+"api_flickr.ttl"),
	Twitter("http://www.programmableweb.com/api/twitter", Const.repoModels+"api_twitter.ttl"),
	Youtube("http://www.programmableweb.com/api/youtube", Const.repoModels+"api_youtube.ttl"),
	
	;
	
	
	private String uri;
	
	private String location;

	DescriptionsEnum(String uri, String location) {
		this.uri = uri;
	}

	public String getURI() {
		return uri;
	}
	
	public String getLocation() {
		return location;
	}

}
