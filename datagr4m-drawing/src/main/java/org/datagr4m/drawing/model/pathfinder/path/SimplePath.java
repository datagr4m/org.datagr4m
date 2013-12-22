package org.datagr4m.drawing.model.pathfinder.path;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.maths.geometry.Pt;
import org.jzy3d.maths.Coord2d;


/**
 * A geometrical representation of a {@link DirectedLink}, that indicates how to represent
 * the link between two items.
 * 
 * Stores an ordered list of points and provides methods for easy insertions, 
 * replacement or deletion.
 * 
 * Points have a locking status.
 * 
 * Path does not accepts several points to the same coordinates.
 */
public class SimplePath implements Serializable, IPath{
    private static final long serialVersionUID = -4201518078322002036L;
    
    public SimplePath(){
        this.points = Collections.synchronizedList(new ArrayList<Point2D>());
    }

    public SimplePath(Point2D p1, Point2D p2){
        this.points = new ArrayList<Point2D>(2);
        this.points.add(p1);
        this.points.add(p2);
        verifyEachPointUnique();
    }
    
    public SimplePath(Point2D p1, Point2D p2, Point2D p3){
        this.points = new ArrayList<Point2D>(3);
        this.points.add(p1);
        this.points.add(p2);
        this.points.add(p3);
        verifyEachPointUnique();
    }
    
    
    public SimplePath(List<Point2D> points){
        this.points = new ArrayList<Point2D>(points.size());
        this.points.addAll(points);
        verifyEachPointUnique();
    }
    
    public SimplePath(Point2D p1, List<Point2D> points, Point2D p2){
        this.points = new ArrayList<Point2D>(points.size()+2);
        this.points.add(p1);
        this.points.addAll(points);
        this.points.add(p2);
        verifyEachPointUnique();
    }
    
    public SimplePath(IPath p2){
        this(p2.getPoints());
    }
    
    public SimplePath(Line2D segment){
        this(segment.getP1(), segment.getP2());
    }
    
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertBefore(java.awt.geom.Point2D, com.netlight.pathfinder.path.IPath)
     */
    
