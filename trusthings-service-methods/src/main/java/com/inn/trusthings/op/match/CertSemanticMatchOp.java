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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.trusthings.kb.config.LocationMapping;
import com.inn.trusthings.model.pojo.CertificateAuthorityAttribute;
import com.inn.trusthings.model.pojo.SecurityAttribute;
import com.inn.trusthings.model.pojo.SecurityGoal;
import com.inn.trusthings.model.pojo.SecurityMechanism;
import com.inn.trusthings.model.pojo.SecurityTechnology;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.op.semsim.SemSim;

/**
 * A  semantic match operator for two given Certificate Authority semantic descriptions.
 * 
 * @author markov 
 * 
 */
public class CertSemanticMatchOp {

	private static final Logger log = LoggerFactory.getLogger(CertSemanticMatchOp.class);

	private SemSim semSim = new SemSim(LocationMapping.resolveLocation(ModelEnum.SecurityOntology.getURI()));

	public CertSemanticMatchOp() {
		//---
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	public double apply(TrustAttribute reqAttribute, List<TrustAttribute> attributes) throws Exception {
		double result = 0;
		CertificateAuthorityAttribute reqCertificateAuthorityAttribute = (CertificateAuthorityAttribute) reqAttribute;
		double val1 = chechCAMatch(reqCertificateAuthorityAttribute, attributes);
		double val2 = chechCACountryMatch(reqCertificateAuthorityAttribute, attributes);
		if (val1 == 0 && val2 == 0) {
			return 0;
		}
		result = sumNotNegative(val1, val2) / countNotNegative(val1, val2);
		log.info("CA-SematicMatch result " + result);
		return result;
	}

	private double sumNotNegative(double... val) {
		double sum = 0;
		for (double d : val) {
			if (d >= 0)
				sum = sum + d;
		}
		return sum;
	}

	private int countNotNegative(double... val) {
		int n = 0;
		for (double d : val) {
			if (d >= 0)
				n++;
		}
		return n;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechCAMatch(CertificateAuthorityAttribute reqAttribute, List<TrustAttribute> attributes)
			throws Exception {
		// String concept1 =
		// String concept2 =
		double semsim = 0;
		if (reqAttribute.getCertificateAuthority() == null) {
			log.info("CertificateAuthority matching: no requested so return -1");
			return -1;
		}
		// find the the best possible match (i.e. with most high semsim
		// value)
		for (TrustAttribute a : attributes) {
			final CertificateAuthorityAttribute ca = (CertificateAuthorityAttribute) a;
			double result = semSim.compute(URI.create(reqAttribute.getCertificateAuthority()), URI.create(ca.getCertificateAuthority()));
			if (result > semsim) {
				{
					semsim = result;
				}
			}
		}

		log.info("CertificateAuthority matching: requested and returns " + semsim);
		return semsim;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechCACountryMatch(CertificateAuthorityAttribute reqAttribute, List<TrustAttribute> attributes)
			throws Exception {
		// String concept1 =
		// String concept2 =
		double semsim = 0;
		if (reqAttribute.getCountry() == null) {
			log.info("CertificateAuthority-Country matching: no requested so return -1");
			return -1;
		}
		// find the the best possible match (i.e. with most high semsim
		// value)
		for (TrustAttribute a : attributes) {
			final CertificateAuthorityAttribute ca = (CertificateAuthorityAttribute) a;
			double result = semSim.compute(URI.create(reqAttribute.getCountry()), URI.create(ca.getCountry()));
			if (result > semsim) {
				{
					semsim = result;
				}
			}
		}

		log.info("CertificateAuthority-Country matching: requested and returns " + semsim);
		return semsim;
	}

}
