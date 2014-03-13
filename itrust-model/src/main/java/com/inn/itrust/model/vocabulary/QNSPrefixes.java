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


import java.util.HashMap;
import java.util.Map;


public class QNSPrefixes {
	
	
	public static final Map<String, String> map = new HashMap<String, String>();

    static {
    	map.putAll(NSPrefixes.map);
    	map.put("fn", "http://www.w3.org/2005/xpath-functions#");
    	map.put("apf", "http://jena.hpl.hp.com/ARQ/property#");
    }
	


}
