package org.datagr4m.drawing.model.items.hierarchical.explorer;

import org.datagr4m.viewer.IDisplay;

public interface IModelExplorer {
    /** Provide a popup menu with actions that allow browsing a model, somehow.*/
    public void showPopupMenuController(IDisplay display, int x, int y);

}