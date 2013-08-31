package org.datagr4m.drawing.viewer.mouse.edges.slothit;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.slots.SlotGroup;


public interface ISlotHitPolicy {
    public void hitHorizontalSlot(Point2D mousePoint, SlotGroup north, double xMaxDist, double yMaxDist);
    public void hitVerticalSlot(Point2D mousePoint, SlotGroup north, double xMaxDist, double yMaxDist);
    public void onHit(int id, SlotGroup slotGroup, Point2D mousePoint);
}
