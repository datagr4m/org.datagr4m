package org.datagr4m.drawing.viewer.mouse.edges;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.viewer.mouse.IClickableItem;


public class ClickedEdge implements IClickableItem{
    private static final long serialVersionUID = -6938506750844450939L;

    public ClickedEdge(IEdge edge) {
        this.edge = edge;
    }

    public IEdge getEdge() {
        return edge;
    }
    
    @Override
	public String toString(){
        return edge.toString();
    }

    protected IEdge edge;
}
