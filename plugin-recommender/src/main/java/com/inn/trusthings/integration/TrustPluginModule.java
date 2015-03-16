package com.inn.trusthings.integration;
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

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.inn.trusthings.integration.TrustFilterByExclusion;
import com.inn.trusthings.integration.TrustFilterByThreshold;
import com.inn.trusthings.integration.TrustScorer;
import uk.ac.open.kmi.iserve.discovery.api.ranking.Filter;
import uk.ac.open.kmi.iserve.discovery.api.ranking.RecommendationPluginModule;
import uk.ac.open.kmi.iserve.discovery.api.ranking.Scorer;

public class TrustPluginModule extends AbstractModule implements RecommendationPluginModule {
    @Override
    protected void configure() {
        Multibinder<Filter> filterBinder = Multibinder.newSetBinder(binder(), Filter.class);
        filterBinder.addBinding().to(TrustFilterByThreshold.class);
        filterBinder.addBinding().to(TrustFilterByExclusion.class);
        Multibinder<Scorer> scorerBinder = Multibinder.newSetBinder(binder(), Scorer.class);
        scorerBinder.addBinding().to(TrustScorer.class);
    }
    
}
