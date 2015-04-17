package com.inn.trusthings.service.mgrs.impl;

import java.util.List;

import com.inn.common.CompositeServiceWrapper;
import com.inn.common.CompositionIdentifier;
import com.inn.trusthings.json.TrustPOJOFactory;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.service.config.GlobalTrustCriteria;
import com.inn.trusthings.service.interfaces.TrustCompositionManager;
import com.inn.util.tuple.Tuple2;


/*
 * #%L
 * trusthings-methods
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



public class TrustCompositionManagerImpl implements TrustCompositionManager{
	
	private TrustCriteria globalTrustCriteria = GlobalTrustCriteria.instance();

	@Override
	public void setGlobalTrustCriteria(TrustCriteria criteria) {
		this.globalTrustCriteria = criteria;
	}

	@Override
	public void setGlobalTrustCriteria(String criteriaAsJson) {
		TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(criteriaAsJson);
		this.globalTrustCriteria = criteria;
	}

	@Override
	public TrustCriteria getGlobalTrustCriteria() {
		return this.globalTrustCriteria;
	}

	@Override
	public List<CompositionIdentifier> filterTrustedByThreshold(List<CompositeServiceWrapper> compositeServiceList,
			TrustCriteria criteria, String level, String strategy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple2<CompositionIdentifier, Double>> obtainTrustIndexes(
			List<CompositeServiceWrapper> compositeServiceList, TrustCriteria criteria, String level, String strategy) { 
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
}
