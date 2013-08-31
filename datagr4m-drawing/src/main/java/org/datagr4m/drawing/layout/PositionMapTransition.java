package org.datagr4m.drawing.layout;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.maths.geometry.functions.LinearInterpolation;
import org.datagr4m.viewer.animation.AbstractAnimation;
import org.datagr4m.viewer.animation.IAnimation;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.jzy3d.maths.Coord2d;


public class PositionMapTransition {
    public IAnimation transition(ItemPositionMap from, ItemPositionMap to, float duration){
        final ItemPositionMap[] interpo = interpolation(from, to, (int)(duration/IAnimationStack.ANIMATION_FRAME_RATE));
        IAnimation anim = new AbstractAnimation(){
            int n = 0;
            @Override
            public boolean next() {
                if(n<interpo.length){
                    interpo[n].apply();
                    n++;
                    return false;
                }
                else{
                    notifyAnimationFinished();
                    clearMonitors();
                    return true;
                }
            }
        };
        return anim;
    }
    
    public ItemPositionMap[] interpolation(ItemPositionMap start, ItemPositionMap stop, int steps){
        ItemPositionMap[] anim = new ItemPositionMap[steps];
        
        anim[0] = start;
        for (int i = 1; i < steps-1; i++) {
            anim[i] = new ItemPositionMap();      
        }
        anim[steps-1] = stop;
        
        for(IBoundedItem item: start.keySet()){
            Coord2d c1 = start.get(item);
            Coord2d c2 = stop.get(item);
            
            if(c1!=null && c2!=null){
                Coord2d[] interpo = LinearInterpolation.build(c1, c2, steps);
                
                for (int j = 1; j < steps-1; j++) {
                    anim[j].put(item, interpo[j]);
                }
            }
            else{
                Logger.getLogger(this.getClass()).error("interpolation error for " + item + " : start=" + c1 + " stop=" + c2);
            }
                
        }
        
        return anim;
    }
}
