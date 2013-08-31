package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local;

import java.awt.Color;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;


public class LocalEdgeRendererSettings implements IEdgeRendererSettings{

    @Override
    public Color getEdgeColor(Pair<IBoundedItem, IBoundedItem> edge) {
        return DefaultStyleSheet.LOCAL_EDGE_COLOR;
    }

    @Override
    public Color getEdgeLabelColor(Pair<IBoundedItem, IBoundedItem> edge) {
        return DefaultStyleSheet.LOCAL_EDGE_COLOR;
    }

    @Override
    public boolean isEdgeLabelDisplayed(Pair<IBoundedItem, IBoundedItem> edge) {
        return true;
    }
    
    @Override
    public boolean isEdgeDisplayed(Pair<IBoundedItem, IBoundedItem> edge) {
        return true;
    }
    
    @Override
    public boolean isEdgeStraight(Pair<IBoundedItem, IBoundedItem> edge) {
        return true;
    }

    @Override
    public int getEdgeLabelSize(Pair<IBoundedItem, IBoundedItem> edge) {
        return DefaultStyleSheet.DEFAULT_FLOWER_NETWORK_TEXT_SIZE;
    }
}
