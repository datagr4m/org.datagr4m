package org.datagr4m.drawing.renderer.items.hierarchical;

import org.datagr4m.drawing.renderer.bounds.IBoundsRendererSettings;

public interface IHierarchicalRendererSettings {
    public IBoundsRendererSettings getBoundsSettings();
    public void setBoundsSettings(IBoundsRendererSettings boundsSettings);
}
