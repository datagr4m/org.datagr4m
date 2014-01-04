package org.datagr4m.drawing.navigation.plugin.edgetables;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;

public class JEdgeTableEntry {
    public JEdgeTableEntry(IEdge edge){
        this.edge = edge;
    }
    public IEdge getEdge() {
        return edge;
    }
    protected IEdge edge;
}
