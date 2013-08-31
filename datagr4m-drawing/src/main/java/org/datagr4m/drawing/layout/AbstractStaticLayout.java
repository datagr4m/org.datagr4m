package org.datagr4m.drawing.layout;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public abstract class AbstractStaticLayout implements IStaticLayout{
    private static final long serialVersionUID = 3376559317661815890L;

    @Override
    public void resetPropertiesValues() {
    }
    
    @Override
	public void fixPosition(IBoundedItem item, Coord2d position){
        item.changePosition(position);        
    }

}
