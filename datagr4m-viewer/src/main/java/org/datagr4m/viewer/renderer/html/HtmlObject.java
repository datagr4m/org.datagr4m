package org.datagr4m.viewer.renderer.html;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class HtmlObject {
    public HtmlObject(String html, Point2D position, float width, float height) {
        this.html = html;
        this.point = position;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle((int)point.getX(), (int)point.getY(), (int)width, (int)height);
    }
    
    public String getHtml() {
        return html;
    }
    public void setHtml(String html) {
        this.html = html;
    }
    public Rectangle getRectangle() {
        return rectangle;
    }
    
    protected String html;
    protected Rectangle rectangle;
    protected Point2D point;
    protected float width;
    protected float height;
}
