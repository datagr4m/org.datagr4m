package org.datagr4m.drawing.renderer.bounds;

import org.datagr4m.drawing.model.items.IBoundedItem;


public interface IBoundsRendererSettings{
    public boolean isBoundDisplayed(IBoundedItem model);
    public void setBoundDisplayed(IBoundedItem model, boolean displayed);
}
