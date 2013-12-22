package org.datagr4m.application.gui.trees.nodes.builder;

import java.util.Set;

import org.datagr4m.topology.graph.IPropertyNode;


public class ShowContentOfFirstNodeOnly implements INodeVisitPredicate {
    public ShowContentOfFirstNodeOnly() {
    }
    
    @Override
    public boolean continueWith(Set<? extends Object> traversed, IPropertyNode node, int depth) {
        int n = hasNode(traversed);
        if(n>=2)
            return false;
        else
            return true;
    }
    
    public int hasNode(Set<? extends Object> traversed){
        int n = 0;
        for(Object o: traversed){
            if(o instanceof IPropertyNode)
                n++;
        }
        return n;
    }
}