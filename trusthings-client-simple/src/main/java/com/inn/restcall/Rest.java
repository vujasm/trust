package com.inn.restcall;

/*
 * #%L
 * trusthings-client-simple
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

public class Rest {

	public static void main(String[] args) {
		String s = new com.inn.uti.httpclient.Client()
				.getJSONReponse("http://localhost:8888/trusthings?srvcid=http%3A%2F%2F127.0.0.1%2Fservices%2F1.1%2Fcity_traffic_service_a.owls%23CITY_TRAFFIC_SERVICE_A");
		System.out.println(s);

	}
}
