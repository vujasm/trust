package com.inn.trusthings.service.collectors;

import com.hp.hpl.jena.rdf.model.Model;

public class InternalCollector extends AbstractCollector {

	public InternalCollector(String sourceUri) {
		super(sourceUri);
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		
		//load from database using d2rq bridge.
		
		return null;
	}

	@Override
	public String getName() {
		return "InternalCollector";
	}

}
