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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.open.kmi.iserve.commons.io.Syntax;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.inn.common.Const;
import com.inn.common.OrderType;
import com.inn.common.ValuesHolder;
import com.inn.trusthings.collector.ValuesHolderLoader;
import com.inn.trusthings.kb.KnowledgeBaseManager;
import com.inn.trusthings.kb.RDFModelsHandler;
import com.inn.trusthings.kb.SharedOntModelSpec;
import com.inn.trusthings.kb.config.LocationMapping;
import com.inn.trusthings.model.expression.Element;
import com.inn.trusthings.model.expression.Expression;
import com.inn.trusthings.model.expression.ExpressionBuilder;
import com.inn.trusthings.model.expression.OrElement;
import com.inn.trusthings.model.expression.SingleElement;
import com.inn.trusthings.model.io.ToModelParser;
import com.inn.trusthings.model.io.ext.SecGuaranteeToModel;
import com.inn.trusthings.model.io.ext.SecProfileExpressionToModel;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.model.pojo.TResource;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.model.pojo.TrustProfile;
import com.inn.trusthings.model.utils.TrustOntologyUtil;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.op.enums.EnumScoreStrategy;
import com.inn.trusthings.op.match.GeneralMatchOp;
import com.inn.trusthings.op.score.AbstractScoreStrategy;
import com.inn.trusthings.op.score.ScoreStrategyFactory;
import com.inn.trusthings.service.command.Sort;
import com.inn.trusthings.service.interfaces.RankingManager;
import com.inn.util.tuple.Tuple2;


/**
 * Implementation of RankingManager interface
 * @author markov
 *
 */
public class BasicRankingManager implements RankingManager {

	private ToModelParser parser = null;
	private KnowledgeBaseManager knowledgeBaseManager;
	private static final Logger log = LoggerFactory.getLogger(BasicRankingManager.class);
	
	private boolean rigorousEval = false;

	@Inject
	protected BasicRankingManager(EventBus eventBus, KnowledgeBaseManager kbManager) throws Exception {
		this.knowledgeBaseManager = kbManager;
		
//		OntModel model = kbManager.getModel(ModelEnum.Trust.getURI(), OntModelSpec.OWL_MEM_TRANS_INF);
		OntModel model = RDFModelsHandler.getGlobalInstance().fetchOntologyFromLocalLocation(ModelEnum.Trust.getURI(), 
				Syntax.TTL.getName(), OntModelSpec.OWL_MEM_TRANS_INF);
		TrustOntologyUtil.init(model);
	}

	/**
	 * 
	 */
	@Override
	public List<Tuple2<URI, Double>> rankServiceModels(List<Model> models, TrustCriteria trustCriteria, EnumScoreStrategy strategy,  boolean filterByAttributeMissing, boolean filterByCriteriaNotMet, OrderType order) throws Exception {
		
//		Stopwatch timer = new Stopwatch().start();
		//FIXME !!!! filterByCriteriaNotMet
		boolean rigorous = filterByCriteriaNotMet;
		
		List<SingleElement> listCriteria = trustCriteria.getListOperandByAnd();
		
		List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSetMain = prepareDataset(models,listCriteria, filterByAttributeMissing, rigorous);
//		timer.stop();
//		log.info("preparedDataset total time: "+timer.elapsed(TimeUnit.MILLISECONDS));
//		timer.reset().start();
		
		List<OrElement> listORs = trustCriteria.getListOperandByOrGroup();
		for (OrElement or : listORs) {
			Double weightOr = or.getWeight();
			List<SingleElement> elements= or.getElements();
			List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSetOr = prepareDataset(models,elements, filterByAttributeMissing, rigorous);
			updateMainDataSet(dataSetMain, dataSetOr, listCriteria, weightOr);
//			for (Tuple2<Agent, Double> t : scores) {
//				updateScores(dataSet, strategy, t);
//			}
		}
		
		List<Tuple2<Agent, Double>> scores = obtainScores(listCriteria, dataSetMain, strategy);
		
//		timer.stop();
//		log.info("obtainedScores  total time:"+timer.elapsed(TimeUnit.MILLISECONDS));
//		timer.reset().start();
		final List<Tuple2<URI, Double>> sortedList = new Sort().sort(scores, order);
//		timer.stop();
//		log.info("sorted  total time:"+timer.elapsed(TimeUnit.MILLISECONDS));
		printRank(sortedList);
		return sortedList;
	}
	
