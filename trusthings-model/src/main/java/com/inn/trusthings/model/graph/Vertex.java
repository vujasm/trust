package com.inn.trusthings.model.graph;

/*
 * #%L
 * trusthings-model
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


import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.inn.trusthings.model.pojo.TrustAttribute;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Maps;

public class Vertex {
	
	private String id ;
	
	private String composeType;
	
	private String composeID;
	
	private Model model;
	
	private HashMap<TrustAttribute, Double> scores = Maps.newHashMap();
	
	private List<String> merged=Lists.newArrayList();
	
	public Vertex(String id) {
		this.setID(id);
	}
	
	public List<String> getMerged() {
		return merged;
	}

	public boolean isMergeOfServices() {
		return (!merged.isEmpty());
	}


	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getComposeType() {
		return composeType;
	}

	public void setComposeType(String composeType) {
		this.composeType = composeType;
	}

	public String getComposeID() {
		return composeID;
	}

	public void setComposeID(String composeID) {
		this.composeID = composeID;
	}
	
	public HashMap<TrustAttribute, Double> getScores() {
		return scores;
	}
	
	public void addScore(TrustAttribute attr, Double value){
		this.getScores().put(attr, value);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	
}
