package org.datagr4m.drawing.items;

import junit.framework.TestCase;

import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;

public class TestBoundedItem extends TestCase{
    public void testDefaultItemGetSet(){
        IBoundedItem item = new DefaultBoundedItem("node", 30);
        assertTrue(item.getLabel().equals("node"));
        assertTrue(item.getRadialBounds()==30);
        assertTrue(item.getRadialBounds(0)==30);
        assertTrue(item.getRadialBounds(Math.PI)==30);
        
        // verify statefull attributes
        assertTrue(item.locked()==false);
        item.lock();
        assertTrue(item.locked()==true);
        item.unlock();
        assertTrue(item.locked()==false);
        
        assertTrue(item.getState().isNone());
        item.setState(ItemState.selected());
        assertTrue(item.getState().isSelected());
        item.setState(ItemState.mouseover());
        assertTrue(item.getState().isMouseOver());
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
