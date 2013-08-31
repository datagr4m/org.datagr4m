package org.datagr4m.drawing.layout.hierarchical.graph.edges.utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.EdgeSection;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.AbstractTubeVisitor;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;


/**
 * Returns edge section objects that indicate which part of which tube/edge stand
 * inside the query group
 * @author Martin
 *
 */
public class TubeSectionsVisitor extends AbstractTubeVisitor{
    public TubeSectionsVisitor(){
        tmpSections = new ArrayList<EdgeSection>();
    }
    
    public List<EdgeSection> getSectionsIn(List<Tube> edges, IHierarchicalModel group){
        tmpSections.clear();
        
        //select all
        for(IEdge t: edges){
            Point2D start = t.getPathGeometry().getFirstPoint();
            
            if(t instanceof Tube){
                Tube tube = (Tube)t;
                if(start!=null) 
                    exploreTubeHierarchyBefore(tube, start, true);
                
                Point2D stop = t.getPathGeometry().getLastPoint();
                if(stop!=null) 
                    exploreTubeHierarchyAfter(tube, stop, true);
            }
        }
        
        // select appropriate tubes 
        List<EdgeSection> selected = new ArrayList<EdgeSection>();
        for(EdgeSection s: tmpSections){
            IBoundedItem src = s.getEdge().getSourceItem();
            IBoundedItem trg = s.getEdge().getTargetItem();
            
            if(src.getParent()==group){
                selected.add(s);
            }
            else if(trg.getParent()==group){
                selected.add(s);
            }
        }
        return selected;
    }
    
    protected List<EdgeSection> tmpSections;

    @Override
    public void doTubeBefore(Tube tube, Point2D pt) {
        int start = 0;
        int stop = tube.getPathGeometry().getIndex(pt);
        
        if(stop!=-1 && (stop-start)>0){
            EdgeSection s = new EdgeSection(tube, start, stop);
            //System.out.println("found " + s);
            tmpSections.add(s);
        }
    }
    
    @Override
    public void doTubeAfter(Tube tube, Point2D pt) {
        int start = tube.getPathGeometry().getIndex(pt);
        int stop = tube.getPathGeometry().getPointNumber()-1;
        
        if(start!=-1 && (stop-start)>0){
            EdgeSection s = new EdgeSection(tube, start, stop);
            //System.out.println("found " + s);
            tmpSections.add(s);
        }
    }
    
    /************/
    
    @Override
    public void doEdgeBefore(IEdge edge, Point2D pt) {
        /*int start = 0;
        int stop = edge.getPathGeometry().getIndex(pt);
        
        if(stop!=-1 && (stop-start)>0){
            EdgeSection s = new EdgeSection(edge, start, stop);
            tmpSections.add(s);
        }*/
    }
    
    @Override
    public void doEdgeAfter(IEdge edge, Point2D pt) {
        /*int start = edge.getPathGeometry().getIndex(pt);
        int stop = edge.getPathGeometry().getPointNumber()-1;
        
        if(start!=-1 && (stop-start)>0){
            EdgeSection s = new EdgeSection(edge, start, stop);
            tmpSections.add(s);
        }*/
    }
    
    /************/
    
    @Override
    public void doTubeBetween(Tube tube, Point2D from, Point2D to) {
    }
    @Override
    public void doEdgeBetween(IEdge edge, Point2D from, Point2D to) {
    }
}
