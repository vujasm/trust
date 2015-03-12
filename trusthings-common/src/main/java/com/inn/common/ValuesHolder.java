package com.inn.common;

/*
 * #%L
 * trusthings-common
 * %%
 * Copyright (C) 2014 - 2015 INNOVA S.p.A
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

import com.google.common.collect.Maps;

public class ValuesHolder {
	
	private HashMap<String, Object> hashMap = Maps.newHashMap();
	
	public void setValue(String key, Object value){
		hashMap.put(key, value);
	}
	
	public Object getValue(String key, Object ifnullValue){
		Object val = hashMap.get(key);
		if (val == null)
			return ifnullValue;
		else
			return val;
	}
	

}
