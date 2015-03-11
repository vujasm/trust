package com.inn.trusthings.model.expression;

import java.util.List;

import com.google.common.collect.Lists;

public class OrElement implements Element{
	
	private  List<SingleElement> elements = Lists.newArrayList();
	
	public OrElement(){
		
	}	

	public List<SingleElement> getElements() {
		return elements;
	}
	

}
