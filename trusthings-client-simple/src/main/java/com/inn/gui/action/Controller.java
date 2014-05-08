package com.inn.gui.action;

import javax.swing.tree.TreeModel;

import com.inn.gui.tree.Builder;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.trusthings.module.Factory;
import com.inn.trusthings.service.interfaces.TrustManager;
import com.inn.util.tree.Tree;

import fordemo.demo1;
import fordemo.demo2;
import fordemo.demo3;

public class Controller {
	
	
	static TrustManager trustManager =  Factory.createInstance(TrustManager.class);
	
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
		Tree t = trustManager.obtainTaxonomy(ModelEnum.SecurityProfiles.getURI(),"http://www.compose-project.eu/ns/web-of-things/security/profiles#SecurityTechnology");
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
