package org.datagr4m.drawing.model.pathfinder.impl;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import junit.framework.TestCase;

import org.datagr4m.drawing.layout.pathfinder.impl.PathFinder;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;


public class TestPathFinderPrimitives extends TestCase{
    public void testHit(){
        PathFinder pf = new PathFinder();
        IPathObstacle o = new PathObstacle("test", 0, 0, 100, 100, 20);
        
        // traverse de part en part
        assertTrue(pf.hit(o, new Line2D.Double(-100,0,100,0)));
        
        // confondu avec un bord
        Rectangle2D r = o.getBypassedBounds();
        Line2D line = new Line2D.Double(r.getMinX(),r.getMaxY(),r.getMaxX(),r.getMaxY());
        assertFalse(pf.hit(o, line, true)); // do not consider border
        assertFalse(pf.hit(o, line)); // equivalent: do not consider border
        assertTrue(pf.hit(o, line, false)); // consider border
        
        // un point � l'interieur
        assertTrue(pf.hit(o, new Point2D.Double(0,0)));
        
        // un point sur le bord
        assertFalse(pf.hit(o, new Point2D.Double(r.getMinX(),r.getMaxY()), true));
        assertFalse(pf.hit(o, new Point2D.Double(r.getMinX(),r.getMaxY())));
        assertTrue(pf.hit(o, new Point2D.Double(r.getMinX(),r.getMaxY()), false));
        
        // premier point dehors, deuxi�me dedans
        assertFalse(pf.hitP1(o, new Line2D.Double(-100,0,0,0)));
        assertTrue(pf.hitP2(o, new Line2D.Double(-100,0,0,0)));
        
        // premier point dedans, deuxi�me dehors
        assertTrue(pf.hitP1(o, new Line2D.Double(0,0,100,0)));
        assertFalse(pf.hitP2(o, new Line2D.Double(0,0,100,0)));
    }
    
    public void testOrdering(){
        PathFinder pf = new PathFinder();
        assertTrue(pf.isLeftToRight(new Point2D.Double(0,0), new Point2D.Double(1,0)));
        assertFalse(pf.isLeftToRight(new Point2D.Double(1,0), new Point2D.Double(0,0)));
        assertTrue(pf.isBottomToTop(new Point2D.Double(0,0), new Point2D.Double(0,1)));
        assertFalse(pf.isBottomToTop(new Point2D.Double(0,1), new Point2D.Double(0,0)));
    }
    
    public void testVerifs(){
        PathFinder pf = new PathFinder();
        assertTrue(pf.areDefined(new Point2D.Double(0,0), new Point2D.Double(1,0)));
        assertFalse(pf.areDefined(new Point2D.Double(0,0), null));
        assertFalse(pf.areDefined(null, new Point2D.Double(1,0)));
        assertFalse(pf.areDefined(null, null));
    }
    
    public void testBypassStraight(){
        PathFinder pf = new PathFinder();
        Rectangle2D r = new Rectangle2D.Double(-50,-50,100,100);
        
        Point2D top    = new Point2D.Double(0,200);
        Point2D bottom = new Point2D.Double(0,-200);
        Point2D left   = new Point2D.Double(-200,0);
        Point2D right  = new Point2D.Double(200,0);
        
        // intersection points
        Point2D topI    = new Point2D.Double(0,50);
        Point2D bottomI = new Point2D.Double(0,-50);
        Point2D leftI   = new Point2D.Double(-50,0);
        Point2D rightI  = new Point2D.Double(50,0);
        
        // corners
        Point2D topLeft = new Point2D.Double(-50,50);
        Point2D topRight = new Point2D.Double(50,50);
        Point2D bottomLeft = new Point2D.Double(-50,-50);
        Point2D bottomRight = new Point2D.Double(50,-50);

        // traverse de haut en bas
        List<Point2D> bp1 = pf.bypassStraight(r, top, bottom);
        assertTrue(bp1.get(0).equals(topI));
        assertTrue(bp1.get(1).equals(topLeft));
        assertTrue(bp1.get(2).equals(bottomLeft));
        assertTrue(bp1.get(3).equals(bottomI));
        
        // traverse de bas en haut
        List<Point2D> bp2 = pf.bypassStraight(r, bottom, top);
        assertTrue(bp2.get(0).equals(bottomI));
        assertTrue(bp2.get(1).equals(bottomLeft));
        assertTrue(bp2.get(2).equals(topLeft));
        assertTrue(bp2.get(3).equals(topI));
        
        // traverse de gauche � droite
        List<Point2D> bp3 = pf.bypassStraight(r, left, right);
        assertTrue(bp3.get(0).equals(leftI));
        assertTrue(bp3.get(1).equals(topLeft));
        assertTrue(bp3.get(2).equals(topRight));
        assertTrue(bp3.get(3).equals(rightI));
        
        // traverse de droite � gauche
        List<Point2D> bp4 = pf.bypassStraight(r, right, left);
        assertTrue(bp4.get(0).equals(rightI));
        assertTrue(bp4.get(1).equals(topRight));
        assertTrue(bp4.get(2).equals(topLeft));
        assertTrue(bp4.get(3).equals(leftI));
    }
    
