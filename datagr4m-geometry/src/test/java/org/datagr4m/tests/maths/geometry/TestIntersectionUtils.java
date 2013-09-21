package org.datagr4m.tests.maths.geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.datagr4m.maths.geometry.LineUtils;
import org.datagr4m.maths.geometry.PointUtils;


public class TestIntersectionUtils extends TestCase {
    
    public void testCase() {
        Line2D line1 = new Line2D.Double();
        Line2D line2 = new Line2D.Double();
        Point2D pt;

        line1.setLine(-45, -45, 645, -45);//h most top line
        line2.setLine(150, -45, 150, 645);//v vertical line
        // should touch @ 150,-45
        pt = LineUtils.getIntersectionPoint(line1, line2);
        //print(pt);
        
        line1.setLine(-45, 150, 645, 150);//h horizontal line
        line2.setLine(-45, -45, -45, 645);//v most left line
        // should touch @ -45, 150
        pt = LineUtils.getIntersectionPoint(line1, line2);
        assertTrue( PointUtils.almostEqual(pt, new Point2D.Float(-45,150), 0.00001) );
        //print(pt);
    }
    
    

    public void print(Point2D pt) {
        if (pt == null)
            System.out.println("was null");
        else
            System.out.println("x=" + pt.getX() + " y=" + pt.getY());
    }

}
