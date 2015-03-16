package com.inn.util.tree;

/*
 * #%L
 * trusthings-common
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


import java.util.List;

import com.google.common.collect.Lists;


public class Node {

	private String name;
	
	private List<Node> subNodes =Lists.newArrayList();

	public Node(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
//
	public void setName(String name) {
		this.name = name;
	}

	public List<Node> getSubNodes() {
		return subNodes;
	}

	public void setSubNodes(List<Node> subNodes) {
		this.subNodes = subNodes;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void addSubNode(Node node){
		getSubNodes().add(node);
	}

	public void getLevel() {
		
	}

}
