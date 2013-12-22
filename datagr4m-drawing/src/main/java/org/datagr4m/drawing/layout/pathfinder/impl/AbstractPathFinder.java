package org.datagr4m.drawing.layout.pathfinder.impl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.maths.geometry.LineUtils;
import org.datagr4m.maths.geometry.PointUtils;
import org.datagr4m.maths.geometry.RectangleUtils;


/** 
 * Provides primitives for computing pathes through a collection of obstacles.
 */
public class AbstractPathFinder {
    public boolean isAtEachOppositeSide(Point2D p1, Rectangle2D r, Point2D p2){
        return isAtEachOppositeVerticalSide(p1, r, p2)
            ^ isAtEachOppositeHorizontalSide(p1, r, p2);
    }
    
    public boolean isDiagonal(Point2D p1, Rectangle2D r, Point2D p2){
        return isAtEachOppositeVerticalSide(p1, r, p2)
            && isAtEachOppositeHorizontalSide(p1, r, p2);
    }

    public boolean isAtEachOppositeVerticalSide(Point2D p1, Rectangle2D r, Point2D p2){
        return    (p1.getX()<=r.getMinX() 
                && p2.getX()>=r.getMaxX())  // p1->p2, left to right
                ||(p1.getX()>=r.getMaxX()
                && p2.getX()<=r.getMinX()); // p2<-p1, right to left
    }
    
    public boolean isAtEachOppositeHorizontalSide(Point2D p1, Rectangle2D r, Point2D p2){
        return    (p1.getY()<=r.getMinY() 
                && p2.getY()>=r.getMaxY())  // p1->p2, bottom to top
                ||(p1.getY()>=r.getMaxY() 
                && p2.getY()<=r.getMinY()); // p2<-p1, top to bottom
    }
    
    public boolean isJointSide(Point2D p1, Rectangle2D r, Point2D p2){
        return isJointSideAsymetric(p1, r, p2) || isJointSideAsymetric(p2, r, p1);
    }
    
    public boolean isJointSideAsymetric(Point2D p1, Rectangle2D r, Point2D p2){
        return (  (p1.getX()> r.getMinX() && p1.getX() <r.getMaxX())
                &&(p1.getY()<=r.getMinY() || p1.getY()>=r.getMaxY()) // p1 in r.width && out of r.height
                &&(p2.getY()> r.getMinY() && p2.getY() <r.getMaxY())
                &&(p2.getX()<=r.getMinX() || p2.getX()>=r.getMaxX())); // p2 in r.height && out of r.width         
    }
    
    public boolean isBothInside(Point2D p1, Rectangle2D r, Point2D p2){
        return RectangleUtils.contains(r, p1, true) && RectangleUtils.contains(r, p2, true);
    }
    
    /* */
    
    public List<Point2D> bypassStraight(Rectangle2D r, Line2D line){
        return bypassStraight(r, line.getP1(), line.getP2());
    }
        
    /** 
     * Return a path allowing to bypass a rectangle, only if the segment fully traverse the rectangle, 
     * otherwise return null.
     * 
     * Output does not contains the input point.
     */
    public List<Point2D> bypassStraight(Rectangle2D r, Point2D p1, Point2D p2){
        Line2D line = new Line2D.Double(p1, p2);
        Line2D left   = lineLeft(r);
        Line2D right  = lineRight(r);
        Line2D bottom = lineBottom(r);
        Line2D top    = lineTop(r);

        Point2D intersectLeft   = LineUtils.getIntersectionPoint(line, left);
        Point2D intersectRight  = LineUtils.getIntersectionPoint(line, right);
        Point2D intersectTop    = LineUtils.getIntersectionPoint(line, top);
        Point2D intersectBottom = LineUtils.getIntersectionPoint(line, bottom);

        // traverse de gauche � droite: passe au dessus
        if(areDefined(intersectLeft, intersectRight)){
            Point2D pBypass1 = new Point2D.Double(r.getMinX(), r.getMaxY());
            Point2D pBypass2 = new Point2D.Double(r.getMaxX(), r.getMaxY());
            
            if (isLeftToRight(p1, p2))
                return PointUtils.list(intersectLeft, pBypass1, pBypass2, intersectRight);
            else
                return PointUtils.list(intersectRight, pBypass2, pBypass1, intersectLeft);
        }
        
        // traverse de haut en bas: passe � gauche
        else if(areDefined(intersectTop, intersectBottom)){
            Point2D pBypass1 = new Point2D.Double(r.getMinX(), r.getMinY());
            Point2D pBypass2 = new Point2D.Double(r.getMinX(), r.getMaxY());
            
            if (isBottomToTop(p1, p2))
                return PointUtils.list(intersectBottom, pBypass1, pBypass2, intersectTop);
            else
                return PointUtils.list(intersectTop, pBypass2, pBypass1, intersectBottom);
        }
        // ne traverse pas de part en part avec un segment unique
        return null;
    }
    
