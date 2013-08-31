package org.datagr4m.drawing.renderer.items.hierarchical.pair;

import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRendererSettings;


public interface IPairRendererSettings extends IHierarchicalRendererSettings{
    public IItemRendererSettings getNodeSettings();
    public void setNodeSettings(IItemRendererSettings nodeSettings);
}
