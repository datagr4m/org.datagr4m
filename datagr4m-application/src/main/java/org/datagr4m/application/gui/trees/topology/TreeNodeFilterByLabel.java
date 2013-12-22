package org.datagr4m.application.gui.trees.topology;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNodeFilterByLabel {
    public TreeNodeFilterByLabel(String filter) {
        this.filter = filter;
    }

    public boolean accepts(DefaultMutableTreeNode node) {
        return match(node);
    }

    protected boolean match(DefaultMutableTreeNode node) {
        if (filter.equals(node.toString()))
            return true;
        else
            return false;
    }

    @Override
	public String toString() {
        return "filter device ip: + " + filter;
    }

    protected String filter;
}