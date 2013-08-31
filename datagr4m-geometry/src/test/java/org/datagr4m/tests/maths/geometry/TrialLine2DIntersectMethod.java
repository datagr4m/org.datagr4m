package org.datagr4m.tests.maths.geometry;

import java.awt.geom.Line2D;

public class TrialLine2DIntersectMethod {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Line2D line = new Line2D.Double(0,0,1,1); //  /
        Line2D line2 = new Line2D.Double(0,0,1,1);
        Line2D line3 = new Line2D.Double(0,1,1,0); // \
        Line2D line4 = new Line2D.Double(0,3,10,3); // \
        Line2D line5 = new Line2D.Double(0,0,-1,-1); // \
        Line2D line6 = new Line2D.Double(-1,-1,2,2); 
        System.out.println("line2="+line.intersectsLine(line2));
        System.out.println("line3="+line.intersectsLine(line3));
        System.out.println("line4="+line.intersectsLine(line4));
        System.out.println("line5="+line.intersectsLine(line5));
        System.out.println("line6="+line.intersectsLine(line6));
        
    }

}
