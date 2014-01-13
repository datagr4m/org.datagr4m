package org.datagr4m.drawing.model.pathfinder;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;
import org.datagr4m.maths.geometry.PointUtils;


public class TestPath extends TestCase{
    public void testInsertPointBeforePoint(){
        Point2D p1 = new Point2D.Double(0,0);
        Point2D p2 = new Point2D.Double(100,0);
        IPath path = new LockablePath(p1, p2, true, true);
        
        Point2D p3 = new Point2D.Double(25,00);
        Point2D p4 = new Point2D.Double(25,50);
        Point2D p5 = new Point2D.Double(75,50);
        Point2D p6 = new Point2D.Double(75,00);
        List<Point2D> bypass = PointUtils.list(p3, p4, p5, p6);
        path.insertBefore(p2, bypass, false);
        
        assertTrue(path.getPoint(0).equals(new Point2D.Double(00,00)));
        assertTrue(path.getPoint(1).equals(new Point2D.Double(25,00)));
        assertTrue(path.getPoint(2).equals(new Point2D.Double(25,50)));
        assertTrue(path.getPoint(3).equals(new Point2D.Double(75,50)));
        assertTrue(path.getPoint(4).equals(new Point2D.Double(75,00)));
        assertTrue(path.getPoint(5).equals(new Point2D.Double(100,0)));

        assertTrue(path.getLock(0)==true);
        assertTrue(path.getLock(1)==false);
        assertTrue(path.getLock(2)==false);
        assertTrue(path.getLock(3)==false);
        assertTrue(path.getLock(4)==false);
        assertTrue(path.getLock(5)==true);
    }
    
    public void testReplacePointByList(){
        Point2D p1 = new Point2D.Double(0,0);
        Point2D p2 = new Point2D.Double(100,0);
        IPath path = new LockablePath(p1, p2, true, true);
        
        Point2D p3 = new Point2D.Double(25,00);
        Point2D p4 = new Point2D.Double(25,50);
        List<Point2D> bypass = PointUtils.list(p3, p4);
        path.replace(p2, bypass, false);
        
        // points
        assertTrue(path.getPoint(0).equals(new Point2D.Double(00,00)));
        assertTrue(path.getPoint(1).equals(new Point2D.Double(25,00)));
        assertTrue(path.getPoint(2).equals(new Point2D.Double(25,50)));
        
        // locks
        assertTrue(path.getLock(0)==true);
        assertTrue(path.getLock(1)==false);
        assertTrue(path.getLock(2)==false);
    }
    
    public void testSegments(){
        Point2D p1 = new Point2D.Double(0,0);
        Point2D p2 = new Point2D.Double(100,0);
        IPath path = new LockablePath(p1, p2, true, true);
        
        boolean thrown = false;
        try{
            path.getSegment(1);
        }
        catch(Exception e){
            thrown = true;
        }
        assertTrue(thrown);
        
        Line2D segment = path.getSegment(0);
        assertTrue(segment.getP1().equals(p1));
        assertTrue(segment.getP2().equals(p2));
    }
    
    public void testDelete(){
        IPath path = new LockablePath();
        path.add(new Point2D.Double(0,0), true);
        path.add(new Point2D.Double(1,0), false);
        path.add(new Point2D.Double(2,0), true);
        path.add(new Point2D.Double(3,0), false);
        path.add(new Point2D.Double(4,0), true);
        path.add(new Point2D.Double(5,0), false);
        path.add(new Point2D.Double(6,0), true);
        path.add(new Point2D.Double(7,0), false);
        path.add(new Point2D.Double(8,0), true);
        path.add(new Point2D.Double(9,0), false);
        
        assertTrue(10==path.getPointNumber());
        
        // assert this delete call does not change anything
        int len = path.getPointNumber();
        //path.delete(3, 2);
        //assertTrue(len==path.getPointNumber());

        // assert this delete remove only one element
        path.delete(3, 3); // remove n3
        assertTrue((len-1)==path.getPointNumber());

        
        path.delete(3, 7); // remove n 4 5 6 7 8
        assertTrue((len-(5+1))==path.getPointNumber());

        assertTrue(path.getPoint(0).equals(new Point2D.Double(0,0)));
        assertTrue(path.getPoint(2).equals(new Point2D.Double(2,0)));
        assertTrue(path.getPoint(3).equals(new Point2D.Double(9,0)));
    }
    
    public void testFlip(){
        IPath path = new LockablePath();
        path.add(0, 0, true);
        path.add(1, 1, false);
        path.add(2, 2, true);
        path.add(3, 3, false);
        path.add(4, 4, true);
        
        path.flip(1, 4);
        
        assertTrue(path.getPoint(1).getX()==4);
        assertTrue(path.getPoint(4).getX()==1);
        assertTrue(path.getLock(1)==true);
        assertTrue(path.getLock(4)==false);
    }
    
