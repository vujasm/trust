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
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.inn.trusthings.collector.AbstractCollector;
import com.inn.trusthings.collector.Collector;
import com.inn.trusthings.collector.trustdb.InternalCollector;

public enum CollectorConfig {
	
	InternalCollector( new InternalCollector(""));
	
	private  final Collector collector;
	
	CollectorConfig(Collector collector) {
		this.collector = collector;
	}
	
	public Collector getCollector(){
		return collector;
	}
	
	public static Collector getCollectorByType(String name) {
		List<Collector> list = Lists.newArrayList();
		for (Collector collector : list) {
			if (collector.getName().contains(name))
				return collector;
		}
		return null;
	}

	public static List<Collector> read() {
		List<Collector> list = Lists.newArrayList();
		try {
			InputStream is = CollectorConfig.class.getResourceAsStream("/collectors.json");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(is);
			ArrayNode colls = (ArrayNode) node.get("collectors");
			for (JsonNode jsonNode : colls) {
				String className = jsonNode.get("class").textValue();
				String endpoint = jsonNode.get("endpoint").textValue();
				AbstractCollector c =  (AbstractCollector) Class.forName(className).newInstance();
				c.setSourceUri(endpoint);
				list.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
