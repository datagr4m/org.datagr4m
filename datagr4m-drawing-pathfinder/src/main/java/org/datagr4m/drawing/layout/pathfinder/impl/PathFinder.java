package org.datagr4m.drawing.layout.pathfinder.impl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.pathfinder.IPathFinder;
import org.datagr4m.drawing.layout.pathfinder.PathFinderException;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleUtils;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;
import org.jzy3d.maths.Coord2d;


public class PathFinder extends AbstractPathFinder implements IPathFinder{
    protected static boolean MERGE_OVERLAPPING_OBSTACLES = false;
    
    @Override
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to) {
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to, List<IPathObstacle> obstacles) {
        return find(from, to, obstacles, true);
    }

    @Override
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to, List<IPathObstacle> obstacles, boolean ignoreDebugger) {
        List<IPathObstacle> other = new ArrayList<IPathObstacle>(obstacles.size()-2);
        for(IPathObstacle o: obstacles){
            if(!(o==from) && !(o==to)){
                other.add(o);
            }
        }
        Point2D start = new Point2D.Double(from.getSlotableCenter().x, from.getSlotableCenter().y);
        Point2D stop = new Point2D.Double(to.getSlotableCenter().x, to.getSlotableCenter().y);
        IPath path = new LockablePath(start, stop, false, false);
        bypass(path, other);
        return path.getCoords();
    }
    
    /******* BYPASS METHODS ********/

    protected void bypass(IPath path, List<IPathObstacle> obstacles){
        bypass(path, obstacles, false);
    }    
    
    protected void bypass(IPath path, List<IPathObstacle> obstacles, boolean allowRecursion){
        
        int s = path.getSegmentNumber();
        
        for (int i = 0; i < s; i++){
            Line2D segment = path.getSegment(i);
            
            if(mustBend(segment)){
                Point2D bend = bend2(segment);
                if(!in(obstacles,bend))
                    path.insertBefore(segment.getP2(), bend, true);
                else{
                    bend = bend1(segment);
                    path.insertBefore(segment.getP2(), bend, false);
                }
            }
            
            // apply a merge of obstacle if their margin 
            // as expand so that they intersect
            if(MERGE_OVERLAPPING_OBSTACLES)
                obstacles = ObstacleUtils.merge(obstacles);
            
            for(IPathObstacle o: obstacles){
                if(hit(o, segment)){
                    boolean hp1 = hitP1(o, segment);
                    boolean hp2 = hitP2(o, segment);
                    
                    List<Point2D> bypass = null;
                    
                    // either straight cross: both point are outside
                    if(!hp1 && !hp2){
                        bypass = bypassStraight(o.getBypassedBounds(), segment);
                        
                        // handle this path change
                        if(bypass!=null){
                            path.insertBefore(segment.getP2(), bypass, false);
                            o.addBypass();
                            if(allowRecursion)
                                bypass(path, obstacles, allowRecursion);
                        }
                        else
                            throw new RuntimeException("illegal state: bypass is null IN the obstacle " + o.getName());
                    }
                    // or half cross, point2 is inside
                    else if(!hp1 && hp2){
                        // get next segment to compute bend bypass
                        if((i+1)<s){
                            Line2D segnext = path.getSegment(i+1);
                            bypass = bypassBend(o.getBypassedBounds(), segment.getP1(), segment.getP2(), segnext.getP2());
                        
                            // handle this path change
                            if(bypass!=null){
                                path.replace(segment.getP2(), bypass, false);
                                o.addBypass();
                                if(allowRecursion)
                                    bypass(path, obstacles, allowRecursion);
                            }
                            else
                                throw new RuntimeException("illegal state: bypass is null IN the obstacle " + o.getName());
                        }
                        else
                            throw new RuntimeException("illegal state: last points stand IN the obstacle " + o.getName());
                    }
                    // or half cross, point1 is inside
                    else if(hp1 && !hp2){
                        // get prev segment to compute bend bypass
                        if((i-1)>=0){
                            Line2D segprev = path.getSegment(i-1);
                            bypass = bypassBend(o.getBypassedBounds(), segprev.getP1(), segment.getP1(), segment.getP2());
                        
                            // handle this path change
                            if(bypass!=null){
                                path.replace(segment.getP1(), bypass, false);
                                o.addBypass();
                                if(allowRecursion)
                                    bypass(path, obstacles, allowRecursion);
                            }
                            else
                                throw new RuntimeException("illegal state: bypass is null IN the obstacle " + o.getName());
                        }
                        else
                            throw new RuntimeException("illegal state: first points stand IN the obstacle " + o.getName());
                    }
                    else
                        throw new RuntimeException("illegal state: both points stand IN the obstacle " + o.getName());
                }
            }
        }
    }

    @Override
    public void find(IPathObstacle from, IPathObstacle to, IPath path, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException {
        throw new RuntimeException("not implemented");
    }
}
