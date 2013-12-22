package org.datagr4m.drawing.layout.slots.geometry;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.slots.SlotGroup;


/** Construit le d�tail de chaque "patte" d'un slot group. 
 * 
 */
public interface ISlotGeometryBuilder{
    public void build(SlotGroup group);
    public void makeSlot(SlotGroup group, int i, Point2D anchor, Point2D path);
    
    public IPathStartValidator getValidator();
    public void setValidator(IPathStartValidator validator);
}
