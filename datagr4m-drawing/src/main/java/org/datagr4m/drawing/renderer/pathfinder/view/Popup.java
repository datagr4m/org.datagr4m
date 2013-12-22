package org.datagr4m.drawing.renderer.pathfinder.view;

import java.awt.geom.Point2D;

public class Popup {
    public Popup(String message, Point2D position) {
        this.message = message;
        this.position = position;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Point2D getPosition() {
        return position;
    }
    public void setPosition(Point2D position) {
        this.position = position;
    }
    protected String message;
    protected Point2D position;
}
