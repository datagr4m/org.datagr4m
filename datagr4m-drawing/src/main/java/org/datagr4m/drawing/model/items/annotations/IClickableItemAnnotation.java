package org.datagr4m.drawing.model.items.annotations;

import java.awt.Color;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.viewer.renderer.IRenderer;


/**
 * This interface represents simple model objects that handle the rendering
 * policy by themselves: labels, buttons, etc.
 */
public interface IClickableItemAnnotation extends IBoundedItem, IRenderer{
    public Color getTextColor();
    public void setTextColor(Color color);
    
    public Color getBackgroundColor();
    public void setBackgroundColor(Color color);

    public Color getBorderColor();
    public void setBorderColor(Color color);
}
