package org.datagr4m.drawing.model.items.hierarchical.graph.edges;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.viewer.mouse.IClickableItem;


public interface IEdge extends IClickableItem{
    public IBoundedItem getSourceItem();
    public IBoundedItem getTargetItem();

    public Object getSourceInterface();
    public Object getTargetInterface();

    public IPathObstacle getSourceObstacle();
    public IPathObstacle getTargetObstacle();
    
    public IEdgeInfo getEdgeInfo();
    public void setEdgeInfo(IEdgeInfo info);
    public IPath getPathGeometry();
    
    public boolean isOriented();

    public ItemState getState();
    public void setState(ItemState state);
    
    public Tube getParent();
    public void setParent(Tube parent);
    
    public void editSource(IBoundedItem sourceItem, IPathObstacle sourceObstacle);
    public void editTarget(IBoundedItem targetItem, IPathObstacle targetObstacle);
    
    
    /** 
     * Returns true if this edge is flipped with respect to its parent,
     * meaning its parent has 
     * a source item that is this edge target item's parent,
     * a target item that is this edge source item's parent.
     * 
     * This topological information is known after running the TubeLayout algorithm.
     */
    public boolean isFlipped();
    public void setFlipped(boolean flipped);
    public void toggleFlipped();
    
    public boolean isParentFlipped();
}
