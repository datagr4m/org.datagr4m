package org.datagr4m.drawing.model.items.listeners;

import org.datagr4m.drawing.model.items.IBoundedItem;

public interface IItemListener {
    public void itemBoundsChanged(IBoundedItem item);
    public void itemPositionChanged(IBoundedItem item);
}
