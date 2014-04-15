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

import com.google.common.io.CharStreams;
import com.inn.trusthings.integration.TrustFilterByExclusion;
import com.inn.trusthings.integration.TrustScorer;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.service.interfaces.TrustManager;


public class TrustEngineExample {
	
	public void runExample(){
		
		try {
			//create trust manager
			TrustManager trustManager =  Factory.createInstance(TrustManager.class);
			//load (from json file) and set trust criteria 
			InputStream is = TrustEngineExample.class.getResourceAsStream("/criteria/criteria1.json");
			String	criteria = CharStreams.toString(new InputStreamReader(is));
			trustManager.setGlobalTrustCriteria(criteria);
			is.close();
			
			//add some descriptions (trust profiles)
			URI resource1Id = URI.create("http://localhost/services/foo_1");
			URI resource2Id = URI.create("http://localhost/services/foo_2");
			InputStream r1is = TrustEngineExample.class.getResourceAsStream("/modelrepo/foo_1.ttl");
			trustManager.addResourceDescription(resource1Id,r1is);
			InputStream r2is = TrustEngineExample.class.getResourceAsStream("/modelrepo/foo_2.ttl");
			trustManager.addResourceDescription(resource2Id, r2is);
			r2is.close();
			
			/*
			 * SCORING
			 */
			//create trust scorer and pass trustManager
			TrustScorer s = new TrustScorer(trustManager);
			//obtain and print trust indexes for resources
			System.out.println(resource1Id.toASCIIString()+" has trust index value:"+s.apply(resource1Id));
			System.out.println(resource2Id.toASCIIString()+" has trust index value:"+s.apply(resource2Id));
			
			/*
			 * FILTERING
			 */
			//create trust filer to filter out those not trusted
			TrustFilterByExclusion f = new TrustFilterByExclusion(trustManager);
			//obtain trust indexes for resources
			System.out.println(resource1Id.toASCIIString()+" is trusted = "+f.apply(resource1Id));
			System.out.println(resource2Id.toASCIIString()+" is trusted = "+f.apply(resource2Id));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new TrustEngineExample().runExample();
	}

}

