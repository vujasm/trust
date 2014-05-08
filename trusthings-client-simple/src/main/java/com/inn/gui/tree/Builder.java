package com.inn.gui.tree;

import javax.swing.tree.TreeModel;

public class Builder {
	
	
	public static TreeModel getModel(com.inn.util.tree.Tree tree){
		
		Node rootNode = new Node(tree.getRoot());
		Model model = new Model(rootNode);
		
		
		
		return model;
		
	}

}
