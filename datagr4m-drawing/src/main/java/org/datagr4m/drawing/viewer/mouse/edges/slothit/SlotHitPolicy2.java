package org.datagr4m.drawing.viewer.mouse.edges.slothit;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.slots.SlotGroup;


public class SlotHitPolicy2 extends SlotHitPolicy implements ISlotHitPolicy{
    @Override
	public void hitHorizontalSlot(Point2D mousePoint, SlotGroup group, double xMaxDist, double yMaxDist) {
        //System.out.println("----");        
        int minId = -1;
        double minDist = Double.MAX_VALUE;
        
        double ydist = Math.abs(group.getCenter().getY()-mousePoint.getY());
        if(ydist<yMaxDist){
            for(int id=0; id<group.getSlotNumber(); id++){
                double d = group.distanceSq(mousePoint, id);
                
                if(d<xMaxDist){ // BUG!!! yMAXDIST
                    if(d<minDist){
                        minId = id;
                        minDist = d;
                    }
                }
            }
        }
        if(minId!=-1){
            onHit(minId, group, mousePoint);
        }
    }
    
    @Override
	public void hitVerticalSlot(Point2D mousePoint, SlotGroup group, double xMaxDist, double yMaxDist) {
        //System.out.println("----");        
        int minId = -1;
        double minDist = Double.MAX_VALUE;
        
        double xdist = Math.abs(group.getCenter().getX()-mousePoint.getX());
        if(xdist<xMaxDist){
            for(int id=0; id<group.getSlotNumber(); id++){
                double d = group.distanceSq(mousePoint, id);
                
                if(d<xMaxDist){
                    if(d<minDist){
                        minId = id;
                        minDist = d;
                    }
                }
            }
        }
        if(minId!=-1){
            onHit(minId, group, mousePoint);
        }
    }
}
