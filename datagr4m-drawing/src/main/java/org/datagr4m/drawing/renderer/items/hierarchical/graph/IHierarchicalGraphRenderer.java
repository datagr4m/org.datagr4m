package org.datagr4m.drawing.renderer.items.hierarchical.graph;

import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.IEdgeRenderer;



public interface IHierarchicalGraphRenderer extends IHierarchicalRenderer{
    // Edit itself
    public IEdgeRenderer getEdgeRenderer();
    //public IEdgeRendererSettings getEdgeRendererSettings();
    public void setEdgeRenderer(IEdgeRenderer renderer);
    //public void setEdgeRendererSettings(IEdgeRendererSettings settings);
    
    public IEdgeRenderer getInterChildrenEdgeRenderer();
    //public IEdgeRendererSettings getInterChildrenEdgeRendererSettings();
    public void setInterChildrenEdgeRenderer(IEdgeRenderer renderer);
    //public void setInterChildrenEdgeRendererSettings(IEdgeRendererSettings settings);
    
    public void setRendererSettings(IGraphRendererSettings settings);
    public IGraphRendererSettings getRendererSettings();
    
    // Edit a child renderer
    /*public INodeRenderer getNodeRenderer(IHierarchicalModel model);
    public INodeViewSettings getNodeViewSettings(IHierarchicalModel model);
    public void setNodeRenderer(IHierarchicalModel model, INodeRenderer renderer);
    public void setNodeViewSettings(IHierarchicalModel model, INodeViewSettings settings);
    
    public IEdgeRenderer getEdgeRenderer(IHierarchicalModel model);
    public IEdgeViewSettings getEdgeViewSettings(IHierarchicalModel model);
    public void setEdgeRenderer(IHierarchicalModel model, IEdgeRenderer renderer);
    public void setEdgeViewSettings(IHierarchicalModel model, IEdgeViewSettings settings);

    public IEdgeRenderer getInterChildrenEdgeRenderer(IHierarchicalModel model);
    public IEdgeViewSettings getInterChildrenEdgeViewSettings(IHierarchicalModel model);
    public void setInterChildrenEdgeRenderer(IHierarchicalModel model, IEdgeRenderer renderer);
    public void setInterChildrenEdgeViewSettings(IHierarchicalModel model, IEdgeViewSettings settings);*/
}
