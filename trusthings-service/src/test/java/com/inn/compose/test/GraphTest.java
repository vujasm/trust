package com.inn.compose.test;

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


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.common.io.CharStreams;
import com.inn.trusthings.model.graph.Edge;
import com.inn.trusthings.model.graph.GraphUtility;
import com.inn.trusthings.model.graph.Vertex;
import com.inn.util.tuple.Tuple2;

public class GraphTest {
	
	public static void main(String[] args) {
		String noderredFlow = "";
		InputStream is = GraphTest.class.getResourceAsStream("/flowSnowUpdate.json");
		try {
			noderredFlow = CharStreams.toString(new InputStreamReader(is));
			GraphUtility gu = new GraphUtility();
			DirectedAcyclicGraph<Vertex, Edge> g = gu.createDAG(noderredFlow);
			Set<Tuple2<Vertex, Vertex>> parallels = gu.detectParallelStructure(g);
			System.out.println(parallels.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
