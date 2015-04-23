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

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;
import com.inn.trusthings.model.expression.SingleElement;
import com.inn.trusthings.model.graph.Edge;
import com.inn.trusthings.model.graph.Vertex;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;
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
		prepare(g, dataSetMain);
		return compute(g, listCriteria);

	}

	private void prepare(DirectedAcyclicGraph<Vertex, Edge> g, List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSetMain) {
		// set scores on vertices
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
		// TODO Auto-generated method stub
		return null;
	}
}
