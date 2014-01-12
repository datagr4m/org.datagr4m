package org.datagr4m.drawing.renderer.items.shaped;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.BoundsType;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.ModelGeometryProcessor;
import org.datagr4m.drawing.renderer.bounds.BoundsRenderer;
import org.datagr4m.drawing.renderer.bounds.IBoundsRenderer;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.pathfinder.view.slots.SlotableItemRenderer;
import org.datagr4m.drawing.viewer.renderer.AbstractExtendedRenderer;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.DifferedRenderer;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


public abstract class AbstractShapedItemRenderer extends AbstractExtendedRenderer implements IItemRenderer{
    public static boolean ALLOW_SLOT_RENDERING = true;
    
    public AbstractShapedItemRenderer() {
        super();
    }
    
    /*********************/
    
    public abstract Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line);
    public abstract Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line, float additionalRadius);
    public abstract Point2D getFirstIntersectionFor(IBoundedItem item, Line2D line, float additionalWidth, float additionalHeight);

    protected abstract void renderBorderAndBody(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings, Coord2d c, float radius);
    
    /*********************/

    @Override
    public void render(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        Coord2d c = item.getAbsolutePosition();
        if(c==null)
            c = item.getPosition();
        float radius = item.getRadialBounds();
        
        renderBorderAndBody(graphic, item, settings, c, radius);
        renderLabel(graphic, item, settings, c);
        renderSlots(graphic, item, settings);
        renderBounds(graphic, item, settings);
    }
    
    protected void renderBounds(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        if(settings.isNodeBoundsDisplayed(item)){
            Color rawColor = settings.getNodeBoundsTypeColor(BoundsType.RAW);
            render(graphic, ModelGeometryProcessor.shiftToAbsolute(item.getRawRectangleBounds(), item.getAbsolutePosition()), rawColor, "raw", false);

            Color extColor = settings.getNodeBoundsTypeColor(BoundsType.EXTERNAL);
            render(graphic, ModelGeometryProcessor.shiftToAbsolute(item.getRawExternalRectangleBounds(), item.getAbsolutePosition()), extColor, "external", false);
            
            Color corColor = settings.getNodeBoundsTypeColor(BoundsType.CORRIDOR);
            render(graphic, ModelGeometryProcessor.shiftToAbsolute(item.getRawCorridorRectangleBounds(), item.getAbsolutePosition()), corColor, "corridor", false);
            
            //Color intColor = settings.getNodeBoundsTypeColor(BoundsType.INTERNAL);
            //render(graphic, ModelGeometryProcessor.shiftToAbsolute(item.getInternalRectangleBounds(), item.getAbsolutePosition()), intColor, "internal", false);
            
            /*RectangleBounds b = item.getRectangleBounds().clone();
            b.enlargeSelfInPlace(item.getMargin(), item.getMargin());
            b.shiftSelfCenterTo(item.getAbsolutePosition());
            render(graphic, b, Color.RED, "margin", false);
            
            render(graphic, ModelGeometryProcessor.shiftToAbsolute(item.getExternalRectangleBounds(), item.getAbsolutePosition()), Color.BLUE, "external(+margin)", false);
            render(graphic, ModelGeometryProcessor.shiftToAbsolute(item.getCorridorRectangleBounds(), item.getAbsolutePosition()), Color.BLUE, "corridor(+corridor)", false);*/
        }
    }  
    
    public void render(Graphics2D graphic, RectangleBounds bounds, Color borderColor, String info, boolean showGeometryText) {
        graphic.setColor(borderColor);
        
        // Draw border
        drawRect(graphic, bounds.x, bounds.y, bounds.width, bounds.height);
        if(showGeometryText)
            drawText(graphic, bounds.width+"x"+bounds.height+" @ "+bounds.getTopLeftCorner(), bounds.getTopLeftCorner());
        if(info!=null)
            drawText(graphic, info, bounds.getTopLeftCorner().add(0, TextUtils.textHeight()));
    }
    
    LabelRendererHelper helper = new LabelRendererHelper();
    
    protected void renderLabel(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings, final Coord2d c) {
        helper.renderLabel(this, graphic, item, settings, c);
    }
    
    
    
    /**************/

    protected String debugInfo(IBoundedItem item){
        /*if(item instanceof HierarchicalGraphModel){
            final HierarchicalGraphModel hm = (HierarchicalGraphModel)item;
            int nattract = hm.getAttractorForces().size();
            int nrepuls  = hm.getNumberOfRepulsors();
            return " (r:" + nrepuls + ",a:" + nattract + ",children:"+hm.getChildren().size()+",enlarge:"+hm.mustEnlarge()+")";
        }
        else*/
            return "";
    }

    protected void renderSlots(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        if(ALLOW_SLOT_RENDERING)
            slotableItemRenderer.render(graphic, item, settings.getNodeBorderColor(item));
    }
    
    /****************/
    
    @Override
    public void clearDiffered(){
        differed.clear();
    }

    @Override
    public List<DifferedRenderer> getDiffered(){
        return differed;
    }
    
    @Override
    public void addDiffered(List<DifferedRenderer> differed){
        this.differed.addAll(differed);
    }
    
    @Override
    public void addDiffered(DifferedRenderer differed){
        this.differed.add(differed);
    }

    protected IDisplay display;
    protected IHierarchicalNodeModel root;
    protected SlotableItemRenderer slotableItemRenderer;
    
    protected List<DifferedRenderer> differed = new ArrayList<DifferedRenderer>(100);
    
    protected IBoundsRenderer boundsRenderer = new BoundsRenderer();

}