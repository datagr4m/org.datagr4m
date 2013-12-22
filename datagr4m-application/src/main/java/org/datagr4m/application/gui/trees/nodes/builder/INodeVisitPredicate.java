package org.datagr4m.application.gui.trees.nodes.builder;

import java.util.Set;

import org.datagr4m.topology.graph.IPropertyNode;


public interface INodeVisitPredicate {
    public boolean continueWith(Set<? extends Object> traversed, IPropertyNode node, int depth);
}