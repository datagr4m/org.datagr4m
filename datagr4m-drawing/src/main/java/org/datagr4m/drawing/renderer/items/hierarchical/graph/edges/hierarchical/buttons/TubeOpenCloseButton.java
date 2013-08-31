package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.buttons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.viewer.renderer.annotations.items.ClickableCircleAnnotation;


/**
 * Hold an open/close status, and renders according to this status
 */
public class TubeOpenCloseButton extends ClickableCircleAnnotation{
    private static final long serialVersionUID = -3050541244985715048L;

    public TubeOpenCloseButton(Tube tube, Point2D position, float radius, String name1){
        super(position, radius, name1);
        this.tube = tube;
        this.crossWidth = Math.max(radius/10, 4);
    }

    @Override
	public void render(Graphics2D graphic){
        render(graphic, bdColor, bgColor, signColor);
    }
    public void render(Graphics2D graphic, Color border, Color background, Color sign){
        graphic.setPaint(background);
        graphic.fill(circle);
        graphic.setPaint(border);
        graphic.draw(circle);
        
        if(position!=null){
            graphic.setColor(sign);
            if(isOpen())
                renderOpened(graphic);
            else
                renderClosed(graphic);
        }
    }
    
    protected void renderOpened(Graphics2D graphic){
        fillRectCentered(graphic, position, radius, crossWidth);
    }
    
    protected void renderClosed(Graphics2D graphic){
        fillRectCentered(graphic, position, radius, crossWidth);
        fillRectCentered(graphic, position, crossWidth, radius);
    }
    
    /***********/
    
    public boolean isOpen(){
        return open;
    }
    
    public void open(){
        open = true;
    }

    public void close(){
        open = false;
    }
    
    public void toggleOpenStatus(){
        open = !open;
    }

    /***********/
    
    public Tube getTube() {
        return tube;
    }
    
    public Ellipse2D getCircle(){
        return circle;
    }

    protected Tube tube;
    protected boolean open = true;
    protected float crossWidth;
    
    protected Color signColor = Color.RED;
}