	private void updateMainDataSet(List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSetMain,
			List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSetOr, List<SingleElement> listCriteria, Double weightOr) {
		
		TrustAttribute criteriaAttribute = null;
		for (Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> tor : dataSetOr) {
			Agent agent = tor.getT1();
			for (Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> tmain : dataSetMain) {
				if (agent.getUri().equals(tmain.getT1().getUri())){
					Tuple2<TrustAttribute, Double> best = findBestScoreInOrDS(tor.getT2(), criteriaAttribute);
					if (best != null){
						best.getT1().setImportance(weightOr);
						tmain.getT2().add(best);
						criteriaAttribute = best.getT1();
					}
				}
			}
		}
		if (criteriaAttribute!=null){
			listCriteria.add(new SingleElement(criteriaAttribute));
		}
	}

	

	private Tuple2<TrustAttribute, Double> findBestScoreInOrDS(List<Tuple2<TrustAttribute, Double>> t2, TrustAttribute criteriaAttribute) {
		Tuple2<TrustAttribute, Double> best = null;
		for (Tuple2<TrustAttribute, Double> current : t2) {
			if ( best == null){
				best = current;
			}
			if (current.getT2()!=null) {
				Double d1 = best.getT2() * best.getT1().getImportance() ;
				Double d2 =  current.getT2() * current.getT1().getImportance();
				if (d1 < d2){
					best = current;
				}
			}
		}
		if (best!=null){
			Double scoreWeighted = best.getT2() * best.getT1().getImportance();
			best.setT2(scoreWeighted);
		}
		if (best != null && criteriaAttribute != null){
			best.setT1(criteriaAttribute);
		}
		return best;
	}


//	//TODO to remove?
//	protected List<TrustProfile> modelsToTrustPOJO(List<Model> models) throws Exception{
//		List<TrustProfile> list = Lists.newArrayList();
//		for (Model model : models) {
//			ToModelParser parser = getOrCreateToModelParser();
//			TrustProfile trustProfile = parser.parse(model);
//			if (trustProfile!=null)
//				list.add(trustProfile);
//		}
//		return list;
//	}

