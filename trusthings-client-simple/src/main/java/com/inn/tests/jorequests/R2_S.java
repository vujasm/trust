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

import com.google.common.collect.Lists;
import com.inn.client.simple.TrustModuleTest;
import com.inn.common.Const;
import com.inn.common.OrderType;
import com.inn.trusthings.model.pojo.TrustRequest;
import com.inn.trusthings.op.enums.EnumScoreStrategy;

public class R2_S {
	
	public static void main(String[] args) {
		
		List<URI> services = Lists.newArrayList();
		services.add(URI.create(Const.Ts3));
		services.add(URI.create(Const.Ts4));
		services.add(URI.create(Const.Ts5));
		TrustRequest trustRequest = Request.request_Example_2();
		try {
			new TrustModuleTest().getTrustManager().
			rankResources(services, trustRequest, EnumScoreStrategy.Weighted_sum_model, false, OrderType.DESC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
