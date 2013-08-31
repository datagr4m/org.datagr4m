package org.datagr4m.viewer.mouse;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.renderer.IRenderer;


public class DefaultMouseViewController extends AbstractMouseViewController{
    public DefaultMouseViewController(IDisplay display, IView view) {
        super(display, view);
    }

    @Override
    public boolean onLeftClick(IRenderer renderer, Point2D screen, Point2D layout) {
        return false;
    }

    @Override
    public boolean onMouseDragged(Point before, Point now) {
        return false;
    }

    @Override
    public boolean onMouseRelease() {
        return false;
    }

    @Override
    public boolean onMouseMoved(IRenderer renderer, Point2D mousePoint) {
        return false;
    }
    
    @Override
    public void onKeyPressed(char c, int code){
        
    }

    @Override
    public boolean onDoubleClick(IRenderer renderer, Point2D screen, Point2D layout) {
        return false;
    }
    
    @Override
    public boolean onRightClick(IRenderer renderer, Point2D screen, Point2D layout) {
        return false;
    }
    
    @Override
    public boolean onPopupQueryClick(IRenderer renderer, Point2D screen, Point2D layout) {
        return false;
    }
}
