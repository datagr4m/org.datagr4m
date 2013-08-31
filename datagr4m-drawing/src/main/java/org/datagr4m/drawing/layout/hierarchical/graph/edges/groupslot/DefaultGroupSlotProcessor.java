package org.datagr4m.drawing.layout.hierarchical.graph.edges.groupslot;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.maths.geometry.RectangleUtils;
import org.datagr4m.maths.geometry.ShapeUtils;
import org.jzy3d.maths.Coord2d;


public class DefaultGroupSlotProcessor implements IGroupSlotProcessor{  
    private static final long serialVersionUID = 4673162037587467942L;

    @Override
    public Coord2d computeBorderCoord(IBoundedItem model, Coord2d target){
        // on veut toujours un rond autour d'un item carr�, sinon les tubes directement
        // connect� � un item sont coll�s � la bordure de l'item.
        if(model.getShape()==ItemShape.CIRCLE || !(model instanceof IHierarchicalModel)){
            return computeRadialBorderCoord(model.getAbsolutePosition(), model.getRadialBounds(), target);
        }
        // bordure rectangle
        else{
            return computeRectangleBorderCoord(model, target);
        }
    }

    /* */
    
    protected RectangleBounds selectRectangleBounds(IBoundedItem item) {
        if(item instanceof IHierarchicalModel)
            return item.getRawCorridorRectangleBounds();
        else
            return item.getRawRectangleBounds();
    }
    
    // version simple: le point d'intersection entre source et cible
    protected Coord2d computeRectangleBorderCoord(IBoundedItem model, Coord2d target) {
        Coord2d abs = model.getAbsolutePosition();
        
        double width = selectRectangleBounds(model).width;// + model.getMargin()*2 + model.getCorridor()*2;
        double height = selectRectangleBounds(model).height;// + model.getMargin()*2 + model.getCorridor()*2;
        
        Shape s = RectangleUtils.build(abs, width, height);
        Line2D line = new Line2D.Float(abs.x, abs.y, target.x, target.y);
        Point2D out = ShapeUtils.getFirstIntersection(s, line);
        if(out!=null)
            return Pt.cloneAsCoord2d(out);
        else
            return null;
    }
    
    protected Coord2d computeRadialBorderCoord(Coord2d center, float radius, Coord2d target){
        Coord2d p1b = target.sub(center).fullPolar();
        p1b.y = radius;
        return p1b.cartesian().add(center);
    }
    
    @Override
    public void clear(){
        
    }
}
