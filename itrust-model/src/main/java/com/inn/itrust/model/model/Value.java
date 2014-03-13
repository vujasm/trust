package com.inn.itrust.model.model;

/*
 * #%L
 * itrust-model
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


/**
 * 
 * LevelOfTrust is a value between zero (0) and one (1). Zero denotates a distrust,
 * whereas one denotates a full trust.
 * 
 * @author marko
 * 
 */
public class Value {

	private double numericalValue = 1;

	public static final double Trustworthy = 1;
	
	public static final double NoTrustworthy = 0;
	
	public static final double Uknown = - 1;

	public Value(double val) {
		this.numericalValue = val;
	}
	
	public Value(){
		
	}

	public double getNumericalValue() {
		return numericalValue;
	}

	public void setNumericalValue(double val) {
		this.numericalValue = val;
	}

	public boolean isTrustworthy(double treshhold) {
		return (numericalValue >= treshhold);
	}

	public boolean isNoTrustworthy() {
		return (numericalValue == NoTrustworthy);
	}

}


//readme - shoudl I introduce MeasureOfTrust
