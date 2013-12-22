package org.datagr4m.drawing.layout.slots;

import java.util.List;

import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.jzy3d.maths.Array;
import org.jzy3d.maths.Coord2d;


public class SlotGroupLayout implements ISlotGroupLayout{
    private static final long serialVersionUID = 3839706970924632792L;

    /**
     * Returns a best side on the source for linking source to target.
     * In other word, if the target stand "on the left" of the source, returns "west".
     */
    @Override
	public SlotSide getTargetBestSlotSide(ISlotableItem source, ISlotableItem target){
        Coord2d c1 = source.getSlotableCenter();
        Coord2d c2 = target.getSlotableCenter();
        return getTargetBestSlotSide(c1, c2);
    }

    @Override
	public SlotSide getTargetBestSlotSide(Coord2d c1, Coord2d c2)  {
        float alpha = c2.sub(c1).fullPolar().x; // position polaire de la cible par rapport � la target
        
        if(0<=alpha && alpha<Math.PI/4)
            return SlotSide.EAST;
        else if(Math.PI/4<=alpha && alpha<3*Math.PI/4)
            return SlotSide.SOUTH; // an increasing Y is going down on a screen display
        else if(3*Math.PI/4<=alpha && alpha<5*Math.PI/4)
            return SlotSide.WEST;
        else if(5*Math.PI/4<=alpha && alpha<7*Math.PI/4)
            return SlotSide.NORTH; // an increasing Y is going down on a screen display
        else if(7*Math.PI/4<=alpha && alpha<=8*Math.PI/4)
            return SlotSide.EAST;
        else
            throw new RuntimeException("error with alpha value");
    }
    
    /** SELECT A SLOT ID FOR A TARGET */    
    @Override
	public int[] getSlotTargetBestId(ISlotableItem source, SlotGroup group, List<SlotTarget> targets){
        return getTargetBestSlotId(source, group, SlotTarget.getObstacles(targets));
    }
    
    /** Rank targets according to their polar coordinate relative to the source.*/
    @Override
	public int[] getTargetBestSlotId(ISlotableItem source, SlotGroup group, List<ISlotableItem> targets){
        //if(targets.size()!=group.getSlotNumber())
        //    throw new RuntimeException("error: " + targets.size() + " targets != " + group.getSlotNumber() + " slots");
        
        float[] angles = computeRelativeAngles(source, targets, group.isEast());
        
        int[] order;
        if(group.isNorth()||group.isEast())
            order = Array.sortAscending(angles);
        else
            order = Array.sortDescending(angles);
        return order;
    }

    /** Compute the polar coordinate of each target relative to the center of the item.*/
    public float[] computeRelativeAngles(ISlotableItem source, List<ISlotableItem> targets, boolean isEast) {
        float[] angles = new float[targets.size()];
        int k = 0;
        Coord2d c1 = source.getSlotableCenter();

        for(ISlotableItem t: targets){
            if(t!=null){
                Coord2d c2 = t.getSlotableCenter();
                float alpha = c2.sub(c1).fullPolar().x;
                if(isEast)
                    alpha = (float)((alpha + 3*Math.PI/2)%(Math.PI*2));
                angles[k++] = alpha;
            }
            else
                angles[k++] = 0; // unknown target at angle 0
        }
        return angles;
    }
}
