package org.datagr4m.maths.geometry;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.NoSuchElementException;

public class ShapeUtils {
    public static Point2D getFirstIntersection(Shape shape, Line2D line) {
        PathIterator path = shape.getPathIterator(null);
        Point2D p1 = new Point2D.Double();
        Point2D p2 = new Point2D.Double();
        double[] buffer = new double[2];
        
        // read first segment
        path.currentSegment(buffer);
        p1 = new Point2D.Double(buffer[0], buffer[1]);
        path.next();
        path.currentSegment(buffer);
        p2 = new Point2D.Double(buffer[0], buffer[1]);
        
        // check all segments
        while (!path.isDone()) {               
            Line2D pathSegment = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            
            // check intersection
            Point2D intersection = LineUtils.getIntersectionPoint(line, pathSegment);
            if(intersection!=null)
                return intersection;
            
            // or continue with next segment
            else {
                try{
                    p1 = new Point2D.Double(buffer[0], buffer[1]);
                    path.next();
                    path.currentSegment(buffer);
                    p2 = new Point2D.Double(buffer[0], buffer[1]);
                }
                catch(NoSuchElementException e){
                    return null;
                }
            }
        }
        return null;
    }
}
