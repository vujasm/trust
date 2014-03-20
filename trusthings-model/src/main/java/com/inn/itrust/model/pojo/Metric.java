package com.inn.itrust.model.pojo;

/*
 * #%L
 * itrust-model
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

public class Metric extends TResource{

	public Metric(URI uri) {
		super(uri);
	} 
	
	private List<MetricValue> metricValues = Lists.newArrayList();
	
	
	public List<MetricValue> getMetricValues() {
		return metricValues;
	}
	
	public void addMetricValue(final MetricValue metricValue){
		metricValues.add(metricValue);
	}
	
	


}