    @Override
    public void insertBefore(Point2D p, IPath bypass){
        insertBefore(p, bypass.getPoints(), bypass.getLocks());
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertBefore(java.awt.geom.Point2D, java.util.List, boolean)
     */
    @Override
    public void insertBefore(Point2D p, List<Point2D> bypass, boolean lock){
        insertBefore(p, bypass, locks(lock, bypass.size()));
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertBefore(java.awt.geom.Point2D, java.util.List, java.util.List)
     */
    @Override
    public void insertBefore(Point2D p, List<Point2D> bypass, List<Boolean> locks){
        int id = points.indexOf(p);
        if(id==-1)
            throw new IllegalArgumentException(p + " not found in the path");
        insertAt(id, bypass, locks);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertBefore(java.awt.geom.Point2D, java.awt.geom.Point2D, java.lang.Boolean)
     */
    @Override
    public void insertBefore(Point2D p, Point2D bend, Boolean lock){
        int id = points.indexOf(p);
        if(id==-1)
            throw new IllegalArgumentException(p + " not found in the path");
        insertAt(id, bend, lock);
    }
    
    /***********/

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertAt(int, com.netlight.pathfinder.path.IPath)
     */
    @Override
    public void insertAt(int id, IPath path){
        this.points.addAll(id, path.getPoints());
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertAt(int, java.util.List, boolean)
     */
    @Override
    public void insertAt(int id, List<Point2D> bypass, boolean lock){
        insertAt(id, bypass, locks(lock, bypass.size()));
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertAt(int, java.util.List, java.util.List)
     */
    @Override
    public void insertAt(int id, List<Point2D> bypass, List<Boolean> locks){
        verify(bypass);
        this.points.addAll(id, bypass);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#insertAt(int, java.awt.geom.Point2D, boolean)
     */
    @Override
    public void insertAt(int id, Point2D point, boolean lock){
        verify(point);
        this.points.add(id, point);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#replace(java.awt.geom.Point2D, com.netlight.pathfinder.path.IPath)
     */
    
    @Override
    public void replace(Point2D p, IPath bypass){
        replace(p, bypass.getPoints(), bypass.getLocks());
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#replace(int, int, com.netlight.pathfinder.path.IPath)
     */
    @Override
    public void replace(int from, int to, IPath bypass) throws HistoryRepeatingException{
        if(logPathEdit)
            logOrCrashPathEdit(from, to, bypass);
        delete(from, to);
        insertAt(from, bypass);
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#replace(java.awt.geom.Point2D, java.util.List, boolean)
     */
    @Override
    public void replace(Point2D p, List<Point2D> bypass, boolean lock){
        replace(p, bypass, locks(lock, bypass.size()));
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#replace(java.awt.geom.Point2D, java.util.List, java.util.List)
     */
    @Override
    public void replace(Point2D p, List<Point2D> bypass, List<Boolean> locks){
        int id = points.indexOf(p);
        if(id==-1)
            throw new IllegalArgumentException(p + " not found in the path");
        this.points.remove(id);
        
        verify(bypass);
        
        this.points.addAll(id, bypass);
    }
    
    /************/
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#flip(int, int)
     */
    @Override
    public void flip(int id1, int id2){
        Point2D p1 = points.get(id1);
        Point2D p2 = points.get(id2);
        
        points.set(id2, p1);
        points.set(id1, p2);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#reverse()
     */
    @Override
    public void reverse(){
        Collections.reverse(points);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#add(com.netlight.pathfinder.path.IPath)
     */

    @Override
    public void add(IPath path){
        this.points.addAll(path.getPoints());
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#add(java.awt.geom.Point2D, boolean)
     */
    @Override
    public void add(Point2D p, boolean lock){
        verify(p);
        
        this.points.add(p);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#add(double, double, boolean)
     */
    @Override
    public void add(double x, double y, boolean lock){
        add(new Point2D.Double(x,y), lock);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#delete(int, int)
     */
    @Override
    public void delete(int from, int to){
        if(from>to){
            throw new RuntimeException("not a valid id range. From=" + from + " - To=" + to);
        }
            
        int n = to-from;
        while(n>=0){
            this.points.remove(from);
            n--;
        }   
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#clear()
     */
    @Override
    public void clear(){
        points.clear();
        history.clear();
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getLocks()
     */
    
    @Override
    public List<Boolean> getLocks(){
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getLock(int)
     */
    @Override
    public boolean getLock(int i){
        return false;
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getLock(java.awt.geom.Point2D)
     */
    @Override
    public boolean getLock(Point2D p){
        int id = points.indexOf(p);
        if(id==-1)
            throw new RuntimeException("point " + p + " not found in this path");
        return getLock(id);
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getPointNumber()
     */

    @Override
    public int getPointNumber(){
        return points.size();
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getPoints()
     */
    @Override
    public List<Point2D> getPoints(){
        return points;
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getPoint(int)
     */
    @Override
    public Point2D getPoint(int i){
        int s = points.size();
        if(s==0)
            return null;
        return points.get(i);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getLastPoint()
     */
    @Override
    public Point2D getLastPoint(){
        int s = points.size();
        if(s==0)
            return null;
        return points.get(s-1);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getFirstPoint()
     */
    @Override
    public Point2D getFirstPoint(){
        if(points.size()==0)
            return null;
        return points.get(0);
    }
    
    // a copy of points, for rendering
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getCoords()
     */
    @Override
    public synchronized List<Coord2d> getCoords(){
        synchronized(points){
            List<Coord2d> coords = new ArrayList<Coord2d>(points.size());
            
            Iterator<Point2D> it = points.iterator();
            while(it.hasNext())
                coords.add(Pt.cloneAsCoord2d(it.next()));
            //for(Point2D p: points)
            //    coords.add(new Coord2d(p));
            return coords;
        }
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#isFirst(int)
     */
    @Override
    public boolean isFirst(int id){
        return id==0;
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#isLast(int)
     */
    @Override
    public boolean isLast(int id){
        return id==(points.size()-1);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getIndex(java.awt.geom.Point2D)
     */
    @Override
    public int getIndex(Point2D p){
        return points.indexOf(p);
    }
    
    /**************/
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#hasSegment(com.netlight.model.pairs.Pair)
     */
    @Override
    public boolean hasSegment(Pair<Point2D, Point2D> segment){
        if(segment instanceof CommutativePair<?>)
            return hasSegment(segment, true);
        else
            return hasSegment(segment, false);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#hasSegment(com.netlight.model.pairs.Pair, boolean)
     */
    @Override
    public boolean hasSegment(Pair<Point2D, Point2D> segment, boolean allowCommutative){
        // only find first instance if we don't have dupplicates
        if(forbidDuplicates){
            int i = getIndex(segment.a); 
            if(i>=0){ // if first point found
                return hasSecondPointSegmentAround(i, segment, allowCommutative);
            }
            else
                return false;
        }
        // ---------------------------
        // otherwise search in the whole path
        else{
            for (int i = 0; i < points.size(); i++) {
                if(points.get(i).equals(segment.a)){
                    if(hasSecondPointSegmentAround(i, segment, allowCommutative))
                        return true;
                }
            }
            return false;
        }
    }
    
    protected boolean hasSecondPointSegmentAround(int i, Pair<Point2D, Point2D> segment, boolean allowCommutative){
        Point2D p2;
        if(i<points.size()-1){
            p2 = points.get(i+1);
            if(p2.equals(segment.b))
                return true;
        }
        else if(i>0 && allowCommutative){
            p2 = points.get(i-1);
            if(p2.equals(segment.b))
                return true;
        }
        return false;  // second point can't be found
    }

    public static Pair<Point2D,Point2D> directedSegment(Point2D p1, Point2D p2){
        return new Pair<Point2D,Point2D>(p1, p2);
    }

    public static CommutativePair<Point2D> undirectedSegment(Point2D p1, Point2D p2){
        return new CommutativePair<Point2D>(p1, p2);
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#hasSequence(java.util.List)
     */
    @Override
    public boolean hasSequence(List<Point2D> points){
        int prev = getIndex(points.get(0));
        if(prev==-1)
            return false;
        
        int next;
        for (int i = 1; i < points.size(); i++) {
            next = getIndex(points.get(i));
            if(next!=(prev+1))
                return false;
            else{
                prev = next;
                next = -1;
            }
        }
        return true;
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getSegment(int)
     */
    
    @Override
    public Line2D getSegment(int i){
        if(i >= getSegmentNumber())
            throw new IllegalArgumentException("no segment at " + i + ", only " + getSegmentNumber() + " segments");
        else{
            return new Line2D.Double(points.get(i), points.get(i+1));
        }
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getSegment(int, int)
     */
    @Override
    public Line2D getSegment(int i, int j){
        if(i >= getPointNumber())
            throw new IllegalArgumentException("no point at " + i + ", only " + getPointNumber() + " points");
        else if(j >= getPointNumber())
            throw new IllegalArgumentException("no point at " + j + ", only " + getPointNumber() + " points");
        else{
            return new Line2D.Double(points.get(i), points.get(j));
        }
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getSegments()
     */
    @Override
    public List<Line2D> getSegments(){
        int s = getSegmentNumber();
        
        List<Line2D> segments = new ArrayList<Line2D>();
        for (int i = 0; i < s; i++)
            segments.add(getSegment(i));
        return segments;
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#getSegmentNumber()
     */
    @Override
    public int getSegmentNumber(){
        return getPointNumber()-1;
    }
    
    /************/
    
    /*public Path clone(){
        return new Path(this);
    }*/
    
    public static List<Boolean> locks(boolean value, int n){
        List<Boolean> locks = new ArrayList<Boolean>(n);
        for (int i = 0; i < n; i++) {
            locks.add(value);
        }
        return locks;
    }
    
    /***************/
    
    protected void verify(List<Point2D> points){
        if(!forbidDuplicates)
            return;
        for(Point2D p: points)
            verify(p);
    }

    protected void verify(Point2D point){
        if(forbidDuplicates)
            if(points.contains(point))
                throw new IllegalArgumentException("path already contains point " + point);
    }

    protected void verifyEachPointUnique(){
        if(forbidDuplicates)
            cleanupDuplicates();
    }
    
    protected void crashDuplicates(){
        if(forbidDuplicates && hasDuplicate())
            throw new IllegalArgumentException("some points are duplicated in " + points);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#hasDuplicate()
     */
    @Override
    public boolean hasDuplicate(){
        Set<Point2D> s = new HashSet<Point2D>(points);
        return (s.size()!=points.size());
    }
    
    protected void cleanupDuplicates(){
        Map<Point2D,Integer> pointId = new HashMap<Point2D,Integer>();
        
        for(int id=0; id<points.size(); id++){
            Point2D point = points.get(id);
            if(!pointId.containsKey(point)){
                pointId.put(point, id);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#isForbidDuplicates()
     */
    @Override
    public boolean isForbidDuplicates() {
        return forbidDuplicates;
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#setForbidDuplicates(boolean)
     */
    @Override
    public void setForbidDuplicates(boolean forbidDuplicates) {
        this.forbidDuplicates = forbidDuplicates;
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#hashCode()
     */
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((points == null) ? 0 : points.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimplePath other = (SimplePath) obj;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;
        return true;
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#clone()
     */
    @Override
    public Object clone(){
        SimplePath p2 = new SimplePath(this);
        p2.forbidDuplicates = forbidDuplicates;
        return p2;
    }
    
    /************/

    protected void logOrCrashPathEdit(int from, int to, IPath path) throws HistoryRepeatingException{
        //System.out.println(history);
        PathEditLog log = new PathEditLog(from, to, path);
        if(history.exists(log))
            throw new HistoryRepeatingException(log, history);
        else
            history.add(log);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPath#toString()
     */

    @Override
    public String toString(){
        StringBuffer b = new StringBuffer();
        b.append("(Path) ");
        for (int i = 0; i < points.size(); i++) {
            b.append("["+i+"]("+points.get(i).getX()+","+points.get(i).getY()+")" + " ");
        }
        return b.toString();
    }

    /************/
    protected boolean logPathEdit = false;
    protected PathEditHistory history = new PathEditHistory();
    protected List<Point2D> points;
    
    protected boolean forbidDuplicates = true;
}
