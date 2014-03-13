package com.inn.common;

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


public class Tuple2<T1, T2> extends Tuple{
	

	private T1 T1;
	
	private T2 T2;
	
	public Tuple2(T1 t1, T2 t2){
		this.T1 = t1;
		this.T2 = t2;
	}
	
	public T1 getT1() {
		return T1;
	}
	
	
	public T2 getT2() {
		return T2;
	}
	
	public void setT1(T1 t){
		T1 = t;
	}
	
	public void setT2(T2 t){
		T2 = t;
	}
	
	
	
	

}
