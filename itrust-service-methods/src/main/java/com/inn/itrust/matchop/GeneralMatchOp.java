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

import java.util.List;

import com.inn.common.match.SemanticMatchOutput;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.utils.TrustOntologyUtil;
import com.inn.itrust.model.vocabulary.Trust;
import com.inn.itrust.service.mgrs.KnowledgeBaseManager;

/**
 * TODO describe me
 *@author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
/**
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */
public class GeneralMatchOp {
	
	
	private KnowledgeBaseManager kbManager; 
	
	public GeneralMatchOp(KnowledgeBaseManager kbManager){
		this.kbManager = kbManager;
	}
	
	public double apply(TrustAttribute reqAttribute, List<TrustAttribute> attributes) throws Exception {
		if (attributes.isEmpty()){
			//TODO
			// If there is no attribute matching requested attribute,then value is
			// 0. this is pessimistic approach.
			// A optimistic approach would be to remove attribute out of
			// consideration/
			return 0;
		}
		if (TrustOntologyUtil.instance().isSubtype(reqAttribute.obtainType().getUri().toString(), 
					Trust.UnmeasurableTrustAttribute.getURI())) {//if descriptive then sem match
			if (TrustOntologyUtil.instance().isSubtype(reqAttribute.obtainType().getUri().toString(), 
					Trust.SecurityAttribute.getURI())) {
				return new SecSemanticMatchOp(kbManager).apply(reqAttribute, attributes);
			}else{
				final SemanticMatchOutput result = new SemanticMatchOp(kbManager).apply(reqAttribute, attributes);
				return result.asNumeric();
			}
			
		} else { // if numeric and measurable then comparison
			final ComparisonMatchOp op = new ComparisonMatchOp();
			// at this point, it is assumed that only and only one exists
			TrustAttribute attribute = attributes.get(0);
			return op.apply(reqAttribute, attribute);
		}
	}
}
