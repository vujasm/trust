package temputil;

/*
 * #%L
 * itrust-service
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
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.inn.common.Const;
import com.inn.itrust.service.component.TrustComponent;
import com.inn.itrust.service.managers.SparqlGraphStoreFactory;
import com.inn.itrust.service.managers.SparqlGraphStoreManager;

public class CreateVocabulary {

	public static void main(String[] args) {

		String graphName = "http://www.compose-project.eu/ns/web-of-things/trust";

		BasicConfigurator.configure();
		org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
		Injector injector = Guice.createInjector(new TrustComponent());
		try {
			

			SparqlGraphStoreFactory factory = injector.getInstance(SparqlGraphStoreFactory.class);
			Set<URI> defaultModels = Sets.newHashSet();
			Map<String, String> locationMappings = Maps.newHashMap();
	        Set<String> ignoredImports = Sets.newHashSet();
			SparqlGraphStoreManager manager = factory.create(
"http://localhost:3030/data/query", "http://localhost:3030/data/update", "http://localhost:3030/data/data",
					defaultModels, locationMappings, ignoredImports);
			OntModel model = manager.getGraph(URI.create(graphName));
			
			StringBuffer b = new StringBuffer();
			b.append("import com.hp.hpl.jena.rdf.model.*;");
			b.append(Const.NEW_LINE);
			b.append("public class TRUST {");
			b.append(Const.NEW_LINE).append(Const.NEW_LINE);
			b.append("	private static Model m_model = TrustModelFactory.createDefaultModel();").append(Const.NEW_LINE);
			b.append("	public static final String NS = \""+graphName+"#\";").append(Const.NEW_LINE);
			b.append("	public static String getURI() {return NS;};").append(Const.NEW_LINE);
			b.append(" 	public static final TResource NAMESPACE = m_model.createResource( NS );").append(Const.NEW_LINE);
			
			{
				Iterator<OntClass> classes = model.listClasses();
				while (classes.hasNext()) {
					OntClass c = (OntClass) classes.next();
					if (c.getLocalName()!=null){
						b.append(Const.NEW_LINE);
						b.append("	").
						append("public static final TResource "+c.getLocalName()+" ="
								+ " m_model.createResource(\""+graphName+"#"+c.getLocalName()+"\");");
						b.append(Const.NEW_LINE);
					}
				}
			}
			
			
			{
				Iterator<ObjectProperty> ops = model.listObjectProperties();
				while (ops.hasNext()) {
					ObjectProperty op = (ObjectProperty) ops.next();
					if (op.getLocalName()!=null){
						b.append(Const.NEW_LINE);
						b.append("	").
						append("public static final Property "+op.getLocalName()+" ="
								+ " m_model.createResource(\""+graphName+"#"+op.getLocalName()+"\");");
						b.append(Const.NEW_LINE);
					}
				}
			}
			
			{
				Iterator<DatatypeProperty> dps = model.listDatatypeProperties();
				while (dps.hasNext()) {
					DatatypeProperty dp = (DatatypeProperty) dps.next();
					if (dp.getLocalName()!=null){
						b.append(Const.NEW_LINE);
						b.append("	").
						append("public static final Property "+dp.getLocalName()+" ="
								+ " m_model.createResource(\""+graphName+"#"+dp.getLocalName()+"\");");
						b.append(Const.NEW_LINE);
						}
					}
			}
			b.append("}");
			System.out.println(b);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
