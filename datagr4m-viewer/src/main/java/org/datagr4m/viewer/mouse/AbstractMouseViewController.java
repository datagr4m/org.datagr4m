package org.datagr4m.viewer.mouse;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.hit.IMouseHitModelListener;
import org.datagr4m.viewer.renderer.IRenderer;


/**
 * Provides base implementation of mouse operation and let the use implementation
 * how to deal with mouse behaviour according to its data model
 * @author Martin
 *
 */
public abstract class AbstractMouseViewController extends MouseAdapter implements ILocalizedMouse, KeyListener {
    @Override
	public abstract boolean onLeftClick(IRenderer renderer, Point2D screen, Point2D layout);
    @Override
	public abstract boolean onDoubleClick(IRenderer renderer, Point2D screen, Point2D layout);
    @Override
	public abstract boolean onRightClick(IRenderer renderer, Point2D screen, Point2D layout);
    @Override
	public abstract boolean onPopupQueryClick(IRenderer renderer, Point2D screen, Point2D layout);

    @Override
	public abstract boolean onMouseDragged(Point beforeLayout, Point nowLayout);
    @Override
	public abstract boolean onMouseRelease();
    @Override
	public abstract boolean onMouseMoved(IRenderer renderer, Point2D layout);
    
    @Override
	public abstract void onKeyPressed(char c, int code);
    
