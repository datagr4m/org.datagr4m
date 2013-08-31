package org.datagr4m.drawing.layout.hierarchical.graph.edges.groupslot;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.jzy3d.maths.Coord2d;


public class StratumGroupSlotProcessor extends DefaultGroupSlotProcessor{
    private static final long serialVersionUID = -6804124965333031118L;
    
    public static int SLOT_GAP = 50;
    public static boolean USE_DIFFERENT_SLOTS = false;
    
    @Override
    public void clear(){
        sideCounter.clear();
    }
    
    @Override
	protected Coord2d computeRectangleBorderCoord(IBoundedItem model, Coord2d target) {
        Coord2d abs = model.getAbsolutePosition();
        
        double width = selectRectangleBounds(model).width;// + model.getMargin()*2 + model.getCorridor()*2;
        double height = selectRectangleBounds(model).height;// + model.getMargin()*2 + model.getCorridor()*2;
        
        SlotSide side = getSide(model, abs, target);
        
        if(side==SlotSide.NORTH){
            Coord2d slot = middleNorth(abs, height);
            int count = getCount(model, side);
            slot.x += (alternate(count)*SLOT_GAP);
            if(USE_DIFFERENT_SLOTS)
                setCount(model, side, count+1);
            return slot;
        }
        else if(side==SlotSide.SOUTH){
            //return middleSouth(abs, height);
            
            Coord2d slot = middleSouth(abs, height);
            int count = getCount(model, side);
            slot.x += (alternate(count)*SLOT_GAP);
            if(USE_DIFFERENT_SLOTS)
                setCount(model, side, count+1);
            return slot;
        }
        else if(side==SlotSide.WEST){
            //return middleWest(abs, width);
            Coord2d slot = middleWest(abs, width);
            int count = getCount(model, side);
            slot.y += (alternate(count)*SLOT_GAP);
            if(USE_DIFFERENT_SLOTS)
                setCount(model, side, count+1);
            return slot;
        }
        else if(side==SlotSide.EAST){
            //return middleEast(abs, width);
            Coord2d slot = middleEast(abs, width);
            int count = getCount(model, side);
            slot.y += (alternate(count)*SLOT_GAP);
            if(USE_DIFFERENT_SLOTS)
                setCount(model, side, count+1);
            return slot;
        }
        else
            return super.computeRectangleBorderCoord(model, target);
    }
    
    
    /**
     * alternate(0) =  0
     * alternate(1) = -1
     * alternate(2) = +1
     * alternate(3) = -2
     * alternate(4) = +2
     * ...
     */
    public static int alternate(int i){
        if(i==0)
            return 0;
        else if(i%2==0)
            return (i/2);
        else if(i%2!=0)
            return -((i/2)+1);
        else
            return 0;
    }
    
    /* MIDDLE POINT OF RECTANGLE SIDE */

    protected Coord2d middleEast(Coord2d abs, double width) {
        return new Coord2d(abs.x+width/2, abs.y);
    }

    protected Coord2d middleWest(Coord2d abs, double width) {
        return new Coord2d(abs.x-width/2, abs.y);
    }

    protected Coord2d middleSouth(Coord2d abs, double height) {
        return new Coord2d(abs.x, abs.y-height/2);
    }

    protected Coord2d middleNorth(Coord2d abs, double height) {
        return new Coord2d(abs.x, abs.y+height/2);
    }
    
    /* GET SIDE */
    
    protected SlotSide getSide(IBoundedItem model, Coord2d modelPosition, Coord2d target){
        double width = selectRectangleBounds(model).width;
        double height = selectRectangleBounds(model).height;
        
        double selfTop = modelPosition.y+height/2;
        double selfBot = modelPosition.y-height/2;
        double selfLef = modelPosition.x-width/2;
        double selfRig = modelPosition.x+width/2;
        
        if(target.y > selfTop)
            return SlotSide.NORTH;
        else if(target.y < selfBot)
            return SlotSide.SOUTH;
        else{
            if(target.x < selfLef)
                return SlotSide.WEST;
            else if(target.x > selfRig)
                return SlotSide.EAST;
            else
                return SlotSide.UNKNOWN;
        }
    }
    
    /* SIDE COUNTER */
    
    protected int getCount(IBoundedItem item, SlotSide side){
        Integer i = sideCounter.get(new Pair<IBoundedItem,SlotSide>(item, side));
        if(i==null)
            i = 0;
        return i;
    }
    
    protected void setCount(IBoundedItem item, SlotSide side, int i){
        sideCounter.put(new Pair<IBoundedItem,SlotSide>(item, side), i);
    }

    protected void increment(IBoundedItem item, SlotSide side){
        int i = getCount(item, side);
        setCount(item,side,i+1);
    }
    
    protected Map<Pair<IBoundedItem,SlotSide>,Integer> sideCounter = new HashMap<Pair<IBoundedItem,SlotSide>,Integer>();

    /* TEST ALTERNATE */

    public static void main(String[] args){
        for (int j = 0; j < 10; j++)
            test(j);
    }
    
    public static void test(int i){
        Logger.getLogger(StratumGroupSlotProcessor.class).info("i=" + i + " > " + alternate(i));
    }
}
