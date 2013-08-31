package org.datagr4m.drawing.viewer.mouse.items;

import org.datagr4m.drawing.model.items.IBoundedItem;


public interface IMouseHitModelListener extends org.datagr4m.viewer.mouse.hit.IMouseHitModelListener{

    public void itemHit(IBoundedItem item);
    public void itemDragged(IBoundedItem item);
    public void itemReleased(IBoundedItem item);

}
