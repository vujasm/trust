package com.inn.client.simple;

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


import java.io.FileInputStream;
import java.net.URI;

import com.google.inject.Guice;
import com.inn.trusthings.integration.TrustScorer;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.service.interfaces.TrustManager;

public class JustTest {
	
	public static void main(String[] args) {

			try {
				
				URI youtube = URI.create("http://www.programmableweb.com/api/youtube");
				String loc = "C://D-Data//Git//itrust//trusthings-common//src//main//resources//modelrepo//";
				TrustManager trustManager =  Guice.createInjector(new TrustModule()).getInstance(TrustManager.class);
				FileInputStream is = new FileInputStream(loc+"api_youtube.ttl");
//				trustManager.addResourceDescription(youtube, is);
				TrustScorer s = new TrustScorer();
				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/youtube")));
				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/twitter")));
				System.err.println(s.apply(URI.create("http://www.programmableweb.com/api/flickr")));
//				
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
}
