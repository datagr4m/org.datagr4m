package org.datagr4m.drawing.renderer.pathfinder.view.struct;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleStructure;
import org.datagr4m.drawing.renderer.pathfinder.view.ObstacleRenderer;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;


public class StructRenderer extends AbstractRenderer implements IRenderer{
    protected ObstacleStructure struct;
    protected String highlight = "";
    
    public StructRenderer(ObstacleStructure struct){
        this.struct = struct;
    }
    
    public void highlightPathWith(String name){
        highlight = name;
    }
    
    @Override
    public void render(Graphics2D graphic) {
        graphic.setColor(Color.black);
        for(IPathObstacle o: struct.getObstacles()){
            obstacleRenderer.render(graphic, o, Color.GRAY);
        }
        
        for(Pair<IPathObstacle, IPathObstacle> p: struct.getLinks()){
            if(p.a.getName().equals(highlight))
                pathRenderer.render(graphic, struct.getPath(p), Color.RED);
            else
                pathRenderer.render(graphic, struct.getPath(p), Color.BLACK);
        }
    }
    
    /***************/
    
    @Override
    public List<IClickableItem> hit(int x, int y) {
        return null;
        //throw new RuntimeException("not implemented");
    }

    protected ObstacleRenderer obstacleRenderer = new ObstacleRenderer();
    protected CoordListRenderer pathRenderer = new CoordListRenderer();
}
