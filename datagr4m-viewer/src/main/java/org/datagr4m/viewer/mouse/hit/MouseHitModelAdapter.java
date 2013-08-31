package org.datagr4m.viewer.mouse.hit;

import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.viewer.mouse.IClickableItem;


public class MouseHitModelAdapter implements IMouseHitModelListener {   
    @Override
    public void objectHit(Object object, Point2D screen, Point2D layout) {
    }
    @Override
    public void objectDragged(Object object, Point2D screen, Point2D layout) {
    }
    @Override
    public void objectReleased(Object object, Point2D screen, Point2D layout) {
    }
    
    @Override
    public void itemHit(IClickableItem item, List<IClickableItem> all, Point2D screen, Point2D layout) {
    }
    @Override
    public void itemDragged(IClickableItem item, Point2D screen, Point2D layout) {
    }
    @Override
    public void itemReleased(IClickableItem item, Point2D screen, Point2D layout) {
    }
}
