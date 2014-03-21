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


import java.net.URI;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import com.inn.trusthings.integration.TrustScorer;

public class JustTest {
	
	public static void main(String[] args) {
//		System.out.println(SemSim.class.getClassLoader().getResource("ontologies/usdl-sec.ttl").toString());
		try {
//			String encodedUrl = URLEncoder.encode(Const.Ts4, "UTF-8");
//			System.out.println(encodedUrl);
			try {
				BasicConfigurator.resetConfiguration();
				BasicConfigurator.configure();
				org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
				TrustScorer s = new TrustScorer();
				s.apply(URI.create("http://www.programmableweb.com/api/youtubes"));
				
//				System.out.println(s.apply(URI.create("http://127.0.0.1/services/1.1/city_traffic_service_a.owls#CITY_TRAFFIC_SERVICE_F")));
//				System.out.println(s.apply(URI.create("http://127.0.0.1/services/1.1/city_traffic_service_a.owls#CITY_TRAFFIC_SERVICE_D")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
