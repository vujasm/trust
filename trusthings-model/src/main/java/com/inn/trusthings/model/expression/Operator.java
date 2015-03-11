package com.inn.trusthings.model.expression;

public class Operator {
	
	private Operator next;
	private Operator before;
	private Operand operandLeft;
	private Operand operandRight;

	
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

	public Operand getOperandLeft() {
		return operandLeft;
	}

	public void setOperandLeft(Operand operandLeft) {
		this.operandLeft = operandLeft;
	}

	public Operand getOperandRight() {
		return operandRight;
	}

	public void setOperandRight(Operand operandRight) {
		this.operandRight = operandRight;
	}

}
