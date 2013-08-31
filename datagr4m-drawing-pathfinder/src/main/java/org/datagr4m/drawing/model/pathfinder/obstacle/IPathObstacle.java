package org.datagr4m.drawing.model.pathfinder.obstacle;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.jzy3d.maths.Coord2d;


public interface IPathObstacle extends ISlotableItem, Serializable{
    @Override
	public Coord2d getSlotableCenter();
    public String getName();
    public Rectangle2D getBounds();
    
    /** 
     * Returns bounds + a margin according to the number of time this obstacle has been bypassed
     * @return
     */
    public Rectangle2D getBypassedBounds();
    public void addBypass();
    

}
