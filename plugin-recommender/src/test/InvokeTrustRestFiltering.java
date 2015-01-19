/*
 * #%L
 * plugin-recommender
 * %%
 * Copyright (C) 2014 - 2015 INNOVA S.p.A
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
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.inn.trusthings.integration.TrustFilterByExclusion;
import com.inn.trusthings.integration.TrustFilterByThreshold;
import com.inn.trusthings.integration.TrustScorer;
import com.inn.trusthings.integration.util.RequestBody;


public class InvokeTrustRestFiltering {
	
	public static void main(String[] args) {
		InputStream is = RequestBody.class.getResourceAsStream("/parameters.json");
		String	criteria = null;
		try {
			Set<URI> set = Sets.newHashSet();
			set.add(new URI("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/d905ea82-ae6d-4eb1-9719-0b801e5c758b/google-maps"));
			set.add(new URI("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/bfef4357-0da5-47d7-9beb-54dd95919179/microsoft-bing-maps"));
			criteria = CharStreams.toString(new InputStreamReader(is));
			is.close();
			if (new TrustFilterByExclusion().apply(set, criteria).isEmpty() == false){
				System.out.println("TrustFilterByExclusion OK");
			}
			if (new TrustFilterByThreshold().apply(set, criteria).isEmpty() == false){
				System.out.println("TrustFilterByThreshold OK");
			}
			if (new TrustScorer().apply(set, criteria).isEmpty() == false){
				System.out.println("TrustScorer OK");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
