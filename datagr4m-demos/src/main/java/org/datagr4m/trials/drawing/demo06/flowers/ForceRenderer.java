package org.datagr4m.trials.drawing.demo06.flowers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.polar.PolarAngleAttractionForce2;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.polar.PolarDistanceRangeForce2;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.layout.algorithms.forces.IReviewableForce;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;
import org.jzy3d.maths.Coord2d;

public class ForceRenderer extends AbstractRenderer implements IRenderer{
    public ForceRenderer(IHierarchicalGraphModel forceModel){
        this.forceModel = forceModel;
    }
    
    @Override
    public void render(Graphics2D graphic){
        
        for(IBoundedItem item: forceModel.getChildren()){
            Collection<IForce> repulsors = forceModel.getRepulsors(item);
            Collection<IForce> attractors = forceModel.getAttractors(item);
            
            renderForces(graphic, repulsors);
            renderForces(graphic, attractors);
        }
    }

    private void renderForces(Graphics2D graphic, Collection<IForce> repulsors) {
        for(IForce r: repulsors){
            if(r instanceof IReviewableForce){
                renderVector(graphic, (IReviewableForce)r);
            }   
        }
    }
    
    public void renderVector(Graphics2D graphic, IReviewableForce force){
        
        if(force.getLastMove()!=null){
            graphic.setColor(getColor(force));
            Coord2d self = force.getOwner().getPosition();
            Coord2d dir = self.add(force.getLastMove().mul(5));
            drawLine(graphic, self, dir);
        }
    }
    
    protected Color getColor(IReviewableForce force){
        if(force instanceof PolarAngleAttractionForce2)
            return Color.BLUE;
        else if(force instanceof PolarDistanceRangeForce2)
            return Color.PINK;
        else
            return Color.BLACK;
    }
    
    protected IHierarchicalGraphModel forceModel;
}
