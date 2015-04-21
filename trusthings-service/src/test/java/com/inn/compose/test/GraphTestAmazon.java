package com.inn.compose.test;

import java.net.URI;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;

import com.inn.trusthings.model.graph.GraphUtility;
import com.inn.trusthings.model.graph.Vertex;
import com.inn.util.tuple.Tuple2;

public class GraphTestAmazon {
	
	
	public  static DirectedAcyclicGraph<Vertex, DefaultEdge> createDAGTest() {
		DirectedAcyclicGraph<Vertex, DefaultEdge> g = new GraphUtility().createDAG();
		try {
			Vertex amazon = new Vertex(URI.create("http://www.amazon.com").toASCIIString());
			Vertex yahoo = new Vertex(URI.create("http://www.yahoo.com").toASCIIString());
			Vertex ebay = new Vertex(URI.create("http://www.ebay.com").toASCIIString());
			Vertex google = new Vertex(URI.create("http://www.google.com").toASCIIString());
			Vertex twitter = new Vertex(URI.create("http://www.twitter.com").toASCIIString());
			Vertex hotmail = new Vertex(URI.create("http://www.hotmail.com").toASCIIString());
			Vertex facebook = new Vertex(URI.create("http://www.facebook.com").toASCIIString());

			// add the vertices
			g.addVertex(amazon);
			g.addVertex(yahoo);
			g.addVertex(ebay);
			g.addVertex(google);
			g.addVertex(twitter);
			g.addVertex(hotmail);
			g.addVertex(facebook);

			// add edges to create linking structure
		
			g.addEdge(yahoo, amazon);
			g.addEdge(yahoo, ebay);
			g.addEdge(ebay, google);
			g.addEdge(amazon, google);
			g.addEdge(google, twitter);
			g.addEdge(amazon, hotmail);
			g.addEdge(hotmail, twitter);
			g.addEdge(yahoo, facebook);
			g.addEdge( facebook, twitter);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Set<Tuple2<Vertex, Vertex>> paralles = new GraphUtility().detectParallelStructure(g);
		for (Tuple2<Vertex, Vertex> tuple2 : paralles) {
			System.out.println(tuple2.getT2().getID()+" "+tuple2.getT1().getID());
		}
		return g;
	}
	
	public static void main(String[] args) {
		createDAGTest();
	}


}
