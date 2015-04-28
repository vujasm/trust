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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inn.util.tuple.Tuple2;

public class GraphUtility {

	

	public static DirectedAcyclicGraph<Vertex, Edge> createDAG(String noderredFlow) throws Exception {
		DirectedAcyclicGraph<Vertex, Edge> g = createDAG();
		List<Vertex> vertices = Lists.newArrayList();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode nodes = mapper.readTree(noderredFlow);
		for (JsonNode node : nodes) {
			JsonNode type = node.get("type");
			if (type != null && type.textValue().equals("tab") == false) {
				JsonNode id = node.get("id");
				JsonNode composeId = node.get("compose_id");
				JsonNode composeType = node.get("compose_type");
				JsonNode name = node.get("name");
				Vertex vStart = getOrCreateVertex(id.textValue(),vertices);
				vStart.setComposeID((composeId != null ? composeId.textValue() : null));
				vStart.setComposeType((composeType != null ? composeType.textValue() : null));
				vStart.setName(name.textValue());
				if (g.containsVertex(vStart) == false)
					g.addVertex(vStart);
				JsonNode wires = node.get("wires");
				// System.out.println(node);
				// System.out.println(id+" "+type+" "+wires);
				for (JsonNode wire : wires) {
					ArrayNode wireArray = (ArrayNode) wire;
					for (JsonNode w : wireArray) {
						Vertex vTarget = getOrCreateVertex(w.textValue(),vertices);
						if (g.containsVertex(vTarget) == false)
							g.addVertex(vTarget);
						if (g.containsEdge(vStart, vTarget) == false)
							g.addEdge(vStart, vTarget);
					}
				}
			}
		}
//		Iterator<Vertex> it = g.iterator();
//		while (it.hasNext()) {
//			Vertex v = it.next();
//			System.out.println(v.getID());
//			for (Edge edge : g.edgesOf(v)) {
//				System.out.println(" " + g.getEdgeSource(edge).getID() + " " + g.getEdgeTarget(edge).getID());
//			}
//		}
		return g;
	}

	private static Vertex getOrCreateVertex(String id, List<Vertex> vertices) {
		for (Vertex v : vertices) {
			if (v.getID().equalsIgnoreCase(id))
				return v;
		}
		Vertex v = new Vertex(id);
		vertices.add(v);
		return v;
	}

	public static DirectedAcyclicGraph<Vertex, Edge> createDAG() {
		return new DirectedAcyclicGraph<Vertex, Edge>(Edge.class);
	}

	public static Set<Tuple2<Vertex, Vertex>> detectParallelStructure(DirectedAcyclicGraph<Vertex, Edge> g) {
		Set<Tuple2<Vertex, Vertex>> paralles = Sets.newHashSet();
		Iterator<Vertex> it = g.iterator();
		while (it.hasNext()) {
			Vertex vertexA = it.next();
			Set<Edge> inEdges = g.incomingEdgesOf(vertexA);
			Set<Edge> outEdges = g.outgoingEdgesOf(vertexA);
			Iterator<Vertex> it2 = g.iterator();
			while (it2.hasNext()) {
				Vertex vertexB = it2.next();
				// if (vertexA.equals(URI.create("http://www.ebay.com"))
				// && vertexB.equals(URI.create("http://www.amazon.com"))){
				// System.out.println("tu");
				// }
				if (vertexA.equals(vertexB) == false) {
					Set<Edge> inEdges2 = g.incomingEdgesOf(vertexB);
					Set<Edge> outEdges2 = g.outgoingEdgesOf(vertexB);
					Edge sharedSource = sameSource(inEdges, inEdges2, g);
					Edge sharedTarget = sameTarget(outEdges, outEdges2, g);
					if (sharedSource != null && sharedTarget != null) {
						Tuple2<Vertex, Vertex> t = new Tuple2<Vertex, Vertex>(vertexA, vertexB);
						paralles.add(t);
					}
				}
			}
		}
		return paralles;
	}

	private static Edge sameTarget(Set<Edge> set1, Set<Edge> set2, DirectedAcyclicGraph<Vertex, Edge> g) {
		for (Edge m1 : set1) {
			for (Edge m2 : set2) {
				if (g.getEdgeTarget(m1).equals(g.getEdgeTarget(m2))) {
					return m2;
				}
			}
		}
		return null;
	}

	private static Edge sameSource(Set<Edge> set1, Set<Edge> set2, DirectedAcyclicGraph<Vertex, Edge> g) {
		for (Edge m1 : set1) {
			for (Edge m2 : set2) {
				if (g.getEdgeSource(m1).equals(g.getEdgeSource(m2))) {
					return m2;
				}
			}
		}
		return null;
	}

}
