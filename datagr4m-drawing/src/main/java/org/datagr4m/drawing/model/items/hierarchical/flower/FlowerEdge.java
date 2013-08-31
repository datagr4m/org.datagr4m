package org.datagr4m.drawing.model.items.hierarchical.flower;

import org.datagr4m.drawing.model.items.IBoundedItem;

public class FlowerEdge<E>{
    public FlowerEdge(E edge, String edgeInfo, IBoundedItem left, String leftInfo, IBoundedItem right, String rightInfo) {
        this.edge = edge;
        this.edgeInfo = edgeInfo;
        this.left = left;
        this.leftInfo = leftInfo;
        this.right = right;
        this.rightInfo = rightInfo;
    }
    E edge;
    String edgeInfo;
    IBoundedItem left;
    String leftInfo;
    IBoundedItem right;
    String rightInfo;
}
