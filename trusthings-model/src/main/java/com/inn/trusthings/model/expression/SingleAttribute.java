package com.inn.trusthings.model.expression;

import com.inn.trusthings.model.pojo.TrustAttribute;

public class SingleAttribute implements RequiredAttribute{
	
	private  TrustAttribute attribute;
	
	public SingleAttribute(TrustAttribute attribute){
		this.attribute = attribute; 
	}	
	
	public TrustAttribute getAttribute() {
		return attribute;
	}

}
