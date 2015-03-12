package com.inn.trusthings.collector.trustdb;

/*
 * #%L
 * trusthings-service
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


import com.hp.hpl.jena.rdf.model.Model;
import com.inn.trusthings.bdg.ABridge;
import com.inn.trusthings.bdg.BridgeDB;
import com.inn.trusthings.collector.AbstractCollector;
import com.mv.util.vcap.VCAPParser;


public class InternalCollector extends AbstractCollector {
	
	private ABridge b ;

	public InternalCollector(String sourceUri) {
		super(sourceUri);
		initBridge();
	}

	private void initBridge() {
//		System.out.println("init internal collector");
		String databaseUrl = null;
		try {
			if (VCAPParser.parseVcap_Services()!=null){
				System.out.println("trust service - vcap service env exists");
				 String username = VCAPParser.obtainDBServiceUsername(VCAPParser.parseVcap_Services());
				 String password = VCAPParser.obtainDBServicePassword(VCAPParser.parseVcap_Services());
				 databaseUrl = VCAPParser.obtainDBServiceJDBCURI(VCAPParser.parseVcap_Services())
						  + "?user="+username+"&password="+password;
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("trust service aims to connect to: "+databaseUrl);
		b = new BridgeDB(databaseUrl);
	}

	@Override
	public Model collectInformation(String resourceIdentifier) {
		return b.obtainTrustProfile((resourceIdentifier));
	}

	@Override
	public String getName() {
		return "InternalCollector";
	}
	
	@Override
	public void shutDown() {
		b.stop();
	}

}
