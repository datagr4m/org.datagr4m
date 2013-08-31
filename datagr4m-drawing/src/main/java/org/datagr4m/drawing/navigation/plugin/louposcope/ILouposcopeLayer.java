package org.datagr4m.drawing.navigation.plugin.louposcope;

import java.awt.Graphics2D;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.viewer.renderer.IRenderer;


public interface ILouposcopeLayer<V, E, C> extends IRenderer{
    public void renderContent(Graphics2D graphic, V o, IBoundedItem item);
    public ILouposcopeContent<C> getOrCreateContent(V o);

    public LouposcopePlugin getPlugin();
    public void setPlugin(LouposcopePlugin plugin);
}