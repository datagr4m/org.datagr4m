package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.QuadCurve2D;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.viewer.renderer.AbstractExtendedRenderer;
import org.jzy3d.maths.Coord2d;


public class LocalEdgeRenderer extends AbstractExtendedRenderer implements IEdgeRenderer{
    public LocalEdgeRenderer(IHierarchicalNodeModel model){
        this.model = model;
    }
    
    @Override
    public void render(Graphics2D graphic, Pair<IBoundedItem, IBoundedItem> edge, IEdgeRendererSettings settings) {
        Coord2d c1 = model.getAbsolutePosition(edge.a);
        Coord2d c2 = model.getAbsolutePosition(edge.b);
        
        if(c1==null)
            throw new RuntimeException("Missing absolute position for: " + edge.a);
        if(c2==null)
            throw new RuntimeException("Missing absolute position for: " + edge.b);
        
        Color color = settings.getEdgeColor(edge);
        if(color==null)
            color = Color.black;
        graphic.setColor(color);
        
        if(settings.isEdgeDisplayed(edge)){
            if(settings.isEdgeStraight(edge))
                drawLine(graphic, c1, c2);
            else
                drawCurve(graphic, c1, c2);
        }
    }
    
    protected void drawCurve(Graphics2D graphic, Coord2d c1, Coord2d c2){
        instance.setCurve(c1.x, c1.y, (c1.x+c2.x)/2, (c1.y*0.3+c2.y*0.7), c2.x, c2.y);
        graphic.draw(instance);
    }
    
    protected static QuadCurve2D instance = new QuadCurve2D.Float();

    protected IHierarchicalNodeModel model;
}
