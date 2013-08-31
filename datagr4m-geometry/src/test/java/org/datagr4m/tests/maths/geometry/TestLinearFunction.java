package org.datagr4m.tests.maths.geometry;

import java.awt.geom.Point2D;

import org.datagr4m.maths.geometry.functions.LinearFunction;

import junit.framework.TestCase;


public class TestLinearFunction extends TestCase{
    public void testInterpo(){
        Point2D p1 = new Point2D.Double(0,0);
        Point2D p2 = new Point2D.Double(1,1);
        LinearFunction li = new LinearFunction(p1,p2);
        assertTrue(0.5==li.f(0.5));
    }

    public void testInterpo2(){
        Point2D p1 = new Point2D.Double(2,2);
        Point2D p2 = new Point2D.Double(1,1);
        LinearFunction li = new LinearFunction(p1,p2);
        assertTrue(0.5==li.f(0.5));
    }
    public void testInterpoOrthogonal(){
        Point2D p1 = new Point2D.Double(2,2);
        Point2D p2 = new Point2D.Double(1,1);
        LinearFunction li = new LinearFunction(p1,p2).orthogonal(p2);
        //System.out.println(li.f(0.5));
        assertTrue(1.5==li.f(0.5));
    }
    public void testInterpoOrthogonal2(){
        Point2D p1 = new Point2D.Double(0,0);
        Point2D p2 = new Point2D.Double(0,1);
        LinearFunction li1 = new LinearFunction(p1,p2);
        assertTrue(li1.isVertical());
        assertTrue(Double.isNaN(li1.f(1)));
        
        Point2D p3 = new Point2D.Double(1,3);
        Point2D p4 = new Point2D.Double(2,3);
        LinearFunction li2 = new LinearFunction(p3,p4);
        assertTrue(li2.isHorizontal());
        assertTrue(li2.f(1000)==3);
        
        LinearFunction li3 = li1.orthogonal(p2);
        assertTrue(li3.isHorizontal());
        assertTrue(li3.f(1000)==1);
    }
    
    public void testInterpoOrthogonal3(){
        Point2D p1 = new Point2D.Double(2,2);
        Point2D p2 = new Point2D.Double(1,1);
        LinearFunction li = new LinearFunction(p1,p2).orthogonal(p2);
        //System.out.println(li.f(0.5));
        assertTrue(1.5==li.f(0.5));
    }
    
    public void testInterpoAt(){
        Point2D p1 = new Point2D.Double(0,2);
        Point2D p2 = new Point2D.Double(2,2);
        LinearFunction li = new LinearFunction(p1,p2);
        //System.out.println(li.f(0.5));
        float expected = 10;
        Point2D p3 = li.at(p1,expected);
        //System.out.println(p3);
        //System.out.println("final dist"+p3.distance(p1));
        assertTrue(p3.distance(p1)==expected);
    }
}
