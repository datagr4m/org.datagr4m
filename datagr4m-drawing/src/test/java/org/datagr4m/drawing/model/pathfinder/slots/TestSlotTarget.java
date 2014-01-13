package org.datagr4m.drawing.model.pathfinder.slots;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.drawing.model.links.UniqueLink;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.drawing.model.slots.SlotTarget;


public class TestSlotTarget extends TestCase{
    public void testSlotEquality(){
        IPathObstacle o1 = new PathObstacle("obs1", new Rectangle2D.Double(0,0,0,0), 20);
        IPathObstacle o2 = o1;
        
        assertTrue(o1.equals(o2));
        
        DirectedLink link1 = new DirectedLink(o1, o2);
        DirectedLink link2 = new DirectedLink(o1, o2);
        assertTrue(link1.equals(link2));
        SlotTarget s11 = new SlotTarget(o1, link1);
        SlotTarget s12 = new SlotTarget(o1, link2);
        assertTrue(s11.equals(s12));
        
        UniqueLink ulink1 = new UniqueLink(o1, o2);
        UniqueLink ulink2 = new UniqueLink(o1, o2);
        assertFalse(ulink1.equals(ulink2));
        SlotTarget s21 = new SlotTarget(o1, ulink1);
        SlotTarget s22 = new SlotTarget(o1, ulink2);
        assertFalse(s21.equals(s22));
    }
}
