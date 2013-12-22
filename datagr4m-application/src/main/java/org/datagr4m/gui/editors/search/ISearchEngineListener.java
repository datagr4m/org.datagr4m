package org.datagr4m.gui.editors.search;

import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;


public interface ISearchEngineListener {
    public void searchFinished(List<IBoundedItem> results);
}
