package com.inn.itrust.op.semsim;

/*
 * #%L
 * itrust-methods
 * %%
 * Copyright (C) 2014 INNOVA S.p.A
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

import org.openrdf.model.URI;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import slib.sglib.algo.graph.utils.GAction;
import slib.sglib.algo.graph.utils.GActionType;
import slib.sglib.io.conf.GDataConf;
import slib.sglib.io.conf.GraphConf;
import slib.sglib.io.loader.GraphLoaderGeneric;
import slib.sglib.io.util.GFormat;
import slib.sglib.model.graph.G;
import slib.sglib.model.impl.graph.memory.GraphMemory;
import slib.sglib.model.impl.repo.URIFactoryMemory;
import slib.sglib.model.repo.URIFactory;
import slib.sml.sm.core.engine.SM_Engine;
import slib.sml.sm.core.metrics.ic.utils.IC_Conf_Topo;
import slib.sml.sm.core.metrics.ic.utils.ICconf;
import slib.sml.sm.core.utils.SMConstants;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.ex.SLIB_Exception;
import slib.utils.impl.Timer;

/**
 * 
 * SemSim operator that uses a Semantic Measurement Library (SML)0 to compute semantic similarity of concepts from taxonomic Knowledge 
 * For SML @see <a href="http://www.semantic-measures-library.org/sml/">http://www.semantic-measures-library.org/sml/</a>
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class SemSim {

	private G g;
	
	 private static final Logger log = LoggerFactory.getLogger(SemSim.class);

	/**
	 * According to SML, the first semsim computation is expensive as the engine compute the IC 
	 * and extra information which are cached by the engine. Because of that SemSim is set for 
	 * a particular ontology, and then @see {@link SemSim#apply(URI, URI)} can be called
	 * @param uriOntology Ontology URI
	 */
	public SemSim(String uriOntology) {
		
		uriOntology = format(uriOntology);
		
		URIFactory factory = URIFactoryMemory.getSingleton();
		URI graphURI = factory.createURI("http://graph/");
		g = new GraphMemory(graphURI);

		GDataConf dataConf = new GDataConf(GFormat.TURTLE, uriOntology);

		// We specify an action to root the vertices, typed as class without outgoing rdfs:subclassOf relationship
		// Those vertices are linked to owl:Thing by an eddge x rdfs:subClassOf owl:Thing
		GAction actionRerootConf = new GAction(GActionType.REROOTING);

		// We now create the configuration we will specify to the generic loader
		GraphConf gConf = new GraphConf();
		gConf.addGDataConf(dataConf);
		gConf.addGAction(actionRerootConf);
		try {
			GraphLoaderGeneric.load(gConf, g);
		} catch (SLIB_Exception e) {
			e.printStackTrace();
		}
	}

	private String format(String uriOntology) {
		return uriOntology.replace("file:///", "").replace("file:/", "");
	}

	/**
	 * Computes semantic similarity between two taxonomical concepts
	 * @param concept1URI Concept URI
	 * @param concept2URI Concept URI
	 * @return similarity as a double in a range [0..1]
	 * @throws Exception
	 */
	public double apply(String concept1URI, String concept2URI) throws Exception {
		
		Timer t = new Timer();
		t.start();

//		Set<URI> roots = new ValidatorDAG().getTaxonomicDAGRoots(g);

		// We compute the similarity between two concepts

		// URI cityURI = factory.createURI("http://www.compose-project.eu/ns/web-of-things/security/profiles#OAuth2");

		// // First we configure an intrincic IC
		// ICconf icConf = new IC_Conf_Topo(SMConstants.FLAG_ICI_DEPTH_MAX_NONLINEAR);
		// // Then we configure the pairwise measure to use, we here choose to use Lin formula
		// SMconf smConf = new SMconf(SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_LIN_1998, icConf);

		// First we configure an intrincic IC
		ICconf icConf = new IC_Conf_Topo(SMConstants.FLAG_ICI_SANCHEZ_2011);
		// Then we configure the pairwise measure to use, we here choose to use Lin formula
		SMconf smConf = new SMconf(SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_LIN_1998, icConf);

		// We define the engine used to compute the similarity
		SM_Engine engine = new SM_Engine(g);

		URI uri1 = ValueFactoryImpl.getInstance().createURI(concept1URI);
		URI uri2 = ValueFactoryImpl.getInstance().createURI(concept2URI);
		
		double sim = engine.computePairwiseSim(smConf, uri1 , uri2);
		log.info("SemSim.java - > Similarity " + sim+" "+uri1+" -- "+uri2);
		t.stop();
		t.elapsedTime();
		return sim;

	}

//	public static void main(String[] args) throws SLIB_Exception {
//		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
//		String ontoFile = com.inn.itrust.service.LocationMapping.resolveLocation(
//				com.inn.itrust.model.vocabulary.ModelEnum.SecurityProfiles.getURI());
//		String concept1URI = "http://www.compose-project.eu/ns/web-of-things/security/profiles#SAML";
//		String concept2URI ="http://www.compose-project.eu/ns/web-of-things/security/profiles#E";
//		try {
//			new SemSim(ontoFile).apply(concept1URI, concept2URI);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
