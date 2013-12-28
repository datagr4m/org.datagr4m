package org.datagr4m.drawing.renderer.items.hierarchical;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.model.bounds.IBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.renderer.IHasDifferedRenderingSupport;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;


public interface IHierarchicalRenderer extends IRenderer, IHasDifferedRenderingSupport{
    // Hierarchy
    public Collection<IHierarchicalRenderer> getChildren();
    public IHierarchicalRenderer getParent();

    // Attach a model to handle
    public void setModel(IHierarchicalModel model);
    public IHierarchicalModel getModel();
    
    // IRenderer methods
    @Override
	public void render(Graphics2D graphic);
    public IBounds getBounds();
    
    @Override
	public List<IClickableItem> hit(int x, int y);
    
    public IDisplay getDisplay();
    
    
    public void addPostRenderer(IRenderer renderer);
    public List<IRenderer> getPostRenderers();
    
    // settings
    /*public IBoundsRendererSettings getBoundsRendererSettings();
    public void setBoundsRendererSettings(IBoundsRendererSettings settings);
    public IItemRendererSettings getItemRendererSettings();
    public void setItemRendererSettings(IItemRendererSettings settings);*/

    public IItemRenderer getItemRenderer();
    public void setItemRenderer(IItemRenderer renderer);

}

