package org.datagr4m.drawing.navigation.plugin.bringandgo.flower.staticf;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.flower.StaticFlowerModel;

public interface IStaticFlowerBuilder<V, E> {

    public StaticFlowerModel<E> getFlowerModel();

    public void build(IBoundedItem item);

}