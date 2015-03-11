package com.inn.trusthings.model.expression;

import java.util.List;

import com.google.common.collect.Lists;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;

public class ExpressionBuilder {
	
	static TrustCriteria criteria;
	private static ExpressionBuilder instance = new ExpressionBuilder();
	
	private List<SingleElement> listOperandByAnd = Lists.newArrayList();
	private List<OrElement> listOperandByOrGroup = Lists.newArrayList();
	
	private SingleElement current;
	private OrElement currentOrgroup;

	public TrustCriteria build() {
		TrustCriteria expression = new TrustCriteria();
		expression.setSingleAttributeList(listOperandByAnd);
		expression.setOrGroupAttributeList(listOperandByOrGroup);
		return expression;
	}

	public static ExpressionBuilder startNewTrustCriteria() {
		criteria = new TrustCriteria();	
		return instance;

}

	public ExpressionBuilder attribute(TrustAttribute att1) {
		SingleElement operand = new SingleElement(att1);
		current = operand;
		return instance;
	}
	
	public ExpressionBuilder and() {
//		And and =  new And();
		listOperandByAnd.add(current);
		return instance;
	}

	public ExpressionBuilder openOrBracket() {
		currentOrgroup = new OrElement();
		return instance;
	}

	public ExpressionBuilder or() {
		currentOrgroup.getElements().add(current);
		return instance;
	}

	public ExpressionBuilder closeOrBracket() {
		listOperandByOrGroup.add(currentOrgroup);
		currentOrgroup = null;
		return instance;
	}
}
