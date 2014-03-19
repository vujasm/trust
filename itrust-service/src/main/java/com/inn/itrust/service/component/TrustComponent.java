package com.inn.itrust.service.component;

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

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.inn.itrust.service.mgrs.KnowledgeBaseManager;
import com.inn.itrust.service.mgrs.RankingManager;
import com.inn.itrust.service.mgrs.SparqlGraphStoreFactory;
import com.inn.itrust.service.mgrs.SparqlGraphStoreManager;
import com.inn.itrust.service.mgrs.TrustManager;
import com.inn.itrust.service.mgrs.impl.ConcurrentSparqlGraphStoreManager;
import com.inn.itrust.service.mgrs.impl.KnowledgeBaseManagerSparql;
import com.inn.itrust.service.mgrs.impl.BasicRankingManager;
import com.inn.itrust.service.mgrs.impl.BasicTrustManager;


public class TrustComponent extends AbstractModule {
	
    private static final String CONFIG_PROPERTIES_FILENAME = "config.properties";

    private static final Logger log = LoggerFactory.getLogger(TrustComponent.class);


    protected void configure() {

    	 Names.bindProperties(binder(), getProperties());
    	 
        // Create the EventBus
        final EventBus eventBus = new EventBus("MyEventBus");
        
        // Bind components
        bind(KnowledgeBaseManager.class).to(KnowledgeBaseManagerSparql.class);
        bind(TrustManager.class).to(BasicTrustManager.class);
        bind(RankingManager.class).to(BasicRankingManager.class);
        bind(EventBus.class).toInstance(eventBus);

        // Assisted Injection for the Graph Store Manager
        log.info("install SparqlGraphStoreFactory for SparqlGraphStoreManager to ConcurrentSparqlGraphStoreManager");
        install(new FactoryModuleBuilder()
                .implement(SparqlGraphStoreManager.class, ConcurrentSparqlGraphStoreManager.class)
                .build(SparqlGraphStoreFactory.class));
    }
    

    private Properties getProperties() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILENAME));
            return properties;
        } catch (IOException ex) {
            log.error("Error obtaining plugin properties", ex);
        }
        return new Properties();
    }
}
