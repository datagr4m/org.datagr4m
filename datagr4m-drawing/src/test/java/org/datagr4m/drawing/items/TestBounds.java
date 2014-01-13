package org.datagr4m.drawing.items;

import junit.framework.TestCase;

import org.datagr4m.drawing.model.bounds.RectangleBounds;


public class TestBounds extends TestCase{
    public void testContain(){
        RectangleBounds r1 = new RectangleBounds(0,0,100,100);
        
        //test containment
        assertTrue(r1.contains(new RectangleBounds(10,10,50,50)));
        assertTrue(r1.contains(new RectangleBounds(0,0,100,100)));

        assertFalse(new RectangleBounds(10,10,50,50).contains(r1));
        assertFalse(r1.contains(new RectangleBounds(10,10,100,100))); // offset of 10,10 for a 100*100 rectangle
    }
    
    /**FIXME: 
    
    public void testShift(){
        // ensure shifting works as expected
        RectangleBounds r1 = new RectangleBounds(0,0,100,100); // centr� en 50,50

        Coord2d newCorner = new Coord2d(-100,-100);
        assertTrue(r1.shiftTopLeftTo(newCorner).getTopLeftCorner().equals(newCorner));
        assertTrue(r1.shiftTopLeftTo(newCorner).getCenter().equals(newCorner.add(r1.width/2, r1.height/2)));
        Coord2d newCenter = new Coord2d(200,200);
        assertTrue(r1.shiftCenterTo(newCenter).getCenter().equals(newCenter));
    }*/
    
    /**FIXME: 
    public void testBoundsAreSlavedToBoundedItemChangePosition(){
        
        // verify self bounds centered on 0
        Object o1 = "o1";
        Object o2 = "o2";
        IBoundedItem i1 = new DefaultBoundedItem(o1);
        IBoundedItem i2 = new DefaultBoundedItem(o2);

        i1.changePosition(100, 0);
        i2.changePosition(-100, 132);
        
        HierarchicalPairModel pair = new HierarchicalPairModel();
        pair.registerChild(i1.getObject(), i1);
        pair.registerChild(i2.getObject(), i2);
        
        // ensure that the center of any localized bound is at the item position
        assertTrue(i1.getRelativeRectangleBounds().getCenter().equals(i1.getPosition()));
        assertTrue(i2.getRelativeRectangleBounds().getCenter().equals(i2.getPosition()));
        assertTrue(pair.getRelativeRectangleBounds().getCenter().equals(pair.getPosition())); 
    }*/
    
    /**FIXME: 
    public void testNestedBox(){
        //RectangleBounds r1 = new RectangleBounds(50,50,10,10);
        Object o11 = "o11";
        Object o12 = "o12";
        Coord2d c11 = new Coord2d(50,50);
        Coord2d c12 = new Coord2d(50,-50);
        IBoundedItem i11 = new DefaultBoundedItem(o11, c11, 10);
        IBoundedItem i12 = new DefaultBoundedItem(o12, c12, 10);
        
        Coord2d cm1 = new Coord2d(300, 300);
        HierarchicalPairModel m1 = new HierarchicalPairModel(i11, i12);
        m1.getPosition().x = cm1.x;
        m1.getPosition().y = cm1.y;
        
        Object o21 = "o21";
        Object o22 = "o22";
        Coord2d c21 = new Coord2d(-50,50);
        Coord2d c22 = new Coord2d(-50,-50);
        IBoundedItem i21 = new DefaultBoundedItem(o21, c21, 10);
        IBoundedItem i22 = new DefaultBoundedItem(o22, c22, 10);

        Coord2d cm2 = new Coord2d(700, 700);
        HierarchicalPairModel m2 = new HierarchicalPairModel(i21, i22);
        m2.getPosition().x = cm2.x;
        m2.getPosition().y = cm2.y;

        HierarchicalPairModel root = new HierarchicalPairModel(m1, m2);

        
        // verify proper bounds for children
        assertTrue(m1.getFirst().getRelativeRectangleBounds().getCenter().equals(c11));
        assertTrue(m1.getSecond().getRelativeRectangleBounds().getCenter().equals(c12));
        assertTrue(m1.getRelativeRectangleBounds().getCenter().equals(cm1));
        assertTrue(m1.getRawRectangleBounds().getCenter().equals(c11.add(c12).div(2)));

        
        assertTrue(m2.getFirst().getRelativeRectangleBounds().getCenter().equals(c21));
        assertTrue(m2.getSecond().getRelativeRectangleBounds().getCenter().equals(c22));
        assertTrue(m2.getRelativeRectangleBounds().getCenter().equals(cm2));

        // verify proper bounds for parent
    }*/
}
