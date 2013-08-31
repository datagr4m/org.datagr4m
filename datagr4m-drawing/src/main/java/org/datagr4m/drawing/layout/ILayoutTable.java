package org.datagr4m.drawing.layout;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public interface ILayoutTable extends IStaticLayout{
    public Coord2d getPosition(IBoundedItem item);
    public ItemPositionMap getPositions();
}
