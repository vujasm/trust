package com.inn.itrust.op.propagation;

/*
 * #%L
 * itrust-methods
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


import com.inn.itrust.model.model.Agent;
public class TrustAssessmentManagerImpl implements TrustAssessmentManager{

	@Override
	public void assessIt(Agent agent) {
		
		if ( isComposed(agent) == false) return ;
		
		// decompose & analize
		
	}

	private boolean isComposed(Agent agent) {
		//TODO - trebam nacin za provjeriti da li je service / service object
		//composed od drugih
		return true;
	}

}
