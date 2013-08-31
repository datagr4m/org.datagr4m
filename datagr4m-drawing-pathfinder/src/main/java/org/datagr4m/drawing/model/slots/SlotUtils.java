package org.datagr4m.drawing.model.slots;


public class SlotUtils {
    public static SlotGroup findSlotGroup(ISlotableItem container, ISlotableItem target, Object link){
        SlotTarget expectedTarget = new SlotTarget(null, target, link);
        return findSlotGroup(container, target, expectedTarget);
    }

    public static SlotGroup findSlotGroup(ISlotableItem container, ISlotableItem target, SlotTarget key){
        for(SlotGroup group: container.getSlotGroups()){
            if(group.contains(key))
                return group;
        }
        return null;
    }
    
    public static SlotGroup findSlotGroupContainingTarget(ISlotableItem container, ISlotableItem target){
        for(SlotGroup group: container.getSlotGroups()){
        	
        	for(SlotTarget t: group.getSlotTargets()){
        		if(target.equals(t.target)){
        			return group;
        		}
        	}
        }
        return null;
    }

    
    //public static int 
}
