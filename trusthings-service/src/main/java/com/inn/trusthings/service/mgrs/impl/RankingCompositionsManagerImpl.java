package com.inn.trusthings.service.mgrs.impl;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;
import com.inn.trusthings.model.expression.SingleElement;
import com.inn.trusthings.model.graph.Edge;
import com.inn.trusthings.model.graph.GraphUtility;
import com.inn.trusthings.model.graph.Vertex;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.op.composition.AggrFunctionGetter;
import com.inn.trusthings.op.composition.AggregationFunction;
import com.inn.trusthings.op.composition.EnumStructure;
import com.inn.trusthings.service.interfaces.RankingCompositionsManager;
import com.inn.trusthings.service.interfaces.RankingManager;
import com.inn.trusthings.service.interfaces.TrustSimpleManager;
import com.inn.util.tuple.Tuple2;

public class RankingCompositionsManagerImpl implements RankingCompositionsManager {

	
	
	TrustSimpleManager trustSimpleManager;
	RankingManager rankingManager;
	private static final Logger log = LoggerFactory.getLogger(RankingCompositionsManagerImpl.class);

	@Inject
	public RankingCompositionsManagerImpl(TrustSimpleManager trustSimpleManager, RankingManager rankingManager) {
		this.trustSimpleManager = trustSimpleManager;
		this.rankingManager = rankingManager;
	}

	@Override
	public Double computeScore(DirectedAcyclicGraph<Vertex, Edge> g, TrustCriteria criteria) {
		List<URI> resources = Lists.newArrayList();
		Iterator<Vertex> it = g.iterator();
		while (it.hasNext()) {
			Vertex v = (Vertex) it.next();
			if (v.getComposeID() != null) {
				resources.add(URI.create(v.getComposeID()));
			} else {
				v.setModel(null);
			}
		}
		List<Tuple2<URI, Model>> models = trustSimpleManager.obtainModelsListTuple(resources, false);
		for (Tuple2<URI, Model> t : models) {
			it = g.iterator();
			while (it.hasNext()) {
				Vertex v = (Vertex) it.next();
				if (v.getComposeID() != null && v.getComposeID().equalsIgnoreCase(t.getT1().toASCIIString())) {
					v.setModel(t.getT2());
					log.info("loaded model for:" + v.getComposeID());
				}
			}
		}

		List<SingleElement> listCriteria = criteria.getListOperandByAnd();
		List<Model> listModels = trustSimpleManager.castListModels(models);
		List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSetMain = rankingManager.prepareDataset(listModels, listCriteria, false, false);
		addScoresToVertices(g, dataSetMain);
		return compute(g, listCriteria);

	}

	private void addScoresToVertices(DirectedAcyclicGraph<Vertex, Edge> g, List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSetMain) {
		Iterator<Vertex> it = g.iterator();
		while (it.hasNext()) {
			Vertex v = (Vertex) it.next();
			if (v.getComposeID() != null) {
				for (Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> t : dataSetMain) {
					if ((t.getT1().getInputUID() != null && t.getT1().getInputUID().toString().equals(v.getComposeID()))) {
						for (Tuple2<TrustAttribute, Double> s : t.getT2()) {
							v.addScore(s.getT1(), s.getT2());
						}
					}
				}
			}
		}

	}
	
	private Double compute(DirectedAcyclicGraph<Vertex, Edge> g, List<SingleElement> listCriteria) {
		
		if (g.vertexSet().size()==1)
			return finalScore(g.vertexSet().iterator().next(), listCriteria);
					
//		1. compute on all sequantal. sequental is with one incoming and one outgoing edge.
		
		DirectedAcyclicGraph<Vertex, Edge> gPrim = cloneGraph(g);
		Iterator<Vertex> vertices = g.iterator();
//		boolean foundSequental = false;
		while (vertices.hasNext()) {
			Vertex current = (Vertex) vertices.next();
			Vertex nextSequental = findNextSequental(current, g);
			if (nextSequental != null){
//				foundSequental = true;
//				System.out.println(current + " -seq- "+nextSequental);
				mergeSequentalVertices(gPrim, current, nextSequental, listCriteria);
			}
		}
		
		Set<Tuple2<Vertex, Vertex>> parallels = GraphUtility.detectParallelStructure(gPrim);
		for (Tuple2<Vertex, Vertex> t : parallels) {
			mergeParallelVertices(gPrim, t.getT1(), t.getT2(), listCriteria);
		}

		return compute(gPrim, listCriteria);
	}



	private Double finalScore(Vertex v, List<SingleElement> listCriteria) {
		Double sumOfWeights = sumAllWeights(listCriteria);
		Double score = 0D;
		Map<TrustAttribute, Double> scores = v.getScores();
		for (SingleElement e : listCriteria) {
			log.info("final scoring:"+e.getAttribute().getTypesAll().get(0).getUri());
			for (TrustAttribute attribute : scores.keySet()) {
				if (attribute.getTypesAll().get(0).getUri().compareTo(e.getAttribute().getTypesAll().get(0).getUri())==0){
					double eval = scores.get(attribute) * e.getAttribute().getImportance();
					log.info(attribute.getTypesAll().get(0).getUri()+" "+scores.get(attribute)+" * weight = "+eval);
					score = score + eval;
					log.info("score =  "+score);
				}
			}
		}
		log.info("(score / sumOfWeights) = "+(score / sumOfWeights));
		return (score / sumOfWeights);
	}
	
