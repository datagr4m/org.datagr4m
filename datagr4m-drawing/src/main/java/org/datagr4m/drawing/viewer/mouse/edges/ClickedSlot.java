package org.datagr4m.drawing.viewer.mouse.edges;

import java.util.List;

import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.datagr4m.viewer.mouse.IClickableItem;


public class ClickedSlot implements IClickableItem{
    private static final long serialVersionUID = -3901229359268803427L;
    
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
        List<SlotTarget> st = group.getSlotTarget(slot);
        String s = "Slot " + slot + " at " + group.getSide() + " of " + parent;
        
        if(st.size()>0){
            Object intr = st.get(0).getInterface();
            if(intr!=null){
                s+="[int:"+intr.toString()+"]";
            }
            else{
                s+="[int:null]";
            }
            if(st.size()>1)
                s+="...";
        }
        
        return s;//"Slot " + slot + " at " + group.getSide() + " of " + parent;
    }
    
    protected int slot;
    protected SlotGroup group;
    protected ISlotableItem parent;
}
