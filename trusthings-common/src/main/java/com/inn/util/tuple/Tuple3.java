package com.inn.util.tuple;

/*
 * #%L
 * trusthings-common
 * %%
 * Copyright (C) 2015 COMPOSE project
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


public class Tuple3<T1, T2, T3> extends Tuple{
	

	private final T1 t1;
	
	private final T2 t2;
	
	private final T3 t3;
	
	
	public Tuple3(T1 t1, T2 t2, T3 t3){
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}
	
	public T1 getT1() {
		return t1;
	}
	
	
	public T2 getT2() {
		return t2;
	}
	
	public T3 getT3() {
		return t3;
	}

}
