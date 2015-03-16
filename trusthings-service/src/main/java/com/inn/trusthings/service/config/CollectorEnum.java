package com.inn.trusthings.service.config;

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
import com.inn.trusthings.collector.Collector;
import com.inn.trusthings.collector.monitoring.MonitoringCollector;
import com.inn.trusthings.collector.reputation.ReputationCollector;
import com.inn.trusthings.collector.trustdb.InternalCollector;

public enum CollectorEnum {
	

	Reputation( new ReputationCollector("http://132.231.11.217:8080/popularioty-api")),
	QoS( new MonitoringCollector("http://localhost//")),
	InternalCollector( new InternalCollector(""));
	
	private  final Collector collector;
	
	CollectorEnum(Collector collector) {
		this.collector = collector;
	}
	
	public Collector getCollector() {
		return collector;
	}

}