    public void testEquality(){
        LockablePath path = new LockablePath();
        path.add(0, 0, true);
        path.add(1, 0, true);
        path.add(1, 5, true);
        path.add(3, 5, true);
        path.add(3, 0, true);
        
        IPath identical = new LockablePath(); // the same
        identical.add(0, 0, true);
        identical.add(1, 0, true);
        identical.add(1, 5, true);
        identical.add(3, 5, true);
        identical.add(3, 0, true);

        IPath lockdiff = new LockablePath();
        lockdiff.add(0, 0, true);
        lockdiff.add(1, 0, true);
        lockdiff.add(1, 5, false); // lock difference
        lockdiff.add(3, 5, true);
        lockdiff.add(3, 0, true);
        
        IPath valuediff = new LockablePath(); 
        valuediff.add(0, -1, true); // value difference
        valuediff.add(1, 0, true);
        valuediff.add(1, 5, true);
        valuediff.add(3, 5, true);
        valuediff.add(3, 0, true);
        
        assertTrue(path.equals(identical));
        assertTrue(identical.equals(path));
        assertFalse(path.equals(lockdiff));
        assertFalse(path.equals(valuediff));
        
        List<LockablePath> history = new ArrayList<LockablePath>(4);
        history.add(path);
        assertTrue(history.contains(identical));
        assertFalse(history.contains(lockdiff));
        assertFalse(history.contains(valuediff));
    }
    
    public void testFindSegment(){
        IPath path = new LockablePath();
        path.add(0, 0, true);
        path.add(1, 0, true);
        path.add(2, 0, true);
        path.add(3, 0, true);
        path.add(4, 0, true);
        
        Pair<Point2D,Point2D> s1 = LockablePath.directedSegment(path.getPoint(0), path.getPoint(1));
        assertTrue(path.hasSegment(s1));
        Pair<Point2D,Point2D> s2 = LockablePath.directedSegment(path.getPoint(1), path.getPoint(0));
        assertFalse(path.hasSegment(s2));
        Pair<Point2D,Point2D> s3 = LockablePath.directedSegment(path.getPoint(0), path.getPoint(2));
        assertFalse(path.hasSegment(s3));
        Pair<Point2D,Point2D> s4 = LockablePath.directedSegment(path.getPoint(3), path.getPoint(4));
        assertTrue(path.hasSegment(s4));

    
        Pair<Point2D,Point2D> s5 = LockablePath.undirectedSegment(path.getPoint(0), path.getPoint(1));
        assertTrue(path.hasSegment(s5));
        Pair<Point2D,Point2D> s6 = LockablePath.undirectedSegment(path.getPoint(1), path.getPoint(0));
        assertFalse(path.hasSegment(s6));
        Pair<Point2D,Point2D> s7 = LockablePath.undirectedSegment(path.getPoint(0), path.getPoint(2));
        assertFalse(path.hasSegment(s7));
        Pair<Point2D,Point2D> s8 = LockablePath.undirectedSegment(path.getPoint(3), path.getPoint(4));
        assertTrue(path.hasSegment(s8));

    }
    
    public void testFindSequence(){
        IPath path = new LockablePath();
        path.add(new Point2D.Double(0,0), true);
        path.add(new Point2D.Double(1,0), true);
        path.add(new Point2D.Double(2,0), true);
        path.add(new Point2D.Double(3,0), true);
        path.add(new Point2D.Double(4,0), true);
        path.add(new Point2D.Double(5,0), true);
        path.add(new Point2D.Double(6,0), true);
        path.add(new Point2D.Double(7,0), true);
        path.add(new Point2D.Double(8,0), true);
        path.add(new Point2D.Double(9,0), true);

        List<Point2D> goodSequence1 = new ArrayList<Point2D>();
        goodSequence1.add(new Point2D.Double(2,0));
        goodSequence1.add(new Point2D.Double(3,0));
        goodSequence1.add(new Point2D.Double(4,0));
        goodSequence1.add(new Point2D.Double(5,0));
        assertTrue(path.hasSequence(goodSequence1));
        
        List<Point2D> badSequence1 = new ArrayList<Point2D>();
        badSequence1.add(new Point2D.Double(2,0));
        badSequence1.add(new Point2D.Double(3,0));
        badSequence1.add(new Point2D.Double(4,0));
        badSequence1.add(new Point2D.Double(8,0));
        assertFalse(path.hasSequence(badSequence1));
    }
}
