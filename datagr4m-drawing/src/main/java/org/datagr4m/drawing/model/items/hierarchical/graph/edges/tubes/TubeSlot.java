package org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes;

import java.awt.geom.Point2D;

public class TubeSlot {
    public TubeSlot(Point2D position, float orientation) {
        this.position = position;
        this.orientation = orientation;
    }
    
    public Point2D getPosition() {
        return position;
    }
    public void setPosition(Point2D position) {
        this.position = position;
    }
    public float getOrientation() {
        return orientation;
    }
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }
    
    protected Point2D position;
    protected float orientation;
}
