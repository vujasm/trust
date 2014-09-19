package com.inn.trusthings.model.vocabulary;

/*
 * #%L
 * trusthings-model
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

public enum ModelEnum {

	Trust("http://www.compose-project.eu/ns/web-of-things/trust"), 
	SecuritypolicyVocab("http://www.compose-project.eu/ns/web-of-things/securitypolicyvocab"), 
	UsdlSec("http://www.linked-usdl.org/ns/usdl-sec#"), 
	Dul("http://www.loa-cnr.it/ontologies/DUL.owl"), 
	Ssn("http://purl.oclc.org/NET/ssnx/ssn"), 
	SecurityOntology("http://www.compose-project.eu/ns/web-of-things/security"),
	// MergedTrust("http://www.compose-project.eu/ns/web-of-things/mergedtrust", Const.repoOntologies+"mergedtrust.ttl")
	;

	private String URI;

	ModelEnum(String uri) {
		this.URI = uri;
	}

	public String getURI() {
		return URI;
	}

}
