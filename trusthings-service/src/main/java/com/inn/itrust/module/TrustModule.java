package com.inn.itrust.module;

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

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.inn.itrust.service.interfaces.RankingManager;
import com.inn.itrust.service.interfaces.TrustManager;
import com.inn.itrust.service.kb.ConcurrentSparqlGraphStoreManager;
import com.inn.itrust.service.kb.KnowledgeBaseManager;
import com.inn.itrust.service.kb.KnowledgeBaseManagerSparql;
import com.inn.itrust.service.kb.SparqlGraphStoreFactory;
import com.inn.itrust.service.kb.SparqlGraphStoreManager;
import com.inn.itrust.service.mgrs.impl.BasicRankingManager;
import com.inn.itrust.service.mgrs.impl.BasicTrustManager;


public class TrustModule extends AbstractModule {
	
    private static final Logger log = LoggerFactory.getLogger(TrustModule.class);

    protected void configure() {

    	Names.bindProperties(binder(), getProperties());
        bind(KnowledgeBaseManager.class).to(KnowledgeBaseManagerSparql.class);
        bind(TrustManager.class).to(BasicTrustManager.class);
        bind(RankingManager.class).to(BasicRankingManager.class);
        install(new FactoryModuleBuilder()
                .implement(SparqlGraphStoreManager.class, ConcurrentSparqlGraphStoreManager.class)
                .build(SparqlGraphStoreFactory.class));
    }
    

    private Properties getProperties() {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            return properties;
        } catch (IOException ex) {
            log.error("Error loading properties from config.properties", ex);
        }
        return new Properties();
    }
}