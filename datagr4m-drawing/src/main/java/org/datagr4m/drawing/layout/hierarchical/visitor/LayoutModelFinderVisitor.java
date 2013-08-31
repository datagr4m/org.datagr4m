package org.datagr4m.drawing.layout.hierarchical.visitor;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;


public class LayoutModelFinderVisitor extends AbstractLayoutVisitor{
    public IHierarchicalLayout findLayoutHolding(IHierarchicalLayout root, IHierarchicalModel toBeFound){
        this.result = null;
        this.model = toBeFound;
        visit(root);
        return result;
    }
    
    @Override
    public void preVisit(IHierarchicalLayout layout) {
        if(layout.getModel()!=null)
            if(layout.getModel()==model)
                result = layout;
    }

    @Override
    public void postVisit(IHierarchicalLayout layout) {
    }

    protected IHierarchicalModel model;
    protected IHierarchicalLayout result;
}

