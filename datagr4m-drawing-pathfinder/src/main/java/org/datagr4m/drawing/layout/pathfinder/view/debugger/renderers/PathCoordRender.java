package org.datagr4m.drawing.layout.pathfinder.view.debugger.renderers;

import java.awt.Color;
import java.awt.Graphics2D;

import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.viewer.renderer.DefaultRenderer;
import org.jzy3d.maths.Coord2d;


public class PathCoordRender extends DefaultRenderer{
    public void render(Graphics2D graphic, IPath path, Color color, boolean showExtremity){
        if(path.getCoords()!=null){
            for (int i = 0; i < path.getCoords().size()-1; i++) {
                Coord2d c1 = path.getCoords().get(i);
                Coord2d c2 = path.getCoords().get(i+1);
                
                // show extremity
                if(showExtremity && path.isFirst(i)){
                    graphic.setColor(Color.GREEN);
                    drawRect(graphic, c1.x, c1.y, 20, 20, -10, -10);
                }
                if(showExtremity && path.isLast(i+1)){
                    graphic.setColor(Color.GREEN);
                    drawRect(graphic, c2.x, c2.y, 20, 20, -10, -10);
                }
                
                // show segment
                graphic.setColor(color);
                graphic.drawLine((int)c1.x, (int)c1.y, (int)c2.x, (int)c2.y);
                
                // show colored id
                if(path.getLock(i))
                    graphic.setColor(Color.RED);
                else
                    graphic.setColor(Color.BLACK);
                graphic.drawString(i+"", (int)c1.x, (int)c1.y);
            }
            
            // show last
            if(path.getCoords().size()>0){
                int id = path.getCoords().size()-1;
                if(path.getLock(id))
                    graphic.setColor(Color.RED);
                else
                    graphic.setColor(Color.BLACK);
                Coord2d c = path.getCoords().get(id);
                graphic.drawString(path.getCoords().size()-1+"", (int)c.x, (int)c.y);
            }
        }
    }
}
