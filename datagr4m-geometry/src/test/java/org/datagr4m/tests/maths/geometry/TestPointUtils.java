package org.datagr4m.tests.maths.geometry;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.datagr4m.maths.geometry.PointUtils;


public class TestPointUtils extends TestCase{
    public void testIt(){
        Point2D source = new Point2D.Double(10,10);
        for (double i = 0; i < Math.PI*2; i+=0.1) {
            double b = 10;
            Point2D target = new Point2D.Double(source.getX()+Math.cos(i)*b, source.getY()+Math.sin(i)*b);
            double angle = PointUtils.angle(source, target);
            System.out.println(angle);
        }
    }
}
