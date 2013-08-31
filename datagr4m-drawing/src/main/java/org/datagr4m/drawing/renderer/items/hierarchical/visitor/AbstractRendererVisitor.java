package org.datagr4m.drawing.renderer.items.hierarchical.visitor;

import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;

public abstract class AbstractRendererVisitor {
    public void visit(IHierarchicalRenderer root){
        preVisit(root);
        
        for(IHierarchicalRenderer sub: root.getChildren())
            if(sub!=null)
                visit(sub);
        
        postVisit(root);
    }
    
    public abstract void preVisit(IHierarchicalRenderer layout);
    public abstract void postVisit(IHierarchicalRenderer layout);
}
