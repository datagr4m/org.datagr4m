package org.datagr4m.drawing.renderer.bounds;

import java.awt.Graphics2D;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;


public interface IBoundsRenderer{
    public void render(Graphics2D graphic, IHierarchicalNodeModel model);
    public void render(Graphics2D graphic, RectangleBounds bounds);
    public void render(Graphics2D graphic, RectangleBounds bounds, String info);
    public void render(Graphics2D graphic, RectangleBounds bounds, String info, boolean infoText);

}
