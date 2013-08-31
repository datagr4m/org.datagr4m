package org.datagr4m.drawing.model.items;

import javax.swing.Icon;

import org.jzy3d.maths.Coord2d;

public interface IBoundedItemIcon extends IBoundedItem{
    public void setIcon(Icon icon);
    public void setIcon(Icon icon, float margin);
    public Icon getIcon();
    
    public Coord2d getScale();
    public void setScale(Coord2d scale);
}
