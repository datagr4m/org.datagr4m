package org.datagr4m.drawing.layout.algorithms.forces;

import org.jzy3d.maths.Coord2d;

public interface IReviewableForce extends IForce {
    public Coord2d getLastMove();
}
