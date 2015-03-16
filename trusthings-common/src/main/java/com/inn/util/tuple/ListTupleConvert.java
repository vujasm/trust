package com.inn.util.tuple;

/*
 * #%L
 * trusthings-common
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


import java.lang.reflect.Method;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * ListTupleConvert util
 *@author markov
 *
 */
public class ListTupleConvert {
	
	@SuppressWarnings("unchecked")
	public static synchronized <Element extends Object> List<Element> toListOfTupleElement(List<?> setOFTuples, int element){
		List<Element> set = Lists.newArrayList();
		for (Object tuple : setOFTuples) {
			try {
				Method  m = tuple.getClass().getMethod("getT"+Integer.valueOf(element).toString(), new Class<?>[0]);
				Element result = (Element) m.invoke(tuple, new Object[0]);
				set.add(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return set;
	}

}
