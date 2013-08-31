package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical;

import java.awt.Color;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;


public class MonochromeTubeRendererSettings extends TubeRendererSettings{
    public MonochromeTubeRendererSettings(Color color){
        this.color = color;
    }
    @Override
	public Color getEdgeColor(IEdge edge) {
        return color;
    }
    
    protected Color color;
}
