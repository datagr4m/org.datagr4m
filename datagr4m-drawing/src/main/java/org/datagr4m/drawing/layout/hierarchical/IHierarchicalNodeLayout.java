package org.datagr4m.drawing.layout.hierarchical;

import java.util.List;

import org.datagr4m.drawing.layout.IRunnableLayout;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.model.bounds.IBounds;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.monitors.ITimeMonitorable;


public interface IHierarchicalNodeLayout extends IRunnableLayout, ITimeMonitorable{
    // Hierarchy
    public List<IHierarchicalNodeLayout> getChildren();
    public List<IHierarchicalNodeLayout> getNeighbours();
    public IHierarchicalNodeLayout getParent();
    public IHierarchicalNodeLayout getLayout(IHierarchicalNodeModel model);
    public boolean isLeaf();
    public int getDepth();
    public boolean addChild(IHierarchicalNodeLayout item);
    
    //public int getMaxLevels();
    
    // Attach a model to handle
    public void setModel(IHierarchicalNodeModel model);
    public IHierarchicalNodeModel getModel();
    
    // Bounds
    public RectangleBounds getActualBounds();
    public IBounds getBounds();
    public void setBounds(IBounds bounds);
    
    // ILayout
    public BoundedForceAtlasLayout getDelegate();
    
    public void onDoneAlgo();
    
    // Edge
    public void goAlgoEdge();    
    public boolean isDoRunEdgeLayout();
    public void setDoRunEdgeLayout(boolean doRunEdgeLayout);    
    public IHierarchicalEdgeModel getTubeModel();
    public IHierarchicalEdgeLayout getEdgeLayout();
    public void setTubeModel(IHierarchicalEdgeModel model);
    public void setTubeLayout(IHierarchicalEdgeLayout layout);
    
    // search
    public IHierarchicalNodeLayout findLayoutHoldingModel(String model);
    public IHierarchicalNodeLayout findLayoutHoldingModel(IHierarchicalNodeModel model);
}
