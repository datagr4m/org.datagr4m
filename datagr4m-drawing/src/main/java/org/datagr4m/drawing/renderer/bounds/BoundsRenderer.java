package org.datagr4m.drawing.renderer.bounds;

import java.awt.Color;
import java.awt.Graphics2D;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.viewer.renderer.AbstractExtendedRenderer;
import org.datagr4m.viewer.renderer.TextUtils;
import org.datagr4m.viewer.renderer.utils.CrossRenderer;


public class BoundsRenderer extends AbstractExtendedRenderer implements IBoundsRenderer{
    public static boolean DEBUG = true;
    
    @Override
    public void render(Graphics2D graphic, IHierarchicalNodeModel model){
        //graphic.setColor(borderColor);
        /*render(graphic, model.getAbsoluteRectangleBounds(), "raw", false);
        render(graphic, model.getExternalRectangleBounds().shiftCenterTo(model.getAbsolutePosition()), "external", false);
        render(graphic, model.getCorridorRectangleBounds().shiftCenterTo(model.getAbsolutePosition()), "corridor", false);
        
        //render(graphic, ModelGeometryProcessor.shiftToAbsolute(model.getRectangleBounds(), model.getAbsolutePosition()), "raw", false);
        //render(graphic, ModelGeometryProcessor.shiftToAbsolute(model.getExternalRectangleBounds(), model.getAbsolutePosition()), "external(+margin)", false);
        //render(graphic, ModelGeometryProcessor.shiftToAbsolute(model.getCorridorRectangleBounds(), model.getAbsolutePosition()), "corridor(+corridor)", false);
        
        
        //cross.render(graphic, borderColor, bounds.getCenter());
        
        //RectangleBounds bounds = ModelGeometryProcessor.shiftToAbsolute(model.getRectangleBounds(),model.getAbsolutePosition());
        //bounds.enlargeSelfInPlace(model.getCorridor(), model.getCorridor());
        
        RectangleBounds bounds = model.getRectangleBounds();
        bounds.enlargeSelfInPlace(model.getCorridor(), model.getCorridor());
        ModelGeometryProcessor.shiftToAbsolute(bounds, model.getAbsolutePosition());
        render(graphic, bounds, "corridor(+corridor)", false);*/
    }
    
    @Override
    public void render(Graphics2D graphic, RectangleBounds bounds) {
        render(graphic, bounds, null);
    }
    
    @Override
    public void render(Graphics2D graphic, RectangleBounds bounds, String info) {
        render(graphic, bounds, info, DEBUG);
    }

    @Override
	public void render(Graphics2D graphic, RectangleBounds bounds, String info, boolean showGeometryText) {
        graphic.setColor(borderColor);
        
        // Draw border
        drawRect(graphic, bounds.x, bounds.y, bounds.width, bounds.height);
        if(showGeometryText){
            drawText(graphic, bounds.width+"x"+bounds.height+" @ "+bounds.getTopLeftCorner(), bounds.getTopLeftCorner());
        }
        if(info!=null)
            drawText(graphic, info, bounds.getTopLeftCorner().add(0, TextUtils.textHeight()));
        cross.render(graphic, borderColor, bounds.getCenter());
    }
    
    protected CrossRenderer cross = new CrossRenderer();
    protected Color borderColor = Color.gray;
}
