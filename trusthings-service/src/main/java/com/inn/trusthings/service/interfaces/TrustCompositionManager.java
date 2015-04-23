package com.inn.trusthings.service.interfaces;

import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.inn.common.CompositeServiceWrapper;
import com.inn.common.CompositionIdentifier;
import com.inn.trusthings.model.graph.Edge;
import com.inn.trusthings.model.graph.Vertex;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.op.enums.EnumLevel;
import com.inn.util.tuple.Tuple2;

/*
 * #%L
 * trusthings-methods
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



public interface TrustCompositionManager extends TrustManager {
	
	List<CompositionIdentifier> filterTrustedByThreshold(List<CompositeServiceWrapper> compositeServiceList, TrustCriteria criteria, EnumLevel level, String strategy, Double thresholdValue) throws Exception;
	
	List<Tuple2<CompositionIdentifier, Double>> obtainTrustIndexes(List<CompositeServiceWrapper> compositeServiceList,TrustCriteria criteria, EnumLevel level, String strategy) throws Exception;

	Tuple2<CompositionIdentifier, Double> obtainTrustIndex(CompositeServiceWrapper compositeServiceWrapper, TrustCriteria criteria, EnumLevel level, String strategy) throws Exception;

	Double obtainTrustIndex(DirectedAcyclicGraph<Vertex, Edge> g, TrustCriteria criteria, EnumLevel level, String strategy) throws Exception;
	
}
