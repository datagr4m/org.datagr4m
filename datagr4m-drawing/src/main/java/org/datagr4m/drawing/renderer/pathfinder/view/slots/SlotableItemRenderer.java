package org.datagr4m.drawing.renderer.pathfinder.view.slots;

import java.awt.Color;
import java.awt.Graphics2D;

import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotSide;


public class SlotableItemRenderer extends SlotGroupRenderer{
    public void render(Graphics2D graphics, ISlotableItem item){
        render(graphics, item, Color.BLACK);
    }
    
    public void render(Graphics2D graphics, ISlotableItem item, Color color){
        /*for(SlotGroup group: item.getSlotGroups())
            render(graphics, group);*/
        synchronized(item.getSlotGroups()){
            render(graphics, item.getSlotGroup(SlotSide.NORTH), color);
            render(graphics, item.getSlotGroup(SlotSide.SOUTH), color);
            render(graphics, item.getSlotGroup(SlotSide.EAST), color);
            render(graphics, item.getSlotGroup(SlotSide.WEST), color);
        }
    }
}
