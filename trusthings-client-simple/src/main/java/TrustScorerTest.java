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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.google.common.io.CharStreams;
import com.inn.trusthings.integration.TrustFilterByExclusion;
import com.inn.trusthings.integration.TrustScorer;


public class TrustScorerTest {
	
	public void runExample(){
		
		try {
			//load (from json file) and set trust criteria 
			InputStream is = TrustScorerTest.class.getResourceAsStream("/criteria/demo/criteria_sc_c.json");
			String	criteria = CharStreams.toString(new InputStreamReader(is));
			is.close();
			
			URI service_a = URI.create("http://iserve.kmi.open.ac.uk/iserve/id/services/c006937c-2777-44d2-bd0a-7586c00a86ce/facebook");			
			URI service_b = URI.create("http://iserve.kmi.open.ac.uk/iserve/id/services/610b64a2-6cc0-4b5c-9d6e-a619bdf0c18f/twitter");
			Set<URI> services = new HashSet<URI>();
			services.add(service_a);
			services.add(service_b);
			/*
			 * SCORING
			 */
			TrustScorer s = new TrustScorer();
			//obtain and print trust indexes for resources
			Map<URI, Double> scores = s.apply(services, criteria);
			for (URI uri : services) {
				System.out.println(uri +" has trust score "+scores.get(uri));
			}
			
			/*
			 * FILTERING
			 */
			TrustFilterByExclusion f = new TrustFilterByExclusion();
			Set<URI> result = f.apply(services, criteria);
			for (URI uri : services) {
				if (result.contains(uri)){
					System.out.println(uri+ " is trusted");
				}
				else{
					System.out.println(uri+ " is not trusted");
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new TrustScorerTest().runExample();
	}

}
