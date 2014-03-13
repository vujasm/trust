package com.inn.client.simple;

/*
 * #%L
 * itrust-client-simple
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


import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Level;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class FusekiTest {
	
	
	public static void main(String[] args) {
		
		org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
		DatasetAccessor datasetAccessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/data/data");
		String uri = "C:/P-Programs/jena-fuseki-1.0.0/Data/books.ttl";
		Model data = RDFDataMgr.loadModel(uri.toLowerCase());
//		System.out.println(data.toString());
		System.out.println(datasetAccessor.getModel("http://example.com/books"));
		datasetAccessor.putModel("http://example.com/books", data);
		
		
		
//		UpdateRequest request = UpdateFactory.create();
//        request.add(new UpdateCreate("http://example.com/books"));
//        request.add(FusekiTest.generateInsertRequest("http://example.com/books", data));
//        System.out.println(request.toString());
//
//        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request,"http://localhost:3030/ds/update");
       // processor.execute(); // TODO: anyway to know if things went ok?
    }
	

}
