package org.datagr4m.drawing.layout.pathfinder;

import java.io.Serializable;
import java.util.List;

import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.jzy3d.maths.Coord2d;


public interface IPathFinder extends Serializable {
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to) throws PathFinderException;
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to, List<IPathObstacle> obstacles) throws PathFinderException;
    public List<Coord2d> find(IPathObstacle from, IPathObstacle to, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException;

    public void find(IPathObstacle from, IPathObstacle to, IPath path, List<IPathObstacle> obstacles, boolean ignoreDebugger) throws PathFinderException;

}
