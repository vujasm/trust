package com.inn.gui.tree;

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


import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Sets;
import com.inn.trusthings.model.vocabulary.Trust;

public class Node implements TreeNode{
	

	/**
     * The title will be displayed in the tree
     */
    private String title;

    

	/**
     * The title will be displayed in the tree
     */
    private String name;
    
    /*
     * Type of this node, which is used by a renderer to set appropriate icon
     * for the node
     */
    private int type;
 
    private Vector<TreeNode> children = new Vector<TreeNode>();
    private TreeNode parent;
    
    // Constants for types of node
    public static final int NODE_ROOT         = 0;
    public static final int NODE_PROJECT     = 1;
    public static final int NODE_SOURCE          = 2;
    public static final int NODE_PACKAGE      = 4;
    public static final int NODE_CLASS          = 5;
    public static final int NODE_FOLDER      = 6;
    
    com.inn.util.tree.Node ontNode = null;
    
    public com.inn.util.tree.Node getOntNode() {
		return ontNode;
	}
    
    public Node(String title) {
    	this.title = title;
	}

    public Node(com.inn.util.tree.Node ontNode) {
    	this.ontNode = ontNode;
    	this.setTitle(ontNode.getName());
    	this.setName(ontNode.getName());
    	List<com.inn.util.tree.Node> list = this.ontNode.getSubNodes();
    	for (com.inn.util.tree.Node node : list) {
    		children.add(new Node(node));
		}
	}

	public void addChild(TreeNode child) {
        children.add(child);
    }
    
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    
    @Override
    public Enumeration<TreeNode> children() {
        return children.elements();
    }
 
    @Override
    public boolean getAllowsChildren() {
        return true;
    }
 
    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.elementAt(childIndex);
    }
 
    @Override
    public int getChildCount() {
        return children.size();
    }
 
    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }
 
    @Override
    public TreeNode getParent() {
        return this.parent;
    }
 
    @Override
    public boolean isLeaf() {
        return (children.size() == 0);
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getTitle() {
    	if (title.equals("UnmeasurableTrustAttribute")){
    		title = "Descriptive Dimensions";
    	}
    	if (title.equals("MeasurableTrustAttribute")){
    		title = "Quantified Dimensions";
    	}
    	if (title.equals("TrustAttribute")){
    		title = "Trust Dimensions";
    	}

    	
        return title;
    }
    
    /**
     * The node object should override this method to provide a text that will
     * be displayed for the node in the tree.
     */
    public String toString() {
        return getTitle();
    }
 
    public int getType() {
        return type;
    }

    
    String[] notSupported = new String [] {
    		
    		Trust.Reputation.getLocalName(),
    		Trust.ProviderCategoryBy3rdParty.getLocalName(),
    		Trust.UserRating.getLocalName(),
    		Trust.Popularity.getLocalName(),
    		Trust.NumberOfRequests.getLocalName(),
    		Trust.ContractCompliance.getLocalName(),
    		Trust.QoSAttribute.getLocalName(),
    		Trust.Availability.getLocalName(),
    		Trust.ResponseTime.getLocalName(),
    		Trust.SecurityAttribute.getLocalName(),
    		Trust.SecurityRequirment.getLocalName(),
    		Trust.ProviderLocation.getLocalName(),
    		Trust.UnmeasurableTrustAttribute.getLocalName(),
    		Trust.MeasurableTrustAttribute.getLocalName(),
    		Trust.TrustAttribute.getLocalName(),
    };
    HashSet<String> notsupportedSet = Sets.newHashSet(notSupported);
    
	public boolean isSupported() {
		if (notsupportedSet.contains(getName())){
			return false;
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}   
}

