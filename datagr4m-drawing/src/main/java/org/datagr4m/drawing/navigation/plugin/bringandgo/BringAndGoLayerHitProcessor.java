package org.datagr4m.drawing.navigation.plugin.bringandgo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.navigation.plugin.bringandgo.simple.SimpleBringAndGoLayer;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.hit.DefaultHitProcessor;


public class BringAndGoLayerHitProcessor extends DefaultHitProcessor{
    protected SimpleBringAndGoLayer renderer;
    
    public BringAndGoLayerHitProcessor(SimpleBringAndGoLayer renderer){
        this.renderer = renderer;
    }
    
    @Override
    public List<IClickableItem> hit(int x, int y) {
        List<IClickableItem> hitStack = new ArrayList<IClickableItem>();
        hitItems(x, y, hitStack);
        return hitStack;
    }
    
    public void hitItems(int x, int y, List<IClickableItem> hitStack){
        Collection<IBoundedItem> items = renderer.getNeighbours(); 
        if(items!=null)
            for(IBoundedItem item: items){
                boolean hit = renderer.getItemRenderer().hit(item, x, y);
                if(hit)
                    hitStack.add(item);
            }
    }
}
