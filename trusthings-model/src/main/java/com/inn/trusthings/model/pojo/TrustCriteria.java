package com.inn.trusthings.model.pojo;

/*
 * #%L
 * trusthings-model
 * %%
 * Copyright (C) 2014 INNOVA S.p.A
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
import com.inn.trusthings.model.expression.Element;
import com.inn.trusthings.model.expression.Expression;
import com.inn.trusthings.model.expression.OrElement;
import com.inn.trusthings.model.expression.SingleElement;

public class TrustCriteria extends Expression {
	
	private List<Element> listOperandByAnd = Lists.newArrayList();
	private List<Element> listOperandByOrGroup = Lists.newArrayList();

	public void setSingleAttributeList(List<Element> list) {
		listOperandByAnd = list;
	}

	public void setOrGroupAttributeList(List<Element> list) {
		listOperandByOrGroup = list;
	}
	
	public List<Element> getListOperandByAnd() {
		return listOperandByAnd;
	}
	
	public List<Element> getListOperandByOrGroup() {
		return listOperandByOrGroup;
	}

}
