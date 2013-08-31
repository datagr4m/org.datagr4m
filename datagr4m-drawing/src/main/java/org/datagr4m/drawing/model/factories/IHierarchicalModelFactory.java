package org.datagr4m.drawing.model.factories;

import org.datagr4m.drawing.model.items.IBoundedItem;

public interface IHierarchicalModelFactory {
    public IBoundedItem getLayoutModel(Object data);
    //public <V,E> IHierarchicalModel getLayoutModel(Topology<V,E> topology);
    //public <V,E> TubeModel getTubeModel(Topology<V,E> topology);
    //public IHierarchicalModel getModel(Object data);
}
