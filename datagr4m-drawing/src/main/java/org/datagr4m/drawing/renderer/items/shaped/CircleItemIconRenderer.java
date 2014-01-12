package org.datagr4m.drawing.renderer.items.shaped;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.pathfinder.view.slots.SlotableItemRenderer;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;
import org.datagr4m.maths.geometry.ShapeUtils;
import org.datagr4m.viewer.IDisplay;
import org.jzy3d.maths.Coord2d;


/** The item must be a DeviceKey.*/
public class CircleItemIconRenderer extends AbstractShapedItemIconRenderer implements IItemRenderer{
    protected Ellipse2D shape;

    public CircleItemIconRenderer(IHierarchicalNodeModel root, IDisplay display){
        this.root = root;
        this.display = display;
        this.slotableItemRenderer = new SlotableItemRenderer();
        
        this.shape = initCircle();
    }
    
    /***********/
    
    @Override
	protected void renderBorderAndBody(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings, Coord2d c, float radius) {
        updateCircle(shape, c, radius);
        
        //if(item instanceof IHierarchicalModel && item.getLabel().contains("fw"))
        //    System.out.println("ploum");
        
        // roll over body display
        if(item.getState().equals(ItemState.MOUSE_OVER)){
            //if(!(item instanceof IHierarchicalPairModel)){
                if(settings.isNodeBodyDisplayed(item)){
                    Color nodeColor = settings.getNodeBodyColor(item);
                    if(nodeColor!=null)
                        drawCircle(graphic, shape, null, nodeColor.brighter());
                    else
                        drawCircle(graphic, shape, null, null);
                }
                else
                    drawCircle(graphic, shape, null, DefaultStyleSheet.SELECTED_ITEM_COLOR);
            //}
        }
        // default body display
        else{
            if(settings.isNodeBodyDisplayed(item)){
                Color nodeColor = settings.getNodeBodyColor(item);
                drawCircle(graphic, shape, null, nodeColor);
            }
        }
        
        // border
        if(settings.isNodeBorderDisplayed(item)){
            Color borderColor = settings.getNodeBorderColor(item);
            if(borderColor==null)
                borderColor = Color.red;
            drawCircle(graphic, shape, borderColor, null);
        }
        //drawText(graphic, "r="+item.getRadialBounds(), item.getAbsolutePosition());
    }
    
    @Override
    public boolean hit(IBoundedItem item, int x, int y){
        updateCircle(shape, item.getAbsolutePosition(), item.getRadialBounds());
        return shape.contains(x, y);
    }

    @Override
    public Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line) {
        updateCircle(shape, item.getAbsolutePosition(), item.getRadialBounds());
        return ShapeUtils.getFirstIntersection(shape, line);
    }
    
    @Override
    public Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line, float additionalRadius) {
        updateCircle(shape, item.getAbsolutePosition(), item.getRadialBounds()+additionalRadius);
        return ShapeUtils.getFirstIntersection(shape, line);
    }

    @Override
    public Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line, float additionalWidth, float additionalHeight) {
        updateCircle(shape, item.getAbsolutePosition(), item.getRadialBounds()+Math.max(additionalWidth,additionalHeight));
        return ShapeUtils.getFirstIntersection(shape, line);
    }
    
}
