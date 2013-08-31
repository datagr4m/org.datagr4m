package org.datagr4m.drawing.renderer.items.shaped;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItemIcon;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


public abstract class AbstractShapedItemIconRenderer extends AbstractShapedItemRenderer implements IItemRenderer{
    public AbstractShapedItemIconRenderer() {
        super();
    }

    @Override
    public void render(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        Coord2d c = item.getAbsolutePosition();
        if(c==null)
            c = item.getPosition();
        float radius = item.getRadialBounds();
                
        /*drawLine(graphic, c, c.add(10,0));
        drawLine(graphic, c, c.add(0,10));*/
        renderBorderAndBody(graphic, item, settings, c, radius);
        renderIcon(graphic, item, settings, c);
        renderLabel(graphic, item, settings, c);
        renderSlots(graphic, item, settings);
        renderBounds(graphic, item, settings);
    }
    
    protected void renderIcon(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings, Coord2d c) {
        if(settings.isNodeDisplayed(item)){
            if(item instanceof IBoundedItemIcon){
                IBoundedItemIcon iNode = (IBoundedItemIcon)item;
                Icon icon = iNode.getIcon();
                
                if(display!=null && icon!=null){
                    Color color = settings.getIconFilterColor(item);
                    
                    int x = (int) c.x - icon.getIconWidth()/2;
                    int y = (int) c.y - icon.getIconHeight()/2 - TextUtils.textHeight();
                    
                    if(color==null)
                        icon.paintIcon((Component)display, graphic, x, y);
                    else{
                        Icon icon2 = settings.getFilteredIcon((ImageIcon)icon, color, display);
                        icon2.paintIcon((Component)display, graphic, x, y);
                    }
                }
            }
        }
    }
}