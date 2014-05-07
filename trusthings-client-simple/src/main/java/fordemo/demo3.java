package fordemo;
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
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.inn.common.OrderType;
import com.inn.trusthings.json.MakePOJO;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.service.interfaces.TrustManager;
import com.inn.util.tuple.Tuple2;


public class demo3 {
	
	public void runExample(){
		
		try {
			//create trust manager
			TrustManager trustManager =  Factory.createInstance(TrustManager.class);
			//load (from json file) and set trust criteria 
			InputStream is = demo3.class.getResourceAsStream("/criteria/demo/criteria_sc_c.json");
			String	criteria = CharStreams.toString(new InputStreamReader(is));
			trustManager.setGlobalTrustCriteria(criteria);
			is.close();
			
			//add some descriptions (trust profiles)
			URI service_a = URI.create("http://localhost/services/Wind_Sensor_A");			
			URI service_b = URI.create("http://localhost/services/Wind_Sensor_B");
			URI service_c = URI.create("http://localhost/services/Wind_Sensor_C");
			List<URI> list = Lists.newArrayList();
			list.add(service_a);
			list.add(service_b);
			list.add(service_c);

			TrustCriteria criteriapojo = new MakePOJO().ofTrustCriteria(criteria);
			
			List<Tuple2<URI, Double>> result = trustManager.rankResources(list, criteriapojo, OrderType.DESC);
			for (Tuple2<URI, Double> t : result) {
				System.out.println(t.getT1() + " has trust score "+t.getT2());
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new demo3().runExample();
	}

}

