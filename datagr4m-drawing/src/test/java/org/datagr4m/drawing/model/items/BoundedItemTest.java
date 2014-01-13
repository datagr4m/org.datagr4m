package org.datagr4m.drawing.model.items;

import org.junit.Assert;
import org.junit.Test;

public class BoundedItemTest {
    @Test
    public void testDefaultItemGetSet(){
        IBoundedItem item = new BoundedItem("node", 30);
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
}
