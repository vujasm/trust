package com.inn.util.tuple;

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


import java.util.List;

import com.google.common.collect.Lists;

public class ListTuple<T> {

	public static synchronized <T extends Object, T1 extends Object, T2 extends Object> List<T> toList(List<Tuple2<T1, T2>> listTuple,
			TFunctor<T> functor) {
		List<T> list = Lists.newArrayList();
		for (Tuple2<?, ?> t : listTuple) {
			list.add(functor.apply(t));
		}
		return list;
	}



}
