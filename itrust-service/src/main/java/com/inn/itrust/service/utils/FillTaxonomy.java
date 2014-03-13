package com.inn.itrust.service.utils;

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


import java.util.Iterator;

import com.hp.hpl.jena.ontology.OntClass;
import com.inn.common.structure.tree.Node;

public class FillTaxonomy {

	public static void execute(OntClass ontClass, Node node) {
			Iterator<OntClass>	it = ontClass.listSubClasses(true);
			while (it.hasNext()) {
				OntClass ontClass2 = (OntClass) it.next();
				Node sNode = new Node(ontClass2.getLocalName());
				node.addSubNode(sNode);
				execute(ontClass2, sNode);
			}
	}

}
