package org.datagr4m.viewer.renderer.hit;

import java.util.List;

import org.datagr4m.viewer.mouse.IClickableItem;


public class DefaultHitProcessor implements IHitProcessor{
    @Override
    public List<IClickableItem> hit(int x, int y){
        return null;
    }
    
    @Override
    public <T> List<IClickableItem> hitOnly(int x, int y, Class<T> type){
        if(type.isInstance(this))
            return hit(x, y);
        else
            return null;
    }
    
    @Override
    public <T> List<IClickableItem> hitExcluding(int x, int y, Class<T> type){
        if(!type.isInstance(this))
            return hit(x, y);
        else
            return null;
    }
}
