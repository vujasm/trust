package com.inn.trusthings.model.expression;

import java.util.List;

import com.google.common.collect.Lists;

public class OrElement implements Element{
	
	private  List<SingleElement> elements = Lists.newArrayList();
	private Double weight;
	
	public OrElement(){
		
	}	

	public OrElement(Double weight) {
		this.weight = weight;
	}

	public List<SingleElement> getElements() {
		return elements;
	}
	
	public Double getWeight() {
		return weight;
	}
	

}
