package com.inn.trusthings.integration.util;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Maps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

public class ConvertTo {

	public Set<URI> toSet(String s) throws Exception{
		Set<URI> set = Sets.newHashSet();
		ObjectMapper m  = new ObjectMapper();
		JsonNode node = m.readTree(s);
		JsonNode values = node.findValue("result");
		for (JsonNode jsonNode : values) {
			set.add(new URI(jsonNode.findValue("resourceURI").asText()));
		}
		return set;
	}

	public Map<URI, Double> toMap(String s) throws Exception{
		Map<URI, Double> map = Maps.newHashMap();
		ObjectMapper m  = new ObjectMapper();
		JsonNode node = m.readTree(s);
		JsonNode values = node.findValue("result");
		for (JsonNode jsonNode : values) {
			map.put(new URI(jsonNode.findValue("resourceURI").asText()),
					new Double(jsonNode.findValue("index").asText()));
		}
		return map;
	}
}
