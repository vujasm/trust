package com.inn.trusthings.model.expression;

/*
 * #%L
 * trusthings-model
 * %%
 * Copyright (C) 2014 - 2015 INNOVA S.p.A
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
