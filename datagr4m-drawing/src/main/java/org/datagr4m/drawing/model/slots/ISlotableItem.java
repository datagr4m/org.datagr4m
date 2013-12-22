package org.datagr4m.drawing.model.slots;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

import org.jzy3d.maths.Coord2d;

/**
 * An item holding slots, i.e. thin rectangle in which one can "plug" an incoming
 * or outgoing edge.
 * 
 * @author Martin Pernollet
 */
public interface ISlotableItem {
    public Coord2d getSlotableCenter(); // center of the item, used to build slots
    public Rectangle2D getSlotableBounds();
    
    public float getSlotMargin();

    public Collection<SlotGroup> getSlotGroups();
    public SlotGroup getSlotGroup(SlotSide side);

    public void addNorthSlot(Collection<SlotTarget> targets);
    public void addSouthSlot(Collection<SlotTarget> targets);
    public void addEastSlot(Collection<SlotTarget> targets);
    public void addWestSlot(Collection<SlotTarget> targets);
    
    public void addSlots(int nNorth, int nSouth, int nEast, int nWest);
    
    public void addNorthSlot(int number);
    public void addSouthSlot(int number);
    public void addEastSlot(int number);
    public void addWestSlot(int number);
    
    public void clearSlots();
}
