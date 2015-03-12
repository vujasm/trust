package com.inn.trusthings.service.interfaces;

/*
 * #%L
 * trusthings-service
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


import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.service.config.GlobalTrustCriteria;

public interface TrustManager {
	
	
	/**
	 *  Set global trust request. If not set, the trust manager will be using the default the default one {@link GlobalTrustCriteria}
	 * @param criteria Trust criteria as POJO
	 */
	public void setGlobalTrustCriteria(TrustCriteria criteria);
	
	/**
	 * Set global trust request. If not set, the trust manager will be using the default the default one {@link GlobalTrustCriteria}
	 * @param critaeriaAsJson  Trust criteria as Json string
	 */
	public void setGlobalTrustCriteria(String critaeriaAsJson) ; 
	
	
	public TrustCriteria getGlobalTrustCriteria();

}
