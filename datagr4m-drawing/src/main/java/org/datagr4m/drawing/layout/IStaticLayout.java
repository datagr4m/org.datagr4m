package org.datagr4m.drawing.layout;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public interface IStaticLayout extends ILayout{
    public void fixPosition(IBoundedItem item, Coord2d position);
}
