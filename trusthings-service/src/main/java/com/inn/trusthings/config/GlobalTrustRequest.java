package com.inn.trusthings.config;

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


import java.net.URI;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.inn.trusthings.model.factory.TrustModelFactory;
import com.inn.trusthings.model.pojo.SecurityAttribute;
import com.inn.trusthings.model.pojo.SecurityGoal;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustRequest;
import com.inn.trusthings.model.types.USDLSecExpression;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.trusthings.model.vocabulary.UsdlSec;
import com.inn.util.uri.UIDGenerator;

/**
 *  Global (or, Absolute) Trust Criteria. This is a temp class for prototype purpose. 
 *  It will be refined afterwords..perhaps by conducting a survey among users to identify how they see/perceive trust in their respective domains.
 * 
 * @author Marko Vujasinovic <m.vujasinovic@innova-eu.net>
 * 
 */
public class GlobalTrustRequest {

	public static TrustRequest instance() {

		final TrustModelFactory factory = new TrustModelFactory(UIDGenerator.instanceRequest);
		final TrustRequest trustRequest = factory.createTrustRequest();

		//reputation
		TrustAttribute att1 = factory.createTrustAttibute();
		att1.addType(URI.create(Trust.Reputation.getURI()));
		att1.setValue(Trust.medium.getURI());
		att1.setValueDatatype(new BaseDatatype(Trust.ReputationScale.getURI()));
		att1.setImportance(1);
		trustRequest.addAttribute(att1);

		//qos
		TrustAttribute att2 = factory.createTrustAttibute();
		att2.addType(URI.create(Trust.QoSAttribute.getURI()));
		att2.setValue("0.3");
		att2.setValueDatatype(XSDDouble.XSDdouble);
		att2.setImportance(1);
		trustRequest.addAttribute(att2);

		{
//		//security Authorization
//		SecurityAttribute att3 = factory.createSecurityAttribute();
//		att3.setValueDatatype(USDLSecExpression.TYPE);
//		att3.addType(URI.create(Trust.SecurityCapability.getURI()));
//		SecurityGoal goal2 = new SecurityGoal(URI.create(UsdlSec.Authorization.getURI()));
//		att3.addSecurityGoal(goal2);
//		att3.setImportance(1);
//		trustRequest.addAttribute(att3);
		}

		{
		//security Authentication
		SecurityAttribute att4 = factory.createSecurityAttribute();
		att4.setValueDatatype(USDLSecExpression.TYPE);
		att4.addType(URI.create(Trust.SecurityCapability.getURI()));
		SecurityGoal goal = new SecurityGoal(URI.create(UsdlSec.Authentication.getURI()));
		att4.addSecurityGoal(goal);
		att4.setImportance(1);
		trustRequest.addAttribute(att4);
		}

		{
		//security Confidentiality
		SecurityAttribute att5 = factory.createSecurityAttribute();
		att5.setValueDatatype(USDLSecExpression.TYPE);
		att5.addType(URI.create(Trust.SecurityCapability.getURI()));
		SecurityGoal goal = new SecurityGoal(URI.create(UsdlSec.Confidentiality.getURI()));
		att5.addSecurityGoal(goal);
		att5.setImportance(1);
		trustRequest.addAttribute(att5);
		}

		

		return trustRequest;
	}

}
