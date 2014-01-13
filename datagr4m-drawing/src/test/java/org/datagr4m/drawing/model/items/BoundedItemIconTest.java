package org.datagr4m.drawing.model.items;

import javax.swing.Icon;

import org.junit.Assert;
import org.junit.Test;

public class BoundedItemIconTest {
    
    /*@Test
    public void testDefaultItemGetSet(){
        IBoundedItem item = new BoundedItemIcon("router", IconSet.ROUTER);
        Assert.assertTrue("has correct label", item.getLabel().equals("router"));
        Assert.assertTrue(item.getRadialBounds()==30);
        Assert.assertTrue(item.getRadialBounds(0)==30);
        Assert.assertTrue(item.getRadialBounds(Math.PI)==30);
        
        // verify statefull attributes
        Assert.assertTrue(item.locked()==false);
        item.lock();
        Assert.assertTrue(item.locked()==true);
        item.unlock();
        Assert.assertTrue(item.locked()==false);
        
        Assert.assertTrue(item.getState().isNone());
        item.setState(ItemState.selected());
        Assert.assertTrue(item.getState().isSelected());
        item.setState(ItemState.mouseover());
        Assert.assertTrue(item.getState().isMouseOver());
    }*/
    
    @Test
    public void testIconItemSize(){
        Icon icon = IconSet.ROUTER;
        int expectedWidth = 151; 
        int expectedHeight = 89; 
        
        // ensure icon has the expected dimensions
        Assert.assertEquals(icon.getIconWidth(), expectedWidth);
        Assert.assertEquals(icon.getIconHeight(), expectedHeight);
        
        // ensure bounds consider icon dimensions
        IBoundedItemIcon item = new BoundedItemIcon("node", icon);
        Assert.assertTrue(item.getIcon()==icon);
        Assert.assertEquals(expectedWidth, item.getRawRectangleBounds().width, 0.001);
        //Assert.assertEquals(expectedHeight, item.getRawRectangleBounds().height, 0.001);
        //Assert.assertEquals(expectedWidth+BoundedItemIcon.DEFAULT_SLOT_HEIGHT*2, item.getExternalRectangleBounds().width, 0.001);
        
        float expectedRadius = (float)(Math.hypot(expectedWidth+BoundedItemIcon.DEFAULT_SLOT_HEIGHT*2, expectedHeight+BoundedItemIcon.DEFAULT_SLOT_HEIGHT*2)/2);
        //System.out.println(item.getRadialBounds() + " - " + expectedRadius);
        //Assert.assertTrue(item.getRadialBounds()==expectedRadius); // radius must be a half icon diagonal
        
        Assert.assertTrue(item.getRadialBounds(Math.PI/2)<=expectedRadius); 
    }
}
