package org.datagr4m.drawing.navigation.plugin.bringandgo;

import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.viewer.renderer.IRenderer;


public interface IBringAndGoLayer extends IRenderer{
    public IBoundedItem getCurrentItem();
    public void setCurrentItem(IBoundedItem currentItem);
    
    public List<IBoundedItem> getNeighbours();
    public void setNeighbours(List<IBoundedItem> neighbours);
    
    public IItemRendererSettings getItemSettings();
    public void setItemSettings(IItemRendererSettings itemSettings);
    
    public IItemRenderer getItemRenderer();
}