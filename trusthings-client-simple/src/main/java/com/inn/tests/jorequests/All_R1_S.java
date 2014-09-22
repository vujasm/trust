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
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.inn.client.simple.TrustModuleTest;
import com.inn.common.OrderType;
import com.inn.testtemp.DescriptionsEnum;
import com.inn.trusthings.db.d2r.Bridge;
import com.inn.trusthings.op.enums.EnumScoreStrategy;
import com.inn.trusthings.service.interfaces.TrustManager;

public class All_R1_S {
	
		public static void main(String[] args) {
		
//		services.add(URI.create(Const.Ts3));
//		services.add(URI.create(Const.Ts4));
//		services.add(URI.create(Const.Ts5));
//		TrustCriteria trustRequest = Request.request_Example_1();
		try {
			 Stopwatch timer = new Stopwatch().start();
			 int size = 1;
			 TrustManager t = new TrustModuleTest().getTrustManager();
			for (int i = 0; i < size; i++) {
//				Bridge b = new Bridge();
//				b.obtainTrustProfile("http://www.programmableweb.com/api/google-plus");
//				t.isTrusted(new URI("http://www.programmableweb.com/api/google-plus"));
				System.out.println(t.isTrusted(new URI("http://www.programmableweb.com/api/google-plus")));
			}
			timer.stop();
		    System.out.println("For size of "+size+" "+timer.elapsed(TimeUnit.SECONDS)+" "+timer.elapsed(TimeUnit.MINUTES));
//		}
		

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//		private static void test(int size, TrustManager t) throws Exception {
//
//			TrustManager tt = new TrustModuleTest().getTrustManager();
//			List<URI> services = Lists.newArrayList();
//			for (int i = 0; i < size; i++) {
//				services.add(URI.create(DescriptionsEnum.TSA.getURI()));
//				services.add(URI.create(DescriptionsEnum.TSB.getURI()));
//				services.add(URI.create(DescriptionsEnum.TSC.getURI()));
//			}
//		    Stopwatch timer = new Stopwatch().start();
//			t.rankResources(services, t.getGlobalTrustCriteria(), EnumScoreStrategy.TOPSIS, false, OrderType.DESC);
////			tt.obtainTrustIndexes(services);
//			timer.stop();
//		    System.out.println("For size of "+size+" "+timer.elapsed(TimeUnit.MILLISECONDS));
//		}

}


