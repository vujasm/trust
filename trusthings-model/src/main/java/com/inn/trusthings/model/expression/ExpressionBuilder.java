package com.inn.trusthings.model.expression;

import java.util.List;

import com.google.common.collect.Lists;
import com.inn.trusthings.model.pojo.TrustAttribute;
import com.inn.trusthings.model.pojo.TrustCriteria;

public class ExpressionBuilder {
	
	private TrustCriteria criteria;	
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

	public  ExpressionBuilder startNewTrustCriteria() {
		criteria = new TrustCriteria();	
		return this;
	}

	public ExpressionBuilder attribute(TrustAttribute att1) {
		SingleElement operand = new SingleElement(att1);
		current = operand;
		return this;
	}
	
	public ExpressionBuilder and() {
//		And and =  new And();
		listOperandByAnd.add(current);
		return this;
	}

	public ExpressionBuilder openOrBracket() {
		currentOrgroup = new OrElement();
		return this;
	}

	public ExpressionBuilder or() {
		currentOrgroup.getElements().add(current);
		return this;
	}

	public ExpressionBuilder closeOrBracket() {
		listOperandByOrGroup.add(currentOrgroup);
		currentOrgroup = null;
		return this;
	}

	public ExpressionBuilder openOrBracket(Double weight) {
		currentOrgroup = new OrElement(weight);
		return this;
	}
}
