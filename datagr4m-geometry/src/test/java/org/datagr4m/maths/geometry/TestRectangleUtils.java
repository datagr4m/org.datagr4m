package org.datagr4m.maths.geometry;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

import org.datagr4m.maths.geometry.RectangleUtils;


public class TestRectangleUtils extends TestCase{
    public void testScale(){
        Rectangle2D r1 = RectangleUtils.build(0, 0, 100, 100);
        assertTrue(r1.getWidth()==100);
        Rectangle2D r2 = RectangleUtils.scale(r1, -2, -2);
        assertTrue(r2.getWidth()==98);
    }
    
    public void testIntersect(){
        Rectangle2D r1 = RectangleUtils.build(0, 0, 100, 100);
        Rectangle2D r2 = RectangleUtils.build(100, 100, 100, 100);
        Rectangle2D r3 = RectangleUtils.build(99, 99, 100, 100);
        assertTrue(RectangleUtils.intersects(r1, r3));
        assertFalse(RectangleUtils.intersects(r1, r2));
    }
}
