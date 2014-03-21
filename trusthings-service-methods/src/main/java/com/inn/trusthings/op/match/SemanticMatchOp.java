package com.inn.trusthings.op.match;

/*
 * #%L
 * trusthings-service
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

import org.slf4j.LoggerFactory;

import com.inn.trusthings.model.pojo.TrustAttribute;

/**
 * A semantic match operator that semantically matches two given semantic descriptions.
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 * 
 */
public class SemanticMatchOp {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(SemanticMatchOp.class);

	public SemanticMatchOp(com.inn.trusthings.service.kb.KnowledgeBaseManager kbManager) {
//		this.kbManager = kbManager;
	}

	public SemanticMatchOutput apply(TrustAttribute reqAttribute, List<TrustAttribute> attributes) {
		// TODO implement me
		log.warn("SemanticMatchOp is not implemented. Returned value is SemanticMatchOutput.Sem_Exact");
		return SemanticMatchOutput.Sem_Exact;
	}
}
