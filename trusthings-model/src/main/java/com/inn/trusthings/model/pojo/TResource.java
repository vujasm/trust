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

import uk.ac.open.kmi.iserve.commons.io.util.URIUtil;

import com.google.common.collect.Lists;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class TResource {

	private URI uri;

	private String label;

	private String comment;
	
	private List<TResource> types = Lists.newArrayList();

	public TResource(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	public String uriToLocalName(){
		return URIUtil.getLocalName(getUri());
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public com.hp.hpl.jena.rdf.model.Resource asJenaResource() {
		return ResourceFactory.createResource(getUri().toASCIIString());
	}

	public List<TResource> getTypesAll() {
		return types;
	}
	
	public TResource obtainType() {
		return (types.isEmpty())? null:types.get(0);
	}
	
	public void addType(TResource tResource) {
		types.add(tResource);
	}
	
	public void addType(URI uri) {
		types.add(new TResource(uri));
	}
	

}
