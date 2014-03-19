
package com.inn.itrust;

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


public interface Component {
    /**
     * This method will be called when the server is initialised.
     * If necessary it should take care of updating any indexes on boot time.
     */
    void initialise();

    /**
     * This method will be called when the server is being shutdown.
     * Ensure a clean shutdown.
     */
    void shutdown();

}
