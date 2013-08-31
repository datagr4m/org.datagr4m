package org.datagr4m.drawing.layout;

import java.util.HashMap;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public class ItemPositionMap extends HashMap<IBoundedItem, Coord2d>{
    public void apply(){
        for(IBoundedItem item: keySet()){
            Coord2d c = get(item);
            if(c!=null)
                item.changePosition(c);
        }
    }
    
    public void copyAbsolutePosition(List<IBoundedItem> items){
        for(IBoundedItem item: items)
            put(item, item.getAbsolutePosition().clone());
    }
    
    public IBoundedItem findByLabel(String name){
        for(IBoundedItem i: keySet())
            if(i.getLabel().equals(name))
                return i;
        return null;
    }
    
    private static final long serialVersionUID = 4172004483185241193L;
}
