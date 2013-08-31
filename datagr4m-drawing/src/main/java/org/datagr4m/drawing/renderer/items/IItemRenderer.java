package org.datagr4m.drawing.renderer.items;

import java.awt.Graphics2D;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.renderer.IHasDifferedRenderingSupport;


public interface IItemRenderer extends IHasDifferedRenderingSupport{
    public void render(Graphics2D graphic, IBoundedItem node, IItemRendererSettings settings);
    public boolean hit(IBoundedItem item, int x, int y);
}
