package com.inn.demo.review2014;

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
