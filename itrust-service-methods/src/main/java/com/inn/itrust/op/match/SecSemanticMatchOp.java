package com.inn.itrust.op.match;

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
import java.util.List;

import org.slf4j.LoggerFactory;

import com.inn.itrust.common.LocationMapping;
import com.inn.itrust.common.service.KnowledgeBaseManager;
import com.inn.itrust.model.model.SecurityAttribute;
import com.inn.itrust.model.model.SecurityGoal;
import com.inn.itrust.model.model.SecurityMechanism;
import com.inn.itrust.model.model.SecurityTechnology;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.vocabulary.ModelEnum;
import com.inn.itrust.op.semsim.SemSim;

/**
 * A specialized semantic match operator that semantically matches two given security semantic descriptions.
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 * 
 */
public class SecSemanticMatchOp {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(SecSemanticMatchOp.class);

	private SemSim semSim = new SemSim(LocationMapping.resolveLocation(ModelEnum.SecurityProfiles.getURI()));
	
	@SuppressWarnings("unused")
	private KnowledgeBaseManager kbManager;

	public SecSemanticMatchOp(KnowledgeBaseManager kbManager) {
		this.kbManager = kbManager;
	}


	// TODO consider to enhance result by implementing support for graph similarity matching
	// by SML (Semantic Measures Library) and/or graph querying
	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	public double apply(TrustAttribute reqAttribute, List<TrustAttribute> attributes) throws Exception {
		double result = 0;
		SecurityAttribute reqSecurityAttribute = (SecurityAttribute) reqAttribute;
		double val1 = chechGoalMatch(reqSecurityAttribute, attributes);
		double val2 = chechMechanismMatch(reqSecurityAttribute, attributes);
		double val3 = chechTechnologyMatch(reqSecurityAttribute, attributes);
		if (val1 == 0 && val2 == 0 && val3 ==0){
			return 0;
		}
		result = sumNotNegative(val1, val2, val3) / countNotNegative(val1, val2, val3);
		System.out.println("SecSematicMatch result "+result);
		return result;
	}


	private double sumNotNegative(double... val) {
		double sum = 0;
		for (double d : val) {
			if (d >=0) sum = sum + d;
		}
		return sum;
	}

	private int countNotNegative(double... val) {
		int n = 0;
		for (double d : val) {
			if (d >=0) n++;
		}
		return n;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechTechnologyMatch(SecurityAttribute reqAttribute, List<TrustAttribute> attributes) throws Exception {
		// String concept1 =
		// String concept2 =
		double semsim = 0;
		final List<SecurityMechanism> mechanismsR = reqAttribute.getImplementedBy();
		if (mechanismsR.isEmpty()) {
			log.info("Doing security technology match, but there was no security mechanism matching: no requested so returns -1");
			return -1;
		}

		for (SecurityMechanism mechanism : mechanismsR) {
			List<SecurityTechnology> securityTechnologies = mechanism.getRealizedByTechnology();
			if (securityTechnologies.isEmpty()) {
				log.info("Security technology matching: no requested so return -1");
				return -1;
			}
			for (SecurityTechnology securityTechnologyR : securityTechnologies) {
				// find the the best possible match (i.e. with most high semsim value)
				for (TrustAttribute a : attributes) {
					final SecurityAttribute securityAttribute = (SecurityAttribute) a;
					List<SecurityMechanism> mechanisms2 = securityAttribute.getImplementedBy();
					for (SecurityMechanism mechanism2 : mechanisms2) {
						List<SecurityTechnology> technologies = mechanism2.getRealizedByTechnology();
						for (SecurityTechnology securityTechnology : technologies) {
							double result = invokeSemSim(securityTechnologyR.getUri(), securityTechnology.getUri());
							if (result > semsim) {
								{
									semsim = result;
								}
							}
						}
					}

				}

			}
		}

		log.info("Security technology matching: requested and returns " + semsim);
		return semsim;
	}


	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechMechanismMatch(SecurityAttribute reqAttribute, List<TrustAttribute> attributes) {
		boolean found = false;
		for (TrustAttribute a : attributes) {
			final List<SecurityMechanism> mechanismsNeeded = reqAttribute.getImplementedBy();
			if (mechanismsNeeded.isEmpty()) {
				log.info("Security mechanism matching: no requested so return -1");
				return -1;
			}
			for (SecurityMechanism mechanism : mechanismsNeeded) {
				final SecurityAttribute securityAttribute = (SecurityAttribute) a;
				List<SecurityMechanism> mechanisms2 = securityAttribute.getImplementedBy();
				for (SecurityMechanism mechanism2 : mechanisms2) {
					if (mechanism.obtainType() == null){
						log.info("Security mechanism matching: no requested so return -1");
						return -1;
					}
					if (mechanism.obtainType().getUri().equals(mechanism2.getUri())) {
						found = true;
					}
				}
			}
		}
		log.info("Security mechanism matching: requested and returns " + ((found) ? 1 : 0));
		return (found) ? 1 : 0;
	}

	/**
	 * 
	 * @param reqAttribute
	 * @param attributes
	 * @return
	 */
	private double chechGoalMatch(SecurityAttribute reqAttribute, List<TrustAttribute> attributes) {
		boolean found = false;
		for (TrustAttribute a : attributes) {
			final List<SecurityGoal> goalsNeeded = reqAttribute.getSecurityGoals();
			if (goalsNeeded.isEmpty()) {
				log.info("Security goal matching: no requested so returns -1");
				return -1;
			}
			for (SecurityGoal securityGoal : goalsNeeded) {
				final SecurityAttribute securityAttribute = (SecurityAttribute) a;
				List<SecurityGoal> goals = securityAttribute.getSecurityGoals();
				for (SecurityGoal securityGoal2 : goals) {
					if (securityGoal.getUri().equals(securityGoal2.getUri())) {
						found = true;
					}
				}
			}
		}
		log.info("Security goal matching: requested and returns " + ((found) ? 1 : 0));
		return (found) ? 1 : 0;
	}
	

	private double invokeSemSim(URI uri1, URI uri2) throws Exception{
		/*
		 * TODO before calling semsim, it should be checked for disjointness and if disjoint return 0. 
		 * The check for disjointness is slow with Jena but DL reasoners (e.g. Fact++) could be of better value. 
		 * For the moment, we set disjointness threshold on a semsim result
		 */
		double threshold = 0.45;
		double result = semSim.apply(uri1.toString(), uri2.toString());
		return (result>=threshold)? result:0;
	}

}
