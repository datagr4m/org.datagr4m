package org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes;

import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;


public interface IHierarchicalEdge extends IEdge{
    public void addChild(IEdge path);
    public boolean acceptChild(IEdge path);
    
    public List<IEdge> getChildren();
    public boolean hasChildren(IEdge child);
    public boolean hasDescendant(IEdge child);
    
    public void setHierarchyState(ItemState state);
    
    /** 
     * Insert a point on the path of a tube, between two points existing in this path.
     * @throws an Exception if start or stop point do not appear in the path.
     */
    public void insertPointBetween(Point2D point, Point2D start, Point2D stop) throws Exception;

    /** 
     * Insert a point on the path of a tube at the given index.
     * Will then hierarchically update all tube children so that any subtube or subedge
     * has a path through this point.
     * @throws an Exception if start or stop point do not appear in the path.
     */
    public void insertPointHierarchicallyAfter(int pathPointId, Point2D point) throws Exception;
}
