package com.inn.trusthings.service.command;

/*
 * #%L
 * trusthings-service
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
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.inn.common.OrderType;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.util.tuple.Tuple2;

/**
 * 
 *@author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 *
 */


public class Sort {

	
	public List<Tuple2<URI, Double>> sort(List<Tuple2<Agent, Double>> list, OrderType order) {
		if (order==OrderType.ASC){
			return asc(list);
		}
		else{
			return desc(list);
		}
		
	}
	
	
	private List<Tuple2<URI, Double>> desc(List<Tuple2<Agent, Double>> list) {
		List<Tuple2<URI, Double>> sort = Lists.newArrayList();
		final Ordering<Tuple2<Agent, Double>> o = new Ordering<Tuple2<Agent, Double>>() {
			@Override
			public int compare(Tuple2<Agent, Double> left, Tuple2<Agent, Double> right) {
				return right.getT2().compareTo(left.getT2());
			}
		};
		list = o.sortedCopy(list);
		for (Tuple2<Agent, Double> t : list) {
			URI id = (t.getT1().getCompose_ID())!=null? t.getT1().getCompose_ID():t.getT1().getUri();
			sort.add(new Tuple2<URI, Double>(id, t.getT2()));
		}
		return sort;
	}
	
	
	private List<Tuple2<URI, Double>> asc(List<Tuple2<Agent, Double>> list) {
		List<Tuple2<URI, Double>> sort = Lists.newArrayList();
		final Ordering<Tuple2<Agent, Double>> o = new Ordering<Tuple2<Agent, Double>>() {
			@Override
			public int compare(Tuple2<Agent, Double> left, Tuple2<Agent, Double> right) {
				return left.getT2().compareTo(right.getT2());
			}
		};
		list = o.sortedCopy(list);
		for (Tuple2<Agent, Double> t : list) {
			URI id = (t.getT1().getCompose_ID())!=null? t.getT1().getCompose_ID():t.getT1().getUri();
			sort.add(new Tuple2<URI, Double>( id , t.getT2()));
		}
		return sort;
	}

}
