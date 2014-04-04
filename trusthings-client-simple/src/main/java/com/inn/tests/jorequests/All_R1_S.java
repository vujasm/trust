package com.inn.tests.jorequests;

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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.inn.client.simple.TrustModuleTest;
import com.inn.common.Const;

import com.inn.trusthings.service.interfaces.TrustManager;

public class All_R1_S {
	
		public static void main(String[] args) {
		
//		services.add(URI.create(Const.Ts3));
//		services.add(URI.create(Const.Ts4));
//		services.add(URI.create(Const.Ts5));
//		TrustCriteria trustRequest = Request.request_Example_1();
		try {
			
			TrustManager t = new TrustModuleTest().getTrustManager();
			test(1, t);
//			test(10, t);
//			test(50, t);
//		    test(100, t);
//		    test(200, t);
//		    test(500, t);
//		    test(1000, t);
//		    test(2000, t);
//		    test(5000, t);
//		    test(10000, t);
//		    test(20000, t);
//		    test(50000, t);
//		    test(100000, t);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		private static void test(int size, TrustManager t) throws Exception {

			List<URI> services = Lists.newArrayList();
			for (int i = 0; i < size; i++) {
				services.add(URI.create(Const.Ts3));
				services.add(URI.create(Const.Ts4));
				services.add(URI.create(Const.Ts5));
			}
		    Stopwatch timer = new Stopwatch().start();
//			t.rankResources(services, t.getGlobalTrustPerception(), EnumScoreStrategy.TOPSIS, false, OrderType.DESC);
			t.obtainTrustIndexes(services);
			timer.stop();
		    System.out.println(timer.elapsed(TimeUnit.MILLISECONDS));
		}

}


