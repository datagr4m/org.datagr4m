package org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;


/**
 * Able to browse a tube hierarchy and to extract three main parts:
 * - the source side of the root tube, and all children tube path section that stand "before" the source side point
 * - the target side of the root tube, and all children tube path section that stand "after" the target side point
 * - the central part of the root tube, and all children tube path section that stand "between" the two points.
 *
 *                                   |
 *                                   *---
 *             -----------------    /
 *                 ROOT         ---/     
 *                 TUBE         ---\
 *             -----------------    \
 *                                   *---
 *                                   |
 *                                   |  
 * 
 */
public abstract class AbstractTubeVisitor {
    public void exploreTubeHierarchy(Tube tube, boolean exploreSourceSide, boolean exploreTargetSide, boolean exploreCenter) {
        if(exploreSourceSide){
            Point2D point = tube.getPathGeometry().getFirstPoint(); // inside group, outside tube
            if(point!=null) // may have not been computed yet
                exploreTubeHierarchyBefore(tube, point);
        }
        if(exploreCenter){
            Point2D first = tube.getPathGeometry().getFirstPoint(); // inside group, outside tube
            Point2D last  = tube.getPathGeometry().getLastPoint(); // inside group, outside tube
            exploreTubeHierarchyBetween(tube, first, last);
        }
        if(exploreTargetSide){
            Point2D point = tube.getPathGeometry().getLastPoint(); // inside group, outside tube
            if(point!=null) // may have not been computed yet
                exploreTubeHierarchyAfter(tube, point); // this tube should not appear
        }
    }
    
    public void exploreTubeHierarchyBefore(Tube tube, Point2D pt) {
        exploreTubeHierarchyBefore(tube, pt, true);
    }
    
    public void exploreTubeHierarchyBefore(Tube tube, Point2D pt, boolean goHierarchy) {
        doTubeBefore(tube, pt);
        
        if(goHierarchy)
            for (IEdge child : tube.getChildren()) {
                if(child.isFlipped()){
                    if (child instanceof Tube)
                        exploreTubeHierarchyAfter((Tube) child, pt, goHierarchy);
                    else
                        doEdgeAfter(child, pt);
                }
                else{
                    if (child instanceof Tube)
                        exploreTubeHierarchyBefore((Tube) child, pt, goHierarchy);
                    else
                        doEdgeBefore(child, pt);
                }
            }
    }
    
    public void exploreTubeHierarchyAfter(Tube tube, Point2D pt) {
        exploreTubeHierarchyAfter(tube, pt, true);
    }
    
    public void exploreTubeHierarchyAfter(Tube tube, Point2D pt, boolean goHierarchy) {
        doTubeAfter(tube, pt);

        if(goHierarchy)
            for (IEdge child : tube.getChildren()) {
                if(child.isFlipped()){
                    if(child instanceof Tube)
                        exploreTubeHierarchyBefore((Tube)child, pt, goHierarchy);
                    else
                        doEdgeBefore(child, pt);
                }
                else{
                    if(child instanceof Tube)
                        exploreTubeHierarchyAfter((Tube)child, pt, goHierarchy);
                    else
                        doEdgeAfter(child, pt);
                }
            }
    }
    
    public void exploreTubeHierarchyBetween(Tube tube, Point2D from, Point2D to){
        
    }
    
    public abstract void doTubeBefore(Tube tube, Point2D pt);
    public abstract void doTubeAfter(Tube tube, Point2D pt);
    public abstract void doTubeBetween(Tube tube, Point2D from, Point2D to);

    public abstract void doEdgeBefore(IEdge edge, Point2D pt);
    public abstract void doEdgeAfter(IEdge edge, Point2D pt);
    public abstract void doEdgeBetween(IEdge edge, Point2D from, Point2D to);
}
