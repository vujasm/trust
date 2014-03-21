package com.inn.trusthings.service.collectors;

/*
 * #%L
 * trusthings-service
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

import com.hp.hpl.jena.rdf.model.Model;


/**
 * 
 * @author marko
 *
 */
public interface Collector {
	
	/**
	 * 
	 * FIXME maybe collectors should be asynchr services, which run periodically.
	 * However, in this case there should be runtime monitors, because after collector returns its value,
	 * the value may influence the trust assessment.
	 * @param uri resource uri
	 * @return Model
	 * 
	 */
	Model collectInformation(URI uri);

}
