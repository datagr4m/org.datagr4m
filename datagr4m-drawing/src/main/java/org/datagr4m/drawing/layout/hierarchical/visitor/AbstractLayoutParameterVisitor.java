package org.datagr4m.drawing.layout.hierarchical.visitor;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.ForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.hierarchical.pair.HierarchicalPairLayout;


public abstract class AbstractLayoutParameterVisitor extends AbstractLayoutVisitor{

    @Override
    public void preVisit(IHierarchicalLayout layout) {
        /*console(layout.getDepth(), "layout:" + layout.getClass().getSimpleName() + "| content:" );
        consoleln(0, layout.getModel().getChildren().toString());
        layout.getModel().refreshBounds(false);*/
        if(layout instanceof HierarchicalGraphLayout){
            editForceAtlas( ((HierarchicalGraphLayout)layout).getDelegate() );
        }
        else if(layout instanceof HierarchicalPairLayout){
            editPairLayout(((HierarchicalPairLayout)layout));
        }
    }

    @Override
    public void postVisit(IHierarchicalLayout layout) {
    }
    
    public abstract void editForceAtlas(ForceAtlasLayout layout);
    public abstract void editPairLayout(HierarchicalPairLayout layout);
}
