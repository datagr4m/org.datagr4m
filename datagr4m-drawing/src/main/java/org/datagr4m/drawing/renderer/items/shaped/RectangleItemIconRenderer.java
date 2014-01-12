package org.datagr4m.drawing.renderer.items.shaped;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.pathfinder.view.slots.SlotableItemRenderer;
import org.datagr4m.maths.geometry.ShapeUtils;
import org.datagr4m.viewer.IDisplay;
import org.jzy3d.maths.Coord2d;


/** The item must be a DeviceKey.*/
public class RectangleItemIconRenderer extends AbstractShapedItemIconRenderer implements IItemRenderer{
    protected Rectangle2D shape;

    public RectangleItemIconRenderer(IHierarchicalNodeModel root, IDisplay display){
        this.root = root;
        this.display = display;
        this.slotableItemRenderer = new SlotableItemRenderer();

        this.shape = initRectangle();
    }

    protected RectangleBounds selectRectangleBounds(IBoundedItem item) {
        if(item instanceof IHierarchicalNodeModel)
            return item.getRawExternalRectangleBounds();
        else
            return item.getRawRectangleBounds();
    }

    /***********/

    private static int RECT_ARC = 10;

    @Override
	protected void renderBorderAndBody(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings, Coord2d absolutePosition, float radius) {
        updateRectangle(shape, selectRectangleBounds(item), absolutePosition);

        if (item.getState().equals(ItemState.MOUSE_OVER))
            renderSelectedBorderAndBody(graphic, item, settings);
        else
            renderDefaultBorderAndBody(graphic, item, settings);
    }
    
    private void renderSelectedBorderAndBody(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        if(settings.isNodeBodyDisplayed(item)) {
            Color nodeColor = settings.getNodeBodyColor(item);
            drawRoundRectangle(graphic, shape, null, /*new Color(242, 242, 242)*/nodeColor, RECT_ARC);
        }
        
        // border
        if(settings.isNodeBorderDisplayed(item)){
            Color borderColor = settings.getNodeBorderColor(item);
            final Stroke init = graphic.getStroke();
            paintOutterGlowRectangle(graphic, shape, borderColor, RECT_ARC);
            // paintInnerGlowBorder(graphic, shape, Color.DARK_GRAY, RECT_ARC);
            graphic.setStroke(new BasicStroke(3.f));
            drawRoundRectangle(graphic, shape, borderColor, null, RECT_ARC);
            graphic.setStroke(init);

        
            /*paintOutterGlowRectangle(graphic, shape, Color.DARK_GRAY, RECT_ARC);
            graphic.setStroke(new BasicStroke(3.f));
            drawRoundRectangle(graphic, shape, Color.DARK_GRAY, null, RECT_ARC);*/

        }
    }

    private void renderDefaultBorderAndBody(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        if(settings.isNodeBodyDisplayed(item)) {
            Color nodeColor = settings.getNodeBodyColor(item);
            drawRoundRectangle(graphic, shape, null, nodeColor/*new Color(242, 242, 242)*/, RECT_ARC);
        }
        // border
        if(settings.isNodeBorderDisplayed(item)){
            Color borderColor = settings.getNodeBorderColor(item);
            drawRoundRectangle(graphic, shape, borderColor, null, RECT_ARC);
        }
    }

    @Override
    public boolean hit(IBoundedItem item, int x, int y){
        updateRectangle(shape, selectRectangleBounds(item), item.getAbsolutePosition());
        return shape.contains(x, y);
    }

    @Override
    public Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line) {
        updateRectangle(shape, selectRectangleBounds(item), item.getAbsolutePosition());
        return ShapeUtils.getFirstIntersection(shape, line);
    }

    @Override
    public Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line, float additionalRadius) {
        updateRectangle(shape, selectRectangleBounds(item).enlarge(additionalRadius, additionalRadius), item.getAbsolutePosition());
        return ShapeUtils.getFirstIntersection(shape, line);
    }

    @Override
    public Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line, float additionalWidth, float additionalHeight) {
        updateRectangle(shape,
                selectRectangleBounds(item).enlarge(additionalWidth, additionalHeight),
                item.getAbsolutePosition().sub(additionalWidth/2, additionalHeight/2));
        return ShapeUtils.getFirstIntersection(shape, line);
    }
}
