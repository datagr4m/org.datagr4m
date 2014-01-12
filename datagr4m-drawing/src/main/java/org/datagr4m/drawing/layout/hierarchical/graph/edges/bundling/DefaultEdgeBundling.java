package org.datagr4m.drawing.layout.hierarchical.graph.edges.bundling;

import java.awt.geom.Point2D;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.groupslot.IGroupSlotProcessor;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.groupslot.StratumGroupSlotProcessor;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.maths.geometry.Pt;
import org.jzy3d.maths.Coord2d;


public class DefaultEdgeBundling implements IEdgeBundling{
    private static final long serialVersionUID = 3105669453774149402L;
    protected static boolean DEFAULT_PATH_LOCK = false;
    protected static boolean DEBUG_TUBE_VALIDATION = false;

    protected IGroupSlotProcessor groupSlotProcessor = new StratumGroupSlotProcessor();
    //protected IGroupSlotProcessor groupSlotProcessor = new DefaultGroupSlotProcessor();
    //protected IGroupSlotProcessor groupSlotProcessor =  new OrthogonalEdgeGroupSlotProcessor();
    
    public DefaultEdgeBundling(){
        //noTubes = new ArrayList<CommutativePair<String>>();
    }

	@Override
	public void bundle(IHierarchicalEdgeModel model){
	    //synchronized(model){
	        doBundle(model);
	    //}
    }

    public void doBundle(IHierarchicalEdgeModel model) {
        groupSlotProcessor.clear();
	    
        for(Tube tube: model.getRootTubes()){
            clear(tube);
            build(tube);  
        }
        
        for(IEdge edge: model.getInternalTubesAndEdges()){
            if(edge instanceof Tube){
                Tube tube = (Tube)edge;
                clear(tube);
                build(tube);
            }
            else{
                clear(edge);
                build(edge);
            }
        }
        
        // TODO: supprimer validation
        //System.err.println("root tube validation");
        if(DEBUG_TUBE_VALIDATION){
            for(Tube tube: model.getRootTubes())
                if(!validate(tube)){
                    Logger.getLogger(this.getClass()).error(" invalid tube:" + tube);
                }
            for(IEdge edge: model.getInternalTubesAndEdges())
                if(!validate(edge)){
                    Logger.getLogger(this.getClass()).error(" invalid edge:" + edge);
                }

        }
        /*System.err.println("internal tube validation");
        for(IEdge edge: model.getInternalTubesAndEdges())
            makeValidation(edge);*/

        //rebuildSlots(model);
    }

	/*********** EDGE **************/
    
    @Override
    public void clear(IEdge path){
        path.getPathGeometry().clear();
    }

    // a raw direct edge
    @Override
    public void build(IEdge path){
        Point2D src = Pt.cloneAsDoublePoint(path.getSourceItem().getAbsolutePosition());
        Point2D trg = Pt.cloneAsDoublePoint(path.getTargetItem().getAbsolutePosition());
        path.getPathGeometry().add(src, DEFAULT_PATH_LOCK);
        path.getPathGeometry().add(trg, DEFAULT_PATH_LOCK);
    }
    
    /*********** TUBE **************/
    
    @Override
    public void clear(IHierarchicalEdge tube){
        tube.getPathGeometry().clear();
        for(IEdge e: tube.getChildren()){
            if(e instanceof Tube)
                clear((Tube)e);
            else
                clear(e);
        }
    }
    
    @Override
    public void build(IHierarchicalEdge tube){
        synchronized(tube.getPathGeometry().getPoints()){
            IBoundedItem source = tube.getSourceItem();
            IBoundedItem target = tube.getTargetItem();
            
            // TODO: optimize?
            // TODO: warning absolute position calcul�e deux fois pour chacun!
            Coord2d sourceSlotCoord = groupSlotProcessor.computeBorderCoord(source, target.getAbsolutePosition());
            Coord2d targetSlotCoord = groupSlotProcessor.computeBorderCoord(target, source.getAbsolutePosition());

            tube.setFlipped(false);
            
            if(sourceSlotCoord!=null && targetSlotCoord != null)
                fixTube(tube, sourceSlotCoord, targetSlotCoord);
            else
                Logger.getLogger(this.getClass()).error("tube data null?!");
        }
    }

