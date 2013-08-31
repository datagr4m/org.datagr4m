package org.datagr4m.drawing.layout.hierarchical.graph.edges.bundling;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdge;
import org.datagr4m.drawing.model.pathfinder.path.IPath;


public class SlotedEdgeBundling extends DefaultEdgeBundling{
    private static final long serialVersionUID = 4290548358386472456L;

    public SlotedEdgeBundling(){
        super();
    }
    
    @Override
    public void clear(IEdge path){
        // do not erase tube path since we asked to slot initializer
        // to setup first and last point according to slot
        // affectation
    }

    @Override
    public void build(IEdge path){
        // do not rebuild edge for the same reason
    }
    
    /**
     * A special version that does consider a precomputed edge path,
     * and only insert parent tube path in the middle of two existing
     * point, that are the start and stop slot points.
     */
    @Override
    protected void fixEdge(IHierarchicalEdge parent, IEdge edge){
        // must flip if 
        //    (1) current should flip according to papa and (2) papa did not flip 
        // OR (1) current should not flip               and (2) papa did flip 
        //    (1) current should flip according to papa and (2) papa did not flip 
        // OR (1) current should not flip               and (2) papa did flip 
        boolean shouldFlip  = shouldFlip(parent,edge);
        boolean reallyFlip = (shouldFlip != edge.isParentFlipped());
        
        if(!reallyFlip){
            edge.setFlipped(false);
            
            if(edge.getPathGeometry().getPointNumber()<2)
                throw new RuntimeException("SlotedTubeLayout: An edge should have two points before being aligned with its parent tube. Here:"+edge.getPathGeometry());
            else{
                edge.getPathGeometry().insertAt(1, parent.getPathGeometry());
            }
        }
        else{
            edge.setFlipped(true);
            
            if(edge.getPathGeometry().getPointNumber()<2)
                throw new RuntimeException("SlotedTubeLayout: An edge should have two points before being aligned with its parent tube. Here:"+edge.getPathGeometry());
            else{
                IPath p = (IPath)parent.getPathGeometry().clone();
                p.reverse();
                edge.getPathGeometry().insertAt(1, p);
            }
            //logNoFlip(edge);
            //edge.getPathGeometry().reverse();
        }
        
        /*if(!validate(edge)){
            //edge.
            edge.getPathGeometry().reverse();
            System.err.println("a geometrical flip");
            if(!validate(edge))
                System.err.println("did not validate| reallyFlip= " + reallyFlip);
        }*/
    }
}
