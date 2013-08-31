package org.datagr4m.drawing.renderer.bounds;

import org.datagr4m.drawing.model.items.IBoundedItem;

public class BoundsRendererSettings implements IBoundsRendererSettings{
    public BoundsRendererSettings(){
        this(true);
    }
    
    public BoundsRendererSettings(boolean displayed){
        boundDisplayed = displayed;
    }

    @Override
    public boolean isBoundDisplayed(IBoundedItem model) {
        return boundDisplayed;
    }
    
    @Override
    public void setBoundDisplayed(IBoundedItem model, boolean displayed){
        boundDisplayed = displayed;
    }
    
    protected boolean boundDisplayed;
}
