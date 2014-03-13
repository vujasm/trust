package com.inn.itrust.model.model;

/*
 * #%L
 * itrust-model
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

public class SecurityAttribute extends TrustAttribute {
	
	private List<SecurityMechanism> implementedBy =  Lists.newArrayList();

	private List<SecurityGoal> securityGoals = Lists.newArrayList();

	public SecurityAttribute(URI uri) {
		super(uri);
	}

	public void addImplementedBy(SecurityMechanism securityMechanism) {
		this.implementedBy.add(securityMechanism);
	}

	public void addSecurityGoal(SecurityGoal securityGoal) {
		this.securityGoals.add(securityGoal);
	}

	public List<SecurityMechanism> getImplementedBy() {
		return implementedBy;
	}

	public List<SecurityGoal> getSecurityGoals() {
		return securityGoals;
	}

	public void removeImplementedBy(SecurityMechanism securityMechanism) {
		this.implementedBy.remove(securityMechanism);
	}

	public void removeSecurityGoal(SecurityGoal securityGoal) {
		this.securityGoals.remove(securityGoal);
	}

	@Override
	public String toString() {
		String s = "";
		List<SecurityGoal> l = getSecurityGoals();
		for (SecurityGoal g : l) {
			s = s + " goal: " + g.getUri();
		}
		List<SecurityMechanism> ll = getImplementedBy();
		for (SecurityMechanism m : ll) {
			s = s + " mechanism " + m.getUri();
			List<SecurityTechnology> lll = m.getRealizedByTechnology();
			for (SecurityTechnology t : lll) {
				s = s + " technology " + t.getUri();
			}
		}
		return getUri().toASCIIString() + " " + s;
	}

}
