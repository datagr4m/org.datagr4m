package org.datagr4m.drawing.model.items.hierarchical.flower;

import org.datagr4m.drawing.model.items.IBoundedItem;

public interface IEdgeFactory<E> {
    public E newEdge();
    public E newEdge(IBoundedItem i1, IBoundedItem i2);
}
