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


import java.net.URI;

import com.hp.hpl.jena.datatypes.RDFDatatype;

/**
 * TrustAttribute For COMPOSE, it is any trust-relevant functional / non-functional property
 * @author marko
 *
 */
public class TrustAttribute extends TResource{
	
	private double importance;
	
	private Object value;
	
	private Object minValue;
	
	private Object maxValue;
	
	private RDFDatatype valueDatatype;

	public TrustAttribute(URI uri) {
		super(uri);
	}

	public double getImportance() {
		return importance;
	}
	
	public void setImportance(double imp) {
		this.importance = imp;
	}
	

	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getMinValue() {
		return minValue;
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public RDFDatatype getValueDatatype() {
		return valueDatatype;
	}

	public void setValueDatatype(RDFDatatype valueDatatype) {
		this.valueDatatype = valueDatatype;
	}
	
	@Override
	public String toString() {
		return getUri()+" "+getValue()+"^^"+getValueDatatype();
	}
	
	

}
