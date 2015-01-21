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
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.google.common.io.CharStreams;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

public class FeedUtil {
	
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(FeedUtil.class);
	
	public static String toPrettyString(SyndFeed feed) throws Exception {
		List<SyndEntry> list = feed.getEntries();
		StringBuffer result = new StringBuffer();
		for (SyndEntry entry : list) {
			result.append(entry.getUri()).append("\n");
		}
		return result.toString();
	}
	
	
	public static String toHTMLString(SyndFeed feed){
//		<a href="url">link text</a>
		List<SyndEntry> list = feed.getEntries();
		StringBuffer result = new StringBuffer();
		for (SyndEntry entry : list) {
			String score = obtainScore(entry);
			if (score.isEmpty()){
				String s = "<a href="+entry.getUri()+">"+entry.getTitle()+"</a>";
				result.append(s).append("<br>");
			}
			else{
					String s = "<a href="+entry.getUri()+">"+entry.getTitle()+" Ranking Score:"+score+"</a>";
					result.append(s).append("<br>");
				}
		}
//		System.out.println(result.toString());
		return result.toString();
		
	}
	
	public static SyndFeed syndFeedForUrlGET(String url) throws Exception {

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
//			System.out.println(feed.toString());

		} catch (Exception e){
			log.error("Exception occured when building the feed object out of the url", e);
			throw e;
		} finally {
			if( is != null)	is.close();
		}

		return feed; 
	}
	
	
	public static SyndFeed syndFeedForUrlPOST(String url, String requestBody) throws Exception {

		SyndFeed feed = null;		
		InputStream is = null;

		try {

//		    com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create();
//		    WebResource webResource = client.resource(url);
//		    String body = requestBody;
//		    ClientResponse response = webResource.accept("application/atom+xml").type("application/json")
//		    		.post(ClientResponse.class, body);
//			if (response.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//			}
//			String output = response.getEntity(String.class);
			
			
			javax.ws.rs.client.Client client = ClientBuilder.newClient();
//			WebTarget webTarget = 
			Response response = client.target(url).request().accept(MediaType.APPLICATION_ATOM_XML_TYPE)
					.post(Entity.entity(requestBody, MediaType.APPLICATION_JSON), Response.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String output = response.readEntity(String.class);
			
			InputSource source = new InputSource(new StringReader(output));
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
	
	private static String obtainScore(SyndEntry entry) {
		List<SyndContentImpl> contents = entry.getContents();
		for (SyndContentImpl object : contents) {
			String s = object.getValue();
			int p1 = s.indexOf("<rankingScore>")+"<rankingScore>".length();
			int p2 = s.indexOf("</rankingScore>");
			if (p1>0 && p2>0)
				return s.substring(p1, p2);
		}
		return "";
	}
	
	public static void main(String[] args) {
		try {
			
			InputStream is = DemoTrust.class.getResourceAsStream("/jsontest.json");
			String	requestBody = null;
			try {
				requestBody = CharStreams.toString(new InputStreamReader(is));
				System.out.println(requestBody);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			SyndFeed feed = FeedUtil.syndFeedForUrlPOST("http://iserve.kmi.open.ac.uk:80/iserve/discovery/", requestBody);
			SyndFeed feed = FeedUtil.syndFeedForUrlPOST("http://abiell.pc.ac.upc.edu:9081/iserve/discovery/", requestBody);
			List<SyndEntry> list = feed.getEntries();
			System.out.println(list.size());
			for (SyndEntry entry : list) {
				System.out.println(entry.getUri()+ " "+obtainScore(entry));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	

}