	private List<Tuple2<Agent, Double>> obtainScores(List<SingleElement> listCriteria, List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet,EnumScoreStrategy strategy) {
		
		final AbstractScoreStrategy scoreStrategy = ScoreStrategyFactory.createScoreStrategy(listCriteria, dataSet, strategy);
		
		List<Tuple2<Agent, Double>> listAgentScore = Lists.newArrayList();
		
		for (Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> dataAgent : dataSet) {
			final Agent agent = dataAgent.getT1();
			if (dataAgent.getT2().isEmpty() == false){
				Tuple2<Agent, Double> result = computeIndex(agent, scoreStrategy);
				listAgentScore.add(result);
			}
			else{
				//return -1 if no data/information about resource
				String agentURI = agent.getUri().toASCIIString();
				log.info("Trust profile of resource with URI "+agentURI+" has no any data. Its trust index is -1");
				Tuple2<Agent, Double> result = new Tuple2<Agent, Double>(agent, -1D);
				listAgentScore.add(result);
			}
		}
		return listAgentScore;
	}

	
	/**
	 * 	Prepares data set. It may exclude agents that have no requested attribute if filterByAttributeMissing true, \
	 * or evaluate as zero if rigorous is true and attributed evaluated lower than expected 
	 * @param models
	 * @param trustProfileRequired
	 * @param filterByAttributeMissing
	 * @param rigorous
	 * @return
	 */
	private List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> prepareDataset(List<Model> models, List<SingleElement> listCriteria, boolean filterByAttributeMissing, boolean rigorous) {
		
		rigorousEval = rigorous;
		
		List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet = Lists.newArrayList();
		try {
			for (Model model : models) {
				ToModelParser parser = getOrCreateToModelParser();
				TrustProfile trustProfile = parser.parse(model);
				if (trustProfile!=null){
					final List<Tuple2<TrustAttribute, Double>> listEA = evaluateAttributes(trustProfile, listCriteria, filterByAttributeMissing);
					if (listEA != null){ //listEA is null in a case when filterIfMissingAttribute=true and Agent has no some requested attribute
						final Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> t = new Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>(
							trustProfile.getAgent(), listEA);
						dataSet.add(t);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			rigorousEval = false;
		}
		return dataSet;

	}

	
	private void printRank(List<Tuple2<URI, Double>> set) {
		log.info("******** <ranking output> ************");
		for (Tuple2<URI, Double> t : set) {
			log.info(t.getT1() + " score " + t.getT2());
		}
		log.info("********  <ranking output> ************");
	}

	
	private Tuple2<Agent, Double> computeIndex(Agent agent, AbstractScoreStrategy scoreStrategy) {
		Double score = scoreStrategy.getScore(agent);
		return new Tuple2<Agent, Double>(agent, score);
	}

	

	/**
	 * Evaluates attributes by calling for each attribute match(), either returns numerical value for measurable attributes
	 * or return estimated similarity in case of semantic descriptions (non measurable attributes)
	 * @param profile
	 * @param requestedProfile
	 * @param filterIfMissingAttribute 
	 * @return
	 */
	private List<Tuple2<TrustAttribute, Double>> evaluateAttributes(TrustProfile profile, List<SingleElement> listCriteria, boolean filterIfMissingAttribute) throws Exception{
		
		List<Tuple2<TrustAttribute, Double>> list = Lists.newArrayList();
		
		if (profile.getAttributes().isEmpty() == false) {
			for (Element element : listCriteria) {
				TrustAttribute requestedAttr = ((SingleElement)element).getAttribute();
				TResource type = requestedAttr.obtainType();
				log.info("evaluting " + type.getUri() + " for " + profile.getAgent().getUri());
				final List<TrustAttribute> attributes = TrustOntologyUtil.instance().filterByTypeDirect(profile.getAttributes(), type.getUri());
				log.info("they will be evaluated w.r.t " + attributes);
				final double value = match(requestedAttr, attributes);
				if (filterIfMissingAttribute && value == 0) {
					return null;
				} else if (rigorousEval && value==0){
					return null;
				} else
				{
					list.add(new Tuple2<TrustAttribute, Double>(requestedAttr, Double.valueOf(value)));
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @param requested required attributes with required value and importance
	 * @param attributes list of attributes of a resource
	 * @return
	 */
	private double match(final TrustAttribute requested, final List<TrustAttribute> attributes) throws Exception{
		ValuesHolder valuesHolder = new ValuesHolderLoader().loadValues(); 
		GeneralMatchOp operator = new GeneralMatchOp(knowledgeBaseManager, valuesHolder);
		double result = operator.apply(requested, attributes);
		return result;
	}

	
	private ToModelParser getOrCreateToModelParser() {
		if (parser == null) {
			parser = new ToModelParser();
//			String uri = ModelEnum.SecurityOntology.getURI();
//			OntModel securityProfileModel = knowledgeBaseManager.getModel(uri, RDFModelsHandler.getGlobalInstance());
//			SecProfileExpressionToModel secProfileExpressionToModel = new SecProfileExpressionToModel(securityProfileModel);
//			parser.registerSpecificParser(secProfileExpressionToModel, Const.parserNameSecurityProfileAsUSDLSecExpression);
			SecGuaranteeToModel secGuaranteeToModel = new SecGuaranteeToModel();
			parser.registerSpecificParser(secGuaranteeToModel, Const.parserNameSecurityGuarantee);
		}
		return parser;
	}

	

}
