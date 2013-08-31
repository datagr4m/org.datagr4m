package org.datagr4m.drawing.model.items.hierarchical;

public interface IHierarchicalModelChangeListener {
    /** Indicates that a child moved or changed size.*/
    public void boundsDirty(IHierarchicalModel model);
}
