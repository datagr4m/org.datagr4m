package org.datagr4m.viewer.mouse;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.datagr4m.viewer.mouse.hit.IMouseHitModelListener;
import org.datagr4m.viewer.renderer.IRenderer;


/** 
 * A localized mouse is aware of the current view transformation and able to indicate
 * with layout point is under the pointer at each mouse event.
 */
public interface ILocalizedMouse {
    public boolean onLeftClick(IRenderer renderer, Point2D screen, Point2D layout);
    public boolean onDoubleClick(IRenderer renderer, Point2D screen, Point2D layout);
    public boolean onRightClick(IRenderer renderer, Point2D screen, Point2D layout);
    public boolean onPopupQueryClick(IRenderer renderer, Point2D screen, Point2D layout);

    public boolean onMouseDragged(Point beforeLayout, Point nowLayout);
    public boolean onMouseRelease();
    public boolean onMouseMoved(IRenderer renderer, Point2D layout);

    public void onKeyPressed(char c, int code);
    
    public Point getPrevMouse();
    public KeyMemoryEventDispatcher getMemoryKey();
    public void setKeyMemory(KeyMemoryEventDispatcher memoryKey);
    public Rectangle2D getCurrentViewBounds();
    
    public boolean addMouseHitListener(IMouseHitModelListener listener);
    public boolean removeMouseHitListener(IMouseHitModelListener listener);
}
