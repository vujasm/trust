package com.inn.itrust.matchop;

/*
 * #%L
 * itrust-service
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
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.inn.common.WarnException;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.utils.TrustOntologyUtil;
import com.inn.itrust.model.vocabulary.Trust;

/**
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 * 
 */
public class ComparisonMatchOp {

	public double apply(final TrustAttribute reqAttribute, final TrustAttribute attribute) throws Exception {

			RDFDatatype datatype = reqAttribute.getValueDatatype();
			if (isNumericDataType(datatype)) {
				  return compareNumeric(attribute, reqAttribute);
			} else if (isMetricScale(datatype)) {
				String metricValueReqURI = (String) reqAttribute.getValue();
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
	 * @param reqAttribute
	 * @return
	 */
	private double compareNumeric(TrustAttribute attribute, TrustAttribute reqAttribute) {
		Object reqValo = reqAttribute.getValue();
		Object valo = attribute.getValue();
		System.out.println("comparing numeric values: requested <= value "+reqValo+" "+valo);
		double returnValue = 0;
		if (reqValo != null) {
			double v = Double.valueOf(valo.toString()).doubleValue();
			double reqv = Double.valueOf(reqValo.toString()).doubleValue();
			if (reqv <= v) {
				returnValue = v;
			}
		}
		System.out.println("comparing numeric values returns "+returnValue);
		return returnValue;
	}

	/**
	 * Answers is typedliteral datatype is of Trust Metric type.
	 * @param datatype typedliteral datatype
	 * @return true if typedliteral datatype is of Trust Metric type.
	 */
	private boolean isMetricScale(RDFDatatype datatype) {
	  String datatypeURI = datatype.getURI();
	  return TrustOntologyUtil.instance().isIndividualOfTypeIgnoreSuper(URI.create(datatypeURI),Trust.Metric.getURI());
	}

	/**
	 * 
	 * @param datatype
	 * @return
	 */
	private boolean isNumericDataType(RDFDatatype datatype) {
		if (datatype instanceof XSDDouble){
			return true;
		}
		return false;
	}

}
