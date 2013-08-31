package org.datagr4m.drawing.layout.hierarchical;

import java.util.List;

import org.datagr4m.drawing.layout.IRunnableLayout;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.model.bounds.IBounds;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;


public interface IHierarchicalLayout extends IRunnableLayout{
    // Hierarchy
    public List<IHierarchicalLayout> getChildren();
    public List<IHierarchicalLayout> getNeighbours();
    public IHierarchicalLayout getParent();
    public IHierarchicalLayout getLayout(IHierarchicalModel model);
    public boolean isLeaf();
    public int getDepth();
    public boolean addChild(IHierarchicalLayout item);
    
    //public int getMaxLevels();
    
    // Attach a model to handle
    public void setModel(IHierarchicalModel model);
    public IHierarchicalModel getModel();
    
    // Bounds
    public RectangleBounds getActualBounds();
    public IBounds getBounds();
    public void setBounds(IBounds bounds);
    
    // ILayout
    public BoundedForceAtlasLayout getDelegate();
    
    @Override
	public void initAlgo();
    @Override
	public void goAlgo();
    @Override
	public void endAlgo();
    @Override
	public boolean canAlgo();
    @Override
	public void resetPropertiesValues();
    
    public void onDoneAlgo();
    
    // Edge
    public void goAlgoEdge();    
    public boolean isDoRunEdgeLayout();
    public void setDoRunEdgeLayout(boolean doRunEdgeLayout);    
    public IHierarchicalEdgeModel getTubeModel();
    public IHierarchicalEdgeLayout getTubeLayout();
    public void setTubeModel(IHierarchicalEdgeModel model);
    public void setTubeLayout(IHierarchicalEdgeLayout layout);
    
    // search
    public IHierarchicalLayout findLayoutHoldingModel(String model);
    public IHierarchicalLayout findLayoutHoldingModel(IHierarchicalModel model);
}
