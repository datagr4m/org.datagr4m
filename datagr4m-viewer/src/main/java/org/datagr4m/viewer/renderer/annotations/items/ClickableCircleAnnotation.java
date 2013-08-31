package org.datagr4m.viewer.renderer.annotations.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.TextUtils;


public class ClickableCircleAnnotation extends AbstractRenderer implements IClickableAnnotation{
    private static final long serialVersionUID = -2573183619877326652L;

    public ClickableCircleAnnotation(Point2D position, float radius, String name1){
		this.name = name1;
		this.position = position;
		this.radius = radius;
		this.circle = new Ellipse2D.Double();
		updateGeometry();
	}
    
	@Override
	public void render(Graphics2D graphic){
		graphic.setPaint(bgColor);
		graphic.fill(circle);
        graphic.setPaint(bdColor);
        graphic.draw(circle);
		
		int height = TextUtils.textHeight();
		int width = TextUtils.textWidth(name);

		renderLabel(graphic, (int)position.getX()-width/2, (int)position.getY()+height, name);
	}
	
	protected void renderLabel(Graphics2D graphic, float x, float y, String label){
		int height = TextUtils.textHeight();
		int width = TextUtils.textWidth(label);

		// label border
		graphic.setPaint(bgColor);
		graphic.fillRect((int)x, (int)y-height+1, width, height);
		graphic.setPaint(Color.black);
		graphic.drawRect((int)x, (int)y-height+1, width, height);

		// label string
		graphic.setPaint(txtColor);
		graphic.setFont(TextUtils.font);
		graphic.drawString(label, x, y);
	}
	
	/********* ATTRIBUTES *********/
	
	public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }	
	
	@Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point2D position) {
        this.position = position;
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
        if(circle!=null && circle.contains(x, y))
            return listClick(this);
        else
            return null;
    }
	
	/*********************/
	
	protected void updateGeometry(){
	    if(circle!=null && position!=null)
	        circle.setFrame(position.getX()-radius, position.getY()-radius, radius*2, radius*2);
    }
	
	/*********************/
    
	protected Ellipse2D circle;
	protected String name;
	protected Point2D position;
	protected float radius;
	
	protected Color bgColor = Color.white;
    protected Color bdColor = Color.black;
    protected Color txtColor = Color.black;
    

    
}
