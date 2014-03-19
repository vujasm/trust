package com.inn.itrust.op.score;

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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.common.Tuple2;
import com.inn.itrust.model.model.Agent;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.utils.TrustOntologyUtil;

/**
 * 
 * An implementation of TOPSIS (Technique for Order Preference by Similarity to the Ideal Solution) method for getting the score of a given
 * alternative among multiple alternatives. TOPSIS ranks alternative by their distance from ideal solution and worst solution. An
 * alternative closest to the ideal solution and farthest from the worst solution is ranked as a first.
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 * 
 */
public class TopsisScoreStrategy extends AbstractScoreStrategy {

	private static final Logger log = LoggerFactory.getLogger(TopsisScoreStrategy.class);

	protected TopsisScoreStrategy(List<TrustAttribute> attributeList, List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet) {
		super(attributeList, dataSet);
	}

	protected void init() {
		List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> normalizeddataSet = vectorNormalizationAndWeightening(dataSet, attributeList);
		maxValues = identifyMaxValues(normalizeddataSet, attributeList);
		minValues = identifyMinValues(normalizeddataSet, attributeList);
	}

	/**
	 * Performs vector normalization to deal with their incongruous criteria dimensions. Vector normalization is calculated using the
	 * following formula: r_{ij} = {x_{ij}} / {sqrt{sum_{i=1}^{m} x_{ij}^2 }}, i = 1, 2, . . ., m, j = 1, 2, . . ., n
	 * 
	 * @param dataSet a data set
	 * @param attributeList list of dimensions of interest
	 * @return
	 */
	private List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> vectorNormalizationAndWeightening(
			List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet, List<TrustAttribute> attributeList) {

		for (TrustAttribute attribute : attributeList) {
			Double sumOfPowValue = 0D;
			// find sum of powered value of attribute
			for (Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> agentDataTuple : dataSet) {
				List<Tuple2<TrustAttribute, Double>> list = agentDataTuple.getT2();
				for (Tuple2<TrustAttribute, Double> attributeValTuple : list) {
					if (TrustOntologyUtil.instance().sameURI(attribute, attributeValTuple.getT1())) {
						Double x = attributeValTuple.getT2();
						Double powX = Math.pow(x, 2);
						sumOfPowValue = sumOfPowValue + powX;
					}
				}
			}
			// now each value should be normalized using x / Math.sqrt(sumOfPowValue),
			// and then such value should be multiplied with the attribute
			// importance (weight)
			Double normalValue = Math.sqrt(sumOfPowValue);
			for (Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> agentDataTuple : dataSet) {
				List<Tuple2<TrustAttribute, Double>> list = agentDataTuple.getT2();
				for (Tuple2<TrustAttribute, Double> attributeValTuple : list) {
					if (TrustOntologyUtil.instance().sameURI(attribute, attributeValTuple.getT1())) {
						if (attributeValTuple.getT2()==0 || normalValue == 0 || attribute.getImportance() == 0){
							attributeValTuple.setT2(0D);
						}else{
							Double r = (attributeValTuple.getT2() / normalValue) * attribute.getImportance();
							attributeValTuple.setT2(r);
						}
					}
				}
			}
		}

		return dataSet;
	}

	/**
	 * Performs linear normalization to deal with their incongruous criteria dimensions. Linear normalization is calculated using the
	 * following formula: r_{ij} = x_{ij} / pmax(v_j), i = 1, 2, . . ., m, j = 1, 2, . . ., n, where pmax( v_j ) is the maximum possible
	 * value of the indicator v_j, j = 1, 2, . . ., n.
	 * 
	 * @param dataSet a data set
	 * @param attributeList list of dimensions of interest
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> linearNormalizationAndWeightening(
			List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet, List<TrustAttribute> attributeList) {
		// TODO - not implemented, and probably not will ever be
		return null;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

	@Override
	public Double getScore(Agent agent) {
		Double distanceFromIdealPositive = obtainDistancefromIdealPositive(agent);
		Double distanceFromIdealNegative = obtainDistancefromIdealNegative(agent);
		Double score = (distanceFromIdealNegative / (distanceFromIdealPositive + distanceFromIdealNegative));
		/*
		 * In cases where all solutions that we compare have exactly the same values for all alternative,
		 * then their relative distance from ideal solution is 0/0.
		 */
		if (distanceFromIdealNegative == 0 && distanceFromIdealPositive == 0) {
			return 0D;
		}
		return score;
	}

	private Double obtainDistancefromIdealPositive(Agent agent) {
		Double value = 0D;
		final List<Tuple2<TrustAttribute, Double>> agentDataSet = obtainDataSetForAgent(agent);
		for (TrustAttribute attribute : attributeList) {
			Double powDistance = 0D;
			for (Tuple2<TrustAttribute, Double> tuple2 : agentDataSet) {
				if (TrustOntologyUtil.instance().sameURI(attribute, tuple2.getT1())) {
					Double maxVal = getMaxValue(attribute);
					Double distance = tuple2.getT2() - maxVal;
					powDistance = Math.pow(distance, 2);
				}
			}
			// System.out.println(value + powDistance );
			value = value + powDistance;
		}
		return Math.sqrt(value);
	}

	private Double obtainDistancefromIdealNegative(Agent agent) {
		Double value = 0D;
		final List<Tuple2<TrustAttribute, Double>> agentDataSet = obtainDataSetForAgent(agent);
		for (TrustAttribute attribute : attributeList) {
			Double powDistance = 0D;
			for (Tuple2<TrustAttribute, Double> tuple2 : agentDataSet) {
				if (TrustOntologyUtil.instance().sameURI(attribute, tuple2.getT1())) {
					Double minVal = getMinValue(attribute);
					Double distance = tuple2.getT2() - minVal;
					powDistance = Math.pow(distance, 2);
				}
			}
			value = value + powDistance;
		}
		return Math.sqrt(value);
	}

	private Double getMaxValue(TrustAttribute attribute) {
		Double val = maxValues.get(attribute);
		return (val != null) ? val : 0D;
	}

	private Double getMinValue(TrustAttribute attribute) {
		Double val = minValues.get(attribute);
		return (val != null) ? val : 1D; // FiXME - provjeri da li da vrati 1 ili 0, ili nesto trece
	}

}
