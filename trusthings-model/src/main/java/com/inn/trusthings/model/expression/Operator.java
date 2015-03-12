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


public class Operator {
	
	private Operator next;
	private Operator before;
	private SingleElement operandLeft;
	private SingleElement operandRight;

	
	public Operator getBefore() {
		return before;
	}
	
	public void setBefore(Operator before) {
		this.before = before;
	}
	
	public Operator getNext() {
		return next;
	}
	
	public void setNext(Operator next) {
		this.next = next;
	}

	public SingleElement getOperandLeft() {
		return operandLeft;
	}

	public void setOperandLeft(SingleElement operandLeft) {
		this.operandLeft = operandLeft;
	}

	public SingleElement getOperandRight() {
		return operandRight;
	}

	public void setOperandRight(SingleElement operandRight) {
		this.operandRight = operandRight;
	}

}
