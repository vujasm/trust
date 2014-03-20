package com.inn.trusthings.model.utils;

/*
 * #%L
 * trusthings-model
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
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.inn.trusthings.model.pojo.TResource;

/**
 * 
 * A singleton holding useful methods for trust attribute types checking
 * and simple querying/retrieval under trust ontology
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
//FIXME rename, perhaps
public class TrustOntologyUtil {
	
	
	//base model (with no inferred axioms) 
	private OntModel baseOntModel ;
	
	//model with inferred axioms
	private OntModel reasonedOntModel;
	
	
	private static TrustOntologyUtil instance;
	
	
	public static TrustOntologyUtil instance(){
		return instance;
	}
	
	private TrustOntologyUtil(OntModel trustontmodel){
		this.baseOntModel = trustontmodel;
		OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		reasonedOntModel = ModelFactory.createOntologyModel(spec, baseOntModel);
	}
	
	public TrustOntologyUtil getInstance() {
		return instance;
	}
	
	public static void init(OntModel trustOntModel){
		instance = new TrustOntologyUtil(trustOntModel);
	}
	
	/**
	 * Checks if given resource have same uri
	 * @param r1 resource 
	 * @param r2 resource
	 * @return
	 */
	public  synchronized boolean sameURI(TResource r1, TResource r2){
		return (r1.getUri().equals(r2.getUri()));
	}
	
	/**
	 * 
	 * @param types list of resources considered to be a type
	 * @param uri uri of a resource considered as a type
	 * @return
	 */
	public  synchronized List<TResource> filterListTypeByTypeDontConsiderSubtypes(List<TResource> types, final URI type) {
		return Lists.newArrayList(Iterables.filter(types, new Predicate<TResource>() {
			@Override
			public boolean apply(TResource r) {
				if (r.getUri().compareTo(type) == 0) {
					return true;
				}
				return false;
			}
		}));
	}
	
	
	/**
	 * 
	 * @param types list of resources considered to be a type
	 * @param uri uri of a resource considered as a type
	 * @return
	 */
	public  synchronized List<TResource> filterListTypeByTypeConsiderSubtypes(List<TResource> types, final URI type) {
		return Lists.newArrayList(Iterables.filter(types, new Predicate<TResource>() {
			@Override
			public boolean apply(TResource r) {
				if (r.getUri().compareTo(type) == 0) {
					return true;
				}
				return false;
			}
		}));
	}
	
	//FIXME - vec na ovom nivou ce morati imati semanticki retrival, jer ako korisnik trazi secAttr a resource je anotiran sa sec capab
	//tada metoda findType nece raditi. Moram naci sve nad tipove / podtipove i uraditi retrival.
	//note ---- ipak sam odlucio da ne. jer ako reputation ima subklase activity/populariyu/rating tada ce
	//za atribut reputation dobiti i popularity/rating. Mozda najbolje da korisnik iskaze direktno da li zeli security capab ili sec req
	//jer to zapravo i jesu dva razlicita (disjoint) pojma
	public   synchronized <T extends TResource> List<T> filterByTypeDirect(final List<T> resources, final URI type) {
		List<T> list = Lists.newArrayList(Iterables.filter(resources, new Predicate<T>() {
			@Override
			public boolean apply(T resource) {
				List<TResource> result = filterListTypeByTypeDontConsiderSubtypes(resource.getTypesAll(), type);
				if (result != null && result.isEmpty() == false)
					return true;
				else
					return false;
			}
		}));
		return list;
	}
	
	
	public  boolean isSubtype(String typeURI, String ofTypeURI) {
		final OntClass cls = reasonedOntModel.getOntClass(ofTypeURI);
		ExtendedIterator<OntClass> it = cls.listSubClasses(false);
		while (it.hasNext()) {
			OntClass subcls = (OntClass) it.next();
			if (subcls.getURI().equalsIgnoreCase(typeURI))
				return true;
		}
		return false;
	}
	
	
	public  synchronized boolean ofSameTypeAsR1(TResource r1, TResource r2) {
		List<TResource> types = r1.getTypesAll();
		for (TResource type : types) {
			if (filterListTypeByTypeDontConsiderSubtypes(r2.getTypesAll(), type.getUri()).isEmpty() == false){
				return true;
			};
		}
		return false;
	}

	
	public  boolean isIndividualOfTypeIgnoreSuper(URI resourceURI, String typeURI) {
		 final Individual individual = baseOntModel.getIndividual(resourceURI.toString());
		 if (individual == null) return false;
		 return isOfType(individual, typeURI, true);
	}
	
	
	private boolean isOfType(Individual individual, String typeURI, boolean isdirect) {
		Iterator<Resource> types = individual.listRDFTypes(isdirect);
		while (types.hasNext()) {
			Resource type = (Resource) types.next();
			if (type.getURI().equalsIgnoreCase(typeURI)) {
				return true;
			}
		}
		return false;
	}

	public Resource retriveResource(URI resourceURI) {
		return baseOntModel.getResource(resourceURI.toASCIIString());
	}

}
