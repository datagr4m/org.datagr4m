package org.datagr4m.drawing.layout.slots.geometry;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.slots.SlotGroup;


public interface IPathStartValidator {
    public boolean validate(Point2D pathstart, SlotGroup group, int slotid);
    public Point2D generate(Point2D base, SlotGroup group, int slotid);
    public void remember(Point2D pathstart, SlotGroup group, int slotid);
}
