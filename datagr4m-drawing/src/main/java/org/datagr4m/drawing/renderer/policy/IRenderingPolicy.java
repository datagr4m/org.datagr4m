package org.datagr4m.drawing.renderer.policy;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.renderer.bounds.BoundsRendererSettings;
import org.datagr4m.drawing.renderer.items.ItemRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.GraphRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.LocalEdgeRendererSettings;


public interface IRenderingPolicy {
	public void setup(IHierarchicalModel model);
    public void apply(IHierarchicalRenderer root);
    
    public ItemRendererSettings getItemSettings();
    public LocalEdgeRendererSettings getEdgeSettings();
    public TubeRendererSettings getTubeSettings();
    public BoundsRendererSettings getBoundsSettings();    
    public GraphRendererSettings getGraphSettings();
}
