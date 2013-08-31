package org.datagr4m.drawing.model.bounds;

import java.awt.Dimension;
import java.io.Serializable;

import org.jzy3d.maths.Coord2d;


public interface IBounds extends Serializable {
    public boolean isIn(float x, float y);
    public Coord2d correct(float x, float y);
    public Coord2d getCenter();
    public Dimension getDimension();
}