    /** Do nothing if source or target coords are null*/
    @Override
    public void fixTube(IHierarchicalEdge tube, Coord2d source, Coord2d target){
        if(source==null || target==null)
            return;
        
        /*if(tube.getSourceItem().intersects(tube.getTargetItem()))
            return;*/
        
        tube.getPathGeometry().insertAt(0, Pt.cloneAsDoublePoint(source), true);
        tube.getPathGeometry().add(Pt.cloneAsDoublePoint(target), true);
        
        //if(isForbidden(tube.getSourceItem().getLabel(), tube.getTargetItem().getLabel()))
        //    return;
        
        
        for(IEdge child: tube.getChildren()){
            if(child instanceof Tube){
                Tube childTube = (Tube)child;
                
                boolean shouldFlip = shouldFlip(tube, childTube);
                boolean didPapFlip = childTube.isParentFlipped();//tube.getParent()==null?false:didFlip(tube.getParent());                
                boolean reallyFlip = (shouldFlip != didPapFlip);

                IBoundedItem childSource = childTube.getSourceItem();
                IBoundedItem childTarget = childTube.getTargetItem();
                Coord2d p1b = groupSlotProcessor.computeBorderCoord(childSource, source);
                Coord2d p2b = groupSlotProcessor.computeBorderCoord(childTarget, target);
                IPath p;
                if(!reallyFlip){
                    p = tube.getPathGeometry();
                    
                    synchronized(childTube.getPathGeometry().getPoints()){
                        childTube.getPathGeometry().add(p);
                    }
                    
                    childTube.setFlipped(false);
                    fixTube(childTube, p1b, p2b);
                }
                else{
                    p = (IPath)tube.getPathGeometry().clone(); 
                    
                    synchronized(childTube.getPathGeometry().getPoints()){
                        childTube.getPathGeometry().add(p);
                    }
                    
                    childTube.setFlipped(true);
                    fixTube(childTube, p2b, p1b);
                }
            }
            else{
                fixEdge(tube, child);
            }
        }
    }
    
    protected void fixEdge(IHierarchicalEdge parent, IEdge edge){
        //if(shouldBreak(edge))
        //    System.out.println("break");
        
        Point2D src = Pt.cloneAsDoublePoint(edge.getSourceItem().getAbsolutePosition());
        Point2D trg = Pt.cloneAsDoublePoint(edge.getTargetItem().getAbsolutePosition());
        
        // must flip if papa did not flip and current should flip according to papa
        // OR if papa did flip, and current should not flip
        boolean shouldFlip = shouldFlip(parent,edge);
        boolean reallyFlip = (shouldFlip != edge.isParentFlipped());
        
        if(!reallyFlip){
            edge.setFlipped(false);
            
            edge.getPathGeometry().insertAt(0, src, DEFAULT_PATH_LOCK);
            edge.getPathGeometry().add(parent.getPathGeometry());
            edge.getPathGeometry().add(trg, DEFAULT_PATH_LOCK);
        }
        else{
            edge.setFlipped(true);

            IPath parentPath = (IPath)parent.getPathGeometry().clone();
            parentPath.reverse();
            edge.getPathGeometry().add(src, DEFAULT_PATH_LOCK);
            edge.getPathGeometry().add(parentPath);
            edge.getPathGeometry().add(trg, DEFAULT_PATH_LOCK);
        }
    }

    /** la source doit �tre plus proche du point de d�part du chemin que la cible!!
     * Retourne vrai tout le temps si le chemin a moins de deux points 
     */
    protected boolean validate(IEdge edge){
        if(edge.getPathGeometry().getPointNumber()<2)
            return true;
        Coord2d pathStart = Pt.cloneAsCoord2d(edge.getPathGeometry().getPoint(0));
        Coord2d pathStop = Pt.cloneAsCoord2d(edge.getPathGeometry().getLastPoint());
        double d1 = edge.getSourceItem().getAbsolutePosition().distance(pathStart);
        double d2 = edge.getTargetItem().getAbsolutePosition().distance(pathStart);
        double d3 = edge.getSourceItem().getAbsolutePosition().distance(pathStop);
        double d4 = edge.getTargetItem().getAbsolutePosition().distance(pathStop);
        
        if(d2<d1 && d3<d4){
            //System.err.println("dist d2=" + d2 + " d1=" + d1);
            //System.err.println("dist d3=" + d2 + " d4=" + d1);
            return false;
        }
        else
            return true;
        
    }
    
    /******** FLIP, ENSURE EDGE CORRECT LAYOUT ********/
    
    protected boolean shouldFlip(IHierarchicalEdge tube, IEdge child){
        if(isSameOrDescendant(tube.getSourceItem(), child.getSourceItem())
        && isSameOrDescendant(tube.getTargetItem(), child.getTargetItem()))
            return false;
        return true;
    }
    
    protected boolean isSameOrDescendant(IBoundedItem model, IBoundedItem child){
        if(model==child)
            return true;
        else if(model instanceof IHierarchicalNodeModel){
            if(((IHierarchicalNodeModel)model).hasDescendant(child))            
                return true;
            else
                return false;
        }
        else
            return false;
    }
    
    /********* TUBE SLOT PLACEMENT **********/
    
    protected void makeValidation(IEdge edge){
        if(!validate(edge)){
            System.err.println(this.getClass().getSimpleName()+" invalid edge, toggling its flipped statuts");
            //edge.toggleFlipped(); 
            // c'est tr�s �trange, les arcs invers� se produisent quand on enl�ve le toggle
            // alors que �a n'a pas d'impact sur la fonction validate
        }

        //boolean isFlipped = tube.isFlipped();
        if(edge instanceof Tube){
            Tube tube = (Tube)edge;
            for(IEdge e: tube.getChildren()){
                makeValidation(e);
            }
        }
    }
}