    public void testBypassStraight2(){
        PathFinder pf = new PathFinder();
        Rectangle2D r = new Rectangle2D.Double(-50,-50,100,100);
        
        Point2D top    = new Point2D.Double(0,200);
        Point2D bottom = new Point2D.Double(0,-200);
        Point2D left   = new Point2D.Double(-200,0);
        Point2D right  = new Point2D.Double(200,0);
        
        // intersection points
        Point2D topI    = new Point2D.Double(0,50);
        Point2D bottomI = new Point2D.Double(0,-50);
        Point2D leftI   = new Point2D.Double(-50,0);
        Point2D rightI  = new Point2D.Double(50,0);
        
        // corners
        Point2D topLeft = new Point2D.Double(-50,50);
        Point2D topRight = new Point2D.Double(50,50);
        Point2D bottomLeft = new Point2D.Double(-50,-50);
        Point2D bottomRight = new Point2D.Double(50,-50);

        // traverse de haut en bas
        List<Point2D> bp1 = pf.bypassStraight2(r, top, bottom);
        assertTrue(bp1.get(0).equals(topI));
        assertTrue(bp1.get(1).equals(topLeft));
        assertTrue(bp1.get(2).equals(bottomLeft));
        assertTrue(bp1.get(3).equals(bottomI));
        
        // traverse de bas en haut
        List<Point2D> bp2 = pf.bypassStraight2(r, bottom, top);
        assertTrue(bp2.get(0).equals(bottomI));
        assertTrue(bp2.get(1).equals(bottomLeft));
        assertTrue(bp2.get(2).equals(topLeft));
        assertTrue(bp2.get(3).equals(topI));
        
        // traverse de gauche � droite
        List<Point2D> bp3 = pf.bypassStraight2(r, left, right);
        assertTrue(bp3.get(0).equals(leftI));
        assertTrue(bp3.get(1).equals(topLeft));
        assertTrue(bp3.get(2).equals(topRight));
        assertTrue(bp3.get(3).equals(rightI));
        
        // traverse de droite � gauche
        List<Point2D> bp4 = pf.bypassStraight2(r, right, left);
        assertTrue(bp4.get(0).equals(rightI));
        assertTrue(bp4.get(1).equals(topRight));
        assertTrue(bp4.get(2).equals(topLeft));
        assertTrue(bp4.get(3).equals(leftI));
    }
    
