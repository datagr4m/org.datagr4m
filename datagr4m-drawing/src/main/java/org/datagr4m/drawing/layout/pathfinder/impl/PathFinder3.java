package org.datagr4m.drawing.layout.pathfinder.impl;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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


public class PathFinder3 extends AbstractPathFinder implements IPathFinder{
    private static final long serialVersionUID = 2001063492262118023L;
    
    protected static boolean LOCKED = true;
    protected static boolean UNLOCKED = false;
    
    protected PathFinderDebugger debugger;
    
    public PathFinder3(){
    }
    public PathFinder3(PathFinderDebugger debugger){
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
        Point2D start = new Point2D.Double(from.getSlotableCenter().x, from.getSlotableCenter().y);
        Point2D stop = new Point2D.Double(to.getSlotableCenter().x, to.getSlotableCenter().y);
        
        IPath path = new LockablePath(start, stop, UNLOCKED, UNLOCKED); // a path with locked start/stop
        find(from, to, path, obstacles, ignoreDebugger);
        
        return path.getCoords();
    }
    
    @Override
    public void find(IPathObstacle from, IPathObstacle to, IPath path, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException{
        List<IPathObstacle> other = new ArrayList<IPathObstacle>(obstacles.size()-2);
        for(IPathObstacle o: obstacles){
            if(!(o==from) && !(o==to)){
                other.add(o);
            }
        }
        bypass(path, 0, 1, other, ignoreDebugger);
        
        if(debugger!=null){ // show final work
            debugger.show(path, obstacles);
            debugger.addInfo("done!");
        }
        lastPath = path;
    }
    
    IPath lastPath;
    
    public IPath getLastPath(){
        return lastPath;
    }
    
    /******* BYPASS METHODS ********/

    protected void bypass(IPath parent, int i1, int i2, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException{
        bypass(0, parent, i1, i2, obstacles, ignoreDebugger);
    }
    
    /**
     * Output can be null if no relevant path was found.
     */
    protected void bypass(int depth, IPath parent, int i1, int i2, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException{
        if(parent==null)
            return;
        if(parent.getLock(i1) && parent.getLock(i2))
            return;
        
        if(debugger!=null && !ignoreDebugger)
            debugger.addInfo(depth, "call bypass on " + i1 + "-" + i2);
        
        // ----------------------------------
        // Computes a hit analysis, indicating
        // how this segment intersects (or not) one of the obstacles.
        // Return null if no intersection
        PathHit hit = computePathHit(obstacles, parent.getPoint(i1), parent.getPoint(i2), parent.getLock(i1), parent.getLock(i2));
        
        if(debugger!=null && !ignoreDebugger){
            if(hit!=null)
                debugger.setCurrentObstacle(hit.getObstacle());
            debugger.highlight(PointUtils.list(parent.getPoint(i1), parent.getPoint(i2)));
            debugger.show(parent, obstacles);
            debugger.breakFlow();
        }
        
        
        IPath subPath = null;
        int prevId = i1; // the parent path section 
        int nextId = i2; // that will be replaced at this call
        
        List<IPathObstacle> otherObstacles = new ArrayList<IPathObstacle>();
        for(IPathObstacle o: obstacles){
            if(hit!=null){
                if(!(o==hit.getObstacle())){
                    otherObstacles.add(o);
                }      
            }
            else 
                otherObstacles.add(o);
        }
        
        // ----------------------------------
        // end of recursion: return the input segment
        // or a simple bend if no obstacle
        if(hit==null){
            subPath = baseSubPath(depth, parent, prevId, nextId, obstacles, ignoreDebugger);
        }
        
        // ----------------------------------
        // otherwise, make other bypass and 
        // append them to the current path,
        else{
            IPathObstacle o = hit.getObstacle();            
            if(debugger!=null && !ignoreDebugger){
                debugger.setCurrentObstacle(o);
                debugger.addInfo(depth, "hit " + hit.getType() + " (" + hit.getObstacle() + ")");
            }
            
            // -------------------------
            // searching the appropriate
            // path section to replace
            boolean ENSURE_NOT_LOCKED = true;
            
            // find the proper P1 point
            if(!parent.isFirst(i1) && hit.isP1Inside()){
                prevId = findPrevFreePoint(parent, i1, o, ENSURE_NOT_LOCKED);
                if(prevId==NO_FREE_NEIGHBOUR)
                    return; // TODO: should return NULL?
                if(debugger!=null){
                    debugger.addInfo(depth, "climb " + i1 + " to " + prevId);
                    debugger.highlight(PointUtils.list(parent.getPoint(prevId), parent.getPoint(nextId)));
                    debugger.breakFlow();
                }
            }
            // find the proper P2 point
            if(!parent.isLast(i2) && hit.isP2Inside()){
                nextId = findNextFreePoint(parent, i2, o, ENSURE_NOT_LOCKED);
                if(nextId==NO_FREE_NEIGHBOUR)
                    return;
                if(debugger!=null){
                    debugger.addInfo(depth, "climb " + i2 + " to " + nextId);
                    debugger.highlight(PointUtils.list(parent.getPoint(prevId), parent.getPoint(nextId)));
                    debugger.breakFlow();
                }
            }
            
            Point2D prev = parent.getPoint(prevId);
            Point2D next = parent.getPoint(nextId);
            Rectangle2D b = o.getBypassedBounds();
            
            // --------------
            // diagonal
            if(isDiagonal(prev, b, next)){
                subPath = baseSubPath(depth, parent, prevId, nextId, obstacles, ignoreDebugger);
                //o.addBypass();
            }
            
            // --------------            
            // opposite side: a crenel bypass
            else if(isAtEachOppositeSide(prev, b, next)){
                List<Point2D> bypass = bypassStraight2(b, prev, next);
                
                if(bypass!=null){
                    List<Boolean> locks = hitAny(bypass, obstacles);
                    subPath = new LockablePath(prev, bypass, next, parent.getLock(prevId), locks, parent.getLock(nextId));
                    //o.addBypass();
                }
                else{
                    error(o, prev, next);
                }
            }
            // --------------
            // joint side
            else if(isJointSide(prev, b, next) && (nextId-prevId>1)){
                Point2D intermediate;
                if(prevId==i1 && nextId!=i2)
                    intermediate = parent.getPoint(i2); // TODO: ATTENTION DONNER LA LISTE DES POINTS!
                else if(prevId!=i1 && nextId==i2)
                    intermediate = parent.getPoint(i1); // TODO: ATTENTION DONNER LA LISTE DES POINTS!
                else
                    intermediate = parent.getPoint(i1); // RANDOM CHOICE!!!
                    //throw new RuntimeException("? what to do");
                
                List<Point2D> bypass = bypassBend(b, prev, intermediate, next);
                
                if(bypass!=null){
                    List<Boolean> locks = hitAny(bypass, obstacles);
                    subPath = new LockablePath(prev, bypass, next, parent.getLock(prevId), locks, parent.getLock(nextId));
                    //o.addBypass();
                }
                else{
                    error(o, prev, next);
                }
            }
            
            // ------------
            // partial cross
            else{
                subPath = baseSubPath(depth, parent, prevId, nextId, obstacles, ignoreDebugger);
                o.addBypass();
            }
        }
        // dive in recursion if we really changed the path
        if(subPath.getPointNumber()>2){
            
            // update parent path with this subpath
            parent.replace(prevId, nextId, subPath);
            
            if(parent.hasDuplicate()){
                //System.out.println("call bypass on " + i1 + "-" + i2 + subPath.getPoints());  
                if(debugger!=null && !ignoreDebugger)
                    debugger.breakFlow();
            }
            
            if(debugger!=null && !ignoreDebugger)
                debugger.addInfo(depth, "merge " + subPath.getPointNumber() + "-path" + " to " + prevId + " " + nextId);
            
            // and do the same for each segment
            for (int i = 0; i < parent.getPointNumber()-1; i++) {
            //int from = prevId-1;
            //int to   = prevId+subPath.getPointNumber()+1;
            //for (int i = from; i <= to; i++) {
                //if(!parent.getLock(i) && !parent.getLock(i+1)){
                    PathHit h = computePathHit(otherObstacles, parent.getPoint(i), parent.getPoint(i+1), parent.getLock(i), parent.getLock(i+1));
                    if(h!=null){
                        bypass(depth+1, parent, i, i+1, obstacles, ignoreDebugger);
                    }
                //}
            }
            
            if(hit!=null)
                hit.getObstacle().addBypass();
        }
    }
    
    /** provide either a single direct two points segment, or a bended segment made of three points.*/
    protected IPath baseSubPath(int depth, IPath parent, int i1, int i2, List<IPathObstacle> obstacles, boolean ignoreDebugger){
        // direct line segment
        if(canStraight(parent.getPoint(i1), parent.getPoint(i2))){ // straight
            return new LockablePath(parent.getPoint(i1), parent.getPoint(i2), parent.getLock(i1), parent.getLock(i2));
        }
        // two segments creating a bend
        else{ 
            Point2D bend = bestBend(parent, i1, i2, obstacles);
            boolean canLock = !hitAny(bend, obstacles);
            return new LockablePath(parent.getPoint(i1), bend, parent.getPoint(i2), parent.getLock(i1), canLock, parent.getLock(i2));
        }
    }
    
    protected Point2D bestBendStupid(IPath parent, int i1, int i2, List<IPathObstacle> obstacles){
        Point2D bend = bend2(parent.getPoint(i1), parent.getPoint(i2));
        if(in(obstacles,bend)) // try the other side if required
            return bend1(parent.getPoint(i1), parent.getPoint(i2)); // do not ensure it works
        else
            return bend;
    }
    
    /** Estimates best bend by counting the number of obstacles hit by each possible two bends. */
    protected Point2D bestBend(IPath parent, int i1, int i2, List<IPathObstacle> obstacles){
        Point2D p1 = parent.getPoint(i1);
        Point2D p2 = parent.getPoint(i2);
        Point2D bend1 = bend1(p1, p2);
        Point2D bend2 = bend2(p1, p2);
        
        List<IPathObstacle> whoHits1 = whoHits(p1, bend1, p2, obstacles);
        List<IPathObstacle> whoHits2 = whoHits(p1, bend2, p2, obstacles);
        
        if(whoHits1.size()<whoHits2.size())
            return bend1;
        else
            return bend2;
    }
    
        
    /*******************/
    
    protected static int NO_FREE_NEIGHBOUR = -1;
    
    protected int findNextFreePoint(IPath parent, int i2, IPathObstacle o, boolean ensureNotLocked){
        return findNextFreePoint(parent, i2, ObstacleUtils.list(o), ensureNotLocked);
    }
    
    protected int findNextFreePoint(IPath parent, int i2, List<IPathObstacle> obstacles, boolean ensureNotLocked){
        if(parent.isLast(i2))
            return NO_FREE_NEIGHBOUR;
        
        int nextId = i2;
        Point2D next = null;
        
        while(true){
            next = parent.getPoint(nextId);
            
            if(ensureNotLocked){
                if(!parent.getLock(nextId)){
                    if(!hit(obstacles, next))
                        return nextId;
                }
                else{// at the first locked item, return it is not interior
                    if(!hit(obstacles, next))
                        return nextId;
                    else
                        return NO_FREE_NEIGHBOUR;
                }
            }
            else{
                if(!hit(obstacles, next))
                    return nextId;
            }
            
            nextId++;
            if(nextId>(parent.getPointNumber()-1)) 
                return NO_FREE_NEIGHBOUR; // impossible to find: exit
        }
    }
    
    protected int findPrevFreePoint(IPath parent, int i1, IPathObstacle o, boolean ensureNotLocked){
        return findPrevFreePoint(parent, i1, ObstacleUtils.list(o), ensureNotLocked);
    }
    
    protected int findPrevFreePoint(IPath parent, int i1, List<IPathObstacle> obstacles, boolean ensureNotLocked){
        if(parent.isFirst(i1))
            return NO_FREE_NEIGHBOUR;
        
        int prevId = i1;
        Point2D prev = null;
        while(true){
            prev = parent.getPoint(prevId);
            
            if(ensureNotLocked){
                if(!parent.getLock(prevId)){
                    if(!hit(obstacles, prev))
                        return prevId;
                }
                else{ // at the first locked item, return it is not interior
                    if(!hit(obstacles, prev))
                        return prevId;
                    else
                        return NO_FREE_NEIGHBOUR;
                }
            }
            else{
                if(!hit(obstacles, prev))
                    return prevId;
            }
            
            prevId--;
            if(prevId<0) 
                return NO_FREE_NEIGHBOUR; // impossible to find: exit
        }
    }
    
    /********************/
    
    protected PathHit computePathHit(List<IPathObstacle> obstacles, Point2D p1, Point2D p2, boolean lock1, boolean lock2){
        for(IPathObstacle o: obstacles){
            PathHit hit = computePathHit(o, p1, p2, lock1, lock2);
            if(hit!=null)
                return hit;
        }
        return null;
    }
    
    /*protected PathHit computeHit(IPathObstacle obstacle, Line2D segment, boolean lock1, boolean lock2){
        return computeHit(obstacle, segment.getP1(), segment.getP2(), lock1, lock2);
    }*/
    
    protected PathHit computePathHit(IPathObstacle obstacle, Point2D p1, Point2D p2, boolean lock1, boolean lock2){
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
        
    /********************/

    protected void error(IPathObstacle o, Point2D p1, Point2D p2){
        if(debugger!=null){
            debugger.highlight(PointUtils.list(p1, p2));
            debugger.breakFlow();
        }
        throw new RuntimeException("illegal state: no bypass found "
                + "\n" + o
                 + " xrange:"+o.getBypassedBounds().getMinX()+":"+o.getBypassedBounds().getMaxX()
                 + " yrange:"+o.getBypassedBounds().getMinY()+":"+o.getBypassedBounds().getMaxY()
                + "\np1:" + p1
                + "\np2:" + p2); 
    }
    
    protected void error(IPathObstacle o, Point2D p1, Point2D p2, Point2D p3){
        if(debugger!=null)
            debugger.error(PointUtils.list(p1, p2, p3));
        
        throw new RuntimeException("illegal state: no bypass found "
                + "\n" + o 
                  + " xrange:"+o.getBypassedBounds().getMinX()+":"+o.getBypassedBounds().getMaxX()
                  + " yrange:"+o.getBypassedBounds().getMinY()+":"+o.getBypassedBounds().getMaxY()
                + "\np1:" + p1
                + "\np2:" + p2
                + "\np3:" + p3); 
    }
    
    /**************/
    
}
