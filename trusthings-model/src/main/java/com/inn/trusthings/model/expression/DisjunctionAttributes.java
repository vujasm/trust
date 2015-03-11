package com.inn.trusthings.model.expression;

import java.util.List;

import com.google.common.collect.Lists;

public class DisjunctionAttributes implements RequiredAttribute{
	
	private  List<SingleAttribute> elements = Lists.newArrayList();
	
	public DisjunctionAttributes(){
		
	}	

	public List<SingleAttribute> getElements() {
		return elements;
	}
	

}
