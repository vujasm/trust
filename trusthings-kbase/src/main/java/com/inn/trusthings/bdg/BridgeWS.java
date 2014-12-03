package com.inn.trusthings.bdg;

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
	
	private String defaultHost = "localhost";
	private String defaultPort = "8080";
	
	private  String ws_endpoint ;
	
	public BridgeWS() {
		String host  = System.getProperty("iserve.filter.trust.host", this.defaultHost);
		String port = System.getProperty("iserve.filter.trust.port", this.defaultPort);
		ws_endpoint = "http://"+host+(port.equals("")? "":":"+port)+"/apidbt/apidbt?find=";
		log.info(ws_endpoint);
	}
	
	
//	http://localhost:8080/apidbt/apidbt?find=http://iserve.kmi.open.ac.uk/iserve/id/services/84bf044f-541e-4a93-886d-36ab4278bfe0/google-maps

	@Override
	public Model obtainTrustProfile(String serviceId) {
		Client client =  new Client();
		log.debug(ws_endpoint + serviceId);
		String rdf = client.getRDFReponse(ws_endpoint + serviceId);
		Model model = ModelFactory.createDefaultModel();
		if (rdf!=null && rdf.startsWith("@prefix dc:")){
			InputStream inputStream = new ByteArrayInputStream(rdf.getBytes(Charset.forName("UTF-8")));
			RDFDataMgr.read(model, inputStream , Lang.TURTLE);
		}
		return model;
	}

	@Override
	public void stop() {
		
	}
	
	
	public static void main(String[] args) {
		
		Model m = new BridgeWS().obtainTrustProfile("http://abiell.pc.ac.upc.edu:9081/iserve/id/services/17933a84-7418-4376-8630-c6f0b4580c1e/stormpulse-maps");
		System.out.println(m);
	}
	

}
