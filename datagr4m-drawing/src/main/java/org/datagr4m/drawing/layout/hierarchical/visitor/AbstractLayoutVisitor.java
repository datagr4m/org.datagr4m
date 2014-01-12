package org.datagr4m.drawing.layout.hierarchical.visitor;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;

public abstract class AbstractLayoutVisitor {
    public void visit(IHierarchicalNodeLayout root){
        preVisit(root);
        
        for(IHierarchicalNodeLayout sub: root.getChildren())
            if(sub!=null)
                visit(sub);
        
        postVisit(root);
    }
    
    public abstract void preVisit(IHierarchicalNodeLayout layout);
    public abstract void postVisit(IHierarchicalNodeLayout layout);
}
