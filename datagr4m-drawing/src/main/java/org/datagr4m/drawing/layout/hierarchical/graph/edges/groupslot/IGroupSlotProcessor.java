package org.datagr4m.drawing.layout.hierarchical.graph.edges.groupslot;

import java.io.Serializable;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public interface IGroupSlotProcessor extends Serializable{
    /** 
     * Computes the absolute position of a point standing on the border of an item,
     * so that one can draw a smart path between the item and the target passing through
     * the border coordinate.
     */
    public Coord2d computeBorderCoord(IBoundedItem item, Coord2d targetPositionAbsolute);
    
    /**
     * May clear any historical data held by the processor.
     */
    public void clear();
    
    /**
     * Indicates how the items are set in space w.r.t. each other
     */
    //public RelativeGroupPosition getRelativePosition(IBoundedItem item, IBoundedItem item2);
}
