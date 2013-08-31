package org.datagr4m.drawing.model.pathfinder.path;

import java.awt.geom.Point2D;

public interface IPathFactory {
    public IPath newPath();
    public IPath newPath(Point2D p1, Point2D p2, boolean lock1, boolean lock2);
}