package org.datagr4m.drawing.layout.hierarchical.graph.edges.post;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.pathfinder.PathFinderException;
import org.datagr4m.drawing.layout.pathfinder.impl.PathFinder3;
import org.datagr4m.drawing.layout.pathfinder.view.debugger.PathFinderDebugger;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.BoundsType;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.ModelGeometryProcessor;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.TubeUtils;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.maths.geometry.ShapeUtils;


public class StratumEdgePostProcess implements IEdgePostProcessor {
    @Override
    public void postProcess(IHierarchicalEdgeModel model){
        for(Tube tube: model.getRootTubes()){
            bendNonOrthogonalPathRecursively(tube);  
            handleRootTubeCrossing(model, tube, 2);
        }
        
        for(IEdge edge: model.getInternalTubesAndEdges()){
            if(edge instanceof IHierarchicalEdge){
                IHierarchicalEdge tube = (IHierarchicalEdge)edge;
                bendNonOrthogonalPathRecursively(tube);
                handleRootTubeCrossing(model, tube, 2);
            }
        }
    }
    
    /*********** BEND TO ORTHOGONAL ***********/

    @Override
    public void bendNonOrthogonalPathRecursively(IHierarchicalEdge edge){
        int start = 0;
        
        bendNonOrthogonalPath(edge, start);
        
        // go recursive
        for(IEdge child: edge.getChildren()){
            if(child instanceof IHierarchicalEdge){
                IHierarchicalEdge tube = (IHierarchicalEdge)child;
                bendNonOrthogonalPathRecursively(tube);
            }
        }
        
        //System.out.println("post process " + ninsert);
    }
    
	protected void bendNonOrthogonalPath(IHierarchicalEdge edge, int start) {
		int ninsert = 0;
        while(start!=-1){
            start = insertBendPoints(edge, start);
            if(start!=-1)
                ninsert++;
        }
	}

    /** Insert bend points on the path to ensure edge are orthogonal. */
    protected int insertBendPoints(IHierarchicalEdge edge, int startId) {
        IPath p = edge.getPathGeometry();
        
        if(startId < p.getPointNumber()){
            Point2D prev = p.getPoint(startId);
            Point2D curr = null;
            
            for (int i = startId+1; i < p.getPointNumber(); i++) {
                curr = p.getPoint(i);
                
                if(!isOrthogonalSegment(prev, curr)){
                	if(!edge.isFlipped()){
	                    Point2D bend = createBendPoint1(prev, curr);
	                    try {
                            edge.insertPointBetween(bend, prev, curr);
                            return i;
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass()).error(e);
                            return -1;
                        }
	                    
                	}
                	else{
                		Point2D bend = createBendPoint2(prev, curr);
	                    try {
                            edge.insertPointBetween(bend, prev, curr);
                            return i;
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass()).error(e);
                            return -1;
                        }
                	}
                }
                prev = curr;
            }
        }
        return -1;
    }
    
    protected boolean isOrthogonalSegment(Point2D p1, Point2D p2){
        if(p1.getX()==p2.getX())
            return true;
        else if(p1.getY()==p2.getY())
            return true;
        return false;
    }
    
    protected Point2D createBendPoint1(Point2D p1, Point2D p2){
        return new Point2D.Double(p1.getX(), p2.getY());
    }
    protected Point2D createBendPoint2(Point2D p1, Point2D p2){
        return new Point2D.Double(p2.getX(), p1.getY());
    }
    
    /*********** CHECK FOR ROOT TUBES CROSSING ***********/
    
    protected void handleRootTubeCrossing(IHierarchicalEdgeModel model, IHierarchicalEdge edge, int maxDepth){
    	IBoundedItem i1 = edge.getSourceItem();
    	IBoundedItem i2 = edge.getTargetItem();
    	
    	IHierarchicalModel m1 = i1.getParent();
    	IHierarchicalModel m2 = i2.getParent();
    	
    	if(m1==m2){
    		if(m1.getDepth()<maxDepth){
	    		List<IBoundedItem> children = new ArrayList<IBoundedItem>(m1.getChildren());
	    		children.remove(i1);
	    		children.remove(i2);
	    		
	    		//System.out.println("checking " + children);
	    		
	    		boolean did = crossPath(edge, children);
	    		
	    		if(did){
	    			handlePath(model, edge, children);
	    		}
	    		
	    		//System.out.println("no cross :)");
    		}
    	}
    	else{
    		throw new RuntimeException("parent differ!!");
    	}
    }

	protected boolean crossPath(IHierarchicalEdge edge, List<IBoundedItem> obstacles) {
		boolean didCross = false;
		
		IPath p = edge.getPathGeometry();
		
		for (int i = 0; i < p.getSegmentNumber(); i++) {
			Line2D line = p.getSegment(i);
			
			for(IBoundedItem c: obstacles){
				RectangleBounds ext = ModelGeometryProcessor.shiftToAbsolute(c.getRawExternalRectangleBounds().clone(), c.getAbsolutePosition());
				Point2D pt = ShapeUtils.getFirstIntersection(ext.cloneAsRectangle2D(), line);
				if(pt!=null){
					//System.err.println("intersect " + c.getLabel());
					didCross = true;
				}
			}
		}
		return didCross;
	}
	
	protected void handlePath(IHierarchicalEdgeModel model, IHierarchicalEdge edge, List<IBoundedItem> obstacles){
		PathFinderDebugger debugger = null;//new PathFinderDebugger();
		
		PathFinder3 finder = new PathFinder3(debugger);
		
		IPathObstacle source = edge.getSourceObstacle();
    	IPathObstacle target = edge.getTargetObstacle();
    	IPath p = edge.getPathGeometry();
    	
    	//p.
    	
    	List<IPathObstacle> pathObstacles = TubeUtils.buildObstacles(obstacles, BoundsType.CORRIDOR);
        
        try {
        	//System.out.println("obstcl:" + pathObstacles);
        	//System.out.println("before:" + p);
            finder.find(source, target, p, pathObstacles, true);
        	//System.out.println("after :" + p);
            //System.out.println("OK    :" + source + " to " + target);
        } 
        catch (PathFinderException e) {
            e.printStackTrace();
            System.err.println("For " + source + " to " + target);
        }
	}
	
}
