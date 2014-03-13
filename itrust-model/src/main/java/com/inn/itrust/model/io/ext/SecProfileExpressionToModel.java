package com.inn.itrust.model.io.ext;

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
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.inn.itrust.model.model.SecurityAttribute;
import com.inn.itrust.model.model.SecurityGoal;
import com.inn.itrust.model.model.SecurityMechanism;
import com.inn.itrust.model.model.SecurityTechnology;
import com.inn.itrust.model.vocabulary.UsdlSec;

/**
 * 
 * A parser to parse SecProfileExpression onto Java Objects
 * 
 * @author marko
 * 
 */

public class SecProfileExpressionToModel {

	private OntModel usdlSecModel;

	public SecProfileExpressionToModel(OntModel usdlSecModel) {
		this.usdlSecModel = usdlSecModel;
	}
	
	public void parse(RDFNode node, SecurityAttribute attribute) {
		parse(node.asResource().getURI(), attribute);
	}

	public void parse(String resourceURI, SecurityAttribute attribute) {
		Resource resource = usdlSecModel.getResource(resourceURI);
		Individual individual = resource.as(Individual.class);
		fillSecurityGoals(individual, attribute);
		fillSecurityMechanismAndTechnologies(individual, attribute);
	}

	private void fillSecurityGoals(Individual individual, SecurityAttribute attribute) {
		NodeIterator goalNodes = individual.listPropertyValues(UsdlSec.hasSecurityGoal);
		while (goalNodes.hasNext()) {
			RDFNode rdfNode = (RDFNode) goalNodes.next();
			SecurityGoal goal = new SecurityGoal(URI.create(rdfNode.asNode().getURI()));
			fillTypes(goal, rdfNode.asResource());
			attribute.addSecurityGoal(goal);
		}
	}

	private void fillSecurityMechanismAndTechnologies(Individual individual, SecurityAttribute attribute) {
		NodeIterator implementedByNodes = individual.listPropertyValues(UsdlSec.isImplementedBy);
		while (implementedByNodes.hasNext()) {
			RDFNode securityMechanismNode = (RDFNode) implementedByNodes.next();
			Individual securityMechanismNodeIdvl = securityMechanismNode.as(Individual.class);
			List<Resource> list = listSectypes(securityMechanismNodeIdvl);
			for (Resource usdlSecMechanism : list) {
				SecurityMechanism mechanism = new SecurityMechanism(URI.create(usdlSecMechanism.asNode().getURI()));
				attribute.addImplementedBy(mechanism);
				fillTypes(mechanism, securityMechanismNodeIdvl.asResource());
				fillTechnologies(securityMechanismNodeIdvl, mechanism);
			}
		}
	}

	private void fillTechnologies(Individual securityMechanismNode, SecurityMechanism mechanism) {
		NodeIterator realizedByNodes = securityMechanismNode.listPropertyValues(UsdlSec.isRealizedByTechnology);
		while (realizedByNodes.hasNext()) {
			RDFNode securityTechnologyNode = (RDFNode) realizedByNodes.next();
			SecurityTechnology technology = new SecurityTechnology(URI.create(securityTechnologyNode.asNode().getURI()));
			mechanism.addRealizedByTechnology(technology);
			Individual securityTechnologyIdvl = securityTechnologyNode.as(Individual.class);
			fillTypes(mechanism, securityTechnologyIdvl.asResource());
		}
	}

	private void fillTypes(com.inn.itrust.model.model.TResource tResource, Resource asResource) {
		Iterator<Resource> types = asResource.as(Individual.class).listRDFTypes(true);
		while (types.hasNext()) {
			Resource type = (Resource) types.next();
			if (tResource != null) {
				tResource.addType(new com.inn.itrust.model.model.TResource(URI.create(type.getURI())));
			}
		}
	}

	/**
	 * Lists types
	 * @param individual
	 * @return
	 */
	private List<Resource> listSectypes(Individual individual) {
		ExtendedIterator<Resource> types = individual.listRDFTypes(true);
		Filter<Resource> typeFilter = new Filter<Resource>() {
			@Override
			public boolean accept(Resource x) {
				if (x.getURI().startsWith(OWL.NS) || x.getURI().startsWith(OWL2.NS)) {
					return false;
				}
				return true;
			}
		};
		return types.filterKeep(typeFilter).toList();
	}

}
