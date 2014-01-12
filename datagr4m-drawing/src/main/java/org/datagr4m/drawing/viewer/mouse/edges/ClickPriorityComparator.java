package org.datagr4m.drawing.viewer.mouse.edges;

import java.util.Comparator;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.viewer.mouse.IClickableItem;


public class ClickPriorityComparator implements Comparator<IClickableItem> {
    @Override
    public int compare(IClickableItem arg0, IClickableItem arg1) {
        // return 0;
        boolean isModel0 = isModel(arg0);
        boolean isModel1 = isModel(arg1); // not model=slot, edge, etc
        if (isModel0 && isModel1) 
            return modelCompare((IHierarchicalNodeModel) arg0, (IHierarchicalNodeModel) arg1);
        else if (isModel0 && !isModel1)
            return 1;
        else if (!isModel0 && isModel1)
            return -1;
        return 0;
    }

    protected int modelCompare(IHierarchicalNodeModel arg0, IHierarchicalNodeModel arg1) {
        return (arg1.getDepth() - arg0.getDepth());
    }

    protected boolean isModel(IClickableItem item) {
        return (item instanceof IHierarchicalNodeModel);
    }
}
