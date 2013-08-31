package org.datagr4m.drawing.viewer.mouse.edges;

import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.viewer.mouse.IClickableItem;


public class ClickedSlot implements IClickableItem{
    public ClickedSlot(int slot, SlotGroup group, ISlotableItem parent) {
        this.slot = slot;
        this.group = group;
        this.parent = parent;
    }
    
    public int getSlot() {
        return slot;
    }
    public SlotGroup getGroup() {
        return group;
    }
    public ISlotableItem getParent() {
        return parent;
    }
    
    @Override
	public String toString(){
        return "Slot " + slot + " at " + group.getSide() + " of " + parent;
    }
    
    protected int slot;
    protected SlotGroup group;
    protected ISlotableItem parent;
    private static final long serialVersionUID = -3901229359268803427L;
}
