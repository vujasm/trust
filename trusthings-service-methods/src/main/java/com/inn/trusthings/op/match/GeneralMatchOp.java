/*
 * Copyright 2014 INNOVA S.p.A

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package com.inn.trusthings.op.match;

/*
 * #%L
 * trusthings-services
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

import java.util.List;

import com.hp.hpl.jena.rdf.model.Resource;
import com.inn.common.ValuesHolder;
import com.inn.trusthings.kb.KnowledgeBaseManager;
import com.inn.trusthings.model.pojo.CertificateAuthorityAttribute;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.utils.TrustOntologyUtil;
import com.inn.trusthings.model.vocabulary.Trust;

/**
 * General match operator as a entry point for specific matchers
 *@author markov
 *
 */
/**
 * @author markov
 *
 */
public class GeneralMatchOp {
	
	
	private KnowledgeBaseManager kbManager; 
	
	private ValuesHolder valuesHolder;
	
	public GeneralMatchOp(KnowledgeBaseManager kbManager, ValuesHolder valuesHolder){
		this.kbManager = kbManager;
		this.valuesHolder = valuesHolder;
	}
	
	/**
	 * 
	 * @param requested Requested attribute
	 * @param attributes List of attributes contained in a trust profile
	 * @return Double value as a matching/evaluation score on the requested attribute
	 * @throws Exception
	 */
	public double apply(TrustAttribute requested, List<TrustAttribute> attributes) throws Exception {
		if (attributes.isEmpty()){
			// If there is no attribute matching requested attribute,then value is
			// 0. this is pessimistic approach.
			// A optimistic approach would be to remove attribute out of
			// consideration/
			return 0;
		}
		if (isSubtype(requested, Trust.UnmeasurableTrustAttribute)) {
			//it is descriptive so then apply specific semantic match depending on the attribute's type
			if (requested instanceof CertificateAuthorityAttribute) {
				return new CertSemanticMatchOp().apply(requested, attributes);
			} else if (isSubtype(requested, Trust.SecurityAttribute)) {
				return new SecSemanticMatchOp(kbManager).apply(requested, attributes);
			} else {
				final SemanticMatchOutput result = new SemanticMatchOp(kbManager).apply(requested, attributes);
				return result.asNumeric();
			}

		} else { // if numeric and measurable then do numeric comparisons
			final ComparisonMatchOp op = new ComparisonMatchOp(valuesHolder);
			// at this point, it is assumed that only and only one exists
			TrustAttribute attribute = attributes.get(0);
			return op.apply(requested, attribute);
		}
	}

	private boolean isSubtype(TrustAttribute reqAttribute, Resource resource) {
		return TrustOntologyUtil.instance().isSubtype(reqAttribute.obtainType().getUri().toString(), resource.getURI());
	}
}
