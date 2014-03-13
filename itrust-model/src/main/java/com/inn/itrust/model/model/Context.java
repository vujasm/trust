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


/**
 * Context or domain where trust of participants is to be established. 
 * E.g. public transport, tourism,  retail. For each context, there can be
 * a recommended trust profile(s) provided.
 * 
 * 
 * The term context defines the nature of the service or service functions, and each Context has 
 * a name, a type and a functional specification, such as ‘rent a car’ or ‘buy a book’.
 * Context Type can be matched to a Service Type.
 *
 * @author marko
 *
 */

import java.net.URI;
import java.util.Collection;

public class Context extends TResource {
	
	
	public Context(URI uri) {
		super(uri);
	}

	private Matcher hasMatcher;

  
    private Collection<TrustProfile> hasRecommendedProfiles;

	public Collection<TrustProfile> getHasRecommendedProfiles() {
		return hasRecommendedProfiles;
	}

	public void setHasRecommendedProfiles(Collection<TrustProfile> hasRecommendedProfiles) {
		this.hasRecommendedProfiles = hasRecommendedProfiles;
	}

	public Matcher getHasMatcher() {
		return hasMatcher;
	}

	public void setHasMatcher(Matcher hasMatcher) {
		this.hasMatcher = hasMatcher;
	}



}
