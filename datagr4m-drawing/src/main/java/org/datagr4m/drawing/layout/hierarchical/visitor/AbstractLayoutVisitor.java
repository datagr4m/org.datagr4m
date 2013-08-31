package org.datagr4m.drawing.layout.hierarchical.visitor;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;

public abstract class AbstractLayoutVisitor {
    public void visit(IHierarchicalLayout root){
        preVisit(root);
        
        for(IHierarchicalLayout sub: root.getChildren())
            if(sub!=null)
                visit(sub);
        
        postVisit(root);
    }
    
    public abstract void preVisit(IHierarchicalLayout layout);
    public abstract void postVisit(IHierarchicalLayout layout);
}
