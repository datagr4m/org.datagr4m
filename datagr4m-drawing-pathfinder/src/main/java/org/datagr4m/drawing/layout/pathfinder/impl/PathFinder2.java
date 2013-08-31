package org.datagr4m.drawing.layout.pathfinder.impl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.pathfinder.IPathFinder;
import org.datagr4m.drawing.layout.pathfinder.PathFinderException;
import org.datagr4m.drawing.layout.pathfinder.impl.PathHit.HitType;
import org.datagr4m.drawing.layout.pathfinder.view.debugger.PathFinderDebugger;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleUtils;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;
import org.datagr4m.maths.geometry.PointUtils;
import org.jzy3d.maths.Coord2d;


public class PathFinder2 extends AbstractPathFinder implements IPathFinder{
    protected static boolean ENLARGE_OBSTACLE_BOUNDS = true;
    
    protected static boolean LOCKED = true;
    protected static boolean UNLOCKED = false;
    
    protected PathFinderDebugger debugger;
    
    public PathFinder2(){
    }
    public PathFinder2(PathFinderDebugger debugger){
        this.debugger = debugger;
    }
    
    @Override
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to) throws PathFinderException{
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to, List<IPathObstacle> obstacles) throws PathFinderException{
        return find(from, to, obstacles, true);
    }
    
    @Override
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException{
        List<IPathObstacle> other = new ArrayList<IPathObstacle>(obstacles.size()-2);
        for(IPathObstacle o: obstacles){
            if(!(o==from) && !(o==to)){
                other.add(o);
            }
        }
        Point2D start = new Point2D.Double(from.getSlotableCenter().x, from.getSlotableCenter().y);
        Point2D stop = new Point2D.Double(to.getSlotableCenter().x, to.getSlotableCenter().y);
        
        IPath path = new LockablePath(start, stop, UNLOCKED, UNLOCKED); // a path with locked start/stop
        path = bypass(path, 0, 1, other, ignoreDebugger);
        
        if(debugger!=null){ // show final work
            debugger.show(path, obstacles);
            debugger.addInfo("done!");
        }
        
        if(path==null){
            //System.err.println("path is null for " + start + " " + stop);
            return null;
        }
        
        return path.getCoords();
    }
    
    /******* BYPASS METHODS ********/

    protected IPath bypass(IPath parent, int i1, int i2, List<IPathObstacle> obstacles, boolean ignoreDebugger)throws PathFinderException{
        return bypass(0, parent, i1, i2, obstacles, ignoreDebugger);
    }
    
    /**
     * Output can be null if no relevant path was found.
     */
    protected IPath bypass(int depth, IPath parent, int i1, int i2, List<IPathObstacle> obstacles, boolean ignoreDebugger)throws PathFinderException{
        if(debugger!=null)
            debugger.addInfo(depth, "call bypass");
        
        if(parent==null)
            return null;
        
        // can work if it is not the first point at a non first call
        if(depth>0){
            boolean canWorkWithFirst = true;  
            canWorkWithFirst = canWorkWithFirst && !parent.getLock(i1); 
            //canWorkWithFirst = canWorkWithFirst && !parent.isFirst(i1);
            
            boolean canWorkWithSecond = true; 
            canWorkWithSecond = canWorkWithSecond && !parent.getLock(i2);
            //canWorkWithSecond = canWorkWithSecond && !parent.isLast(i2);
            
            if(!canWorkWithFirst && !canWorkWithSecond){
                //if(debugger!=null)
                //    debugger.info("can't work " + i1 + " and " + i2);
                return null;
            }
        }
        
        // debugger update
        if(debugger!=null && !ignoreDebugger){
            debugger.highlight(PointUtils.list(parent.getPoint(i1), parent.getPoint(i2)));
            debugger.show(parent, obstacles);
            debugger.breakFlow();
        }
        
        // Actual work starts
        Line2D segment = parent.getSegment(i1, i2);
        PathHit hit = findFirstHittingObstacle(obstacles, segment, parent.getLock(i1), parent.getLock(i2));
        //IPathObstacle o = hit.getObstacle();
        
        // ----------------------------------
        // end of recursion: return the input segment
        // or a simple bend if no obstacle
        if(hit==null){
            if(canStraight(segment)){ // straight
                return null;
            }
            else{ // create a bend
                Point2D bend = bend2(segment);
                if(in(obstacles,bend)) // try the other side if required
                    bend = bend1(segment); // do not ensure it works
                return new LockablePath(parent.getPoint(i1), bend, parent.getPoint(i2), parent.getLock(i1), UNLOCKED, parent.getLock(i2));
            }
        }
        
        // ----------------------------------
        // otherwise, make other bypass and 
        // append them to the current path,
        else{
            IPathObstacle o = hit.getObstacle();
            
            if(debugger!=null && !ignoreDebugger){
                debugger.setCurrentObstacle(o);
            }
            
            List<Point2D> bypass  = null;
            
            boolean ENSURE_NOT_LOCKED = true;
            
            // --------------------------------------
            // straight cross: both point are outside
            if(hit.isBothOutside()){
                if(debugger!=null && !ignoreDebugger)
                    debugger.addInfo(depth, "both ouside");
                
                //bypass = bypassStraight2(o.getBypassedBounds(), segment);
                
                if(isAtEachOppositeSide(segment.getP1(), o.getBypassedBounds(), segment.getP2())){
                    bypass = bypassStraight2(o.getBypassedBounds(), segment.getP1(), segment.getP2());
                }
                else{
                    if(canStraight(segment.getP1(), segment.getP2())){ // straight
                        return null;
                    }
                    else{ // create a bend
                        Point2D bend = bend2(segment.getP1(), segment.getP2());
                        if(in(obstacles,bend)) // try the other side if required
                            bend = bend1(segment.getP1(), segment.getP2()); // do not ensure it works
                        return new LockablePath(segment.getP1(), bend, parent.getPoint(i2), parent.getLock(i1), UNLOCKED, parent.getLock(i2));
                    }
                }
                
                if(bypass!=null){
                    IPath path = callRecursive(depth, parent, i1, i2, bypass, obstacles, ignoreDebugger);
                    if(ENLARGE_OBSTACLE_BOUNDS)
                        o.addBypass();
                    return path;
                }
                else
                    error(o, parent.getPoint(i1), parent.getPoint(i2));
            }
            
            // --------------------------------------
            // half cross, point1 is inside
            else if(hit.isP1Inside()){
                if(debugger!=null && !ignoreDebugger)
                    debugger.addInfo(depth, "p1 inside");
                
                int prevId = findPrevFreePoint(parent, i1, o, ENSURE_NOT_LOCKED);
                if(prevId==NO_FREE_NEIGHBOUR)
                    return null;
                Point2D prev = parent.getPoint(prevId);
                
                if(debugger!=null && !ignoreDebugger){
                    debugger.highlight(PointUtils.list(prev, segment.getP2()));
                    debugger.breakFlow();
                }
                
                int dist = i2-prevId;
                if(dist>1)
                    System.out.println("isP1Inside.dist=" + dist);
                
                // check if we change anything by clibbing the hill
                PathHit newHit = computeHit(o, prev, segment.getP2(), parent.getLock(prevId), parent.getLock(i2));
                if(newHit == null){
                    if(canStraight(prev, segment.getP2())){ // straight
                        return null;
                    }
                    else{ // create a bend
                        Point2D bend = bend2(prev, segment.getP2());
                        if(in(obstacles,bend)) // try the other side if required
                            bend = bend1(prev, segment.getP2()); // do not ensure it works
                        return new LockablePath(prev, bend, parent.getPoint(i2), parent.getLock(prevId), UNLOCKED, parent.getLock(i2));
                    }
                }
                
                
                if(isAtEachOppositeSide(prev, o.getBypassedBounds(), segment.getP2())){
                    bypass = bypassStraight2(o.getBypassedBounds(), prev, segment.getP2());
                }
                else{
                    bypass = bypassBend(o.getBypassedBounds(), prev, segment.getP1(), segment.getP2());
                }
                // handle this path change
                if(bypass!=null){
                    IPath path = callRecursive(depth, parent, prevId, i2, bypass, obstacles, ignoreDebugger);
                    if(ENLARGE_OBSTACLE_BOUNDS)
                        o.addBypass();
                    return path;
                }
                else
                    error(o, prev, segment.getP1(), segment.getP2());
            }
            
            // --------------------------------------
            // half cross, point2 is inside
            else if(hit.isP2Inside()){
                if(debugger!=null && !ignoreDebugger)
                    debugger.addInfo(depth, "p2 inside");
                
                int nextId = findNextFreePoint(parent, i2, o, ENSURE_NOT_LOCKED);
                if(nextId==NO_FREE_NEIGHBOUR)
                    return null;
                Point2D next = parent.getPoint(nextId);
                
                int dist = nextId-i1;
                if(dist>1)
                    System.out.println("isP2Inside.dist=" + dist);
                
                if(debugger!=null && !ignoreDebugger){
                    debugger.highlight(PointUtils.list(segment.getP1(), next));
                    debugger.breakFlow();
                }
                
             // check if we change anything by clibbing the hill
                PathHit newHit = computeHit(o, segment.getP1(), next, parent.getLock(i1), parent.getLock(nextId));
                if(newHit == null){
                    if(canStraight(segment.getP1(), next)){ // straight
                        return null;
                    }
                    else{ // create a bend
                        Point2D bend = bend2(segment.getP1(), next);
                        if(in(obstacles,bend)) // try the other side if required
                            bend = bend1(segment.getP1(), next); // do not ensure it works
                        return new LockablePath(segment.getP1(), bend, next, parent.getLock(i1), UNLOCKED, parent.getLock(nextId));
                    }
                }
                
                if(isAtEachOppositeSide(segment.getP1(), o.getBypassedBounds(), next))
                    bypass = bypassStraight2(o.getBypassedBounds(), segment.getP1(), next);
                else
                    bypass = bypassBend(o.getBypassedBounds(), segment.getP1(), segment.getP2(), next);
                
                // handle this path change
                if(bypass!=null){
                    IPath path = callRecursive(depth, parent, i1, nextId, bypass, obstacles, ignoreDebugger);
                    if(ENLARGE_OBSTACLE_BOUNDS)
                        o.addBypass();
                    return path;
                }
                else
                    error(o, segment.getP1(), segment.getP2(), next);
            }
            
            // --------------------------------------
            // both points inside, search for the first next and prev
            // points standing outside to create a bypass from them
            // and remove all these nested segments from parent path
            else if(hit.isBothInside()){
                if(debugger!=null && !ignoreDebugger)
                    debugger.addInfo(depth, "both inside");
                
                // search a prev point that do not hit obstacle, and return if can't find one
                int prevId = findPrevFreePoint(parent, i1, o, false);
                if(prevId==NO_FREE_NEIGHBOUR)
                    return null;
                Point2D prev = parent.getPoint(prevId);
                
                // search a next point that do not hit obstacle, and return if can't find one
                int nextId = findNextFreePoint(parent, i2, o, false);
                if(nextId==NO_FREE_NEIGHBOUR)
                    return null;
                Point2D next = parent.getPoint(nextId);
                
                int dist = nextId-prevId;
                if(dist>1)
                System.out.println("isBothInside.dist=" + dist);
                                
                bypass = bypassStraight2(o.getBypassedBounds(), prev, next);
                
                if(bypass!=null){
                    IPath path = callRecursive(depth, parent, prevId, nextId, bypass, obstacles, ignoreDebugger);
                    if(ENLARGE_OBSTACLE_BOUNDS)
                        o.addBypass();
                    return path;
                }
                else
                    error(o, parent.getPoint(i1), parent.getPoint(i2));
            }
            
            // --------------------------------------
            // otherwise it's an error
            else
                throw new RuntimeException("illegal state: both points stand IN the obstacle " + o.getName());
        }
        return null; // should not reach this point
    }
    
    /**
     * Here we will:
     * 1) define lock of the created segments.
     * 2) recursively call the bypass method for each segment that were 
     *    created previously
     */
    protected IPath callRecursive(int depth, IPath parent, int p1, int p2, List<Point2D> bypass, List<IPathObstacle> obstacles, boolean ignoreDebugger)throws PathFinderException{
        List<Boolean> locks = whoShouldLock(bypass, obstacles);
        
        // merging bypass result into parent path to create a new path
        IPath path = new LockablePath(parent.getPoint(p1), bypass, parent.getPoint(p2), parent.getLock(p1), locks, parent.getLock(p2));
        
        // now work on each path segment
        for (int i = 0; i < path.getPointNumber()-1; i++) {
            // dive in recursion for each segment
            IPath sp = bypass(depth+1, path, i, i+1, obstacles, ignoreDebugger);
            
            // if subpath worked, replace the segment by its subpath
            if(sp!=null && sp.getPointNumber()>0){
                path.replace(i, i+1, sp);
            }
        }
        return path;
    }
    
    /*protected Path callRecursive(int depth, Path parent, int p1, int p2, List<Point2D> bypass, List<IPathObstacle> obstacles, boolean ignoreDebugger){
        List<Boolean> locks = whoShouldLock(bypass, obstacles);
        
        // merging bypass result into parent path to create a new path
        Path path = new Path(bypass, locks);
        
        if(p2-p1==1)
            parent.insertAt(p2, path);
        else
            parent.replace(p1+1, p2-1, path);
        
        // now work on each path segment
        for (int i = 0; i < parent.getPointNumber()-1; i++) {
            // dive in recursion for each segment
            Path sp = bypass(depth+1, parent, i, i+1, obstacles, ignoreDebugger);
        }
        return parent;
    }*/
    
    
    protected List<Boolean> whoShouldLock(Line2D segment, List<IPathObstacle> obstacles){
        return whoShouldLock(PointUtils.list(segment.getP1(), segment.getP2()), obstacles);
    }
    
    protected List<Boolean> whoShouldLock(List<Point2D> bypass, List<IPathObstacle> obstacles){
        List<Boolean> locks = new ArrayList<Boolean>(bypass.size());
        
        for(Point2D p: bypass){
            boolean lock = ! hitAny(p, obstacles);
            locks.add(lock);
        }
        return locks;
    }
    
    /*******************/
    
    protected static int NO_FREE_NEIGHBOUR = -1;
    
    protected int findNextFreePoint(IPath parent, int i2, IPathObstacle o, boolean ensureNotLocked){
        return findNextFreePoint(parent, i2, ObstacleUtils.list(o), ensureNotLocked);
    }
    
    protected int findNextFreePoint(IPath parent, int i2, List<IPathObstacle> o, boolean ensureNotLocked){
        if(parent.isLast(i2))
            return NO_FREE_NEIGHBOUR;
        
        int nextId = i2+1;
        Point2D next = null;
        
        while(true){
            next = parent.getPoint(nextId);
            
            if(ensureNotLocked){
                if(!parent.getLock(nextId) && !hit(o, next))
                    return nextId;
            }
            else{
                if(!hit(o, next))
                    return nextId;
            }
            
            nextId++;
            if(nextId>=(parent.getPointNumber()-1)) 
                return NO_FREE_NEIGHBOUR; // impossible to find: exit
        }
    }
    
    protected int findPrevFreePoint(IPath parent, int i1, IPathObstacle o, boolean ensureNotLocked){
        return findPrevFreePoint(parent, i1, ObstacleUtils.list(o), ensureNotLocked);
    }
    
    protected int findPrevFreePoint(IPath parent, int i1, List<IPathObstacle> o, boolean ensureNotLocked){
        if(parent.isFirst(i1))
            return NO_FREE_NEIGHBOUR;
        
        int prevId = i1-1;
        Point2D prev = null;
        while(true){
            prev = parent.getPoint(prevId);
            
            if(ensureNotLocked){
                if(!parent.getLock(prevId) && !hit(o, prev))
                    return prevId;
            }
            else{
                if(!hit(o, prev))
                    return prevId;
            }
            
            prevId--;
            if(prevId<0) 
                return NO_FREE_NEIGHBOUR; // impossible to find: exit
        }
    }
    
    /********************/
    
    protected PathHit findFirstHittingObstacle(List<IPathObstacle> obstacles, Line2D segment, boolean lock1, boolean lock2){
        for(IPathObstacle o: obstacles){
            PathHit hit = computeHit(o, segment, lock1, lock2);
            if(hit!=null)
                return hit;
        }
        return null;
    }
    
    protected PathHit computeHit(IPathObstacle obstacle, Line2D segment, boolean lock1, boolean lock2){
        return computeHit(obstacle, segment.getP1(), segment.getP2(), lock1, lock2);
    }
    
    protected PathHit computeHit(IPathObstacle obstacle, Point2D p1, Point2D p2, boolean lock1, boolean lock2){
        if(hit(obstacle, p1, p2)){
            boolean hp1 = hit(obstacle, p1);
            boolean hp2 = hit(obstacle, p2);
            boolean isBothOutside = !hp1 && !hp2;
            boolean isBothInside  =  hp1 &&  hp2;
            boolean isP1Inside    =  hp1 && !hp2;
            boolean isP2Inside    = !hp1 &&  hp2;
            
            if(isBothOutside){
                return new PathHit(obstacle, HitType.BOTH_OUTSIDE);
            }
            else if(isBothInside){
                if(lock1 || lock2)
                    return null;
                return new PathHit(obstacle, HitType.BOTH_INSIDE);
            }
            else if(isP1Inside){
                if(lock1 /*|| lock2*/)
                    return null;
                return new PathHit(obstacle, HitType.P1_INSIDE);
            }
            else if(isP2Inside){
                if(/*lock1 ||*/ lock2)
                    return null;
                return new PathHit(obstacle, HitType.P2_INSIDE);
            }
            else
                return new PathHit(obstacle, HitType.UNDEFINED);
        }
        else 
            return null;
    }
    
    /*protected boolean hitAny(Path path, List<IPathObstacle> obstacles){
        int s = path.getSegmentNumber();
        for (int i = 0; i < s; i++) {
            Line2D line = path.getSegment(i);
            PathHit first = findFirstHittingObstacle(line, obstacles);
            if(first!=null)
                return true;
        }
        return false;
    }*/
    
    /*protected boolean hitAny(Point2D p, List<IPathObstacle> obstacles){
        for(IPathObstacle o: obstacles)
            if(hit(o, p))
                return true;
        return false;
    }*/
    
    /********************/

    protected void error(IPathObstacle o, Point2D p1, Point2D p2){
        if(debugger!=null)
            debugger.highlight(PointUtils.list(p1, p2));
        
        throw new RuntimeException("illegal state: bypass is null IN the obstacle "
                + "\n" + o
                 + " xrange:"+o.getBypassedBounds().getMinX()+":"+o.getBypassedBounds().getMaxX()
                 + " yrange:"+o.getBypassedBounds().getMinY()+":"+o.getBypassedBounds().getMaxY()
                + "\np1:" + p1
                + "\np2:" + p2); 
    }
    
    protected void error(IPathObstacle o, Point2D p1, Point2D p2, Point2D p3){
        if(debugger!=null)
            debugger.error(PointUtils.list(p1, p2, p3));
        
        throw new RuntimeException("illegal state: bypass is null IN the obstacle "
                + "\n" + o 
                  + " xrange:"+o.getBypassedBounds().getMinX()+":"+o.getBypassedBounds().getMaxX()
                  + " yrange:"+o.getBypassedBounds().getMinY()+":"+o.getBypassedBounds().getMaxY()
                + "\np1:" + p1
                + "\np2:" + p2
                + "\np3:" + p3); 
    }
    @Override
    public void find(IPathObstacle from, IPathObstacle to, IPath path, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException {
        throw new RuntimeException("not implemented");
    }
    
    /**************/
    
}
