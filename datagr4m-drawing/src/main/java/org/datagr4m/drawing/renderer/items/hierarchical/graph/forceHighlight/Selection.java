package org.datagr4m.drawing.renderer.items.hierarchical.graph.forceHighlight;

import org.datagr4m.drawing.model.items.IBoundedItem;

public class Selection {
    public void select(IBoundedItem item){
        selected = item;
    }
    
    public boolean isSelected(IBoundedItem item){
        if(!hasSelection())
            return false;
        else
            return selected.equals(item);
    }
    
    public void clear(){
        selected = null;
    }
    
    public IBoundedItem getSelected(){
        return selected;
    }
    
    public boolean hasSelection(){
        return selected!=null;
    }
    
    IBoundedItem selected;
}
