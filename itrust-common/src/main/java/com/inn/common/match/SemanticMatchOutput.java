package com.inn.common.match;

/*
 * #%L
 * itrust-common
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


public enum SemanticMatchOutput {

	// Increasing order of match
	Sem_Disjoint("Semantically, there is no match between concept required and concept present", 0D), // 0
	Sem_MoreGeneral("The concept required is a superclass (a more general concept) of the one present", 0.3D), //
	Sem_MoreSpecific("The concept required is a subclass (a more specific concept) of the one present", 0.6D), // 1
	Sem_Exact("The concept present is semantically exact match to the concept required", 1D); // 1

	private String desc;

	private double asNumeric;

	private SemanticMatchOutput(String desc, double asNumeric) {
		this.desc = desc;
		this.asNumeric = asNumeric;
	}

	public String getDesc() {
		return desc;
	}

	public double asNumeric() {
		return asNumeric;
	}
}
