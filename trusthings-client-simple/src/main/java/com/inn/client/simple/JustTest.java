package com.inn.client.simple;

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


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import com.google.common.io.CharStreams;
import com.inn.trusthings.integration.fat.TrustFilterByExclusion;
import com.inn.trusthings.json.TrustPOJOFactory;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.service.interfaces.TrustSimpleManager;

public class JustTest {
	
	public static void main(String[] args) {

			try {
				
				URI youtube = URI.create("http://www.programmableweb.com/api/youtube");
				String loc = "C://D-Data//Git//itrust//trusthings-kbase//src//main//resources//modelrepo//";
				TrustSimpleManager trustManager =  Factory.createInstance(TrustSimpleManager.class);
//				FileInputStream is = new FileInputStream(loc+"api_youtube.ttl");
//				trustManager.addResourceDescription(youtube, is);
				
				InputStream is = TrustPOJOFactory.class.getResourceAsStream("/criteria/criteria1.json");
				String	string = CharStreams.toString(new InputStreamReader(is));
				trustManager.setGlobalTrustCriteria(string);
				TrustFilterByExclusion s = new TrustFilterByExclusion(trustManager);
//				System.out.println(s.apply(URI.create("http://localhost/services/CITY_TRAFFIC_SERVICE_A")));
				
//				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/youtube")));
//				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/twitter")));
//				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/flickr")));
//				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/lowreputation")));
//				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/nosecurity")));
//				
				
//				
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}

