package org.datagr4m.application.gui.trees.nodes.builder;

import java.util.Set;

import org.datagr4m.topology.graph.IPropertyNode;


public class VisitAllWithMaxDepth implements INodeVisitPredicate {
    protected int maxDepth = 10;
    
    public VisitAllWithMaxDepth() {
    }
    
    public VisitAllWithMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean continueWith(Set<? extends Object> traversed, IPropertyNode node, int depth) {
        if (depth < maxDepth)
            return true;
        else
            return false;
    }
}