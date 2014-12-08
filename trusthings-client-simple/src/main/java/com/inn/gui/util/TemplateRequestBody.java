package com.inn.gui.util;

/*
 * #%L
 * trusthings-client-simple
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
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.io.CharStreams;

import fordemo.demo1;

public class TemplateRequestBody {

	public static String getRequestSearchOnly(String keyword) {
		InputStream is = demo1.class.getResourceAsStream("/jsonTemplateSearch.json");
		try {
			String criteria = CharStreams.toString(new InputStreamReader(is));

			criteria = criteria.replace("?keyword", "\"" + keyword + "\"");

			return criteria;
		} catch (IOException e) {
			e.printStackTrace();
			return "something went wrong - see stack trace";
		}

	}

	public static String getRequestFilteringOnly(String attributes, String keyword) {
		InputStream is = demo1.class.getResourceAsStream("/jsonTemplateFiltering.json");
		try {
			String criteria = CharStreams.toString(new InputStreamReader(is));

			criteria = criteria.replace("?params", attributes);
			criteria = criteria.replace("?keyword", "\"" + keyword + "\"");

			return criteria;
		} catch (IOException e) {
			e.printStackTrace();
			return "something went wrong - see stack trace";
		}

	}

	public static String getRequestRankingOnly(String attributes, String rankingOrder, String keyword) {
		InputStream is = demo1.class.getResourceAsStream("/jsonTemplateScoring.json");
		try {
			String criteria = CharStreams.toString(new InputStreamReader(is));
			criteria = criteria.replace("?params", attributes);
			criteria = criteria.replace("?keyword", "\"" + keyword + "\"");
			criteria = criteria.replace("?ranking", "\"" + rankingOrder + "\"");

			return criteria;
		} catch (IOException e) {
			e.printStackTrace();
			return "something went wrong - see stack trace";
		}
	}

	public static String getRequestFilteringRanking(String attributes, String rankingOrder, String keyword) {
		InputStream is = demo1.class.getResourceAsStream("/jsonTemplateFilteringScoring.json");
		try {
			String criteria = CharStreams.toString(new InputStreamReader(is));
			criteria = criteria.replace("?params", attributes);
			criteria = criteria.replace("?keyword", "\"" + keyword + "\"");
			criteria = criteria.replace("?ranking", "\"" + rankingOrder + "\"");
			return criteria;
		} catch (IOException e) {
			e.printStackTrace();
			return "something went wrong - see stack trace";
		}
	}

}