    public List<Point2D> bypassStraight2(Rectangle2D r, Line2D line){
        return bypassStraight2(r, line.getP1(), line.getP2());
    }
    
    /** 
     * Return a path allowing to bypass a rectangle, only if the segment fully traverse the rectangle, 
     * otherwise return null.
     * Supports a non horizontal or vertical segment, but will return points implying an orthogonal
     * bypass.
     * Output does not contain the input point.
     */
    public List<Point2D> bypassStraight2(Rectangle2D r, Point2D p1, Point2D p2){
        boolean horizontal = isAtEachOppositeVerticalSide(p1, r, p2);
        boolean vertical   = isAtEachOppositeHorizontalSide(p1, r, p2);
        boolean diagonal   = horizontal && vertical;
        
        // ------------------------------
        // if both horizontal & vertical,
        // means we only need a point to form a bend
        // that won't traverse the rectangle
        if(diagonal){
            if(randomSwitch())
                return PointUtils.list(bend1(p1, p2));
            else
                return PointUtils.list(bend2(p1, p2));
        }
        
        // -------------------------------
        // if one point stand on the left, 
        // and the other on the right of the rectangle
        else if(horizontal){
            if(isLeftToRight(p1, p2)){
                Point2D bp1 = new Point2D.Double(r.getMinX(), p1.getY());
                Point2D bp2 = new Point2D.Double(r.getMinX(), r.getMaxY());
                Point2D bp3 = new Point2D.Double(r.getMaxX(), r.getMaxY());
                Point2D bp4 = new Point2D.Double(r.getMaxX(), p2.getY());
                return PointUtils.list(bp1, bp2, bp3, bp4);
            }
            else{
                Point2D bp1 = new Point2D.Double(r.getMaxX(), p1.getY());
                Point2D bp2 = new Point2D.Double(r.getMaxX(), r.getMaxY());
                Point2D bp3 = new Point2D.Double(r.getMinX(), r.getMaxY());
                Point2D bp4 = new Point2D.Double(r.getMinX(), p2.getY());
                return PointUtils.list(bp1, bp2, bp3, bp4);
            }
        }
        
        // ------------------------------
        // if one point stand on the top,
        // and the other on the bottom of the rectangle
        else if(vertical){
            if(isBottomToTop(p1, p2)){
                Point2D bp1 = new Point2D.Double(p1.getX(),   r.getMinY());
                Point2D bp2 = new Point2D.Double(r.getMinX(), r.getMinY());
                Point2D bp3 = new Point2D.Double(r.getMinX(), r.getMaxY());
                Point2D bp4 = new Point2D.Double(p2.getX(),   r.getMaxY());
                return PointUtils.list(bp1, bp2, bp3, bp4);
            }
            else{
                Point2D bp1 = new Point2D.Double(p1.getX(),   r.getMaxY());
                Point2D bp2 = new Point2D.Double(r.getMinX(), r.getMaxY());
                Point2D bp3 = new Point2D.Double(r.getMinX(), r.getMinY());
                Point2D bp4 = new Point2D.Double(p2.getX(),   r.getMinY());
                return PointUtils.list(bp1, bp2, bp3, bp4);
            }
        }
        
        // ------------------------------
        // n'�tait pas une intersection de part en part
        // on a peut �tre un point � c�t� et un point �
        // l'int�rieur du rectangle, ou les deux � l'int�rieur
        else if(!horizontal && !vertical){
            System.err.println("bypassStraight2: wasn't a traversal!");            
            return null; 
        }

        // otherwise??
        else{
            System.err.println("bypassStraight2: why??!!");
            return null;
        }
    }
    
