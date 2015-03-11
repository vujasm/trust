package com.inn.trusthings.service;

import java.util.List;

import com.inn.trusthings.model.expression.Expression;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustProfile;

public class ExpressionBuilder {

	public static Expression build(TrustProfile requestedProfile) {
		Expression expression = new Expression(); 
		List<TrustAttribute> requestedAttrList = requestedProfile.getAttributes();
		
		
		
		return expression;
	}

}
