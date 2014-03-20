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
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.inn.trusthings.json.MakeJson;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.service.interfaces.TrustManager;
import com.inn.util.tuple.Tuple2;

public class Server extends Verticle {

	public void start() {

		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		
		HttpServer server = vertx.createHttpServer();

		RouteMatcher matcher = new RouteMatcher();

		matcher.get("/trusthings", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				Injector injector = Guice.createInjector(new TrustModule());
				final TrustManager trustManager = injector.getInstance(TrustManager.class);
				List<String> ids = req.params().getAll("srvcid");
				try {
					List<URI> list = castToListUris(ids); 
					List<Tuple2<URI, Double>> result = trustManager.obtainTrustIndexes(list);
					String stringJson = new MakeJson().ofRankingResult(result);
					respondJsonMsgToClient(stringJson, req.response());
				} catch (Exception e) {
					String stringJson = new MakeJson().ofError(e);
					respondJsonMsgToClient(stringJson, req.response());
				}
			}
		});
		
		matcher.get("/pages/:page", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String userDir = System.getProperties().getProperty("user.dir");
				String file = req.params().get("page");
				req.response().setStatusCode(200);
				req.response().sendFile(userDir + "/pages/" + file);
			}
		});

		matcher.get("/", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String userDir = System.getProperties().getProperty("user.dir");				
				String file = "index.html";
//				req.response().headers().add("Content-Type", "text/html; charset=UTF-8");
				req.response().setStatusCode(200);
				req.response().sendFile(userDir + "/pages/" + file);
			}
		});

		server.requestHandler(matcher);
		// start the server
		server.listen(8888);
		container.logger().info("Webserver started, listening on port: 8888");
	}
	

	protected void respondJsonMsgToClient(String message, HttpServerResponse response) {
		response.headers().add("Content-Type", "text/json; charset=UTF-8");
		response.setStatusCode(200);
		response.end(new JsonObject(message).encodePrettily());
	}
	
	protected void respondJsonErrorMsgToClient(String message, HttpServerResponse response) {
		response.headers().add("Content-Type", "text/json; charset=UTF-8");
		response.setStatusCode(400);
		response.end(new JsonObject(message).encodePrettily());
	}
	
	protected List<URI> castToListUris(List<String> ids) {
		List<URI> list = Lists.newArrayList();
		for (String id : ids) {
			list.add(URI.create(id));
		}
		return list;
	}


}