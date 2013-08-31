package org.datagr4m.drawing.model.links;

import org.datagr4m.datastructures.pairs.ITypedPair;

public interface ILink<T> extends ITypedPair<T> {
    /** Returns the instance that actually involved the creation of this link. */
    public Object getModelEdge();
    public void setModelEdge(Object modelEdge);
}
