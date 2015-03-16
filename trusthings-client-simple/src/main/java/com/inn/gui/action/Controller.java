package com.inn.gui.action;

/*
 * #%L
 * trusthings-client-simple
 * %%
 * Copyright (C) 2015 COMPOSE project
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


import javax.swing.tree.TreeModel;

import com.inn.gui.tree.Builder;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.service.interfaces.TrustSimpleManager;
import com.inn.util.tree.Tree;

import fordemo.demo1;
import fordemo.demo2;
import fordemo.demo3;

public class Controller {
	
	
	static TrustSimpleManager trustManager =  Factory.createInstance(TrustSimpleManager.class);
	
	public static TreeModel getTreeModel(){
		Tree t = trustManager.obtainTaxonomy(ModelEnum.Trust.getURI(), Trust.TrustAttribute.getURI());
//		System.out.println(t);
		return Builder.getModel(t);
	}
	
	
	public static String getScorestDemo1(){
		return new demo1().runExample(trustManager, 1);
	}
	
	public static String getScorestDemo2(){
		return new demo2().runExample(trustManager, 1);
	}
	
	public static String getScorestDemo3(){
		return new demo3().runExample(trustManager, 1);
	}
	
	public static void main(String[] args) {
		Tree t = trustManager.obtainTaxonomy(ModelEnum.SecurityOntology.getURI(),"http://www.compose-project.eu/ns/web-of-things/security/profiles#SecurityTechnology");
		System.out.println(t);
	}


	public static String getFilteredDemo1() {
		 return new demo1().runExample(trustManager, 2);
	}


	public static String getFilteredDemo2() {
		return new demo2().runExample(trustManager, 2);
	}


	public static String getFilteredDemo3() {
		return new demo3().runExample(trustManager, 2);
	}

}
