package org.datagr4m.drawing.model.pathfinder.path;

import java.awt.geom.Point2D;

public class PathFactory implements IPathFactory {
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPathFactory#getPath()
     */
    @Override
    public IPath newPath(){
        return new SimplePath();
    }
    /* (non-Javadoc)
     * @see com.netlight.pathfinder.path.IPathFactory#getPath()
     */
    @Override
    public IPath newPath(Point2D p1, Point2D p2, boolean lock1, boolean lock2) {
        return new SimplePath(p1, p2);
    }
    
    
}
