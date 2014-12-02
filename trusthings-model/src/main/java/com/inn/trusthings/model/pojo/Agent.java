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

public class Agent extends TResource {
	
    public Agent(URI uri) {
		super(uri);
	}

	public TrustProfile getHasTrustProfile() {
		return hasTrustProfile;
	}

	public void setHasTrustProfile(TrustProfile hasTrustProfile) {
		this.hasTrustProfile = hasTrustProfile;
	}
	
	@Override
	public URI getUri() {
		return super.getUri();
	}

	private TrustProfile hasTrustProfile;
	private URI compose_ID;
	
	private URI inputUID;
	
	public void setCompose_ID(URI compose_ID) {
		this.compose_ID = compose_ID;
	}

	public URI getCompose_ID() {
		return compose_ID;
	}

	public void setInputUID(URI uid) {
		this.inputUID = uid;
		
	}
	
	public URI getInputUID() {
		return inputUID;
	}
	
	
	
}
