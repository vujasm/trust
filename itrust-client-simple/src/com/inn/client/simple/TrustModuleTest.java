package com.inn.client.simple;

/*
 * #%L
 * itrust-client-simple
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

import java.net.URI;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.inn.common.structure.tree.Tree;
import com.inn.itrust.model.vocabulary.ModelsNSEnum;
import com.inn.itrust.model.vocabulary.Trust;
import com.inn.itrust.service.component.TrustComponent;
import com.inn.itrust.service.managers.TrustManager;

public class TrustModuleTest {

	protected static final Logger log = LoggerFactory.getLogger(TrustModuleTest.class);
	Level level = Level.OFF;

	String graphName = ModelsNSEnum.Trust.getNS();
	private Injector injector;
	private TrustManager trustManager;

	public TrustModuleTest() {
		init();
	}

	private void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		org.apache.log4j.Logger.getRootLogger().setLevel(level);
		injector = Guice.createInjector(new TrustComponent());
		trustManager = injector.getInstance(TrustManager.class);
	}

	public void computeTaxonomy() {
		Tree t = trustManager.computeTaxonomy(graphName, Trust.TrustAttribute.getURI());
		System.out.println(t);
	}

	public void getLoadedModels() {
		Set<URI> set = trustManager.getKnowledgeBaseManager().getLoadedModels();
		for (URI uri : set) {
			log.info(uri.toASCIIString());
		}
	}

	//testiranje kako jena radi inference taksonomije
	public void testSubclass() {
		OntModel base = trustManager.getKnowledgeBaseManager().getModel(ModelsNSEnum.Trust.getNS());
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		OntModel model = ModelFactory.createOntologyModel(spec, base);
		final OntClass cls = model.getOntClass(Trust.SecurityAttribute.getURI().toString());
		ExtendedIterator<OntClass> it = cls.listSubClasses(false);
		while (it.hasNext()) {
			OntClass subcls = (OntClass) it.next();
			log.info(subcls.getURI());
		}
	}

	public TrustManager getTrustManager() {
		return trustManager;
	}

}
