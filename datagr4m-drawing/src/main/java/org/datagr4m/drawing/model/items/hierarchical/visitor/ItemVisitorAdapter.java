package org.datagr4m.drawing.model.items.hierarchical.visitor;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

public class ItemVisitorAdapter extends AbstractItemVisitor{
    @Override
    public void doVisitElement(IHierarchicalModel parent, IBoundedItem element, int depth) {
        throw new RuntimeException("not implemented");
    }

}
