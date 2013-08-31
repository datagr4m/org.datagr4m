package org.datagr4m.drawing.navigation.context;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.viewer.mouse.IClickableItem;


public class NavigationContext {
    public NavigationContext(){
        this(ContextType.DEFAULT, (IBoundedItem)null);
    }
    
    public NavigationContext(ContextType type){
        this(type, (IBoundedItem)null);
    }
    
    public NavigationContext(ContextType type, IBoundedItem item){
        this.type = type;
        this.boundedItem = item;
    }
    
    public NavigationContext(ContextType type, IClickableItem item){
        this.type = type;
        this.clickableItem = item;
    }
    
    public ContextType getType() {
        return type;
    }
    public void setBoundedType(ContextType type) {
        this.type = type;
    }
    public IBoundedItem getBoundedItem() {
        return boundedItem;
    }
    public void setItem(IBoundedItem item) {
        this.boundedItem = item;
    }
    public IClickableItem getClickableItem() {
        return clickableItem;
    }
    public void setClickableItem(IClickableItem clickableItem) {
        this.clickableItem = clickableItem;
    }
    

    public Point2D getScreen() {
        return screen;
    }

    public void setScreen(Point2D screen) {
        this.screen = screen;
    }

    public Point2D getLayout() {
        return layout;
    }

    public void setLayout(Point2D layout) {
        this.layout = layout;
    }

    public boolean is(ContextType ctx){
        if(type==null)
            return ctx==null;
        else
            return type.equals(ctx);
    }
    
    protected ContextType type;
    protected IBoundedItem boundedItem;
    protected IClickableItem clickableItem;
    protected Point2D screen;
    protected Point2D layout;
}
