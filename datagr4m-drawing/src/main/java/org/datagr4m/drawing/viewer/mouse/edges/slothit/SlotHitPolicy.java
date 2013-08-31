package org.datagr4m.drawing.viewer.mouse.edges.slothit;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.items.annotations.SlotAnnotation;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.navigation.plugin.tooltips.TooltipPlugin;
import org.datagr4m.viewer.model.annotations.AnnotationModel;


public class SlotHitPolicy implements ISlotHitPolicy{
    protected TooltipPlugin tooltipPlugin;
    
    public TooltipPlugin getTooltipPlugin() {
        return tooltipPlugin;
    }

    public void setTooltipPlugin(TooltipPlugin tooltipPlugin) {
        this.tooltipPlugin = tooltipPlugin;
    }

    @Override
	public void hitHorizontalSlot(Point2D mousePoint, SlotGroup group, double xMaxDist, double yMaxDist) {
        //System.out.println("----");        

        double ydist = Math.abs(group.getCenter().getY()-mousePoint.getY());
        if(ydist<yMaxDist){
            for(int id=0; id<group.getSlotNumber(); id++){
                Point2D slotPoint = group.getSlotAnchorPoint(id);
                double xdist = Math.abs(slotPoint.getX()-mousePoint.getX());
                if(xdist<xMaxDist){
                    onHit(id, group, mousePoint);
                }
            }
        }
    }
    
    @Override
	public void hitVerticalSlot(Point2D mousePoint, SlotGroup group, double xMaxDist, double yMaxDist) {
        //System.out.println("----");        

        double xdist = Math.abs(group.getCenter().getX()-mousePoint.getX());
        if(xdist<xMaxDist){
            for(int id=0; id<group.getSlotNumber(); id++){
                Point2D slotPoint = group.getSlotAnchorPoint(id);
                double ydist = Math.abs(slotPoint.getY()-mousePoint.getY());
                if(ydist<yMaxDist){
                    onHit(id, group, mousePoint);
                }
            }
        }
    }
    
    /** Deal with this slot hit. This implementation immediatly triggers an annotation to display.
     * 
     * TODO: store in model "is selected", and handle annotation @ rendering
     */
    @Override
	public void onHit(int id, SlotGroup slotGroup, Point2D mousePoint){
        //Logger.getLogger(this.getClass()).debug("over:" + slotGroup.getSlotTarget(id));
        //System.out.println("over slot: " + id + " " + slotGroup.getSlotAnchorPoint(id).getX() +  slotGroup.getSlotTarget(id));
        
        AnnotationModel model = tooltipPlugin.getTooltipModel();
        model.addAnnotation(new SlotAnnotation(slotGroup, id), slotGroup.getSlotAnchorPoint(id));
    }
}
