package com.inn.trusthings.op.score;

/*
 * #%L
 * trusthings-methods
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

import com.inn.trusthings.model.expression.Element;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.op.enums.EnumNormalizationType;
import com.inn.trusthings.op.enums.EnumScoreStrategy;
import com.inn.util.tuple.Tuple2;

/**
 * Factory for Trust Scorers
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class ScoreStrategyFactory {
	
	
	public static synchronized AbstractScoreStrategy createScoreStrategy(List<Element> listCriteria,  List<Tuple2<Agent, List<Tuple2<TrustAttribute, Double>>>> dataSet,  EnumScoreStrategy scoreStrategy){
		
		if (scoreStrategy == EnumScoreStrategy.TOPSIS){
			return new TopsisScoreStrategy(listCriteria, dataSet);
		}
		if (scoreStrategy == EnumScoreStrategy.Weighted_sum_model){
			return new WeightedSumStrategy(listCriteria, dataSet, EnumNormalizationType.Zero_One);
		}
		if (scoreStrategy == EnumScoreStrategy.Weighted_sum_model_to01scale_divMaxInSet){
			return new WeightedSumStrategy(listCriteria, dataSet, EnumNormalizationType.Zero_One_by_divMax);
		}
		//default strategy is a Weighted Sum Strategy with 0..1 scaling and all feature having 1 as max value and 0 as min value.
		return new WeightedSumStrategy(listCriteria, dataSet, EnumNormalizationType.Zero_One);
	}

}
