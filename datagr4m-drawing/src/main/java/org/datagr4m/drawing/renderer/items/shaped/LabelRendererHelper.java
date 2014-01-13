package org.datagr4m.drawing.renderer.items.shaped;

import java.awt.Color;
import java.awt.Graphics2D;

import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItemIcon;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


public class LabelRendererHelper {
    public void renderLabel(AbstractShapedItemRenderer renderer, Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings, final Coord2d c) {
        if(settings.isNodeLabelDisplayed(item)){
        	if(item instanceof BoundedItem){
        		BoundedItem i = ((BoundedItem)item);
        		
        		Coord2d labelCoord = c.clone();
        		for(String label: i.getLabels()){
        			int width = TextUtils.textWidth(label);
	                //int height = TextUtils.textHeight();
	                final Color color = settings.getNodeLabelColor(item);
	                
	                graphic.setColor(color);
	                if(settings.isNodeLabelScaled(item)){
	                    renderScaledLabel(renderer, graphic, item, labelCoord, label, width);
	                }
	                else{
	                    renderUnscaledLabel(renderer, item, labelCoord, label, color);
	                }
	                
	                labelCoord.addSelf(0, TextUtils.textHeight()+5);
        		}
        	}
        	else{
	            final String label = item.getLabel();// + "(" + item.getDebugInfo() + ")";
	            
	            if(label!=null){
	                int width = TextUtils.textWidth(label);
	                //int height = TextUtils.textHeight();
	                final Color color = settings.getNodeLabelColor(item);
	                
	                graphic.setColor(color);
	                if(settings.isNodeLabelScaled(item)){
	                    renderScaledLabel(renderer, graphic, item, c, label, width);
	                }
	                else{
	                    renderUnscaledLabel(renderer, item, c, label, color);
	                }
	            }
        	}
        }
    }

    protected void renderUnscaledLabel(AbstractShapedItemRenderer renderer, IBoundedItem item, final Coord2d c, final String label, final Color color) {
        //final float yoff = yTextOffset;
        Coord2d labelPosition;
        if(isRectangle(item))
            labelPosition = item.getRawRectangleBounds().shiftCenterTo(c).getTopPoint();//c.add(0,yoff);
        else if(isCircle(item))
            labelPosition = c.add(0, -item.getRadialBounds());
        else
            labelPosition = c;
        
        int priority = item.getDepth();
        renderer.getDiffered().add(new UnscaledTextDifferedRenderer(label + renderer.debugInfo(item), labelPosition, TextUtils.size, priority) {
            @Override
            public synchronized void renderText(Graphics2D graphic) {
                if(size>0)
                    drawUnscaledTextCell(graphic, label, position, color, Color.WHITE);
            }
        });
        

    }

    protected void renderScaledLabel(AbstractShapedItemRenderer renderer, Graphics2D graphic, IBoundedItem item, final Coord2d c, final String label, int width) {
        float yTextOffset = 0;//-TextUtils.textHeight(); // cas icone: icone au dessus, texte sous le point central
        
        if(isIconItem(item))
            yTextOffset = + item.getRawRectangleBounds().height/2 - TextUtils.textHeight();
        else if(isCircle(item))
            yTextOffset = - item.getRadialBounds();
        else if(isRectangle(item))
            yTextOffset = - item.getRawRectangleBounds().height/2 + TextUtils.textHeight();
        else
            throw new RuntimeException("Item label placement strategy undefined!");
        
        renderer.drawText(graphic, label, c.add(-width/2, yTextOffset));
        
        //renderer.drawUnscaledText(graphic, label, c.add(-width/2, yTextOffset));
    }
    
    protected boolean isIconItem(IBoundedItem item){
        return (item instanceof IBoundedItemIcon);
    }

    protected boolean isCircle(IBoundedItem item){
        return item.getShape()==ItemShape.CIRCLE;
    }

    protected boolean isRectangle(IBoundedItem item){
        return item.getShape()==ItemShape.RECTANGLE;
    }
}
