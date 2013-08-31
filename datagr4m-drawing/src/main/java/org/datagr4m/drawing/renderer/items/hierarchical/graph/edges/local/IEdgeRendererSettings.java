package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local;

import java.awt.Color;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;


public interface IEdgeRendererSettings {
    public Color getEdgeColor(Pair<IBoundedItem,IBoundedItem> edge);
    public Color getEdgeLabelColor(Pair<IBoundedItem, IBoundedItem> edge);
    public int getEdgeLabelSize(Pair<IBoundedItem, IBoundedItem> edge);
    public boolean isEdgeLabelDisplayed(Pair<IBoundedItem, IBoundedItem> edge);
    public boolean isEdgeDisplayed(Pair<IBoundedItem, IBoundedItem> edge);
    public boolean isEdgeStraight(Pair<IBoundedItem, IBoundedItem> edge);
}
