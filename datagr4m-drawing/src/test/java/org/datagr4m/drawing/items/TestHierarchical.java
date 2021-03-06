package org.datagr4m.drawing.items;

import junit.framework.TestCase;

import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;


public class TestHierarchical extends TestCase{
    public static float RAD1 = 30;
    public static float RAD2 = 50;
    
    public void testHierarchicalPairs(){
        IBoundedItem left1 = new BoundedItem("left", RAD1);
        IBoundedItem right1 = new BoundedItem("right", RAD1);
        HierarchicalPairModel child1 = new HierarchicalPairModel(left1, right1);
        
        // another way of building pairs
        IBoundedItem left2 = new BoundedItem("left", RAD2);
        IBoundedItem right2 = new BoundedItem("right", RAD2);
        HierarchicalPairModel child2 = new HierarchicalPairModel(left2, right2);        
        HierarchicalPairModel root = new HierarchicalPairModel(child1, child2);
        
        
        assertTrue(left2.getFirstCommonAncestor(right2)==child2);
        assertTrue(left2.getFirstCommonAncestor(child2)==root);
        
        assertTrue(left1.getFirstCommonAncestor(right2)==root);
    }
    
}
