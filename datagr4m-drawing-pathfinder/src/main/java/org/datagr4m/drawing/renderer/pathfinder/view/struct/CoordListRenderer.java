package org.datagr4m.drawing.renderer.pathfinder.view.struct;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.jzy3d.maths.Coord2d;

public class CoordListRenderer {
    public void render(Graphics2D graphic, List<Coord2d> coords, Color color){
        graphic.setColor(color);
        if(coords!=null){
            for (int i = 0; i < coords.size()-1; i++) {
                Coord2d c1 = coords.get(i);
                Coord2d c2 = coords.get(i+1);
                graphic.drawLine((int)c1.x, (int)c1.y, (int)c2.x, (int)c2.y);
                graphic.drawString(i+"", (int)c1.x, (int)c1.y);
            }
            if(coords.size()>0){
                Coord2d c = coords.get(coords.size()-1);
                graphic.drawString(coords.size()-1+"", (int)c.x, (int)c.y);
            }
        }
    }
}
