package org.datagr4m.drawing.navigation.plugin.bringandgo.flower.forcef;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.flower.ForceFlowerModel;

public interface IForceFlowerBuilder<V,E> {
    public void build(IBoundedItem item);
    public ForceFlowerModel<E> getFlowerModel();
}
