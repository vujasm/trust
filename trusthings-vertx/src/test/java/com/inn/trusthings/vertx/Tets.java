package com.inn.trusthings.vertx;

/*
 * #%L
 * trusthings-vertx
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


public class Tets {
	
	public static void main(String[] args) {
		System.out.println("Marko 1");
		String[] args2 = {"run", "com.inn.trusthings.vertx.Server"};
		System.out.println("Marko 2");
		org.vertx.java.platform.impl.cli.Starter.main(args2);
		
	}

}