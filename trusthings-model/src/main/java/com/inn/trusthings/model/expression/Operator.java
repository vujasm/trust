package com.inn.trusthings.model.expression;

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
