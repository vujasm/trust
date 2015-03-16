package com.inn.trusthings.op.propagation;

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


import com.inn.trusthings.model.pojo.Agent;
public class TrustAssessmentManagerImpl implements TrustAssessmentManager{

	@Override
	public void assessIt(Agent agent) {
		
		if ( isComposed(agent) == false) return ;
		
		// decompose & analize
		
	}

	private boolean isComposed(Agent agent) {
		//TODO isComposed of TrustAssessmentManagerImpl implement me
		/*
		 * there should be info if agent is composed
		 */
		return true;
	}

}
