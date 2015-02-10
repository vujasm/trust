package com.inn.util.uri;

/*
 * #%L
 * trusthings-common
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


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.inn.common.Const;

public class UIDGenerator {
	
	public static UIDGenerator  instanceRequest = new UIDGenerator("http://localhost/requests#");
	public static UIDGenerator instanceTrust = new UIDGenerator("http://localhost/trustdata#");
	
	private String baseURI = "http://localhost/triples#" ;
	
	@SuppressWarnings("unused")
	private  Map<Class<?>, Long> map = Maps.newHashMap();
	 
	public UIDGenerator(String baseURI) {
		this.baseURI = baseURI;
	}
	
	public  URI create(Class<?> clazz){
		UUID uid = getUUID(clazz); 
		
		String str = baseURI 
				+
				clazz.getSimpleName()+Const.underScore+uid.toString();
		try {
			return new URI(str);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static UUID getUUID(Class<?> clazz) {
		return UUID.randomUUID();
	}

}