    public List<Point2D> bypassBend(Rectangle2D r, Point2D p1, Point2D p2, Point2D p3) {
        //System.err.println("bypassbend");
        Line2D left   = lineLeft(r);
        Line2D right  = lineRight(r);
        Line2D bottom = lineBottom(r);
        Line2D top    = lineTop(r);

        // intersections of input segment with rectangle
        Line2D lineIn             = new Line2D.Double(p1, p2);
        Point2D intersectLeftIn   = LineUtils.getIntersectionPoint(lineIn, left);
        Point2D intersectRightIn  = LineUtils.getIntersectionPoint(lineIn, right);
        Point2D intersectTopIn    = LineUtils.getIntersectionPoint(lineIn, top);
        Point2D intersectBottomIn = LineUtils.getIntersectionPoint(lineIn, bottom);

        // intersections of output segment with rectangle
        Line2D lineOut             = new Line2D.Double(p2, p3);
        Point2D intersectLeftOut   = LineUtils.getIntersectionPoint(lineOut, left);
        Point2D intersectRightOut  = LineUtils.getIntersectionPoint(lineOut, right);
        Point2D intersectTopOut    = LineUtils.getIntersectionPoint(lineOut, top);
        Point2D intersectBottomOut = LineUtils.getIntersectionPoint(lineOut, bottom);

        // enter bottom
        if(intersectBottomIn!=null){
            if(intersectLeftOut!=null){
                Point2D newBend = new Point2D.Double(intersectLeftOut.getX(), intersectBottomIn.getY());
                return PointUtils.list(intersectBottomIn, newBend, intersectLeftOut);
            }
            else if(intersectRightOut!=null){
                Point2D newBend = new Point2D.Double(intersectRightOut.getX(), intersectBottomIn.getY());
                return PointUtils.list(intersectBottomIn, newBend, intersectRightOut);
            }
            else{
                System.err.println("is p1 inside? " + RectangleUtils.contains(r, p1));
                System.err.println("is p2 inside? " + RectangleUtils.contains(r, p2));
                System.err.println("is p3 inside? " + RectangleUtils.contains(r, p3));
                return null; // was not a bend, p2 was not inside
            }
        }
        // enter top
        else if(intersectTopIn!=null){
            if(intersectLeftOut!=null){
                Point2D newBend = new Point2D.Double(intersectLeftOut.getX(), intersectTopIn.getY());
                return PointUtils.list(intersectTopIn, newBend, intersectLeftOut);
            }
            else if(intersectRightOut!=null){
                Point2D newBend = new Point2D.Double(intersectRightOut.getX(), intersectTopIn.getY());
                return PointUtils.list(intersectTopIn, newBend, intersectRightOut);
            }
            else{
                System.err.println("is p1 inside? " + RectangleUtils.contains(r, p1));
                System.err.println("is p2 inside? " + RectangleUtils.contains(r, p2));
                System.err.println("is p3 inside? " + RectangleUtils.contains(r, p3));
                return null; // was not a bend
            }
        }
        // also need to check if we enter left since the path is ordered
        // enter left
        else if(intersectLeftIn!=null){
            if(intersectBottomOut!=null){
                Point2D newBend = new Point2D.Double(intersectLeftIn.getX(), intersectBottomOut.getY());
                return PointUtils.list(intersectLeftIn, newBend, intersectBottomOut);
            }
            else if(intersectTopOut!=null){
                Point2D newBend = new Point2D.Double(intersectLeftIn.getX(), intersectTopOut.getY());
                return PointUtils.list(intersectLeftIn, newBend, intersectTopOut);
            }
            else{
                System.err.println("is p1 inside? " + RectangleUtils.contains(r, p1));
                System.err.println("is p2 inside? " + RectangleUtils.contains(r, p2));
                System.err.println("is p3 inside? " + RectangleUtils.contains(r, p3));
                return null; // was not a bend
            }
        }
        else if(intersectRightIn!=null){
            if(intersectBottomOut!=null){
                Point2D newBend = new Point2D.Double(intersectRightIn.getX(), intersectBottomOut.getY());
                return PointUtils.list(intersectRightIn, newBend, intersectBottomOut);
            }
            else if(intersectTopOut!=null){
                Point2D newBend = new Point2D.Double(intersectRightIn.getX(), intersectTopOut.getY());
                return PointUtils.list(intersectRightIn, newBend, intersectTopOut);
            }
            else{
                System.err.println("is p1 inside? " + RectangleUtils.contains(r, p1));
                System.err.println("is p2 inside? " + RectangleUtils.contains(r, p2));
                System.err.println("is p3 inside? " + RectangleUtils.contains(r, p3));
                return null; // was not a bend
            }
        }
        return null; // was not a bend
    }
    
    public boolean areDefined(Point2D p1, Point2D p2){
        return p1!=null && p2!=null;
    }
    
    /* HIT TESTS */
    
    /**
     * Return true if at least one point of the segment is in the rectangle.
     */
    public boolean hit(IPathObstacle o, Line2D segment){
        return hit(o, segment, true);
    }
    
    // TODO: am�liorer cette intersection pourrie
    public boolean hit(IPathObstacle o, Line2D segment, boolean ignoreBorder){
        if(ignoreBorder)
            return RectangleUtils.scale(o.getBypassedBounds(),-2,-2).intersectsLine(segment);
        else
            return o.getBypassedBounds().intersectsLine(segment);
    }
    
    public boolean hit(IPathObstacle o, Point2D p1, Point2D p2){
        return hit(o, new Line2D.Double(p1, p2));
    }
    
    public boolean hit(IPathObstacle o, Point2D p1, Point2D p2, boolean ignoreBorder){
        return hit(o, new Line2D.Double(p1, p2));
    }
    
