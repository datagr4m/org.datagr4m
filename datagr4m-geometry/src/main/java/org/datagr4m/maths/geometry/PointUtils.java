package org.datagr4m.maths.geometry;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class PointUtils {
    public static boolean almostEqual(Point2D pt, Point2D expected, double limit){
        return Math.abs(pt.getX()-expected.getX())<limit && Math.abs(pt.getY()-expected.getY())<limit;
    }
    public static List<Point2D> list(Point2D coord) {
        List<Point2D> list = new ArrayList<Point2D>(1);
        list.add(coord);
        return list;
    }
    
    public static List<Point2D> list(Point2D c1, Point2D c2) {
        List<Point2D> list = new ArrayList<Point2D>(2);
        list.add(c1);
        list.add(c2);
        return list;
    }
    
    public static List<Point2D> list(Point2D c1, Point2D c2, Point2D c3) {
        List<Point2D> list = new ArrayList<Point2D>(3);
        list.add(c1);
        list.add(c2);
        list.add(c3);
        return list;
    }

    public static List<Point2D> list(Point2D c1, Point2D c2, Point2D c3, Point2D c4) {
        List<Point2D> list = new ArrayList<Point2D>(4);
        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        return list;
    }
    
    public static boolean areHorizontal(Point2D p1, Point2D p2){
        return p1.getY()==p2.getY();
    }
    
    public static boolean isLeftTo(Point2D p1, Point2D p2){
        return p1.getX()<=p2.getX();
    }
    
    public static boolean areVertical(Point2D p1, Point2D p2){
        return p1.getX()==p2.getX();
    }
    
    public static double distance(Point2D p1, Point2D p2){
        return Math.sqrt(Math.pow(p2.getX()-p1.getX(),2)+Math.pow(p2.getY()-p1.getY(),2));
    }
    
    /*******************/
    
    // angle formé par axe horizontal passant par p1, et axe passant par point p1 et point p2
    public static double angle(Point2D p1, Point2D p2){
        return polar(p2.getX()-p1.getX(), p2.getY()-p1.getY());
    }
    
    public static Point2D sub(Point2D p1, Point2D p2){
        return new Point2D.Double(p1.getX()-p2.getX(), p1.getY()-p2.getY());
    }
    
    public static Point2D mean(Point2D p1, Point2D p2){
        return new Point2D.Double((p1.getX()+p2.getX())/2, (p1.getY()+p2.getY())/2);
    }
    
    public static Point2D add(Point2D p1, Point2D p2){
        return new Point2D.Double(p1.getX()+p2.getX(), p1.getY()+p2.getY());
    }
    
    /*******************/
    
    public static Point2D cartesian(Point2D p){
        return cartesian(p.getX(), p.getY());
    }
    
    public static Point2D cartesian(double x, double y){
        return new Point2D.Double(
                Math.cos(x) * y,
                Math.sin(x) * y);
    }

    /**
     * Return a real polar value, with an angle in the range [0;2*PI]
     * x is alpha, y is radius
     * 
     * http://fr.wikipedia.org/wiki/Coordonn%C3%A9es_polaires
     */
    public static Point2D polar(Point2D p){
        double radius = Math.sqrt(p.getX()*p.getX() + p.getY()*p.getY());

        if(p.getX()<0){
            return new Point2D.Double(Math.atan(p.getY()/p.getX())+Math.PI, radius);
        }
        else if(p.getX()>0){
            if(p.getY()>=0)
                return new Point2D.Double(Math.atan(p.getY()/p.getX()), radius);
            else
                return new Point2D.Double(Math.atan(p.getY()/p.getX())+2*Math.PI, radius);
        }
        else{ // p.getX()==0
            if(p.getY()>0)
                return new Point2D.Double(Math.PI/2,radius);
            else if(p.getY()<0)
                return new Point2D.Double(3*Math.PI/2,radius);
            else // p.getY()==0
                return new Point2D.Double(0,0);
        }
    }
    
    public static double polar(double x, double y){
        if(x<0){
            return Math.atan(y/x)+Math.PI;
        }
        else if(x>0){
            if(y>=0)
                return Math.atan(y/x);
            else
                return Math.atan(y/x)+2*Math.PI;
        }
        else{ // x==0
            if(y>0)
                return Math.PI/2;
            else if(y<0)
                return 3*Math.PI/2;
            else // y==0
                return 0;
        }
    }
}
