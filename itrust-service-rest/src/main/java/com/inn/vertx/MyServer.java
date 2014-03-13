package com.inn.vertx;

/*
 * #%L
 * itrust-service-rest
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
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.xsd.impl.XSDDouble;
import com.inn.common.Const;
import com.inn.common.OrderType;
import com.inn.common.util.UIDGenerator;
import com.inn.itrust.model.factory.TrustModelFactory;
import com.inn.itrust.model.model.SecurityAttribute;
import com.inn.itrust.model.model.SecurityGoal;
import com.inn.itrust.model.model.TResource;
import com.inn.itrust.model.model.TrustAttribute;
import com.inn.itrust.model.model.TrustRequest;
import com.inn.itrust.model.types.USDLSecExpression;
import com.inn.itrust.model.vocabulary.Trust;
import com.inn.itrust.model.vocabulary.UsdlSec;
import com.inn.itrust.service.component.TrustComponent;
import com.inn.itrust.service.enums.EnumScoreStrategy;
import com.inn.itrust.service.managers.TrustManager;
 
public class MyServer extends Verticle {
	
protected static TrustRequest request_Example_4(double... importance) {
		
		final TrustModelFactory factory = new TrustModelFactory(UIDGenerator.instanceRequest);
		final  TrustRequest trustRequest = factory.createTrustRequest();

		TrustAttribute att1 = factory.createTrustAttibute();
		att1.addType(URI.create(Trust.Reputation.getURI()));
		att1.setValue(Trust.medium.getURI());
		att1.setValueDatatype(new BaseDatatype(Trust.ReputationScale.getURI()));
		att1.setImportance(importance[2]);

		TrustAttribute att2 = factory.createTrustAttibute();
		att2.addType(URI.create(Trust.QoSAttribute.getURI()));
		att2.setValue("0.3");
		att2.setValueDatatype(XSDDouble.XSDdouble);
		att2.setImportance(importance[3]);

		SecurityAttribute att3 = factory.createSecurityAttribute();
		att3.setValueDatatype(USDLSecExpression.TYPE);
		att3.addType(URI.create(Trust.SecurityCapability.getURI()));
		SecurityGoal goal2 = new SecurityGoal(URI.create(UsdlSec.Authorization.getURI()));
		att3.addSecurityGoal(goal2);
		att3.setImportance(importance[0]);

		SecurityAttribute att4 = factory.createSecurityAttribute();
		att4.setValueDatatype(USDLSecExpression.TYPE);
		att4.addType(URI.create(Trust.SecurityCapability.getURI()));
		goal2 = new SecurityGoal(URI.create(UsdlSec.Authentication.getURI()));
		att4.addSecurityGoal(goal2);
		att4.setImportance(importance[1]);
		
		SecurityAttribute att5 = factory.createSecurityAttribute();
		att5.setValueDatatype(USDLSecExpression.TYPE);
		att5.addType(URI.create(Trust.SecurityCapability.getURI()));
		goal2 = new SecurityGoal(URI.create(UsdlSec.Confidentiality.getURI()));
		att5.addSecurityGoal(goal2);
		att5.setImportance(importance[1]);

		trustRequest.addAttribute(att1, att2, att3, att4, att5);
		
		return trustRequest;
	}

 
  public void start() {

	 
//	  URL url = Resources.getResource("text.html");
//	  final File f = new File(url.getPath());
	  
	  RouteMatcher matcher = new RouteMatcher();
	  
	  matcher.get("/", new Handler<HttpServerRequest>() {
	        @Override
	        public void handle(HttpServerRequest req) {
	        	BasicConfigurator.resetConfiguration();
	    		BasicConfigurator.configure();
	    		Injector injector = Guice.createInjector(new TrustComponent());
	    		TrustManager trustManager = injector.getInstance(TrustManager.class);
	    		List<URI> services = Lists.newArrayList();
	    		services.add(URI.create(Const.Ts3));
	    		services.add(URI.create(Const.Ts4));
	    		services.add(URI.create(Const.Ts5));
	    		services.add(URI.create(Const.Ts3));
	    		services.add(URI.create(Const.Ts4));
	    		services.add(URI.create(Const.Ts5));
	    		TrustRequest trustRequest = request_Example_4(1, 1, 1, 1);
	    		double d = 0;
	    		try {
	    			 d = trustManager.obtainTrustIndex(services.get(0), trustRequest);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	        	req.response().end(services.get(0)+" trust index "+d);
//	        	req.response().sendFile(f.getAbsolutePath());
	        }
	    });
	  
	  matcher.get("/service", new Handler<HttpServerRequest>() {
	        @Override
	        public void handle(HttpServerRequest req) {
	        	req.response().end("service result");
	        }
	    });
	  
	  
    vertx.createHttpServer().requestHandler(matcher).listen(8888);
    container.logger().info("Webserver started, listening on port: 8888");
 
  }
}