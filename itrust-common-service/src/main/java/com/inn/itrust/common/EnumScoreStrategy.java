package com.inn.itrust.common;

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

public enum EnumScoreStrategy {

	/**
	 * @see http://en.wikipedia.org/wiki/TOPSIS
	 */
	TOPSIS, // TOPSIS (Technique for Order Preference by Similarity to the Ideal Solution)

	/**
	 * @see <a href="http://en.wikipedia.org/wiki/Weighted_sum_model">http://en.wikipedia.org/wiki/Weighted_sum_model</a>
	 */
	Weighted_sum_model, // Sums weighted values of all features. All features must be scaled to 0..1 scale.

	/**
	 * A modified version of {@link EnumScoreStrategy.Weighted_sum_model} This strategy sums weighted values of all features. All features
	 * values must be scaled to 0..1 scale. Scaling is done by diving a value of a feature with max value of that feature considering values
	 * in a given dataset
	 */
	Weighted_sum_model_to01scale_divMaxInSet,

}
