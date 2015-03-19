package com.inn.compose.test;
/*
 * #%L
 * trusthings-service
 * %%
 * Copyright (C) 2014 - 2015 INNOVA S.p.A
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.inn.trusthings.kb.SharedOntModelSpec;
import com.inn.trusthings.model.factory.TrustModelFactory;
import com.inn.trusthings.model.io.ToGraphParser;
import com.inn.trusthings.model.pojo.Agent;
import com.inn.trusthings.op.match.MetricMatchOp;
import com.inn.trusthings.service.config.CollectorConfig;
import com.inn.util.uri.UIDGenerator;


public class TestReputationCollector {
	
	
	public void call(){
		
		List<URI> list = Lists.newArrayList();
		try {
			list.add(new URI("http:/localhost/service_instance/aaa/service_instance1"));
			list.add(new URI("http:/localhost/service_instance/aaa/service_instanceX"));
//			list.add(new URI("http:/localhost/service_object/aaa/service_object1"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Map<URI, Model> maps = Maps.newHashMap();
		for (URI uri : list) {
			Agent service = new Agent(uri);
			TrustModelFactory trm = new TrustModelFactory(UIDGenerator.instanceTrust);
			service.setHasTrustProfile(trm.createTrustProfile());
			OntModel m = new ToGraphParser().parse(service);
			maps.put(uri, m);
			RDFDataMgr.write(System.out, m, Lang.TURTLE) ;
		}
		CollectorConfig.getCollectorByType("reputation").collectInformation(list, maps);
		
		for (URI uri : list) {
			RDFDataMgr.write(System.out, maps.get(uri), Lang.TURTLE) ;
		}
	}
	
	
	public static void main(String[] args) {
		new TestReputationCollector().call();
	}

}
