package org.datagr4m.drawing.layout.pathfinder.view.debugger.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.renderer.pathfinder.view.ObstacleRenderer;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;


public class PathFinderDebuggerRenderer extends AbstractRenderer implements IRenderer{
    protected List<IPathObstacle> obstacles;
    protected IPath path;
    protected List<Point2D> highlights;
    protected List<Point2D> errors;
    protected IPathObstacle currentObstacle;

    protected List<IPath> computedPath = new ArrayList<IPath>();
    
    public PathFinderDebuggerRenderer(){
    }
    
    public void setContent(List<IPathObstacle> obstacles, IPath path){
        this.path = path;
        this.obstacles = obstacles;
    }

    public void setHighlightPoints(List<Point2D> p){
        highlights = p;
    }
    
    public void setErrorPoints(List<Point2D> p){
        errors = p;
    }
    
    public void setCurrentObstacle(IPathObstacle currentObstacle) {
        this.currentObstacle = currentObstacle;
    }
    
    public void addComputedPath(IPath p){
        computedPath.add(p);
    }

    @Override
    public void render(Graphics2D graphic) {
        graphic.setColor(Color.black);
        
        // obstacles
        if(obstacles!=null)
            for(IPathObstacle o: obstacles){
                if(o==currentObstacle)
                    obstacleRenderer.render(graphic, o, Color.PINK);
                else
                    obstacleRenderer.render(graphic, o, Color.GRAY);
            }
        
        // paths
        if(computedPath!=null)
            for(IPath p: computedPath)
                pathRenderer.render(graphic, p, Color.GRAY, false);
        if(path!=null)
            pathRenderer.render(graphic, path, Color.RED, true);
        
        // highlights & errors
        if(highlights!=null)
            drawCircles(graphic, highlights, 15, Color.BLUE);
        if(errors!=null)
            drawCircles(graphic, errors, 15, Color.RED);
    }
    
    
    
    
    

    /***************/
    

    @Override
    public List<IClickableItem> hit(int x, int y) {
        return null;
        //throw new RuntimeException("not implemented");
    }

    protected PathCoordRender pathRenderer = new PathCoordRender();
    protected ObstacleRenderer obstacleRenderer = new ObstacleRenderer();
}
