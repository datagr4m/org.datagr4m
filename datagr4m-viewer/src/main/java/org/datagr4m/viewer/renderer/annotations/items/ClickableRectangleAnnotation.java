package org.datagr4m.viewer.renderer.annotations.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.CellAnchor;
import org.jzy3d.maths.Coord2d;


public class ClickableRectangleAnnotation extends AbstractRenderer implements IClickableAnnotation{
    private static final long serialVersionUID = -2573183619877326652L;
    protected static int AUTO_SIZE = -1;
    
    public ClickableRectangleAnnotation(Point2D position, String txt){
        this(position, txt, 1, AUTO_SIZE, AUTO_SIZE);
    }
    
    public ClickableRectangleAnnotation(String txt, int boxMargin){
        this(new Point2D.Float(), txt, 1, AUTO_SIZE, AUTO_SIZE);
    }
    
    public ClickableRectangleAnnotation(String txt, int boxMargin, int width, int height){
        this(new Point2D.Float(), txt, boxMargin, width, height);
    }
    
    public ClickableRectangleAnnotation(Point2D position, String txt, int boxMargin, int width, int height){
		this.name = txt;
		this.position = position;
		this.rectangle = new Rectangle2D.Double();
		this.anchor = CellAnchor.CENTER;
		this.boxMargin = boxMargin;
		this.width = width;
		this.height = height;
		//updateGeometry();
	}
    public ClickableRectangleAnnotation(Coord2d position, String txt){
        this(Pt.cloneAsDoublePoint(position), txt);
    }
    
	@Override
	public void render(Graphics2D graphic){
	    if(isAutoSize())
	        drawTextCell(graphic, name, position, txtColor, bdColor, bgColor, anchor, boxMargin, false);
	    else
	        drawTextCell(graphic, name, position, txtColor, bdColor, bgColor, anchor, boxMargin, width, height, false);
	}
	
	/********* ATTRIBUTES *********/
	
	@Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point2D position) {
        this.position = position;
        //updateGeometry();
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
    
	public CellAnchor getAnchor() {
        return anchor;
    }

    public void setAnchor(CellAnchor anchor) {
        this.anchor = anchor;
    }

    public boolean isAutoSize(){
        return width==AUTO_SIZE;
    }
    
    /*********** HIT TEST **********/

	@Override
	public List<IClickableItem> hit(int x, int y){
        if(rectangle!=null && rectangle.contains(x, y))
            return list(this);
        else
            return null;
    }

	public static List<IClickableItem> list(IClickableItem item){
        List<IClickableItem> list = new ArrayList<IClickableItem>(1);
        list.add(item);
        return list;
    }
	
	/*********************/
	
	protected void updateGeometry(){
	    if(rectangle!=null && position!=null)
	        rectangle.setFrame(position.getX()-txtWidth/2, position.getY()-txtWidth/2, txtWidth, txtWidth);
    }
	
	/*********************/
    
	protected Rectangle2D rectangle;
	protected String name;
	protected Point2D position;
	protected int boxMargin;
	protected float txtWidth;
	protected CellAnchor anchor;
	
	protected Color bgColor = Color.white;
    protected Color bdColor = Color.black;
    protected Color txtColor = Color.black;
    protected int width;
    protected int height;
}
