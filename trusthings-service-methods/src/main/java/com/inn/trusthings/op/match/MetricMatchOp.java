package com.inn.trusthings.op.match;

/*
 * #%L
 * trusthings-service
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Ordering;
import com.hp.hpl.jena.rdf.model.Resource;
import com.inn.common.OrderType;
import com.inn.trusthings.model.io.ToModelParser;
import com.inn.trusthings.model.pojo.Metric;
import com.inn.trusthings.model.pojo.MetricValue;
import com.inn.trusthings.model.utils.TrustOntologyUtil;

public class MetricMatchOp {
	
	 private static final Logger log = LoggerFactory.getLogger(MetricMatchOp.class);

	/**
	 * 
	 * @param metricURI
	 * @param metricValue1URI
	 *            requested value
	 * @param metricValue2URI
	 *            value
	 * @return
	 */
	public double apply(URI metricURI, final URI metricValue1URI, final URI metricValue2URI) {

		log.info("comparing metricscale values R vs A"+metricValue1URI+" vs. "+metricValue2URI);
		Resource resource = TrustOntologyUtil.instance().retriveResource(metricURI);
		Metric metric = new ToModelParser().parseMetric(resource);
		double returnValue = 0; 
		MetricValue metricValue1 = obtainMetricValue(metric, metricValue1URI);
		MetricValue metricValue2 = obtainMetricValue(metric, metricValue2URI);
		List<MetricValue> list = sortMetricValueList(metric.getMetricValues(), OrderType.ASC);
		if (metricValue1.getRank() != null && metricValue2.getRank() != null) {
			//we do normalization of the rank into 0..1 range
			double normalizedMetricValue1 = getNormalized(metricValue1, metric);
			double normalizedMetricValue2 = getNormalized(metricValue2, metric);
			returnValue = (normalizedMetricValue1 >  normalizedMetricValue2) ? 0 :normalizedMetricValue2;
		}
		else { 		// Otherwise, find out what would be relative value by doing
			// (relative value = (position of metric value in a ordered list of
			// metric values/ total number of metric values))
			int totalNo = list.size();
			double positionOfArg1 = list.indexOf(metricValue1) + 1;
			double positionOfArg2 = list.indexOf(metricValue2) + 1;
			double relVal1 = positionOfArg1 / totalNo;
			double relVal2 = positionOfArg2 / totalNo;
			returnValue = (relVal1 > relVal2) ? 0 : relVal2;
		}
		log.info("comparing metricscale returns "+returnValue);
		return returnValue;
	}


	private double getNormalized(MetricValue metricValue1, Metric metric) {
		MetricValue max = sortMetricValueList(metric.getMetricValues(), OrderType.DESC).get(0);
		double maxValue = max.getRank();
		double v = metricValue1.getRank() / maxValue;
		return v;
	}


	private MetricValue obtainMetricValue(final Metric metric, final URI metricValueURI) {
		final List<MetricValue> list = metric.getMetricValues();
		for (MetricValue metricValue : list) {
			if (metricValue.getUri().toASCIIString().equals(metricValueURI.toASCIIString())) {
				return metricValue;
			}
		}
		return null;// not found
	}


	/**
	 * Sorts metric values by metricValue.next relationship
	 * @param list a list of metric values
	 * @param orderType ASC/DESC. In a case of ASC orderding then the first element in the list will be a metric value that is not referenced as next,
	 * while the latest element in the list will be a metric value that has no next element. Vice-versa to ASC, in a case of DESC ordering,  
	 * @return
	 */
	private List<MetricValue> sortMetricValueList(List<MetricValue> list, final OrderType orderType) {
		Ordering<MetricValue> o = new Ordering<MetricValue>() {
			@Override
			public int compare(MetricValue left, MetricValue right) {
				if (obtainDistance(left) < obtainDistance(right))
					return orderType == OrderType.ASC ? 1 : -1;
				else
					return orderType == OrderType.ASC ? -1 : 1;
			}

			private int obtainDistance(MetricValue left) {
				int distance = 0;
				MetricValue current = left;
				while (current.getNext() != null) {
					distance++;
					current = current.getNext();
				}
				return distance;
			}
		};
		return o.sortedCopy(list);
	}

}
