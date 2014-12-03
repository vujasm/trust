package com.inn.trusthings.bdg;

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


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.inn.util.httpclient.Client;


/**
 * BridgeWS serves to pass a request to a web service that returns rdf model of trust profile for given service identified with serviceId
 * @author marko
 *
 */
public class BridgeWS extends ABridge {
	
	private static final Logger log = LoggerFactory.getLogger(BridgeWS.class);
	
	private String defaultHost = "apidbt.147.83.30.133.xip.io";
	private String defaultPort = "";
	
	private  String ws_endpoint ;
	
	public BridgeWS() {
		String host  = System.getProperty("iserve.filter.trust.host", this.defaultHost);
		String port = System.getProperty("iserve.filter.trust.port", this.defaultPort);
		ws_endpoint = "http://"+host+(port.equals("")? "":":"+port)+"/apidbt?find=";
		log.info(ws_endpoint);
	}
	
	public synchronized Model obtainTrustProfile(String serviceId) {
		return getTrustProfile(serviceId);
	};

	@Override
	public Model getTrustProfile(String serviceId) {
		Client client =  new Client();
		log.info(ws_endpoint + serviceId);
		Model model = ModelFactory.createDefaultModel();
		String rdf = client.getRDFReponse(ws_endpoint + serviceId);
		if (rdf!=null && rdf.contains("@prefix dc:")){
			InputStream inputStream = new ByteArrayInputStream(rdf.getBytes(Charset.forName("UTF-8")));
			RDFDataMgr.read(model, inputStream , Lang.TURTLE);
		}
		return model;
	}

	@Override
	public void stop() {
		
	}
	

//	public static void main(String[] args) {
//		
//		Model m = new BridgeWS().obtainTrustProfile("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/17933a84-7418-4376-8630-c6f0b4580c1e/stormpulse-maps");
//		System.out.println(m);
//	}
	

}
