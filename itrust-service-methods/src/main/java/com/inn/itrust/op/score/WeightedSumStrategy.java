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

import com.inn.itrust.common.EnumNormalizationType;
import com.inn.itrust.model.model.Agent;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.util.tuple.Tuple2;

/**
 * 
 * 
 *@author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 * 
 */

public class WeightedSumStrategy extends AbstractScoreStrategy {

	private static final Logger log = LoggerFactory.getLogger(WeightedSumStrategy.class);
	protected EnumNormalizationType enumNormalizationType;
	
	
	@Override
	protected Logger getLogger() {
		return log;
	}


	protected WeightedSumStrategy(final List<TrustAttribute> attributeList, final List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet,
			final EnumNormalizationType enumNormalizationType) {
		super(attributeList, dataSet);
		this.enumNormalizationType = enumNormalizationType;
	}
	
	@Override
	protected void init() {
		identifyMaxValues(dataSet, attributeList);
		identifyMinValues(dataSet, attributeList);
	}

	/**
	 * 
	 * @param agent
	 * @return
	 */
	public Double getScore(Agent agent) {
		final List<Tuple2<TrustAttribute, Double>> agentDataSet = obtainDataSetForAgent(agent);
		Double score = 0D;
		for (TrustAttribute attribute : attributeList) {
				Tuple2<TrustAttribute, Double> t = obtainTuple(agentDataSet, attribute);
				Double scaledVal = scaleTo01(t);
				Double scaledImportance =  t.getT1().getImportance() / weightsSum;
				score = score + (scaledVal * scaledImportance);
				System.out.println("** "+attribute.obtainType().getUri()+" "+score +" as score = "+(score - (scaledVal * scaledImportance))
						+" + ("+scaledVal+" * "+ t.getT1().getImportance()+" / "+weightsSum+")");
		}
		return score;
	}

	
	private Double scaleTo01(Tuple2<TrustAttribute, Double> t) {
		Double attributeValue  = t.getT2();
		if (enumNormalizationType == EnumNormalizationType.Zero_One_by_divMax){
			return (attributeValue/maxValues.get(t.getT1()));
		}
		else{
			return attributeValue;
		}
	}

	
}

// final List<Tuple2<TrustAttribute, Double>> aggregatedList =
// ControlObjects.createListGeneric();
// for (Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>> t :
// dataList) {
// aggregatedList.addAll(t.getT2());
// }
// Ordering<Tuple2<TrustAttribute, Double>> o = new
// Ordering<Tuple2<TrustAttribute,Double>>() {
// @Override
// public int compare(Tuple2<TrustAttribute, Double> left,
// Tuple2<TrustAttribute, Double> right) {
// return left.getT2().compareTo(right.getT2());
// }
// };
// o.max(aggregatedList);
