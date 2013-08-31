package org.datagr4m.viewer.renderer.annotations;

import java.awt.Color;

import org.datagr4m.viewer.model.annotations.Annotation;
import org.datagr4m.viewer.renderer.CellAnchor;


public class AnnotationRendererSettings implements IAnnotationRendererSettings {
    @Override
    public Color getTextColor(Annotation annotation) {
        return textColor;
    }
    @Override
    public void setTextColor(Annotation annotation, Color textColor) {
        this.textColor = textColor;
    }
    @Override
    public Color getBorderColor(Annotation annotation) {
        return borderColor;
    }
    @Override
    public void setBorderColor(Annotation annotation, Color borderColor) {
        this.borderColor = borderColor;
    }
    @Override
    public Color getBodyColor(Annotation annotation) {
        return bodyColor;
    }
    @Override
    public void setBodyColor(Annotation annotation, Color bodyColor) {
        this.bodyColor = bodyColor;
    }
    @Override
    public CellAnchor getAnchor(Annotation annotation) {
        return anchor;
    }
    @Override
    public void setAnchor(Annotation annotation, CellAnchor anchor) {
        this.anchor = anchor;
    }
    @Override
    public int getBoxMargin(Annotation annotation) {
        return boxMargin;
    }
    @Override
    public void setBoxMargin(Annotation annotation, int boxMargin) {
        this.boxMargin = boxMargin;
    }
    @Override
    public int getWidth(Annotation annotation) {
        return width;
    }
    @Override
    public void setWidth(Annotation annotation, int width) {
        this.width = width;
    }
    @Override
    public int getHeight(Annotation annotation) {
        return height;
    }
    @Override
    public void setHeight(Annotation annotation, int height) {
        this.height = height;
    }
    @Override
    public boolean isDrawAnchor(Annotation annotation) {
        return drawAnchor;
    }
    @Override
    public void setDrawAnchor(Annotation annotation, boolean drawAnchor) {
        this.drawAnchor = drawAnchor;
    }
    
    protected Color textColor = Color.BLACK;
    protected Color borderColor = Color.BLACK;
    protected Color bodyColor = Color.WHITE;
    protected CellAnchor anchor = CellAnchor.BOTTOM_LEFT;
    protected int boxMargin = 3;
    protected int width = AUTO_SIZE;
    protected int height = AUTO_SIZE;
    protected boolean drawAnchor = true;

    /*protected Map<Annotation,Color> textColors = new HashMap<Annotation,Color>();
    protected Color borderColor = Color.BLACK;
    protected Color bodyColor = Color.WHITE;
    protected CellAnchor anchor = CellAnchor.BOTTOM_LEFT;
    protected int boxMargin = 3;
    protected int width = AUTO_SIZE;
    protected int height = AUTO_SIZE;
    protected boolean drawAnchor = true;*/

    
    
    public static int AUTO_SIZE = -1;
}
