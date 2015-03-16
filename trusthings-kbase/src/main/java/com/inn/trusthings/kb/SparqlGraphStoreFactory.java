package com.inn.trusthings.kb;

/*
 * #%L
 * trusthings-service
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

import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;


public interface SparqlGraphStoreFactory {

    SparqlGraphStoreManager create(@Assisted("sparqlQueryEndpoint") String sparqlQueryEndpoint,
                                   @Assisted("sparqlUpdateEndpoint") String sparqlUpdateEndpoint,
                                   @Assisted("sparqlServiceEndpoint") String sparqlServiceEndpoint,
                                   @Assisted("baseModels") Set<URI> baseModels,
                                   @Assisted("locationMappings") Map<String, String> locationMappings,
                                   @Assisted("ignoredImports") Set<String> ignoredImports);
}
