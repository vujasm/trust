package com.inn.trusthings.module;

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

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.inn.trusthings.kb.ConcurrentSparqlGraphStoreManager;
import com.inn.trusthings.kb.KnowledgeBaseManager;
import com.inn.trusthings.kb.KnowledgeBaseManagerSparql;
import com.inn.trusthings.kb.SparqlGraphStoreFactory;
import com.inn.trusthings.kb.SparqlGraphStoreManager;
import com.inn.trusthings.service.interfaces.RankingCompositionsManager;
import com.inn.trusthings.service.interfaces.RankingManager;
import com.inn.trusthings.service.interfaces.TrustCompositionManager;
import com.inn.trusthings.service.interfaces.TrustSimpleManager;
import com.inn.trusthings.service.mgrs.impl.BasicRankingManager;
import com.inn.trusthings.service.mgrs.impl.BasicTrustManager;
import com.inn.trusthings.service.mgrs.impl.RankingCompositionsManagerImpl;
import com.inn.trusthings.service.mgrs.impl.TrustCompositionManagerImpl;


public class TrustModule extends AbstractModule {
	
    private static final Logger log = LoggerFactory.getLogger(TrustModule.class);

    protected void configure() {

    	Names.bindProperties(binder(), getProperties());
        bind(KnowledgeBaseManager.class).to(KnowledgeBaseManagerSparql.class);
        bind(TrustSimpleManager.class).to(BasicTrustManager.class);
        bind(TrustCompositionManager.class).to(TrustCompositionManagerImpl.class);
        bind(RankingManager.class).to(BasicRankingManager.class);
        bind(RankingCompositionsManager.class).to(RankingCompositionsManagerImpl.class);
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
