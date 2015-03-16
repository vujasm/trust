package com.inn.trusthings.op.match;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.inn.common.Const;
import com.inn.common.ValuesHolder;
import com.inn.common.WarnException;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.utils.TrustOntologyUtil;
import com.inn.trusthings.model.vocabulary.Trust;

/**
 * 
 * @author markov
 * 
 */
public class ComparisonMatchOp {

	private static final Logger log = LoggerFactory.getLogger(ComparisonMatchOp.class);

	// //FIXME
	// private static int MAX_NumUsers = 623;
	// private static int MAX_NumCompositions = 2541;

	private ValuesHolder valuesHolder;

	public ComparisonMatchOp(ValuesHolder valuesHolder) {
		this.valuesHolder = valuesHolder;
	}

	public double apply(final TrustAttribute requested, final TrustAttribute attribute) throws Exception {

			RDFDatatype datatype = requested.getValueDatatype();
			String type = attribute.getTypesAll().get(0).getUri().toASCIIString();
			if (isNumericDataType(datatype)) {
					return compareNumeric(attribute, requested, type);
				//not numeric
			} else if (isMetricScale(datatype)) {
				String metricValueReqURI = (String) requested.getValue();
				String metricValuePresentURI = (String) attribute.getValue();
				String metricURI = datatype.getURI();
				return new MetricMatchOp().apply( URI.create(metricURI), URI.create(metricValueReqURI), URI.create(metricValuePresentURI));
			} else {
				throw new WarnException("Comparison unsupported for datatype " + datatype);
			}
	}

	/**
	 * 
	 * @param attribute
	 * @param type
	 * @param reqAttribute
	 * @return
	 */
	private double compareNumeric(TrustAttribute attribute, TrustAttribute requested, String type) {
		log.info("comparing numeric values: requested <= value " + requested.getValue() + " " + attribute.getValue());

		double returnValue = 0;

		if (requested.getValue() == null){
				requested.setValue(0) ;
		}
		// should be greater than expected, and if so, return attribute value
		// normalized.
		if (requested.getValue().equals(0) == false && isNotByMinMax(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double reqestedValue = Double.valueOf(requested.getValue().toString()).doubleValue();
			if (reqestedValue <= value) {
				return cast(normalize(attribute, type).getValue());
			}
		}

		// should be greater than expected, and if so, return 1;
		if (isComparisonByMin(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double requestedMinValue = Double.valueOf(requested.getMinValue().toString()).doubleValue();
			return (value >= requestedMinValue) ? 1 : 0;
		}
		// should be less than expected, and if so, return 1;
		else if (isComparisonByMax(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double requestedMaxValue = Double.valueOf(requested.getMaxValue().toString()).doubleValue();
			return (value <= requestedMaxValue) ? 1 : 0;
		}
		// should be greater in a range, and if so, return 1;
		else if (isComparisonWithinRange(requested)) {
			double value = Double.valueOf(attribute.getValue().toString()).doubleValue();
			double requestedMaxValue = Double.valueOf(requested.getMaxValue().toString()).doubleValue();
			double requestedMinValue = Double.valueOf(requested.getMinValue().toString()).doubleValue();
			return (value >= requestedMinValue && value <= requestedMaxValue) ? 1 : 0;
		}

		log.info("comparing numeric values returns " + returnValue);
		return returnValue;
	}

	private boolean isNotByMinMax(TrustAttribute requested) {
		return (isComparisonWithinRange(requested) == false && isComparisonByMax(requested) == false
			&& isComparisonByMin(requested) == false);
	}

	private boolean isComparisonWithinRange(TrustAttribute requested) {
		return (requested.getMaxValue() != null && requested.getMinValue() != null);
	}

	private boolean isComparisonByMax(TrustAttribute requested) {
		return (requested.getMaxValue() != null && requested.getMaxValue().equals(0) == false)
				&& (requested.getMinValue() == null || requested.getMinValue().equals(0));
	}

	private boolean isComparisonByMin(TrustAttribute requested) {
		return (requested.getMinValue() != null && requested.getMinValue().equals(0) == false)
				&& (requested.getMaxValue() == null || requested.getMaxValue().equals(0));
	}

	private TrustAttribute normalize(TrustAttribute attribute, String type) {
		if (attribute.getValue() != null) {
			attribute.setValue(cast(attribute.getValue()) / getScaleMaxValue(type));
		}
		return attribute;

	}

	private Double cast(Object value) {
		return Double.valueOf(value.toString());
	}

	private Integer getScaleMaxValue(String type) {

		if (type.equals(Trust.ProviderWebReputationBy3rdParty.getURI())) {
			return 100;
		} else if (type.equals(Trust.Reputation.getURI()) || type.equals(Trust.ContractCompliance.getURI())
				|| type.equals(Trust.UserRating.getURI())) {
			return 10;
		} else if (type.equals(Trust.NumberOfDevelopers.getURI())) {
			return (Integer) valuesHolder.getValue(Const.MAX + Trust.NumberOfDevelopers.getLocalName(), 1);
		} else if (type.equals(Trust.NumberOfCompositions.getURI())) {
			return (Integer) valuesHolder.getValue(Const.MAX + Trust.NumberOfCompositions.getLocalName(), 1);
		} else
			return null;
	}

	/**
	 * Answers is typedliteral datatype is of Trust Metric type.
	 * 
	 * @param datatype
	 *            typedliteral datatype
	 * @return true if typedliteral datatype is of Trust Metric type.
	 */
	private boolean isMetricScale(RDFDatatype datatype) {
		String datatypeURI = datatype.getURI();
		return TrustOntologyUtil.instance().isIndividualOfTypeIgnoreSuper(URI.create(datatypeURI),
				Trust.Metric.getURI());
	}

	/**
	 * 
	 * @param datatype
	 * @return
	 */
	private boolean isNumericDataType(RDFDatatype datatype) {
		if (datatype instanceof XSDDouble) {
			return true;
		}
		return false;
	}

}