	protected Double sumAllWeights(List<SingleElement> list){
		double sum = 0;
		for (SingleElement e : list) {
			sum = sum + e.getAttribute().getImportance();
		}
		return sum;
	}

	private DirectedAcyclicGraph<Vertex, Edge> cloneGraph(DirectedAcyclicGraph<Vertex, Edge> g) {
		DirectedAcyclicGraph<Vertex, Edge> gPrim = GraphUtility.createDAG();
		Set<Vertex> s = g.vertexSet();
		for (Vertex vertex : s) {
			gPrim.addVertex(vertex);
		}
		Set<Edge> e = g.edgeSet();
		for (Edge edge : e) {
			gPrim.addEdge(g.getEdgeSource(edge), g.getEdgeTarget(edge));
		}
		return gPrim;
	}

	private DirectedAcyclicGraph<Vertex, Edge> mergeSequentalVertices(DirectedAcyclicGraph<Vertex, Edge> gPrim, Vertex current, Vertex next, List<SingleElement> listCriteria) {
		Set<Edge> in = gPrim.incomingEdgesOf(current);
		for (Edge inedge : in) {
			gPrim.addEdge(gPrim.getEdgeSource(inedge), next);
		}
		//relink also outgoing edges whose target is not vertex 'next'
		Set<Edge> out = gPrim.outgoingEdgesOf(current);
		for (Edge outedge : out) {
			if (gPrim.getEdgeTarget(outedge).getID().equals(next.getID()) == false)
					gPrim.addEdge(next, gPrim.getEdgeTarget(outedge));
		}
		next.getMerged().add(current);
		gPrim.removeVertex(current);
		applyAggregationFunction(listCriteria, current, next, EnumStructure.SEQUENTAL);
//		System.out.println(gPrim);
		return gPrim;
	}
	
	private void applyAggregationFunction(List<SingleElement> listCriteria, Vertex source, Vertex target, EnumStructure structure) {
		Map<TrustAttribute, Double> newScores = Maps.newHashMap();
		for (SingleElement singleElement : listCriteria) {
			TrustAttribute  a = singleElement.getAttribute();
			AggregationFunction function = AggrFunctionGetter.getFunction(a.getTypesAll().get(0).getUri(), structure);
			List<Vertex> vertices = Lists.newArrayList();
			vertices.add(source);
			vertices.add(target);
			Double score = function.compute(vertices, a);
			newScores.put(a, score);
		}
		target.getScores().clear();
		target.setScores(newScores);
		
	}

	private DirectedAcyclicGraph<Vertex, Edge>  mergeParallelVertices(DirectedAcyclicGraph<Vertex, Edge> gPrim, Vertex v1, Vertex v2, List<SingleElement> listCriteria) {
		if (gPrim.containsVertex(v1) && gPrim.containsVertex(v2)){
//			System.out.println("removing "+v1);
			gPrim.removeVertex(v1);
			v2.getMerged().add(v1);
			applyAggregationFunction(listCriteria, v1, v2, EnumStructure.PARALLEL);
		}
//		System.out.println(gPrim);
		return gPrim;
	}

	private Vertex findNextSequental(Vertex current, DirectedAcyclicGraph<Vertex, Edge> g) {
		Set<Edge> in = g.incomingEdgesOf(current);
		Set<Edge> out = g.outgoingEdgesOf(current);
		if ((in.size()==1 || in.size()==0 ) && (out.size()==1 || out.size()==0)){
			if (out.size()==1){
				Vertex next = g.getEdgeTarget(out.iterator().next());
				Set<Edge> innext = g.incomingEdgesOf(next);
				if ((innext.size()==1 || innext.size()==0)){
					return next;
				}
			}
		}
		if ((in.size()==1 || in.size()==0 ) && (out.size()>1)){
			   	Iterator<Edge> it = out.iterator();
			   	while (it.hasNext()) {
			   		Vertex next = g.getEdgeTarget(it.next());
					Set<Edge> innext = g.incomingEdgesOf(next);
					Set<Edge> outNext = g.outgoingEdgesOf(next);
					if ((innext.size()==1 && (outNext.size()==1 || outNext.size()==0))){
						if (notInParallel(g,next))
							return next;
					}
				}
		}
		return null;
	}

	private boolean notInParallel(DirectedAcyclicGraph<Vertex, Edge> g, Vertex next) {
		//it is not in parallell when there is no another vertix with same source and target node
		Set<Tuple2<Vertex, Vertex>> parallels  = GraphUtility.detectParallelStructure(g);
		for (Tuple2<Vertex, Vertex> t : parallels) {
			if (t.getT1().getID().equals(next.getID())
					|| t.getT2().getID().equals(next.getID()))
				return false;
		}
		return true;
	}
}
