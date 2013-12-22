package org.datagr4m.drawing.layout.slots.geometry;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.slots.SlotGroup;


public class DefaultSlotGeometryBuilder extends AbstractSlotGeometryBuilder{
    private static final long serialVersionUID = 1065606857251591794L;
    public static float MIN_SLOT_WIDTH = 5;
    public static float MAX_SLOT_WIDTH = 10;
    
    /**
     * Build the slot geometry according to its current center and dimensions.
     */
    @Override
    public void build(SlotGroup group){
        Point2D center = group.getCenter();
        float width = group.getWidth();
        float height = group.getHeight();
        float margin = group.getMargin();
        
        if(width==0 || height==0)
            throw new IllegalArgumentException("width is " + width + " and height is " + height + " which is forbidden");
        
        if(margin>=100 || margin<0)
            throw new IllegalArgumentException("wmargin should be value in [0 and 100[");
        
        // Horizontal (north/south)
        if((group.isHorizontal() && !group.isFlipped90()) || (group.isVertical() && group.isFlipped90())){
            double actualWidth = width-(width*margin/100);
            double wstep = actualWidth/group.getSlotNumber();
            double hstep = height/2;

            float anchorWidth = Math.max(MIN_SLOT_WIDTH, Math.min(MAX_SLOT_WIDTH, (float)wstep/3));
            group.setSlotAnchorWidth(anchorWidth);
            
            double startx = center.getX() - actualWidth/2 + wstep/2;
            double anchory;
            double pathy;
            
            if(group.isNorth()){
                anchory = center.getY() + hstep;
                pathy = center.getY();// - hstep;
            }
            else{ // south
                anchory = center.getY() - hstep;
                pathy = center.getY();// + hstep;                
            }
            
            for (int i = 0; i < group.getSlotNumber(); i++) {
                makeSlot(group, i, new Point2D.Double(startx, anchory), new Point2D.Double(startx, pathy));
                startx+=wstep;
            }
        }
        // vertical (east/west)
        else{ 
            double actualHeight = height-(height*margin/100);
            double hstep = actualHeight/group.getSlotNumber();
            double wstep = width/2;
            float anchorWidth = Math.max(MIN_SLOT_WIDTH, Math.min(MAX_SLOT_WIDTH, (float)hstep/3));
            group.setSlotAnchorWidth(anchorWidth);
            //System.out.println(hstep/3 + " " + anchorWidth);
            
            double starty = center.getY() - actualHeight/2 + hstep/2;
            double anchorx;
            double pathx;
            
            if(group.isWest()){ //left
                anchorx = center.getX() + wstep;
                pathx = center.getX();// - wstep;
            }
            else{ 
                anchorx = center.getX() - wstep;
                pathx = center.getX();// + wstep;                
            }
            
            for (int i = 0; i < group.getSlotNumber(); i++) {
                makeSlot(group, i, new Point2D.Double(anchorx, starty), new Point2D.Double(pathx, starty));
                starty+=hstep;
            }
        }
    }
}
