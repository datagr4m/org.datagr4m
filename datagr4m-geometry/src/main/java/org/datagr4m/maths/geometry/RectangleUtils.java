package org.datagr4m.maths.geometry;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jzy3d.maths.Coord2d;


public class RectangleUtils {
    public static boolean DEFAULT_BORDER_CONSIDERATION = false;
    
    public static Rectangle2D scale(Rectangle2D r, double widthChange, double heightChange){
        return build(r.getCenterX(), r.getCenterY(), r.getWidth()+widthChange, r.getHeight()+heightChange);
    }
    
    public static Rectangle2D build(double centerx, double centery, double width, double height){
        return new Rectangle2D.Double(centerx-width/2, centery-height/2, width, height);
    }
    
    public static Rectangle2D build(Coord2d center, double width, double height){
        return new Rectangle2D.Double(center.x-width/2, center.y-height/2, width, height);
    }
    
    public static Rectangle2D build(Point2D topLeft, Point2D bottomRight){
        return new Rectangle2D.Double(topLeft.getX(), topLeft.getY(), bottomRight.getX()-topLeft.getX(), bottomRight.getY()-topLeft.getY());
    }
    
    public static Point2D topLeft(Rectangle2D rectangle){
        return new Point2D.Double(rectangle.getMinX(), rectangle.getMaxY());
    }

    public static Point2D bottomRight(Rectangle2D rectangle){
        return new Point2D.Double(rectangle.getMaxX(), rectangle.getMinY());
    }
    
    public static Point2D center(Rectangle2D rectangle){
        return new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY());
    }

    
    /** Return true if the interior of r1 intersect interior of r2. */
    public static boolean intersects(Rectangle2D r1, Rectangle2D r2){
        return r1.intersects(r2.getMinX(), r2.getMinY(), r2.getWidth(), r2.getHeight()); 
    }
    
    public static boolean contains(Rectangle2D r, Point2D point){
        return contains(r, point, !DEFAULT_BORDER_CONSIDERATION);
    }
    
    public static boolean contains(Rectangle2D r, Point2D point, boolean ignoreBorder){
        if(ignoreBorder){
            if(point.getX()<=r.getMinX())
                return false;
            else if(point.getX()>=r.getMaxX())
                return false;
            else if(point.getY()<=r.getMinY())
                return false;
            else if(point.getY()>=r.getMaxY())
                return false;
            return true;
        }
        else{
            if(point.getX()<r.getMinX())
                return false;
            else if(point.getX()>r.getMaxX())
                return false;
            else if(point.getY()<r.getMinY())
                return false;
            else if(point.getY()>r.getMaxY())
                return false;
            return true;
        }
    }
    
    public static boolean contains(Rectangle2D r, Coord2d point){
        return contains(r, point, !DEFAULT_BORDER_CONSIDERATION);
    }
    
    public static boolean contains(Rectangle2D r, Coord2d point, boolean ignoreBorder){
        if(ignoreBorder){
            if(point.x<=r.getMinX())
                return false;
            else if(point.x>=r.getMaxX())
                return false;
            else if(point.y<=r.getMinY())
                return false;
            else if(point.y>=r.getMaxY())
                return false;
            return true;
        }
        else{
            if(point.x<r.getMinX())
                return false;
            else if(point.x>r.getMaxX())
                return false;
            else if(point.y<r.getMinY())
                return false;
            else if(point.y>r.getMaxY())
                return false;
            return true;
        }
    }

    public static boolean contains(Point2D rectCenter, float rectWidth, float rectHeight, Point2D point){
        return contains(rectCenter, rectWidth, rectHeight, point.getX(), point.getY(), !DEFAULT_BORDER_CONSIDERATION);
    }
    
    public static boolean contains(Point2D rectCenter, float rectWidth, float rectHeight, Point2D point, boolean ignoreBorder){
        return contains(rectCenter, rectWidth, rectHeight, point.getX(), point.getY(), ignoreBorder);
    }
    
    public static boolean contains(Point2D rectCenter, float rectWidth, float rectHeight, double x, double y, boolean ignoreBorder){
        double rminx = rectCenter.getX() - rectWidth/2;
        double rmaxx = rectCenter.getX() + rectWidth/2;
        double rminy = rectCenter.getY() - rectHeight/2;
        double rmaxy = rectCenter.getY() + rectHeight/2;
        
        if(ignoreBorder){
            if(x<=rminx)
                return false;
            else if(x>=rmaxx)
                return false;
            else if(y<=rminy)
                return false;
            else if(y>=rmaxy)
                return false;
            return true;
        }
        else{
            if(x<rminx)
                return false;
            else if(x>rmaxx)
                return false;
            else if(y<rminy)
                return false;
            else if(y>rmaxy)
                return false;
            return true;
        }
    }
    
    public static boolean contains(Point2D p1, Point2D p2, float width, double x, double y){
        return contains(p1, p2, width, x, y, !DEFAULT_BORDER_CONSIDERATION);
    }
    
    /** Check intersection for a rectangle described as a tube made of two extremities and a width.*/
    public static boolean contains(Point2D p1, Point2D p2, float width, double x, double y, boolean ignoreBorder){
        double rminx = 0;
        double rmaxx = 0;
        double rminy = 0;
        double rmaxy = 0;
        
        if(PointUtils.areHorizontal(p1, p2)){
            rminx = Math.min(p1.getX(), p2.getX());
            rmaxx = Math.max(p1.getX(), p2.getX());
            rminy = p1.getY() - width/2;
            rmaxy = p1.getY() + width/2;
        }
        else if(PointUtils.areVertical(p1, p2)){
            rminx = p1.getX() - width/2;
            rmaxx = p1.getX() + width/2;
            rminy = Math.min(p1.getY(), p2.getY());
            rmaxy = Math.max(p1.getY(), p2.getY());
        }
        else{ 
            return LineUtils.inTube(p1, p2, width, x, y);
        }
        
        if(ignoreBorder){
            if(x<=rminx)
                return false;
            else if(x>=rmaxx)
                return false;
            else if(y<=rminy)
                return false;
            else if(y>=rmaxy)
                return false;
            return true;
        }
        else{
            if(x<rminx)
                return false;
            else if(x>rmaxx)
                return false;
            else if(y<rminy)
                return false;
            else if(y>rmaxy)
                return false;
            return true;
        }
    }
}
