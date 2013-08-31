package org.datagr4m.viewer.renderer.annotations.items;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;


public interface IClickableAnnotation extends IRenderer, IClickableItem{
    public Color getTextColor();
    public void setTextColor(Color color);
    
    public Color getBackgroundColor();
    public void setBackgroundColor(Color color);

    public Color getBorderColor();
    public void setBorderColor(Color color);
    
    public Point2D getPosition();
    public void setPosition(Point2D position);
}
