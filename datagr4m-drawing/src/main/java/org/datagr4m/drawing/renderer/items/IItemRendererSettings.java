package org.datagr4m.drawing.renderer.items;

import java.awt.Color;

import javax.swing.ImageIcon;

import org.datagr4m.drawing.model.items.BoundsType;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.viewer.IDisplay;


public interface IItemRendererSettings {
    public void setNodeBodyColor(IBoundedItem item, Color color);
    public void setNodeBorderColor(IBoundedItem item, Color color);
    public void setNodeLabelColor(IBoundedItem item, Color color);
    public Color getNodeBodyColor(IBoundedItem item);
    public Color getNodeBorderColor(IBoundedItem item);
    public Color getNodeLabelColor(IBoundedItem item);

    public boolean isNodeLabelScaled(IBoundedItem item);
    public boolean setNodeLabelScaled(IBoundedItem item, boolean scaled);

    public void setNodeDisplayed(IBoundedItem item, boolean displayed);
    public void setNodeBorderDisplayed(IBoundedItem item, boolean displayed);
    public void setNodeCenterDisplayed(IBoundedItem item, boolean displayed);
    public boolean isNodeDisplayed(IBoundedItem item);
    public boolean isNodeLabelDisplayed(IBoundedItem item);
    public boolean isNodeCenterDisplayed(IBoundedItem item);

    
    public boolean isNodeBodyDisplayed(IBoundedItem item);
    public boolean isNodeBorderDisplayed(IBoundedItem item);

//    public void setNodeBorderColor(IBoundedItem item, ItemState state);
    public Color getNodeStatusBackgroundColor(IBoundedItem item, ItemState state);

    public void setNodeIconFilterColor(IBoundedItem item, Color color);
    public Color getIconFilterColor(IBoundedItem item);
    public ImageIcon getFilteredIcon(ImageIcon icon, Color filter, IDisplay display);


    public boolean isNodeBoundsDisplayed(IBoundedItem item);
    public void setNodeBoundsDisplayed(IBoundedItem item, boolean status);
    
    public void setNodeBoundsTypeColor(BoundsType type, Color color);
    public Color getNodeBoundsTypeColor(BoundsType type);

}
