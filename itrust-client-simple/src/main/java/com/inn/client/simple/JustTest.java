package com.inn.client.simple;

/*
 * #%L
 * itrust-client-simple
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


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import com.inn.common.Const;
import com.inn.itrust.recommender.TrustFilter;
import com.inn.itrust.recommender.TrustScorer;

public class JustTest {
	
	public static void main(String[] args) {
//		System.out.println(SemSim.class.getClassLoader().getResource("ontologies/usdl-sec.ttl").toString());
		try {
//			String encodedUrl = URLEncoder.encode(Const.Ts4, "UTF-8");
//			System.out.println(encodedUrl);
			try {
				System.out.println(new TrustScorer().apply(URI.create(Const.Ts3)));
				System.out.println(new TrustFilter().apply(URI.create(Const.Ts3)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
