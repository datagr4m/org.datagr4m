package org.datagr4m.drawing.renderer.items.hierarchical.graph;

import org.datagr4m.drawing.renderer.bounds.BoundsRendererSettings;
import org.datagr4m.drawing.renderer.bounds.IBoundsRendererSettings;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.ItemRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.IEdgeRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.LocalEdgeRendererSettings;



public class GraphRendererSettings implements IGraphRendererSettings {
    public GraphRendererSettings(){
        nodeSettings = new ItemRendererSettings();
        edgeSettings = new LocalEdgeRendererSettings();
        boundsSettings = new BoundsRendererSettings();
    }
    
    @Override
    public IItemRendererSettings getNodeSettings() {
        return nodeSettings;
    }
    @Override
    public void setNodeSettings(IItemRendererSettings nodeSettings) {
        this.nodeSettings = nodeSettings;
    }
    @Override
    public IEdgeRendererSettings getEdgeSettings() {
        return edgeSettings;
    }
    @Override
    public void setEdgeSettings(IEdgeRendererSettings edgeSettings) {
        this.edgeSettings = edgeSettings;
    }
    @Override
    public IBoundsRendererSettings getBoundsSettings() {
        return boundsSettings;
    }
    @Override
    public void setBoundsSettings(IBoundsRendererSettings boundsSettings) {
        this.boundsSettings = boundsSettings;
    }
    
    @Override
    public boolean isLocalEdgeDisplayed() {
        return isLocalEdgeDisplayed;
    }

    @Override
    public void setLocalEdgeDisplayed(boolean isEmbeddedEdgeDisplayed) {
        this.isLocalEdgeDisplayed = isEmbeddedEdgeDisplayed;
    }


    protected IItemRendererSettings nodeSettings;
    protected IEdgeRendererSettings edgeSettings;
    protected IBoundsRendererSettings boundsSettings;
    
    protected boolean isLocalEdgeDisplayed = true;
}
