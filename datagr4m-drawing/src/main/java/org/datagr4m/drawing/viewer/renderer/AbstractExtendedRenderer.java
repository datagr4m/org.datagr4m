package org.datagr4m.drawing.viewer.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.viewer.renderer.DefaultRenderer;
import org.jzy3d.maths.Coord2d;


/** Provide primitives for drawing. */
public class AbstractExtendedRenderer extends DefaultRenderer{
    protected void drawRect(Graphics2D graphic, RectangleBounds rect) {
        graphic.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
    }
    
    protected void drawRect(Graphics2D graphic, RectangleBounds rect, Color color) {
        graphic.setColor(color);
        graphic.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
    }
    
    protected void fillRect(Graphics2D graphic, RectangleBounds rect, Color color) {
        graphic.setColor(color);
        graphic.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
    }
    
    protected void updateRectangle(Rectangle2D rectangle, RectangleBounds bounds){
        rectangle.setFrame(bounds.x, bounds.y, bounds.width, bounds.height);
    }
    
    protected void updateRectangle(Rectangle2D rectangle, RectangleBounds bounds, Coord2d offset){
        rectangle.setFrame(bounds.x + offset.x, bounds.y + offset.y, bounds.width, bounds.height);
    }

    protected void updateRectangle(Rectangle2D rectangle, double x, double y, double width, double height){
        rectangle.setFrame(x, y, width, height);
    }
    
}