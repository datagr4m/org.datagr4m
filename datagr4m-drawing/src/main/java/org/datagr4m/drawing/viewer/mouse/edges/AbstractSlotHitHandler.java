package org.datagr4m.drawing.viewer.mouse.edges;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.viewer.mouse.edges.slothit.ISlotHitPolicy;


public class AbstractSlotHitHandler {
    protected void processItemRollOver(IBoundedItem item, Point2D mousePoint, ISlotHitPolicy slotHitPolicy, INavigationController navigation, double manhattanDistance){
        //navigation
        if(navigation!=null && navigation.getTooltipsPlugin()!=null)
            navigation.getTooltipsPlugin().clearTooltips();
        
        SlotGroup north = item.getSlotGroup(SlotSide.NORTH);
        if(north!=null)
            slotHitPolicy.hitHorizontalSlot(mousePoint, north, manhattanDistance, manhattanDistance);
        
        SlotGroup south = item.getSlotGroup(SlotSide.SOUTH);
        if(south!=null)
            slotHitPolicy.hitHorizontalSlot(mousePoint, south, manhattanDistance, manhattanDistance);
            
        SlotGroup west = item.getSlotGroup(SlotSide.WEST);
        if(west!=null)
            slotHitPolicy.hitVerticalSlot(mousePoint, west, manhattanDistance, manhattanDistance);
        
        SlotGroup east = item.getSlotGroup(SlotSide.EAST);
        if(east!=null)
            slotHitPolicy.hitVerticalSlot(mousePoint, east, manhattanDistance, manhattanDistance);
    }
}