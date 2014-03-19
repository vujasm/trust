

package com.inn.itrust.config;

/*
 * #%L
 * itrust-service
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

public class Configuration {

    // Configuration properties
    public static final String PROXY_HOST_NAME_PROP = "http.proxyHost";
    public static final String PROXY_PORT_PROP = "http.proxyPort";

    // TRust Data Triple Store ENdpoints
    public static final String SPARQL_ENDPOINT_QUERY_PROP = "services.sparql.query";
    public static final String SPARQL_ENDPOINT_UPDATE_PROP = "services.sparql.update";
    public static final String SPARQL_ENDPOINT_SERVICE_PROP = "services.sparql.service";
    
    // Services data Triple Store ENdpoints
    public static final String EXT_SPARQL_ENDPOINT_QUERY = "http://localhost:3033/data/query";
    public static final String EXT_SPARQL_ENDPOINT_UPDATE = "http://localhost:3033/data/upload";
    public static final String EXT_SPARQL_ENDPOINT_SERVICE = "http://localhost:3033/data/data";

}
