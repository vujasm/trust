package com.inn.trusthings.service.mgrs.impl;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.open.kmi.iserve.commons.io.Syntax;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.inn.common.CompositeServiceWrapper;
import com.inn.common.CompositionIdentifier;
import com.inn.trusthings.json.TrustPOJOFactory;
import com.inn.trusthings.kb.RDFModelsHandler;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.model.utils.TrustOntologyUtil;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.op.enums.EnumLevel;
import com.inn.trusthings.service.config.GlobalTrustCriteria;
import com.inn.trusthings.service.interfaces.TrustCompositionManager;
import com.inn.util.tuple.ListTupleConvert;
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



public class TrustCompositionManagerImpl implements TrustCompositionManager{
	
	private TrustCriteria globalTrustCriteria = GlobalTrustCriteria.instance();
	
	private static final Logger log = LoggerFactory.getLogger(TrustCompositionManagerImpl.class);
	
	public TrustCompositionManagerImpl() {
		OntModel model = RDFModelsHandler.getGlobalInstance().fetchOntologyFromLocalLocation(ModelEnum.Trust.getURI(), 
				Syntax.TTL.getName(), OntModelSpec.OWL_MEM_TRANS_INF);
		TrustOntologyUtil.init(model);
	}

	@Override
	public void setGlobalTrustCriteria(TrustCriteria criteria) {
		this.globalTrustCriteria = criteria;
	}

	@Override
	public void setGlobalTrustCriteria(String criteriaAsJson) {
		TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(criteriaAsJson);
		this.globalTrustCriteria = criteria;
	}

	@Override
	public TrustCriteria getGlobalTrustCriteria() {
		return this.globalTrustCriteria;
	}

	@Override
	public List<CompositionIdentifier> filterTrustedByThreshold(List<CompositeServiceWrapper> compositeServiceList,TrustCriteria criteria, EnumLevel level, String strategy, final Double thresholdValue) {
		List<Tuple2<CompositionIdentifier, Double>> scored = obtainTrustIndexes(compositeServiceList, criteria, level, strategy);
		Iterable<Tuple2<CompositionIdentifier, Double>> filtered = Iterables.filter(scored, new Predicate<Tuple2<CompositionIdentifier, Double>>() {
			@Override
			public boolean apply(Tuple2<CompositionIdentifier, Double> t) {
				return (Double.valueOf(t.getT2()).compareTo(thresholdValue) >= 0);
			}
		});
		printList(Lists.newArrayList(filtered), " filtered with thresholdValue value of " + thresholdValue);
		final List<CompositionIdentifier> filteredList = ListTupleConvert.toListOfTupleElement(Lists.newArrayList(filtered), 1);
		return filteredList;
	}
	
	private void printList(List<Tuple2<CompositionIdentifier, Double>> set, String note) {
		log.info("******** <" + note + "> ************");
		for (Tuple2<CompositionIdentifier, Double> t : set) {
			log.info(t.getT1().getId() + " score " + t.getT2());
		}
		log.info("******** </" + note + "> ************");
	}

	@Override
	public List<Tuple2<CompositionIdentifier, Double>> obtainTrustIndexes(List<CompositeServiceWrapper> compositeServiceList, TrustCriteria criteria, EnumLevel level, String strategy) {
		List<Tuple2<CompositionIdentifier, Double>> list = Lists.newArrayList();
		for (CompositeServiceWrapper wrapper : compositeServiceList) {
			Tuple2<CompositionIdentifier, Double> tuple = new Tuple2<CompositionIdentifier, Double>(wrapper.getCompositionIdentifier(),0.4D);
			list.add(tuple);
		}
		return list;
	}
	
	

	
}
