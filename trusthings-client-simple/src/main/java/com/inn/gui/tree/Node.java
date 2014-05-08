package com.inn.gui.tree;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class Node implements TreeNode{
	

	/**
     * The title will be displayed in the tree
     */
    private String title;
    
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
        return title;
    }
    
    /**
     * The node object should override this method to provide a text that will
     * be displayed for the node in the tree.
     */
    public String toString() {
        return ontNode.getName();
    }
 
    public int getType() {
        return type;
    }   
}

