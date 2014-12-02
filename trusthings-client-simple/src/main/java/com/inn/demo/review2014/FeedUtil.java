package com.inn.demo.review2014;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

public class FeedUtil {
	
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(FeedUtil.class);
	
	public static SyndFeed getSyndFeedForUrl(String url) throws Exception {

		SyndFeed feed = null;		
		InputStream is = null;

		try {

			URLConnection openConnection = new URL(url).openConnection();			
			is = new URL(url).openConnection().getInputStream();
			if("gzip".equals(openConnection.getContentEncoding())){
				is = new GZIPInputStream(is);
			}			
			InputSource source = new InputSource(is);			
			SyndFeedInput input = new SyndFeedInput();
			feed = input.build(source);

		} catch (Exception e){
			e.printStackTrace();
			log.error("Exception occured when building the feed object out of the url", e);				
		} finally {
			if( is != null)	is.close();
		}

		return feed; 
	}
	
	public static void main(String[] args) {
		try {
			SyndFeed feed = FeedUtil.getSyndFeedForUrl("http://iserve.kmi.open.ac.uk/iserve/discovery/svc/search?q=maps");
			List<SyndEntry> list = feed.getEntries();
			System.out.println(list.size());
			for (SyndEntry entry : list) {
				System.out.println(entry.getUri());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
