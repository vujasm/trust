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


public class CompositeServiceWrapper {
	

	private CompositionIdentifier compositionIdentifier;
	
	private String flow;
	
	public CompositeServiceWrapper(CompositionIdentifier id, String flow) {
		this.compositionIdentifier = id;
		this.flow = flow;
	}

	
	public CompositionIdentifier getCompositionIdentifier() {
		return compositionIdentifier;
	}
	
	public void setCompositionIdentifier(CompositionIdentifier compositionIdentifier) {
		this.compositionIdentifier = compositionIdentifier;
	}
	
	public String getFlow() {
		return flow;
	}
	
	public void setFlow(String flow) {
		this.flow = flow;
	}

}