    public void testBend(){
        PathFinder pf = new PathFinder();
        Rectangle2D r = new Rectangle2D.Double(-50,-50,100,100);
        
        Point2D center = new Point2D.Double(0,0);
        Point2D top    = new Point2D.Double(0,200);
        Point2D bottom = new Point2D.Double(0,-200);
        Point2D left   = new Point2D.Double(-200,0);
        Point2D right  = new Point2D.Double(200,0);
        
        // intersection points
        Point2D topI    = new Point2D.Double(0,50);
        Point2D bottomI = new Point2D.Double(0,-50);
        Point2D leftI   = new Point2D.Double(-50,0);
        Point2D rightI  = new Point2D.Double(50,0);
        
        // corners
        Point2D topLeft = new Point2D.Double(-50,50);
        Point2D topRight = new Point2D.Double(50,50);
        Point2D bottomLeft = new Point2D.Double(-50,-50);
        Point2D bottomRight = new Point2D.Double(50,-50);
        
        // traverse de haut vers droite
        List<Point2D> bp1 = pf.bypassBend(r, top, center, right);
        assertTrue(bp1.get(0).equals(topI));
        assertTrue(bp1.get(1).equals(topRight));
        assertTrue(bp1.get(2).equals(rightI));
        // inverse
        List<Point2D> bp2 = pf.bypassBend(r, right, center, top);
        assertTrue(bp2.get(0).equals(rightI));
        assertTrue(bp2.get(1).equals(topRight));
        assertTrue(bp2.get(2).equals(topI));
        
        // traverse de haut vers gauche
        List<Point2D> bp3 = pf.bypassBend(r, top, center, left);
        assertTrue(bp3.get(0).equals(topI));
        assertTrue(bp3.get(1).equals(topLeft));
        assertTrue(bp3.get(2).equals(leftI));
        // inverse
        List<Point2D> bp4 = pf.bypassBend(r, left, center, top);
        assertTrue(bp4.get(0).equals(leftI));
        assertTrue(bp4.get(1).equals(topLeft));
        assertTrue(bp4.get(2).equals(topI));
        
        // traverse de bas vers droite
        List<Point2D> bp5 = pf.bypassBend(r, bottom, center, right);
        assertTrue(bp5.get(0).equals(bottomI));
        assertTrue(bp5.get(1).equals(bottomRight));
        assertTrue(bp5.get(2).equals(rightI));
        // inverse
        List<Point2D> bp6 = pf.bypassBend(r, right, center, bottom);
        assertTrue(bp6.get(0).equals(rightI));
        assertTrue(bp6.get(1).equals(bottomRight));
        assertTrue(bp6.get(2).equals(bottomI));
        
        // traverse de bas vers gauche
        List<Point2D> bp7 = pf.bypassBend(r, bottom, center, left);
        assertTrue(bp7.get(0).equals(bottomI));
        assertTrue(bp7.get(1).equals(bottomLeft));
        assertTrue(bp7.get(2).equals(leftI));
        // inverse
        List<Point2D> bp8 = pf.bypassBend(r, left, center, bottom);
        assertTrue(bp8.get(0).equals(leftI));
        assertTrue(bp8.get(1).equals(bottomLeft));
        assertTrue(bp8.get(2).equals(bottomI));
    }
    
    
    public void testAnalysis(){
        PathFinder pf = new PathFinder();
        Rectangle2D r = new Rectangle2D.Double(-50,-50,100,100);
        
        Point2D top    = new Point2D.Double(0,200);
        Point2D bottom = new Point2D.Double(0,-200);
        Point2D left   = new Point2D.Double(-200,0);
        Point2D right  = new Point2D.Double(200,0);
        
        // top to bottom
        assertTrue(pf.isAtEachOppositeHorizontalSide(top, r, bottom));
        assertFalse(pf.isAtEachOppositeVerticalSide(top, r, bottom));
        assertFalse(pf.isJointSide(top, r, bottom));
        assertFalse(pf.isBothInside(top, r, bottom));
        assertFalse(pf.isDiagonal(top, r, bottom));
        
        // bottom to top
        assertTrue(pf.isAtEachOppositeHorizontalSide(bottom, r, top));
        assertFalse(pf.isAtEachOppositeVerticalSide(bottom, r, top));
        assertFalse(pf.isJointSide(bottom, r, top));
        assertFalse(pf.isBothInside(bottom, r, top));
        assertFalse(pf.isDiagonal(bottom, r, top));
        
        // left to right
        assertTrue(pf.isAtEachOppositeVerticalSide(left, r, right));
        assertFalse(pf.isAtEachOppositeHorizontalSide(left, r, right));
        assertFalse(pf.isJointSide(left, r, right));
        assertFalse(pf.isBothInside(left, r, right));
        assertFalse(pf.isDiagonal(left, r, right));
        
        // right to left
        assertTrue(pf.isAtEachOppositeVerticalSide(right, r, left));
        assertFalse(pf.isAtEachOppositeHorizontalSide(right, r, left));
        assertFalse(pf.isJointSide(right, r, left));
        assertFalse(pf.isBothInside(right, r, left));
        assertFalse(pf.isDiagonal(right, r, left));

        // ------------
        // top & left
        assertTrue(pf.isJointSide(top, r, left));
        assertFalse(pf.isAtEachOppositeSide(top, r, left));
        assertFalse(pf.isDiagonal(top, r, left));
        
        // left & top
        assertTrue(pf.isJointSide(left, r, top));
        assertFalse(pf.isAtEachOppositeSide(left, r, top));
        assertFalse(pf.isDiagonal(left, r, top));
        
        // top & right
        assertTrue(pf.isJointSide(top, r, right));
        assertFalse(pf.isAtEachOppositeSide(top, r, right));
        assertFalse(pf.isDiagonal(top, r, right));
        
        // right & top
        assertTrue(pf.isJointSide(right, r, top));
        assertFalse(pf.isAtEachOppositeSide(right, r, top));
        assertFalse(pf.isDiagonal(right, r, top));
        
        // ------------
        // bottom & left
        assertTrue(pf.isJointSide(bottom, r, left));
        assertFalse(pf.isAtEachOppositeSide(bottom, r, left));
        assertFalse(pf.isDiagonal(bottom, r, left));
        
        // left & bottom
        assertTrue(pf.isJointSide(left, r, bottom));
        assertFalse(pf.isAtEachOppositeSide(left, r, bottom));
        assertFalse(pf.isDiagonal(left, r, bottom));
        
        // bottom & right
        assertTrue(pf.isJointSide(bottom, r, right));
        assertFalse(pf.isAtEachOppositeSide(bottom, r, right));
        assertFalse(pf.isDiagonal(bottom, r, right));
        
        // right & bottom
        assertTrue(pf.isJointSide(right, r, bottom));
        assertFalse(pf.isAtEachOppositeSide(right, r, bottom));
        assertFalse(pf.isDiagonal(right, r, bottom));
    }
}
