package com.inn.client.simple;

/*
 * #%L
 * trusthings-client-simple
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
import com.inn.trusthings.kb.RDFModelsHandler;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.service.interfaces.TrustSimpleManager;
import com.inn.util.tree.Tree;

public class TrustModuleTest {

	protected static final Logger log = LoggerFactory.getLogger(TrustModuleTest.class);
	Level level = Level.OFF;


	private Injector injector;
	private TrustSimpleManager trustManager;

	public TrustModuleTest() {
		init();
	}

	private void init() {
		injector = Guice.createInjector(new TrustModule());
		trustManager = injector.getInstance(TrustSimpleManager.class);
	}

	public Tree computeTaxonomy() {
		Tree t = trustManager.obtainTaxonomy(ModelEnum.Trust.getURI(), Trust.TrustAttribute.getURI());
		System.out.println(t);
		return t;
	}

	public void getLoadedModels() {
		Set<URI> set = trustManager.getKnowledgeBaseManager().getLoadedModels();
		for (URI uri : set) {
			log.info(uri.toASCIIString());
		}
	}

	//testiranje kako jena radi inference taksonomije
	public void testSubclass() {
		OntModel base = trustManager.getKnowledgeBaseManager().getModel(ModelEnum.Trust.getURI());
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		OntModel model = ModelFactory.createOntologyModel(spec, base);
		final OntClass cls = model.getOntClass(Trust.SecurityAttribute.getURI().toString());
		ExtendedIterator<OntClass> it = cls.listSubClasses(false);
		while (it.hasNext()) {
			OntClass subcls = (OntClass) it.next();
			log.info(subcls.getURI());
		}
	}

	public TrustSimpleManager getTrustManager() {
		return trustManager;
	}

}
