package org.datagr4m.maths.geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineUtils {
    /** @see http://www.topcoder.com/tc?module=Static&d1=tutorials&d2=geometry2*/
    public static Point2D.Double getIntersectionPoint(Line2D line1, Line2D line2) {
        return getIntersectionPoint(line1, line2, false);
    }   
    
    public static Point2D.Double getIntersectionPoint(Line2D line1, Line2D line2, boolean noEndPoint) {
        if (!line1.intersectsLine(line2))
            return null;
        // pxy = start point line 1
        // rxy = line len line 1
        // qxy = start point line 2
        // sxy = line len line 2
        double px = line1.getX1(), py = line1.getY1(), rx = line1.getX2() - px, ry = line1.getY2() - py;
        double qx = line2.getX1(), qy = line2.getY1(), sx = line2.getX2() - qx, sy = line2.getY2() - qy;
        double det = sx * ry - sy * rx;
        
        if (det == 0) {
            return null;
        } else {
            double z = (sx * (qy - py) + sy * (px - qx)) / det;
            if( noEndPoint )
                if (z == 0 || z == 1)
                    return null; // intersection at end point!
            return new Point2D.Double( (px + z * rx), (py + z * ry) );
        }
    }
    
    public static double getDecimalPrecision(double number, int precision) {
        int multi = (int)Math.pow(10, precision);
        return (((int)(number * multi)) / (multi * 1.0));
      }
    
    /****************************/

    /**
     * @return 0 if no touch
     * @return 1 if touch by 1 point
     * @return 2 if touch by 2 points
     * @return 3 if first is a point line touching second real line
     * @return 4 if first is a point line touching another point line
     */
    public static int justTouch(Line2D line1, Line2D line2){
        int rank = 0;
        if( overlap(line1.getX1(), line1.getY1(), line2.getX1(), line2.getY1()) ) // start touch start
            rank++;
        if( overlap(line1.getX1(), line1.getY1(), line2.getX2(), line2.getY2()) ) // start touch end
            rank++;
        if( overlap(line1.getX2(), line1.getY2(), line2.getX1(), line2.getY1()) ) // end touch start
            rank++;
        if( overlap(line1.getX2(), line1.getY2(), line2.getX2(), line2.getY2()) ) // end touch end
            rank++;
        return rank;
    }
    
    public static boolean overlap(double x1, double y1, double x2, double y2){
        return (x1==x2 && y1==y2);
    }
    
    /********************/
    
    public static boolean inTube(Point2D p1, Point2D p2, float width, double x, double y){
        Line2D line = new Line2D.Double(p1, p2);
        double dist = line.ptLineDistSq(x, y);
        if(dist<((width*width)/2)){
            // ensure also stand between the two points
            double rminx = Math.min(p1.getX(), p2.getX());
            double rmaxx = Math.max(p1.getX(), p2.getX());
            double rminy = Math.min(p1.getY(), p2.getY());
            double rmaxy = Math.max(p1.getY(), p2.getY());
            
            if((rminx<=x && x<=rmaxx) && (rminy<=y && y<=rmaxy))
                return true;
            else
                return false;
        }
        else
            return false;
    }
    
    public static String toString(Line2D line){
        return "(" + line.getX1() + ", " + line.getY1() + ") ("+ line.getX2() + ", " + line.getY2() + ")";
    }
}
