package com.inn.trusthings.db.d2r;

/*
 * #%L
 * trusthings-kbase
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.query.Query;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.core.QueryHashCode;
import com.hp.hpl.jena.vocabulary.RDF;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class Bridge {

	private static final Logger log = LoggerFactory.getLogger(Bridge.class);

	public Model obtainTrustProfile(String serviceId) {

		// Set up the ModelD2RQ using a mapping file
		Model m = new ModelD2RQ("file:C://P-Programs//d2rq-0.8.1/mapping.ttl");

		String sparql =

		" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "	PREFIX db: <http://localhost:2020/resource/>"
				+ "	PREFIX compose-trust: <http://www.compose-project.eu/ns/web-of-things/trust#>"
				+ "	PREFIX usdl: <http://www.linked-usdl.org/ns/usdl-sec#>"
				+ "	PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "	PREFIX map: <http://localhost:2020/resource/#>"
				+ "	PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "	PREFIX compose-sec: <http://www.compose-project.eu/ns/web-of-things/security/profiles#>"
				+ "	PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "	PREFIX vocab: <http://localhost:2020/resource/vocab/>"
				+ "		describe ?agent ?profile ?attribute ?security ?certificate"
				+
				// "				WHERE " +
				// "				  { ?agent compose-trust:hasProfile ?profile ;" +
				// "				       compose-trust:hasName 'Twitter' ." +
				// "				    ?profile compose-trust:hasAttribute ?attribute ." +
				// "				    ?attribute rdf:type ?attributeType ;" +
				// "				       compose-trust:hasValue ?value ." +
				// "				  }";
				" WHERE "
				+ " { ?agent compose-trust:hasProfile ?profile ;"
				+ "  compose-trust:agent_url '"
				+ serviceId
				+ "' ."
				+ "    ?profile compose-trust:hasAttribute ?attribute ."
				+ "    ?attribute rdf:type ?attributeType ."
				+ "      OPTIONAL {?attribute compose-trust:hasValue ?value}"
				+ "     OPTIONAL {?attribute compose-trust:hasSecurityDescription ?security}"
				+ "    OPTIONAL {?attribute compose-trust:hasCertificateDetail ?certificate}"
				+ " }";
		// System.err.println(sparql);
		com.hp.hpl.jena.query.Query query = QueryFactory.create(sparql);
		// Model modelo = QueryExecutionFactory.create(q, m).execDescribe();

		Model modelo = QueryExecutionFactory.sparqlService(
				"http://localhost:2020/sparql", query).execDescribe();
		// ResultSet rs = QueryExecutionFactory.create(q, m).execSelect();
		// int i = 0;
		// while (rs.hasNext()) {
		// QuerySolution row = rs.nextSolution();
		// System.out.println(row);
		// i++;
		// };
		m.close();
		log.info("Model loaded sucessfully");
		return modelo;
	}

	public static void main(String[] args) {
		System.out
				.println(new Bridge()
						.obtainTrustProfile("http://www.programmableweb.com/api/google-earth"));
	}
}
