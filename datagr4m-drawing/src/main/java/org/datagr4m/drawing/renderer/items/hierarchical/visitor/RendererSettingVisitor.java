package org.datagr4m.drawing.renderer.items.hierarchical.visitor;

import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.pair.HierarchicalPairRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.pair.IHierarchicalPairRenderer;


public abstract class RendererSettingVisitor extends AbstractRendererVisitor{

    @Override
    public void preVisit(IHierarchicalRenderer renderer) {
        
        if(renderer instanceof HierarchicalGraphRenderer){
            editGraphRenderer((HierarchicalGraphRenderer)renderer);
        }
        else if(renderer instanceof HierarchicalPairRenderer){
            editPairRenderer((IHierarchicalPairRenderer)renderer);
        }
    }

    @Override
    public void postVisit(IHierarchicalRenderer layout) {
    }
    
    public abstract void editGraphRenderer(HierarchicalGraphRenderer renderer);
    public abstract void editPairRenderer(IHierarchicalPairRenderer renderer);
}
