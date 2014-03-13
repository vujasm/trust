package com.inn.itrust.model.factory;

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


import com.inn.common.util.UIDGenerator;
import com.inn.itrust.model.model.SecurityAttribute;
import com.inn.itrust.model.model.SecurityMechanism;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.model.TrustProfile;
import com.inn.itrust.model.model.TrustRequest;

public class TrustModelFactory {

	private UIDGenerator uidGenerator ;
	
	public TrustModelFactory(UIDGenerator uidGenerator){
		this.uidGenerator = uidGenerator;
	}
	
	public TrustProfile createTrustProfile() {
		return new TrustProfile(uidGenerator.create(TrustProfile.class));
	}
	
	public TrustRequest createTrustRequest() {
		return new TrustRequest(uidGenerator.create(TrustRequest.class));
	}

	public TrustAttribute createTrustAttibute() {
		return new TrustAttribute(uidGenerator.create(TrustProfile.class));
	}

	public SecurityAttribute createSecurityAttribute() {
		return  new SecurityAttribute(uidGenerator.create(SecurityAttribute.class));
	}
	
	public SecurityMechanism createSecurityMechanism() {
		return  new SecurityMechanism(uidGenerator.create(SecurityMechanism.class));
	}

}
