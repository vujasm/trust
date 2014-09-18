package com.inn.trusthings.json;

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


//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import com.google.common.io.CharStreams;
//import org.apache.xerces.util.SecurityManager;
//import com.hp.hpl.jena.ontology.OntModel;
//import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.ModelFactory;
//import com.inn.trusthings.kb.RDFModelsHandler;
//import com.inn.trusthings.kb.SharedOntModelSpec;
//import com.inn.trusthings.model.vocabulary.ModelEnum;
//import com.inn.util.json.IAFJSONParser;

import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.inn.trusthings.model.factory.TrustModelFactory;
import com.inn.trusthings.model.pojo.SecurityAttribute;
import com.inn.trusthings.model.pojo.SecurityGoal;
import com.inn.trusthings.model.pojo.SecurityMechanism;
import com.inn.trusthings.model.pojo.SecurityTechnology;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.model.types.USDLSecExpression;
import com.inn.trusthings.model.utils.TrustOntologyUtil;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.util.uri.UIDGenerator;

public class MakePOJO {
	
	final TrustModelFactory factory = new TrustModelFactory(UIDGenerator.instanceRequest);

	public TrustCriteria ofTrustCriteria(String json) {
		ObjectMapper m = new ObjectMapper();
		final TrustCriteria criteria = factory.createTrustRequest();
		try {
			
			JsonNode rootNode = m.readTree(json);
			JsonNode attributesNode = rootNode.get("attributes");
			for (JsonNode element : attributesNode) {
				TrustAttribute attribute = createTrustAttribute(element);
				criteria.addAttribute(attribute);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return criteria;
	}

	private TrustAttribute createTrustAttribute(JsonNode element) {
		
		JsonNode importance = element.get("importance");
		JsonNode value = element.get("value");
		JsonNode minvalue = element.get("minValue");
		JsonNode maxvalue = element.get("maxValue");
		JsonNode type = element.get("type");
		
		TrustAttribute attr  = null;
		Double d_importance = obtainImportance(importance);
		Object o_value = obtainValue(value);
		Object o_valueMin = obtainValueMin(minvalue);
		Object o_valueMax = obtainValueMax(maxvalue);
		for (JsonNode t : type) {
			String sURI = t.get("uri").asText();
			URI uri = URI.create(sURI);
			boolean isSecurity = TrustOntologyUtil.instance().isSubtype(sURI, Trust.SecurityAttribute.getURI());
			if (isSecurity){
				attr = createPopulateSecurityAttribute(element);
				attr.setValueDatatype(USDLSecExpression.TYPE);
			}
			else{
				attr = factory.createTrustAttibute();
				if (isNumeric(o_value)){
					attr.setValueDatatype(XSDDouble.XSDdouble);
				}
				else{
					//FIXME Scale - Attribute link should be defined in a model / kb
					if (uri.toASCIIString().equals(Trust.Reputation.getURI())){
						attr.setValueDatatype(new BaseDatatype(Trust.ReputationScale.getURI()));
					}
					else if (uri.toASCIIString().equals(Trust.Rating.getURI())){
						attr.setValueDatatype(new BaseDatatype(Trust.RatingScale.getURI()));
					}
					else {
						attr.setValueDatatype(XSDDouble.XSDdouble);
					}
				}
			}
			attr.addType(uri);
			attr.setImportance(d_importance);
			attr.setValue(o_value);
			attr.setMinValue(o_valueMin);
			attr.setMaxValue(o_valueMax);
		}
		//System.out.println(attr);
		return attr;
	}

	
	private TrustAttribute createPopulateSecurityAttribute(JsonNode element) {
		SecurityAttribute attr = factory.createSecurityAttribute();
		JsonNode implementedBy = element.get("implementedBy");
		if (implementedBy!=null){
		for (JsonNode implementedByElement : implementedBy) {
			SecurityMechanism mechanism = factory.createSecurityMechanism();
			attr.addImplementedBy(mechanism);
			JsonNode types = implementedByElement.get("type");
			for (JsonNode t : types) {
				String sURI = t.get("uri").asText();
				URI uri = URI.create(sURI);
				mechanism.addType(uri);
			}
		}
		}
		//	JsonNode realizedByTechnology = implementedByElement.get("realizedByTechnology");
//			if (realizedByTechnology!=null){
//				for (JsonNode realizedByTechnologyElement : realizedByTechnology) {
//					String sURI = realizedByTechnologyElement.get("uri").asText();
//					URI uri = URI.create(sURI);
//					mechanism.addRealizedByTechnology(new SecurityTechnology(uri));
//				}
//			}
		JsonNode realizedByTechnology = element.get("realizedByTechnology");
		if (realizedByTechnology!=null){
		for (JsonNode realizedByElement : realizedByTechnology) {
			String sURI = realizedByElement.get("uri").asText();
			URI uri = URI.create(sURI);
			SecurityTechnology technology =new SecurityTechnology( uri);
			JsonNode types = realizedByElement.get("type");
			for (JsonNode t : types) {
				String sURIt = t.get("uri").asText();
				URI urit = URI.create(sURIt);
				technology.addType(urit);
			}
			attr.addRealizedByTechnology(technology);

		}
		}
		
		JsonNode securityGoals = element.get("securityGoals");
		if (securityGoals!=null){
			for (JsonNode goalnode : securityGoals) {
				String uri = goalnode.get("uri").textValue();
				attr.addSecurityGoal(new SecurityGoal(URI.create(uri)));
			}
		}
		
		return attr;
	}

	private boolean isNumeric(Object o_value) {
		try {
			new Double(o_value.toString());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private Object obtainValueMax(JsonNode maxvalue) {
		if (maxvalue !=null){
			return maxvalue.asText();
		}
		return null;
	}

	
	private Object obtainValueMin(JsonNode minvalue) {
		if (minvalue !=null){
			return minvalue.asText();
		}
		return null;
	}

	
	private Object obtainValue(JsonNode value) {
		if (value !=null){
			return value.asText();
		}
		return null;
	}

	private Double obtainImportance(JsonNode importance) {
		Double i =1D;
		if (importance !=null){
			 i = importance.asDouble();
		}
		return i;
	}

//	public static void main(String[] args) {
//		Model model = RDFModelsHandler.getGlobalInstance().fetch(ModelEnum.Trust.getURI(), "TURTLE",SharedOntModelSpec.getModelSpecShared());
//    	OntModel oModel = ModelFactory.createOntologyModel(SharedOntModelSpec.getModelSpecShared(), model);
//		TrustOntologyUtil.init(oModel);
//		InputStream is = MakePOJO.class.getResourceAsStream("/criteria/criteria2.json");
//		String s;
//		try {
//			s = CharStreams.toString(new InputStreamReader(is));
//			TrustCriteria c = new MakePOJO().ofTrustCriteria(s);
//			System.out.println( IAFJSONParser.toJson(c));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
