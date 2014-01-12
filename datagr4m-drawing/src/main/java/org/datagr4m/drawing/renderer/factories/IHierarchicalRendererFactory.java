package org.datagr4m.drawing.renderer.factories;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.viewer.IDisplay;



public interface IHierarchicalRendererFactory {
    public IHierarchicalRenderer getRenderer(IDisplay display, IHierarchicalNodeModel model);
}
