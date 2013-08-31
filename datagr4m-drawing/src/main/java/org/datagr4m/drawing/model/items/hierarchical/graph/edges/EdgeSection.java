package org.datagr4m.drawing.model.items.hierarchical.graph.edges;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;


public class EdgeSection {
    public EdgeSection(IEdge edge) {
        this(edge, 0, edge.getPathGeometry().getPointNumber()-1);
    }

    public EdgeSection(IEdge edge, int startId, int stopId) {
        this.edge = edge;
        this.startId = startId;
        this.stopId = stopId;
    }
    
    public IEdge getEdge() {
        return edge;
    }
    public int getStartId() {
        return startId;
    }
    public int getStopId() {
        return stopId;
    }
    public Point2D getStartPoint() {
        return edge.getPathGeometry().getPoint(startId);
    }
    public Point2D getStopPoint() {
        return edge.getPathGeometry().getPoint(stopId);
    }

    /*******/

    public void updateStartId(int start) {
        startId = start;
    }
    public void updateStopId(int stop) {
        stopId = stop;
    }
    
    @Override
	public String toString(){
        return "from " + edge.getSourceItem().getObject() + " to " + edge.getTargetItem().getObject() + "  start=" + startId + " stop=" + stopId;
    }

    /*******/
    
    public List<Point2D> getSection(){
        List<Point2D> section = new ArrayList<Point2D>();

        IPath p = edge.getPathGeometry();
        for (int i = startId; i <= stopId; i++)
            section.add(p.getPoint(i));
        return section;
    }
    
    public IPath getPathSection(){
        List<Point2D> points = getSection();
        return new LockablePath(points);
    }

    protected IEdge edge;
    protected int startId;
    protected int stopId;
}
