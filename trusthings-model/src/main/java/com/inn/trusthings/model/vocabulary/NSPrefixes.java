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



import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class NSPrefixes {
	
	public static final Map<String, String> map;

    static {
    	map = new HashMap<String, String>();
    	map.put("rdf", RDF.getURI());
    	map.put("rdfs", RDFS.getURI());
        map.put("trust", Trust.getURI());
    	map.put("dc", DC.getURI());
    	map.put("xsd", XSD.getURI());
    	map.put("owl", OWL.getURI());
    	map.put("usdl-sec", UsdlSec.getURI());
    }

}
