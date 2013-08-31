package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local;

import java.awt.Graphics2D;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;


public interface IEdgeRenderer {
    public void render(Graphics2D graphic, Pair<IBoundedItem,IBoundedItem> edge, IEdgeRendererSettings settings);
}
