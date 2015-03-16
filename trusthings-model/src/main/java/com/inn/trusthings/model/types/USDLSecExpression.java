package com.inn.trusthings.model.types;

/*
 * #%L
 * trusthings-model
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


import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.RDFDatatype;

public class USDLSecExpression extends BaseDatatype{

	public USDLSecExpression(String uri) {
		super(uri);
	}
	
	public static final String theTypeURI = "http://www.compose-project.eu/ns/web-of-things/security/profiles#USDLSecType";
    public static final RDFDatatype TYPE = new USDLSecExpression(theTypeURI);
    
    @Override
    public String getURI() {
    	return super.getURI();
    }


}
