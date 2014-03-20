package com.inn.trusthings.model.pojo;

/*
 * #%L
 * trusthings-model
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

public class TrustProfile extends TResource {
   
	public TrustProfile(URI uri) {
		super(uri);
	}

	private List<TrustAttribute> attributeSet =  Lists.newArrayList();
	
	private Agent agent ; 
	
	private Context context ;

	public void addAttribute(TrustAttribute... p) {
		for (TrustAttribute a : p) {
			attributeSet.add(a);
		}
	}

	public void removeAttribute(TrustAttribute... p) {
		for (TrustAttribute a : p) {
			attributeSet.remove(a);
		}
	}

	public void addAttributes(List<TrustAttribute> set) {
		attributeSet.addAll(set);
	}

	public List<TrustAttribute> getAttributes(){
		return attributeSet;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}
