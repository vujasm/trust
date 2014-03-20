package com.inn.trusthings.vertx;

/*
 * #%L
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

import org.apache.log4j.BasicConfigurator;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.service.interfaces.TrustManager;

public class MyServer extends Verticle {

	public void start() {

		
		HttpServer server = vertx.createHttpServer();

		RouteMatcher matcher = new RouteMatcher();

		matcher.get("/trusthings", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				BasicConfigurator.resetConfiguration();
				BasicConfigurator.configure();
				Injector injector = Guice.createInjector(new TrustModule());
				final TrustManager trustManager = injector.getInstance(TrustManager.class);
				String serviceId = req.params().get("srvcid");
				double d = 0;
				try {
					d = trustManager.obtainTrustIndex(URI.create(serviceId));
				} catch (Exception e) {
					e.printStackTrace();
					req.response().setStatusCode(400);
					req.response().end(e.getMessage());
				}
				req.response().setStatusCode(200);
				req.response().end(URI.create(serviceId).toASCIIString() + " has trust index " + d);
			}
		});

		matcher.get("/webroot/:page", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String userDir = System.getProperties().getProperty("user.dir");
				String file = req.params().get("page");
				System.out.println(userDir + "/webroot/" + file);
				req.response().setStatusCode(200);
				req.response().sendFile(userDir + "/webroot/" + file);
			}
		});

		matcher.get("/", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String userDir = System.getProperties().getProperty("user.dir");
				String file = "index.html";
				System.out.println(userDir + "/webroot/" + file);
				req.response().setStatusCode(200);
				req.response().sendFile(userDir + "/webroot/" + file);
			}
		});

		server.requestHandler(matcher);

		//
		// // Register HTTP handler
		// server.requestHandler(new Handler<HttpServerRequest>() {
		// @Override
		// public void handle(HttpServerRequest req) {
		// String userDir = System.getProperties().getProperty("user.dir");
		// String file = req.path().equals("/")? "index.html" : req.path();
		// System.out.println(userDir+"webroot/"+file);
		// req.response().setStatusCode(200);
		// req.response().sendFile(userDir+"/webroot/"+file);
		// }
		//
		// });

		// start the server
		server.listen(8888);

		container.logger().info("Webserver started, listening on port: 8888");

	}
}