package org.datagr4m.maths.geometry;

import java.awt.geom.Point2D;

import org.jzy3d.maths.Coord2d;

public class Pt {
	public static Point2D cloneAsFloatPoint(Coord2d c) {
		return new Point2D.Float(c.x, c.y);
	}
	public static Point2D cloneAsDoublePoint(Coord2d c) {
		return new Point2D.Double(c.x, c.y);
	}
	public static Coord2d cloneAsCoord2d(Point2D p){
		return new Coord2d(p.getX(), p.getY());
	}	
	public static Point2D add(Point2D c1, Point2D c2) {
		c1.setLocation(c1.getX()+c2.getX(), c1.getY()+c2.getY());
		return c1;
	}
}
