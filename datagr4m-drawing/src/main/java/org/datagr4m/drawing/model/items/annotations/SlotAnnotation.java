package org.datagr4m.drawing.model.items.annotations;

import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.viewer.model.annotations.Annotation;
import org.datagr4m.viewer.renderer.CellAnchor;


public class SlotAnnotation extends Annotation{
    public SlotAnnotation(SlotGroup group, int id) {
        super(group.getSlotTargetInterfaceLabels(id, " "));
        if(this.text==null)
            System.err.println(text);
        //System.err.println("ann" + group.getSlotTargetInterfaceLabels(id, " "));
        this.group = group;
        this.id = id;
    }
    
    public CellAnchor getGuessedAnchor(){
        if(group.getSide()==SlotSide.NORTH)
            return CellAnchor.MIDDLE_BOTTOM;
        else if(group.getSide()==SlotSide.SOUTH)
            return CellAnchor.MIDDLE_TOP;
        else if(group.getSide()==SlotSide.WEST)
            return CellAnchor.MIDDLE_RIGHT;
        else if(group.getSide()==SlotSide.EAST)
            return CellAnchor.MIDDLE_LEFT;
        else
            throw new RuntimeException("unknown side");
    }
    
    protected SlotGroup group;
    protected int id;
}

