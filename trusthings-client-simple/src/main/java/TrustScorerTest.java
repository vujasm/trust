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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.inn.trusthings.integration.fat.TrustFilterByExclusion;
import com.inn.trusthings.integration.fat.TrustFilterByThreshold;
import com.inn.trusthings.integration.fat.TrustScorer;


public class TrustScorerTest {
	
	public void runExample(){
		
		try {
			//load (from json file) and set trust criteria 
//			InputStream is = TrustScorerTest.class.getResourceAsStream("/integration/trustcriteria1.json");
			InputStream is = TrustScorerTest.class.getResourceAsStream("/criteria/demo/test2ca.json");
			String	criteria = CharStreams.toString(new InputStreamReader(is));
			is.close();
			
//			URI service_a = URI.create("http://iserve.kmi.open.ac.uk/iserve/id/services/c006937c-2777-44d2-bd0a-7586c00a86ce/facebook");			
//			URI service_b = URI.create("http://iserve.kmi.open.ac.uk/iserve/id/services/610b64a2-6cc0-4b5c-9d6e-a619bdf0c18f/twitter");
//			URI service_c = URI.create("http://iserve.kmi.open.ac.uk/iserve/id/services/84bf044f-541e-4a93-886d-36ab4278bfe0/service_instance1");
			
			List<URI> list = Lists.newArrayList();
			try {
				URI service_a = URI.create("http://iserve.kmi.open.ac.uk/iserve/id/services/c006937c-2777-44d2-bd0a-7586c00a86ce/facebook");
				URI service_b = URI.create("http://iserve.kmi.open.ac.uk/iserve/id/services/c006937c-2777-44d2-bd0a-7586c00a86ce/yandex-podpiski");
				list.add(service_a);
				list.add(service_b);
//				list.add(new URI("http:/localhost/service_instance/aaa/service_instance1"));
//				list.add(new URI("http:/localhost/service_instance/aaa/service_instance2"));
//				list.add(new URI("http:/localhost/service_object/aaa/service_object1"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Set<URI> services = new HashSet<URI>(list);
//			services.add(service_a);
//			services.add(service_b);
			/*
			 * SCORING
			 */
			TrustScorer s = new TrustScorer();
			//obtain and print trust indexes for resources
			Map<URI, Double> scores = s.apply(services, criteria);
			for (URI uri : services) {
				System.out.println(uri +" has trust score "+scores.get(uri));
			}
//			
//			/*
//			 * FILTERING
//			 */
//			TrustFilterByThreshold f = new TrustFilterByThreshold();
//			Set<URI> result = f.apply(services, criteria);
//			for (URI uri : services) {
//				if (result.contains(uri)){
//					System.out.println(uri+ " is trusted");
//				}
//				else{
//					System.out.println(uri+ " is not trusted");
//				}
//			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new TrustScorerTest().runExample();
	}

}

