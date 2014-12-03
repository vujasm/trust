package com.inn.demo.review2014;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.io.CharStreams;
import com.inn.trusthings.integration.TrustFilterByThreshold;
import com.inn.trusthings.integration.TrustScorer;

public class DemoTrust {
	
	public static List<Entry<URI, Double>> sort_map_by_values (Map<URI, Double> map) {
	    
	    Comparator<Map.Entry<URI, Double>> byMapValues = new Comparator<Map.Entry<URI, Double>>() {
	        @Override
	        public int compare(Map.Entry<URI, Double> left, Map.Entry<URI, Double> right) {
	            return right.getValue().compareTo(left.getValue());
	        }
	    };
	    
	    List<Map.Entry<URI, Double>> list = new ArrayList<Map.Entry<URI, Double>>();
	    
	    // add all candy bars
	    list.addAll(map.entrySet());
	    
	    // sort the collection
	    Collections.sort(list, byMapValues);
	    
	    return list;
	}
	
	public static void main(String[] args) {
		
		InputStream is = DemoTrust.class.getResourceAsStream("/criteria/demo/trust_demo_2.json");
		String	criteria = null;
		try {
			criteria = CharStreams.toString(new InputStreamReader(is));
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<URI> set = iServeFreeTextSearch.search("maps");
		TrustFilterByThreshold filter = new TrustFilterByThreshold();
		Set<URI> filtered = filter.apply(set, criteria);
		
		System.out.println(filtered.size());
		
		TrustScorer scorer = new TrustScorer();
		Map<URI, Double> result = scorer.apply(filtered, criteria);
		List<Entry<URI, Double>>  list = sort_map_by_values(result);
		for (Entry<URI, Double> entry : list) {
			System.out.println(entry.getKey() +"  "+entry.getValue());
		}
		
//		for (URI uri : result.keySet()) {
//			System.out.println(uri +" has trust score "+result.get(uri));
//		}
	}

//	http://iserve.kmi.open.ac.uk/iserve/id/services/b8cafe8e-1682-44e4-ac8f-9aba6cded688/yahoo-map-image
//	http://iserve.kmi.open.ac.uk/iserve/id/services/b9f2297e-003f-4d41-b7f5-616183b5de10/yahoo-map-image
	
}
