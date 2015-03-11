package com.inn.trusthings.model.expression;

import com.inn.trusthings.model.pojo.TrustAttribute;

public class SingleElement implements Element{
	
	private  TrustAttribute attribute;
	
	public SingleElement(TrustAttribute attribute){
		this.attribute = attribute; 
	}	
	
	public TrustAttribute getAttribute() {
		return attribute;
	}

}
