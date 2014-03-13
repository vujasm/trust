package com.inn.itrust.model.io;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.inn.common.Const;
import com.inn.itrust.model.io.ext.SecProfileExpressionToModel;
import com.inn.itrust.model.model.Agent;
import com.inn.itrust.model.model.Metric;
import com.inn.itrust.model.model.MetricValue;
import com.inn.itrust.model.model.SecurityAttribute;
import com.inn.itrust.model.model.SecurityCapability;
import com.inn.itrust.model.model.SecurityRequirment;
import com.inn.itrust.model.model.TResource;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.model.TrustProfile;
import com.inn.itrust.model.types.USDLSecExpression;
import com.inn.itrust.model.vocabulary.ModelEnum;
import com.inn.itrust.model.vocabulary.Trust;

public class ToModelParser {

	private static final Logger log = LoggerFactory.getLogger(ToModelParser.class);
	private HashMap<String, Object> specificParsers = Maps.newHashMap();

	/**
	 * 
	 * Takes Jena Rdf Model and converts it into Java Objects
	 * 
	 * @param model
	 *            jena rdf model
	 * @return TrustProfile as Java objects
	 */
	public TrustProfile parse(Model model) throws Exception {
		TrustProfile tp = null;
		log.debug("transforming " + model + " into Triples");
		OntModel oModel = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM, model);
		// oModel.add(model);
		Iterator<Individual> iterator = oModel.listIndividuals(Trust.TrustProfile);
		while (iterator.hasNext()) {
			Individual individual = (Individual) iterator.next();
			if (tp == null) {
				tp = new TrustProfile(URI.create(individual.getURI()));
				Resource r = findAgentURI(oModel);
				tp.setAgent(new Agent(URI.create(r.getURI())));
			}
			parseAttributes(tp, individual);
			log.debug(individual.toString());
		}

		return tp;
	}

	private Resource findAgentURI(OntModel oModel) throws Exception {
		ResIterator iterator = oModel.listSubjectsWithProperty(Trust.hasTrustProfile);
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			log.error("Given trust profile is not assigned to any resource");
			throw new Exception("trust profile is not assigned to any resource");
		}
	}

	private void parseAttributes(TrustProfile tp, Individual tpIndividual) {

		NodeIterator nodeIterator = tpIndividual.listPropertyValues(Trust.hasAttribute);
		while (nodeIterator.hasNext()) {
			RDFNode rdfNode = (RDFNode) nodeIterator.next();
			if (rdfNode.canAs(Individual.class)) {
				TrustAttribute attribute = null;
				Individual individual = rdfNode.as(Individual.class);
				Iterator<Resource> types = individual.listRDFTypes(true);

				// FIXME razlozi ovo nekako da nije zakovano. mozda da mapiras
				// ili preko datatype koji u slucaju securitija nosi usdl-sec
				if (attribute == null) {
					if (isOfType(individual, Trust.SecurityCapability.getURI())) {
						attribute = new SecurityCapability(URI.create(individual.getURI()));
						attribute = parseSecurityAttribute(individual, (SecurityAttribute) attribute);
					} else if (isOfType(individual, Trust.SecurityRequirment.getURI())) {
						attribute = new SecurityRequirment(URI.create(individual.getURI()));
						attribute = parseSecurityAttribute(individual, (SecurityAttribute) attribute);
					} else {
						attribute = new TrustAttribute(URI.create(individual.getURI()));
						parseAttributeValue(attribute, individual);
					}
				}

				while (types.hasNext()) {
					Resource type = (Resource) types.next();
					if (attribute != null) {
						attribute.addType(new com.inn.itrust.model.model.TResource(URI.create(type.getURI())));
					}
				}
				tp.addAttribute(attribute);
			}
		}

	}

	private boolean isOfType(Individual individual, String typeURI) {
		Iterator<Resource> types = individual.listRDFTypes(true);
		while (types.hasNext()) {
			Resource type = (Resource) types.next();
			if (type.getURI().equalsIgnoreCase(typeURI)) {
				return true;
			}
		}
		return false;
	}

	private void parseAttributeValue(TrustAttribute attribute, Individual individual) {
		RDFNode valueNode = individual.getPropertyValue(Trust.hasValue);
		Literal literal = valueNode.asLiteral();
		Object value = literal.getLexicalForm();
		attribute.setValue(value);
		attribute.setValueDatatype(literal.getDatatype());
	}

	private TrustAttribute parseSecurityAttribute(Individual individual, SecurityAttribute attribute) {
		final RDFNode individualValue = individual.getPropertyValue(Trust.hasValue);
		// Individual individualValueAsIndividual = individualValue.as(Individual.class);
		final RDFDatatype datype = individualValue.asLiteral().getDatatype();
		final String lexicalForm = individualValue.asLiteral().getLexicalForm();
		
		//TODO uradi elegantnije - razlozi i implementiraj slucaj kad je opis ne iz predefinisanog securityprofila vec zapravo expression
		if (datype.getURI().equals(USDLSecExpression.TYPE.getURI())  && lexicalForm.startsWith(ModelEnum.SecurityProfiles.getURI())) {
			SecProfileExpressionToModel parser = (SecProfileExpressionToModel) specificParsers.get(Const.ParserNameSecurityProfileAsUSDLSec);
			if (parser == null) {
				log.error("A parser to parse " + individualValue + " into java objects is not registered. Use registerSpecificParser(Object parser, String name)");
			}
			parser.parse(lexicalForm, attribute);
		} else {
			log.error("A parser to parse " + individualValue + " into java objects not supported / implemented");
			throw new UnsupportedOperationException();
		}

		attribute.setValueDatatype(USDLSecExpression.TYPE);
		return attribute;
	}

	/**
	 * 
	 * @param parser
	 *            additional parser
	 * @param name
	 *            name of the additional parser
	 */
	public void registerSpecificParser(Object parser, String name) {
		specificParsers.put(name, parser);
	}

	public Metric parseMetric(Resource resource) {
		final Metric metric = new Metric(URI.create(resource.getURI()));
		metric.addType(new TResource(URI.create(Trust.Metric.getURI())));
		StmtIterator it = resource.listProperties(Trust.hasMetricValue);
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			Resource resourceMetricValue = statement.getObject().asResource();
			MetricValue metricValue = getOrCreateMetricValue(metric, resourceMetricValue);
			if (resourceMetricValue.getProperty(Trust.next) != null) {
				Resource next = resourceMetricValue.getProperty(Trust.next).getObject().asResource();
				MetricValue metricValueNext = getOrCreateMetricValue(metric, next);
				metricValue.setNext(metricValueNext);
			}
		}
		return metric;
	}

	private MetricValue getOrCreateMetricValue(Metric metric, Resource resourceMetricValue) {
		List<MetricValue> list = metric.getMetricValues();
		for (MetricValue metricValue : list) {
			if (metricValue.getUri().toASCIIString().compareTo(resourceMetricValue.getURI()) == 0) {
				return metricValue;
			}
		}
		MetricValue metricValue = new MetricValue(URI.create(resourceMetricValue.getURI()));
		metricValue.addType(new TResource(URI.create(Trust.MetricValue.getURI())));
		if (resourceMetricValue.getProperty(Trust.rank) != null) {
			String rank = resourceMetricValue.getProperty(Trust.rank).getObject().asLiteral().getLexicalForm();
			metricValue.setRank(Double.valueOf(rank));
		}
		metric.addMetricValue(metricValue);
		return metricValue;
	}

}
