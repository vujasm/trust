package com.inn.test.compositions;

/*
 * #%L
 * trusthings-client-simple
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.inject.Guice;
import com.inn.common.CompositeServiceWrapper;
import com.inn.common.CompositionIdentifier;
import com.inn.compose.test.GraphTest;
import com.inn.trusthings.json.TrustPOJOFactory;
import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.module.TrustModule;
import com.inn.trusthings.op.enums.EnumLevel;
import com.inn.trusthings.service.interfaces.TrustCompositionManager;
import com.inn.util.tuple.Tuple2;

public class TestRankCompositions {

	public static void test() {

		List<CompositeServiceWrapper> list = Lists.newArrayList();
		String noderredFlow = "";
		String critaeriaAsJson = "";
		InputStream is = TestRankCompositions.class.getResourceAsStream("/flowSnowUpdate.json");
		InputStream is2 = TestRankCompositions.class.getResourceAsStream("/compositecriteria.json");
		try {
			noderredFlow = CharStreams.toString(new InputStreamReader(is));
			critaeriaAsJson = CharStreams.toString(new InputStreamReader(is2));
			is.close();
			is2.close();

			CompositionIdentifier compositionIdentifier = new CompositionIdentifier(UUID.randomUUID().toString());
			list.add(new CompositeServiceWrapper(compositionIdentifier, noderredFlow.toString()));

			TrustCompositionManager trustManager = Guice.createInjector(new TrustModule()).getInstance(TrustCompositionManager.class);
			TrustCriteria criteria = new TrustPOJOFactory().ofTrustCriteria(critaeriaAsJson);
			List<Tuple2<CompositionIdentifier, Double>> scores = trustManager.obtainTrustIndexes(list, criteria, EnumLevel.COMPOSITE, null);
			for (Tuple2<CompositionIdentifier, Double> t : scores) {
				System.out.println(t.getT1().getId() + "  " + t.getT2());
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void main(String[] args) {
		test();
	}

}
