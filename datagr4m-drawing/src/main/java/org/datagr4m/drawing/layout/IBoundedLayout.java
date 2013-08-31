package org.datagr4m.drawing.layout;

import org.datagr4m.drawing.model.bounds.IBounds;
import org.datagr4m.drawing.model.bounds.RectangleBounds;

public interface IBoundedLayout extends ILayout{
    public IBounds getBounds();
    public void setBounds(IBounds bounds);
    public RectangleBounds getActualBounds();
    
    //public IBoundedItemModel getModel();
}
