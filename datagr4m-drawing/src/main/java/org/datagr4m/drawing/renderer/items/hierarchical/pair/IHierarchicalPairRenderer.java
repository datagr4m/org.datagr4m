package org.datagr4m.drawing.renderer.items.hierarchical.pair;

import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;

public interface IHierarchicalPairRenderer extends IHierarchicalRenderer{
    public IPairRendererSettings getRendererSettings();
    public void getRendererSettings(IPairRendererSettings settings);
}