    public AbstractMouseViewController(IDisplay display, IView view){
        this.display = display;
        this.view = view;
        this.objectHitListeners = new ArrayList<IMouseHitModelListener>();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // Get picked object
        prevMouse.x = e.getX();
    	prevMouse.y = e.getY();
    	Point2D screenMouse = new Point2D.Float(e.getX(), e.getY());
        Point2D layoutMouse = new Point2D.Float(e.getX(), e.getY());
    	view.screenToModel(layoutMouse);
    	
    	if(AWTMouseUtilities.isDoubleClick(e)){
            onDoubleClick(view.getRenderer(), screenMouse, layoutMouse);
        }
    	else{
    	    // click droit
    	    if(AWTMouseUtilities.isRightDown(e)){
    	        onRightClick(view.getRenderer(), screenMouse, layoutMouse);
    	    }
    	    // click gauche
    	    else{
                boolean didSelect = onLeftClick(view.getRenderer(), screenMouse, layoutMouse);
                if(!didSelect)
                    grabMap = true;   
    	    }    	    
    	}
    	view.refresh();
    	
    	mousePopupIfRelevant(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // try panning the map
    	if(grabMap){
    		doPan(e);
    	}
    	
    	// scale mouse offset
        Point pt2 = e.getPoint();
        Point pt3 = getPrevMouse();
        view.screenToModel(pt2);
        view.screenToModel(pt3);
        
    	// try grabbing an object
        onMouseDragged(pt2, pt3);
    	
    	// update
        prevMouse.x = e.getX();
        prevMouse.y = e.getY();
        
        view.refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        onMouseRelease();
    	grabMap = false;    	
    	view.refresh();    	
    	mousePopupIfRelevant(e);
    }
    
    /** Handling popup triggering if e.isPopupTrigger() is true.
     *  To be called @ mouseReleased for windows, and mousePressed on linux/mac
     */
    protected void mousePopupIfRelevant(MouseEvent e){
    	if(e.isPopupTrigger()){ // CE TEST NE FONCTIONNE QUE DANS MOUSE RELEASE, PAS DANS MOUSE PRESSED 
    	    Point2D screenMouse = new Point2D.Float(e.getX(), e.getY());
            Point2D layoutMouse = new Point2D.Float(e.getX(), e.getY());
            view.screenToModel(layoutMouse);
            onPopupQueryClick(view.getRenderer(), screenMouse, layoutMouse);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        doZoom(e);
    }

    @Override
	public void mouseMoved(MouseEvent e) {
        Point2D mousePoint = new Point2D.Float(e.getX(), e.getY());
        view.screenToModel(mousePoint);
        onMouseMoved(view.getRenderer(), mousePoint);
        view.refresh();
    }
    
    /**********************/

    @Override
    public void keyPressed(KeyEvent e) {
        onKeyPressed(e.getKeyChar(), e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /**********************/
    
    protected void doPan(MouseEvent e) {		
    	Point2D prev = new Point2D.Float(prevMouse.x, prevMouse.y);
    	Point2D mouse = e.getPoint();
        view.screenToModel(prev);
        view.screenToModel(mouse);
        
        float dx = (float) (mouse.getX()-prev.getX());
        float dy = (float) (mouse.getY()-prev.getY());        
        view.pan(dx, dy);   
        
        ((JComponent)display).repaint();
        doViewBoundsRefresh();
    }
    
    protected void doZoom(MouseWheelEvent e){
        float change = -e.getWheelRotation()/10.0f;
        
        wheelZoom = 1+change;
        Point2D from = new Point2D.Float(e.getX(), e.getY());
        view.scale(wheelZoom, wheelZoom, from);
        
        ((JComponent)display).repaint();
        doViewBoundsRefresh();
    }
    
    protected void doViewBoundsRefresh(){
        currentViewBounds = view.getViewBounds();
    }

    /****************/
    
    @Override
    public Rectangle2D getCurrentViewBounds(){
        return currentViewBounds;
    }
    
    protected Point2D mouseInView(MouseEvent e) {
        return mouseInView(e, -display.getSize().width/2, -display.getSize().height/2);
    }

    protected Point2D mouseInView(MouseEvent e, float xoffset, float yoffset) {
        Point2D mouse = e.getPoint();
        view.screenToModel(mouse);
        mouse.setLocation(mouse.getX()+xoffset,mouse.getY()+yoffset);
        return mouse;
    }

    @Override
	public Point getPrevMouse() {
        return prevMouse;
    }

    /****************/
    
    @Override
    public boolean addMouseHitListener(IMouseHitModelListener listener){
        return objectHitListeners.add(listener);
    }
    
    @Override
    public boolean removeMouseHitListener(IMouseHitModelListener listener){
        return objectHitListeners.remove(listener);
    }
    
    protected void fireObjectHit(Object o, Point2D screen, Point2D layout) {
        for (IMouseHitModelListener listener : objectHitListeners)
            listener.objectHit(o, screen, layout);
    }

    protected void fireObjectDragged(Object o, Point2D screen, Point2D layout) {
        for (IMouseHitModelListener listener : objectHitListeners)
            listener.objectDragged(o, screen, layout);
    }

    protected void fireObjectReleased(Object o, Point2D screen, Point2D layout) {
        for (IMouseHitModelListener listener : objectHitListeners)
            listener.objectReleased(o, screen, layout);
    }
    
    protected void fireItemHit(IClickableItem o, List<IClickableItem> items, Point2D screen, Point2D layout) {
        for (IMouseHitModelListener listener : objectHitListeners)
            listener.itemHit(o, items, screen, layout);
    }

    protected void fireItemDragged(IClickableItem o, Point2D screen, Point2D layout) {
        for (IMouseHitModelListener listener : objectHitListeners)
            listener.itemDragged(o, screen, layout);
    }

    protected void fireItemReleased(IClickableItem o, Point2D screen, Point2D layout) {
        for (IMouseHitModelListener listener : objectHitListeners)
            listener.itemReleased(o, screen, layout);
    }

    /****************/

    @Override
    public KeyMemoryEventDispatcher getMemoryKey() {
        return keyMemory;
    }
    
    @Override
    public void setKeyMemory(KeyMemoryEventDispatcher memoryKey) {
        this.keyMemory = memoryKey;
        keyMemory.addKeyListener(this);
    }
    
    /****************/

    protected List<IMouseHitModelListener> objectHitListeners;  

    protected IView view;
    protected IDisplay display;
    protected float wheelZoom = 1;
    protected boolean ALLOW_RESTART = false;
    protected boolean grabMap = false;
    protected Point prevMouse = new Point();
    
    protected Rectangle2D currentViewBounds;
    
    protected KeyMemoryEventDispatcher keyMemory;
}