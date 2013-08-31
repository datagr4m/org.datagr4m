package org.datagr4m.drawing.model.items.annotations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.DefaultRenderer;
import org.datagr4m.viewer.renderer.hit.IHitProcessor;


public class ClickableRectangleAnnotationItem extends DefaultBoundedItem implements IClickableItemAnnotation{
    private static final long serialVersionUID = -2573183619877326652L;

    public ClickableRectangleAnnotationItem(Object o, String label){
        super(o, label);
		this.name = label;
		this.rectangle = new Rectangle2D.Double();
		updateGeometry();
	}
    
	@Override
	public void render(Graphics2D graphic){
	    if(visible)
	        renderer.drawTextCell(graphic, name, position, txtColor, bdColor, bgColor);
	}
	
	/********* ATTRIBUTES *********/
	    
    /**
     * Change position by copying input coordinate values into self coordinate values.
     * Parent is set dirty
     */
    @Override
    public void changePosition(float x, float y){
        super.changePosition(x, y);
        updateGeometry();
    }
    
    /**
     * Change position by adding input coordinate values to self coordinate values.
     */
    @Override
    public void shiftPosition(float x, float y){
        super.shiftPosition(x, y);
        updateGeometry();
    }
	
    @Override
	public Color getBackgroundColor() {
		return bgColor;
	}

    @Override
	public void setBackgroundColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	
	@Override
    public Color getBorderColor() {
        return bdColor;
    }

    @Override
    public void setBorderColor(Color color) {
        bdColor = color;
    }

    @Override
    public Color getTextColor() {
        return txtColor;
    }

    @Override
    public void setTextColor(Color color) {
        this.txtColor = color;
    }
	
	/*********** HIT TEST **********/

	@Override
	public List<IClickableItem> hit(int x, int y){
        if(rectangle!=null && rectangle.contains(x, y))
            return list(this);
        else
            return null;
    }
	
	@Override
    public <T> List<IClickableItem> hitOnly(int x, int y, Class<T> type){
        if(type.isInstance(this))
            return hit(x, y);
        else
            return null;
    }
    
    @Override
    public <T> List<IClickableItem> hitExcluding(int x, int y, Class<T> type){
        if(!type.isInstance(this))
            return hit(x, y);
        else
            return null;
    }
    
    @Override
    public IHitProcessor getHitProcessor() {
        return this;
    }

    @Override
    public void setHitProcessor(IHitProcessor hitProcessor) {
        throw new RuntimeException("unsupported, is a processor itself");
    }

	public static List<IClickableItem> list(IClickableItem item){
        List<IClickableItem> list = new ArrayList<IClickableItem>(1);
        list.add(item);
        return list;
    }
	
	/*********************/
	
	protected void updateGeometry(){
	    if(rectangle!=null && position!=null)
	        rectangle.setFrame(position.x-txtWidth/2, position.y-txtWidth/2, txtWidth, txtWidth);
    }
	
	/*********************/
    
	protected Rectangle2D rectangle;
	protected String name;
	protected float txtWidth;
	
	protected Color bgColor = Color.white;
    protected Color bdColor = Color.black;
    protected Color txtColor = Color.black;

    protected static AbstractRenderer renderer = new DefaultRenderer();
}
