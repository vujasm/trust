package com.inn.itrust.model.vocabulary;

/*
 * #%L
 * itrust-model
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


import com.inn.common.Const;


public enum ModelsNSEnum {
	
	Trust("http://www.compose-project.eu/ns/web-of-things/trust",  Const.repoOntologies+"trustontology.ttl"),
	SecuritypolicyVocab("http://www.compose-project.eu/ns/web-of-things/securitypolicyvocab", Const.repoOntologies+"securitypolicyvocab.ttl"),
	UsdlSec("http://www.linked-usdl.org/ns/usdl-sec#",Const.repoOntologies+"usdl-sec.ttl"),
	Dul("http://www.loa-cnr.it/ontologies/DUL.owl", "http://www.loa-cnr.it/ontologies/DUL.owl"),
	Ssn("http://purl.oclc.org/NET/ssnx/ssn", Const.repoOntologies+"ssn.owl"), 
	SecurityProfiles ("http://www.compose-project.eu/ns/web-of-things/security/profiles", Const.repoOntologies+"securityprofiles.ttl"),
	//MergedTrust("http://www.compose-project.eu/ns/web-of-things/mergedtrust", Const.repoOntologies+"mergedtrust.ttl")
	;
	
	//FIXME reimenuj ns u URI, mozda
	private  String ns; //namespace
	
	private String location;
	
	ModelsNSEnum(String ns, String loc){
		this.ns = ns;
		this.location = loc;
//		System.out.println(loc);
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getLocationNoProtocol(){
		return location.replace("file:///", "");
	}
	
	public String getNS() {
		return ns;
	}

}
