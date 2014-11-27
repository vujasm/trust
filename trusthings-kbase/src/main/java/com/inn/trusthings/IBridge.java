package com.inn.trusthings;

import com.hp.hpl.jena.rdf.model.Model;

public interface IBridge {

	Model obtainTrustProfile(String serviceId) ;

	void stop();
	
}
