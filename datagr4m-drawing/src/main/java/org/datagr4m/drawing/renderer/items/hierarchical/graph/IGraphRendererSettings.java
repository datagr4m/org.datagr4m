package org.datagr4m.drawing.renderer.items.hierarchical.graph;

import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.IEdgeRendererSettings;



public interface IGraphRendererSettings extends IHierarchicalRendererSettings{
    public IItemRendererSettings getNodeSettings();
    public IEdgeRendererSettings getEdgeSettings();
    public void setNodeSettings(IItemRendererSettings nodeSettings);
    public void setEdgeSettings(IEdgeRendererSettings edgeSettings);
    
    public boolean isLocalEdgeDisplayed();
    public void setLocalEdgeDisplayed(boolean isEmbeddedEdgeDisplayed);
}