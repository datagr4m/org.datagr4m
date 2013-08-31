package org.datagr4m.tests.drawing.items;

import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;

import junit.framework.TestCase;


public class TestHierarchicalPair extends TestCase{
    public static float RAD1 = 30;
    public static float RAD2 = 50;
    
    public void testHierarchicalPairs(){
        IBoundedItem left1 = new DefaultBoundedItem("left", RAD1);
        IBoundedItem right1 = new DefaultBoundedItem("right", RAD1);
        HierarchicalPairModel child1 = new HierarchicalPairModel(left1, right1);
        
        // another way of building pairs
        IBoundedItem left2 = new DefaultBoundedItem("left", RAD2);
        IBoundedItem right2 = new DefaultBoundedItem("right", RAD2);
        HierarchicalPairModel child2 = new HierarchicalPairModel(left2, right2);        
        HierarchicalPairModel root = new HierarchicalPairModel(child1, child2);
        
        // verify hierarchy
        assertTrue(root.hasChild(child1));
        assertTrue(root.hasChild(child2));
        assertTrue(child2.hasChild(left2));
        assertTrue(child2.hasChild(right2));
        assertTrue(child1.hasChild(left1));
        assertTrue(child1.hasChild(right1));
        assertFalse(child2.hasChild(left1));
        assertFalse(child2.hasChild(right1));
        
        assertTrue(root.hasDescendant(child2));
        assertTrue(root.hasDescendant(left2));
        assertFalse(child2.hasDescendant(left1));
        
        assertTrue(child1.getRoot()==root); // appel a get root a eu lieu avant (construction topologie)
        assertTrue(child2.getRoot()==root);
        
        // verify neighbourhood
        //assertTrue(child1.hasNeighbour(child2));
        //assertTrue(child2.hasNeighbour(child1));
        
        // verify depth
        assertTrue(root.getDepth(root)==0);
        assertTrue(root.getDepth(child1)==1);
        assertTrue(root.getDepth(child2)==1);
        assertTrue(root.getDepth(left1)==2);
        assertTrue(root.getDepth(right2)==2);
        
        // verify spacing
        left1.getPosition().x = -50;
        right1.getPosition().x = 50;
        assertTrue(child1.getSpacing()==100);
        
        left2.getPosition().x = -50;
        right2.getPosition().x = 50;
        assertTrue(child2.getSpacing()==100);
        
        assertTrue(root.getSpacing()==0);
        child1.getPosition().x = -100;
        child2.getPosition().x =  100;
        assertTrue(root.getSpacing()==200);
        
        // verify position
        assertTrue(root.getAbsolutePosition(left1).x  == -150);
        assertTrue(root.getAbsolutePosition(right1).x ==  -50);
        assertTrue(root.getAbsolutePosition(right2).x ==  150);
        assertTrue(root.getAbsolutePosition(left2).x  ==   50);
        
     // recursive update
        root.refreshBounds(true);
        
        // verify few item bounds
        System.out.println(left1.getRawRectangleBounds().width);
        
        // LA TAILLE DEPEND DU TEXTE MAINTENANT!!
        /*assertTrue(left1.getRectangleBounds().width == RAD1*2);// item radius
        assertTrue(left1.getRectangleBounds().height == RAD1*2);
        assertTrue(right2.getRectangleBounds().width == RAD2*2);
        assertTrue(right2.getRectangleBounds().height == RAD2*2);*/
        
        
        // du coup le reste est faux!!
        /*assertTrue(child1.getRectangleBounds().width == RAD1*2 + child1.getSpacing());
        assertTrue(child2.getRectangleBounds().width == RAD2*2 + child2.getSpacing());
        assertTrue(root.getRectangleBounds().width == (child1.getRectangleBounds().width/2
                                                      +child2.getRectangleBounds().width/2
                                                      +root.getSpacing()));*/
        
        
    }
    
    public void testHierarchicalPairs2(){
        String left = "left";
        String right = "right";
        IBoundedItem left1 = new DefaultBoundedItem(left);
        IBoundedItem right1 = new DefaultBoundedItem(right);
        HierarchicalPairModel child1 = new HierarchicalPairModel();
        child1.registerChild(left, left1);
        child1.registerChild(right, right1);
        
        assertTrue(child1.hasChild(left1));
        assertTrue(child1.hasChild(right1));
        assertTrue(child1.getFirst()==left1);
        assertTrue(child1.getSecond()==right1);
    }
}
