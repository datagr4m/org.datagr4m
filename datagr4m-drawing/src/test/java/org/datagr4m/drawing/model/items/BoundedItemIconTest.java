package org.datagr4m.drawing.model.items;

import org.junit.Assert;
import org.junit.Test;

public class BoundedItemIconTest {
    @Test
    public void testDefaultItemGetSet(){
        IBoundedItem item = new DefaultBoundedItem("node", 30);
        Assert.assertTrue(item.getLabel().equals("node"));
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
    }
    
    /*public void testIconItemSize(){
        Icon icon = DeviceIconLibrary.getDeviceIcon(DeviceType.ROUTER_FILTERING);
        
        // ensure icon has the expected dimensions
        int expectedWidth = 67/2; // weird
        int expectedHeight = 40/2; // weird
        assertTrue(icon.getIconWidth()<=expectedWidth+1);
        assertTrue(icon.getIconWidth()>=expectedWidth-1);
        assertTrue(icon.getIconHeight()<=expectedHeight+1);
        assertTrue(icon.getIconHeight()>=expectedHeight-1);
        
        // ensure bounds consider icon dimensions
        IBoundedItemIcon item = new DefaultBoundedItemIcon("node", icon);
        assertTrue(item.getIcon()==icon);
        
        double expectedRadius = (Math.hypot(expectedWidth+DefaultBoundedItemIcon.DEFAULT_SLOT_HEIGHT*2, expectedHeight+DefaultBoundedItemIcon.DEFAULT_SLOT_HEIGHT*2)/2);
        assertTrue(item.getRadialBounds()<=expectedRadius+1); // radius must be a half icon diagonal
        
        // TODO: understand this problem!!
        //assertTrue(item.getRadialBounds()>=expectedRadius-1); // radius must be a half icon diagonal
        
        assertTrue(item.getRadialBounds(Math.PI/2)<=expectedRadius+1); // ... all around
        
        // TODO: understand this problem!!
        //assertTrue(item.getRadialBounds(Math.PI/2)>=expectedRadius-1); // ... all around
    }*/
}
