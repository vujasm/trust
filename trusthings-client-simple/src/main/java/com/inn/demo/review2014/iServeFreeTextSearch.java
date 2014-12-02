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


import java.net.URI;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class iServeFreeTextSearch {
	
	public static Set<URI> search(String text){
		
		Set<URI> set = Sets.newHashSet();
		
		try {
			SyndFeed feed = FeedUtil.getSyndFeedForUrl("http://iserve.kmi.open.ac.uk/iserve/discovery/svc/search?q="+text);
			List<SyndEntry> list = feed.getEntries();
			System.out.println("iserve returned "+list.size());
			for (SyndEntry entry : list) {
				set.add(new URI(entry.getUri()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return set;
	}

}
