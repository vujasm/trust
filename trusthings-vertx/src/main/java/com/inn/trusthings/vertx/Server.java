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

import io.netty.handler.codec.http.QueryStringDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.inn.common.OrderType;
import com.inn.trusthings.json.MakeJson;
import com.inn.trusthings.json.TrustPOJOFactory;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.op.enums.EnumScoreStrategy;
import com.inn.trusthings.service.interfaces.TrustManager;
import com.inn.util.tuple.Tuple2;

public class Server extends Verticle {

	private static final Integer DEFAULT_PORT = 8888;
	private static final String DEFAULT_HOST = "localhost";

	public void start() {
		
		HttpServer server = vertx.createHttpServer();

		RouteMatcher matcher = new RouteMatcher();

		matcher.get("/trusthings", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				final TrustManager trustManager = Factory.createInstance(TrustManager.class);
				
				List<String> ids = req.params().getAll("srvcid");
				try {
					trustManager.setGlobalTrustCriteria( new TrustPOJOFactory().ofTrustCriteria(obtainCriteriaDemo()));
					List<URI> list = castToListUris(ids); 
					List<Tuple2<URI, Double>> result = trustManager.obtainTrustIndexes(list);
					String stringJson = new MakeJson().ofRankingResult(result);
					respondJsonMsgToClient(stringJson, req.response());
				} catch (Exception e) {
					String stringJson = new MakeJson().ofError(e);
					respondJsonErrorMsgToClient(stringJson, req.response());
				}
			}
		});
		
		matcher.get("/trusthingsrank", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				final TrustManager trustManager = Factory.createInstance(TrustManager.class);
				List<String> ids = req.params().getAll("srvcid");
				try {
					List<URI> list = castToListUris(ids); 
					trustManager.setGlobalTrustCriteria( new TrustPOJOFactory().ofTrustCriteria(obtainCriteriaDemo()));
					List<Tuple2<URI, Double>> result = trustManager.rankResources(list, trustManager.getGlobalTrustCriteria(), 
							EnumScoreStrategy.TOPSIS, false, OrderType.DESC);
					String stringJson = new MakeJson().ofRankingResult(result);
					respondJsonMsgToClient(stringJson, req.response());
				} catch (Exception e) {
					String stringJson = new MakeJson().ofError(e);
					respondJsonErrorMsgToClient(stringJson, req.response());
				}
			}
		});
		
		matcher.get("/web/:page", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String webroot = obtainWebRoot(); 
				String file = req.params().get("page");
				req.response().setStatusCode(200);
				req.response().sendFile(webroot + "/" + file);
			}
		});

		matcher.get("/", new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String webroot = obtainWebRoot(); 
				String file = "index.html";
//				req.response().headers().add("Content-Type", "text/html; charset=UTF-8");
				req.response().setStatusCode(200);
				req.response().sendFile(webroot + "/" + file);
			}
		});

		matcher.post("/trusthings/do", new Handler<HttpServerRequest>() {
			@Override
			public void handle(final HttpServerRequest req) {
				System.out.println("handle post");
				req.bodyHandler(new Handler<Buffer>() {
					@Override
					public void handle(Buffer buff) {
						QueryStringDecoder qsd = new QueryStringDecoder(buff.toString(), false);
		                Map<String, List<String>> params = qsd.parameters();
		                String listOfURIs = params.get("things").get(0);
		                String criteriaJSON = params.get("tc").get(0);
		                boolean isTopsis = false;
		                if (params.get("topsis")!=null){
		                	isTopsis = true;
		                }
		                final TrustManager trustManager = Factory.createInstance(TrustManager.class);
		                TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(criteriaJSON);
						try {
							List<URI> list = parseJsonAgents(listOfURIs);
							List<Tuple2<URI, Double>> result = null; 
							if (isTopsis == false){
								trustManager.setGlobalTrustCriteria(criteria);
								result = trustManager.obtainTrustIndexes(list);	
							}
							else{
								result = trustManager.rankResources(list, criteria, EnumScoreStrategy.TOPSIS, false, OrderType.DESC);
							}
							
							String stringJson = new MakeJson().ofRankingResult(result);
							respondJsonMsgToClient(stringJson, req.response());
						} catch (Exception e) {
							e.printStackTrace();
							String stringJson = new MakeJson().ofError(e);
							respondJsonErrorMsgToClient(stringJson, req.response());
						}

					}
				});
				
				
			}
		});
		
		matcher.noMatch(new Handler<HttpServerRequest>() {
			@Override
			public void handle(HttpServerRequest req) {
				String stringJson = new MakeJson().ofErrorSimpleMessage("bad request. the url you requested not found.");
				req.response().headers().add("Content-Type", "text/json; charset=UTF-8");
				req.response().setStatusCode(404);
				req.response().end(new JsonObject(stringJson).encodePrettily());
			}
		});
		
		server.requestHandler(matcher);
		// start the server
		System.out.println("Config is " + container.config());
		Integer portConfig = container.config().getInteger("port");
		String hostConfig = container.config().getString("host");
		
		Integer port = (portConfig!=null)? portConfig:DEFAULT_PORT;
		String host = (hostConfig!=null)? hostConfig:DEFAULT_HOST;
		
		server.listen(port, host);
		container.logger().info("Webserver started on host: "+host+" port "+port);
		container.logger().info("Web root is "+obtainWebRoot());
		
		System.out.println("Webserver started on host: "+host+" port "+port);
		System.out.println("Web root is "+obtainWebRoot());

	}
	

	private String obtainCriteriaDemo() throws Exception{
		InputStream is = Server.class.getResourceAsStream("/criteria/demo/criteria_sc_c.json");
		String val =  CharStreams.toString(new InputStreamReader(is));
		return val;
	}
	
	
	private List<URI> parseJsonAgents(String things) throws JsonProcessingException, IOException {
		List<URI> list = Lists.newArrayList();
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode = m.readTree(things);
		JsonNode nodes = rootNode.get("things");
		for (JsonNode node : nodes) {
			list.add((URI.create(node.get("id").textValue())));
		}
		
		return list; 
	}


	protected String obtainWebRoot() {
		String userDir = System.getProperties().getProperty("user.dir");
		if (container.config().getString("webroot")!=null){
			return container.config().getString("webroot");
		}
		return userDir+"/web";
	}


	protected void respondJsonMsgToClient(String message, HttpServerResponse response) {
		response.headers().add("Content-Type", "text/json; charset=UTF-8");
		response.setStatusCode(200);
		response.end(new JsonObject(message).encodePrettily());
	}
	
	protected void respondJsonErrorMsgToClient(String message, HttpServerResponse response) {
		response.headers().add("Content-Type", "text/json; charset=UTF-8");
		response.setStatusCode(500);
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