package com.inn.gui.tree;

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


import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class Model implements TreeModel {
 
    private TreeNode rootNode;
   private Vector<TreeModelListener> listeners =new Vector<TreeModelListener>();
    
    public Model(TreeNode rootNode) {
        this.rootNode = rootNode;
    }
    
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }
 
    @Override
    public Object getChild(Object parent, int index) {
        TreeNode parentNode = (TreeNode) parent;
        return parentNode.getChildAt(index);
    }
 
    @Override
    public int getChildCount(Object parent) {
        TreeNode parentNode = (TreeNode) parent;
        return parentNode.getChildCount();
    }
 
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        TreeNode parentNode = (TreeNode) parent;
        TreeNode childNode = (TreeNode) child;
        return parentNode.getIndex(childNode);
    }
 
    @Override
    public Object getRoot() {
        return rootNode;
    }
 
    @Override
    public boolean isLeaf(Object node) {
        TreeNode treeNode = (TreeNode) node;
        return treeNode.isLeaf();
    }
 
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }
 
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }
 

}
