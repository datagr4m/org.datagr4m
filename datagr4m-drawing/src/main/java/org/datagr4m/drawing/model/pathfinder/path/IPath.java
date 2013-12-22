package org.datagr4m.drawing.model.pathfinder.path;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.datastructures.pairs.Pair;
import org.jzy3d.maths.Coord2d;


public interface IPath {

    /***********/

    public void insertBefore(Point2D p, IPath bypass);

    public void insertBefore(Point2D p, List<Point2D> bypass, boolean lock);

    public void insertBefore(Point2D p, List<Point2D> bypass, List<Boolean> locks);

    public void insertBefore(Point2D p, Point2D bend, Boolean lock);

    /** Insert path points and locks at the specified position. 
     * All point and lock standing at and after this id are shifted to higher indices.
     */
    public void insertAt(int id, IPath path);

    public void insertAt(int id, List<Point2D> bypass, boolean lock);

    public void insertAt(int id, List<Point2D> bypass, List<Boolean> locks);

    public void insertAt(int id, Point2D point, boolean lock);

    /************/

    public void replace(Point2D p, IPath bypass);

    public void replace(int from, int to, IPath bypass) throws HistoryRepeatingException;

    public void replace(Point2D p, List<Point2D> bypass, boolean lock);

    public void replace(Point2D p, List<Point2D> bypass, List<Boolean> locks);

    /** Swap position of the two points identified by their id.*/
    public void flip(int id1, int id2);

    /** Reverse the complete path.*/
    public void reverse();

    /************/

    public void add(IPath path);

    public void add(Point2D p, boolean lock);

    public void add(double x, double y, boolean lock);

    public void delete(int from, int to);

    public void clear();

    /************/

    public List<Boolean> getLocks();

    public boolean getLock(int i);

    public boolean getLock(Point2D p);

    /************/

    public int getPointNumber();

    public List<Point2D> getPoints();

    public Point2D getPoint(int i);

    /**
     * @return null if no point in the path
     */
    public Point2D getLastPoint();

    public Point2D getFirstPoint();

    // a copy of points, for rendering
    public List<Coord2d> getCoords();

    public boolean isFirst(int id);

    public boolean isLast(int id);

    public int getIndex(Point2D p);

    /**
     * Returns true if the path contains the two points in the given
     * order.
     * If the input pair is a {@link CommutativePair}, then the method returns true
     * if either a-b or b-a can be found in the sequence.
     */
    public boolean hasSegment(Pair<Point2D, Point2D> segment);

    public boolean hasSegment(Pair<Point2D, Point2D> segment, boolean allowCommutative);

    /** 
     * Return true if the path contains the sequence of points ordered as 
     * the input is ordered
     */
    public boolean hasSequence(List<Point2D> points);

    /**************/

    public Line2D getSegment(int i);

    public Line2D getSegment(int i, int j);

    public List<Line2D> getSegments();

    public int getSegmentNumber();

    public boolean hasDuplicate();

    public boolean isForbidDuplicates();

    public void setForbidDuplicates(boolean forbidDuplicates);

    public Object clone();

    @Override
	public String toString();
}