package com.inn.trusthings.model.pojo;

/*
 * #%L
 * trusthings-model
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


/**
 * Context or domain where trust of participants is to be established. 
 * E.g. public transport, tourism,  retail, shopping, finance,  news feeds.
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
