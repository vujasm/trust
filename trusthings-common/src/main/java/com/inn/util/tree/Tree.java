package com.inn.util.tree;

/*
 * #%L
 * trusthings-common
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


import java.util.List;

import com.inn.common.Const;

public class Tree {
	
	
	private Node root;
	
	public Node getRoot() {
		return root;
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		append(root, b, 1);
		return b.toString();
	}

	private void append(Node node, StringBuffer b, int pos) {
		b.append(node.toString()).append(Const.NEW_LINE);
		List<Node> nodes = node.getSubNodes();
		for (Node subNode : nodes) {
			append(subNode, b, pos);
		}
	}
	
	

}
