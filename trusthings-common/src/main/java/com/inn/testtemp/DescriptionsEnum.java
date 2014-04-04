package com.inn.testtemp;

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