    public boolean hit(List<IPathObstacle> obstacles, Point2D point){
        for(IPathObstacle o: obstacles)
            if(hit(o, point))
                return true;
        return false;
    }
    
    public boolean hit(IPathObstacle o, Point2D point){
        return hit(o, point, true);
    }
    
    public boolean hit(IPathObstacle o, Point2D point, boolean ignoreBorder){
        return RectangleUtils.contains(o.getBypassedBounds(), point, ignoreBorder);
    }
    
    public boolean hitP1(IPathObstacle o, Line2D segment){
        return hit(o, segment.getP1());
    }

    public boolean hitP2(IPathObstacle o, Line2D segment){
        return hit(o, segment.getP2());
    }
    
    /**** HIT TEST ON LISTS OF OBSTACLES ****/

    public boolean hitAny(Point2D p, List<IPathObstacle> obstacles){
        for(IPathObstacle o: obstacles)
            if(hit(o, p))
                return true;
        return false;
    }
    
    public List<Boolean> hitAny(List<Point2D> bypass, List<IPathObstacle> obstacles){
        List<Boolean> locks = new ArrayList<Boolean>(bypass.size());
        
        for(Point2D p: bypass){
            boolean lock = ! hitAny(p, obstacles);
            locks.add(lock);
        }
        return locks;
    }
    
    /**** HIT TEST FOR BENDS ****/
    
    /** Return the list of obstacles hit by the bend formed by p1, p2 and p3. Each hit obstacle appears only one time*/
    public List<IPathObstacle> whoHits(Point2D p1, Point2D p2, Point2D p3, List<IPathObstacle> obstacles){
        List<IPathObstacle> hitting1 = whoHits(p1, p2, obstacles);
        List<IPathObstacle> hitting2 = whoHits(p2, p3, obstacles);
        
        if(hitting1.size()==0)
            return hitting2;
        else if(hitting2.size()==0)
            return hitting1;
        else{
            for(IPathObstacle o: hitting2)
                if(!hitting1.contains(o))
                    hitting1.add(o);
            return hitting1;
        }
    }
    
    /** Return the list of obstacles hit by the segment formed by p1 and p2.*/
    public List<IPathObstacle> whoHits(Point2D p1, Point2D p2, List<IPathObstacle> obstacles){
        List<IPathObstacle> hitting = new ArrayList<IPathObstacle>();
        
        for(IPathObstacle obstacle: obstacles){
            if(hit(obstacle, p1, p2))
                hitting.add(obstacle);
        }
        return hitting;
    }
    
    /******** RECTANGLE DECOMPOSITION *********/

    public Line2D lineLeft(Rectangle2D r) {
        return new Line2D.Double(r.getMinX(), r.getMinY(), r.getMinX(), r.getMaxY());
    }

    public Line2D lineRight(Rectangle2D r) {
        return new Line2D.Double(r.getMaxX(), r.getMinY(), r.getMaxX(), r.getMaxY());
    }

    public Line2D lineTop(Rectangle2D r) {
        return new Line2D.Double(r.getMinX(), r.getMaxY(), r.getMaxX(), r.getMaxY());
    }

    public Line2D lineBottom(Rectangle2D r) {
        return new Line2D.Double(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMinY());
    }
    
    /******* ORDERING **********/
    
    public boolean isLeftToRight(Point2D p1, Point2D p2) {
        return p1.getX() <= p2.getX();
    }

    public boolean isBottomToTop(Point2D p1, Point2D p2) {
        return p1.getY() <= p2.getY();
    }

    /******* BEND BUILDER **********/
    
    public boolean mustBend(Line2D line){
        return !canStraight(line.getP1(), line.getP2());
    }
    
    public boolean canStraight(Line2D line){
        return canStraight(line.getP1(), line.getP2());
    }
    
    public boolean canStraight(Point2D p1, Point2D p2){
        return (p1.getX() == p2.getX() || p1.getY() == p2.getY());
    }

    public Point2D bend1(Line2D line) {
        return bend1(line.getP1(), line.getP2());
    }

    public Point2D bend2(Line2D line) {
        return bend2(line.getP1(), line.getP2());
    }

    public Point2D bend1(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX(), p2.getY());
    }

    public Point2D bend2(Point2D p1, Point2D p2) {
        return new Point2D.Double(p2.getX(), p1.getY());
    }
    
    public boolean randomSwitch(){
        return Math.random()>0.5;
    }
    
    /****** OBSTACLE HELPERS **********/
    
    public boolean in(IPathObstacle obstacle, Point2D p){
        return (obstacle.getBypassedBounds().contains(p));
    }
    
    public boolean in(List<IPathObstacle> obstacles, Point2D p){
        for(IPathObstacle o: obstacles)
            if(in(o, p))
                return true;
        return false;
    }
}
