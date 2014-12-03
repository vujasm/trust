package com.inn.trusthings.service.collectors;

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


import com.hp.hpl.jena.rdf.model.Model;
import com.inn.trusthings.db.ABridge;


public class InternalCollector extends AbstractCollector {
	
	ABridge b = new com.inn.trusthings.db.BridgeWS();

	public InternalCollector(String sourceUri) {
		super(sourceUri);
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		return b.obtainTrustProfileFixID((resourceIdentifier));
	}

	@Override
	public String getName() {
		return "InternalCollector";
	}
	
	@Override
	public void shutDown() {
		b.stop();
	}

}
