package com.inn.tests.jorequests;

/*
 * #%L
 * trusthings-client-simple
 * %%
 * Copyright (C) 2015 COMPOSE project
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
import com.inn.common.OrderType;
import com.inn.testtemp.DescriptionsEnum;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.op.enums.EnumScoreStrategy;

public class R4_S {
	
	public static void main(String[] args) {
	
		List<URI> services = Lists.newArrayList();
		services.add(URI.create(DescriptionsEnum.TSA.getURI()));
		services.add(URI.create(DescriptionsEnum.TSB.getURI()));
		services.add(URI.create(DescriptionsEnum.TSC.getURI()));
		TrustCriteria trustRequest = Request.request_Example_4(1, 1, 1, 1);
		try {
			new TrustModuleTest().getTrustManager().
			rankResources(services, trustRequest, EnumScoreStrategy.Weighted_sum_model, false, OrderType.DESC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
