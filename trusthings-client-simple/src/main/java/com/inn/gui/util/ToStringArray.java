package com.inn.gui.util;

/*
 * #%L
 * trusthings-client-simple
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


import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.service.interfaces.TrustManager;
import com.inn.util.tree.Tree;

public class ToStringArray {
	
	static TrustManager trustManager =  Factory.createInstance(TrustManager.class);
	
	
	public static Object[] getSecurityTechnologies(){
		Tree t= trustManager.obtainTaxonomy(ModelEnum.SecurityOntology.getURI(),"http://www.compose-project.eu/ns/web-of-things/security#SecurityTechnology");
		Object[] array = t.toArray();
		return array;
	}
	
	
	public static Object[] getCAProviders(){
		Tree t= trustManager.obtainTaxonomy(ModelEnum.SecurityOntology.getURI(),"http://www.compose-project.eu/ns/web-of-things/security#CertificateAuthority");
		Object[] array = t.toArray();
		return array;
	}
	
	public static void main(String[] args) {
		Object[] o = getSecurityTechnologies();
		for (int i = 0; i < o.length; i++) {
			System.out.println(o[i]);
		}
	}

}
