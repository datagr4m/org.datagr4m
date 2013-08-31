package org.datagr4m.maths.geometry.functions;

import java.awt.geom.Point2D;

import org.jzy3d.maths.Coord2d;

/**
 * Defines A and B in: y=A*x+b, given the two input points.
 */
public class LinearFunction {
    public LinearFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public LinearFunction(Point2D p1, Point2D p2) {
        compute(p1,p2);
    }

    public LinearFunction(Coord2d p1, Coord2d p2) {
        compute(p1,p2);
    }
    
    /** 
     * Setup A and B values for this interpolation.
     * @see http://fr.wikipedia.org/wiki/%C3%89quation_de_droite
     */
    public void compute(Point2D p1, Point2D p2){
        a = (p2.getY()-p1.getY())/(p2.getX()-p1.getX());
        b = p1.getY() - a * p1.getX();
    }
    
    public void compute(Coord2d p1, Coord2d p2){
        a = (p2.y-p1.y)/(p2.x-p1.x);
        b = p1.y - a * p1.x;
    }
    
    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double f(double x){
        return a*x+b;
    }
    
    public Point2D at(Point2D source, float dist){
        double n = Math.sqrt((dist*dist)/(a*a+1));
        double x = source.getX()+n;
        double y = f(x);
        return new Point2D.Double(x,y);
    }

    public Point2D at(Coord2d source, float dist){
        double n = Math.sqrt((dist*dist)/(a*a+1));
        double x = source.x;
        if(dist>0)
            x+=n;
        else
            x-=n;
        double y = f(x);
        return new Point2D.Double(x,y);
    }
    
    protected static double DIAG = Math.sqrt(2);
    
    public boolean isVertical(){
        return Double.isInfinite(a);
    }
    
    public boolean isHorizontal(){
        return a==0;
    }
    
    public LinearFunction orthogonal(Point2D p){
        double ao = Double.isInfinite(a)?0:-a;
        double bo = p.getY() - ao * p.getX();
        return new LinearFunction(ao,bo);
    }
    
    public static Point2D getPointOnSegment(Point2D p1, Point2D p2, float distToSrc){
        if(distToSrc<0)
            throw new RuntimeException("not an appropriate value");
        
        float dist = (float)p1.distance(p2);
        
        if(distToSrc>dist || distToSrc<0){
            LinearFunction lf = new LinearFunction(p1, p2);
            return lf.at(p1, distToSrc);
        }
        else{
            float w2 = distToSrc/dist;
            float w1 = 1-w2;
            return new Point2D.Double((p1.getX()*w1+p2.getX()*w2), (p1.getY()*w1+p2.getY()*w2));            
        }
    }
    
    public static Point2D getAveragePointOnSegment(Point2D p1, Point2D p2, float ratioOfp1){
        if(ratioOfp1>1)
            throw new RuntimeException("not an appropriate value:" + ratioOfp1);
        else{
            float w2 = ratioOfp1;
            float w1 = 1-w2;
            return new Point2D.Double((p1.getX()*w1+p2.getX()*w2), (p1.getY()*w1+p2.getY()*w2));            
        }
    }
    
    protected double a;
    protected double b;
}
