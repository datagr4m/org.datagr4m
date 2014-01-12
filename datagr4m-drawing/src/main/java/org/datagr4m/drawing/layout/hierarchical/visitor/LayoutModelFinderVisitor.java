package org.datagr4m.drawing.layout.hierarchical.visitor;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;


public class LayoutModelFinderVisitor extends AbstractLayoutVisitor{
    public IHierarchicalNodeLayout findLayoutHolding(IHierarchicalNodeLayout root, IHierarchicalNodeModel toBeFound){
        this.result = null;
        this.model = toBeFound;
        visit(root);
        return result;
    }
    
    @Override
    public void preVisit(IHierarchicalNodeLayout layout) {
        if(layout.getModel()!=null)
            if(layout.getModel()==model)
                result = layout;
    }

    @Override
    public void postVisit(IHierarchicalNodeLayout layout) {
    }

    protected IHierarchicalNodeModel model;
    protected IHierarchicalNodeLayout result;
}

