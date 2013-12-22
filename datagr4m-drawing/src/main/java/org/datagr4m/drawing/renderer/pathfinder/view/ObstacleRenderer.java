package org.datagr4m.drawing.renderer.pathfinder.view;

import java.awt.Color;
import java.awt.Graphics2D;

import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.renderer.pathfinder.view.slots.SlotGroupRenderer;
import org.datagr4m.viewer.renderer.DefaultRenderer;


public class ObstacleRenderer extends DefaultRenderer{
    public void render(Graphics2D graphic, IPathObstacle o, Color color){
        render(graphic, o, color, true);
    }
    
    public void render(Graphics2D graphic, IPathObstacle o, Color color, boolean showBypassArea){
        // obstacle bypassed bounds color
        if(showBypassArea){
            graphic.setColor(color);
            applyAlpha(graphic, 0.5f);
            graphic.fill(o.getBypassedBounds());
            resetAlpha(graphic);
        }
        
        // obstacle bounds
        graphic.setColor(Color.BLACK);
        graphic.draw(o.getBounds());
        
        // obstalce name
        drawTextCentered(graphic, o.getName(), o.getSlotableCenter());
        //graphic.drawString(o.getName(), o.getCenter().x, o.getCenter().y);
        
        // obstacle slots
        slotGroupRenderer.render(graphic, o.getSlotGroup(SlotSide.NORTH));
        slotGroupRenderer.render(graphic, o.getSlotGroup(SlotSide.SOUTH));
        slotGroupRenderer.render(graphic, o.getSlotGroup(SlotSide.EAST));
        slotGroupRenderer.render(graphic, o.getSlotGroup(SlotSide.WEST));
    }
    
    protected SlotGroupRenderer slotGroupRenderer = new SlotGroupRenderer();
}
