package org.datagr4m.drawing.layout.runner;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.hierarchical.pair.IHierarchicalPairLayout;
import org.datagr4m.drawing.layout.hierarchical.visitor.AbstractLayoutVisitor;

public class LayoutUtils {
    public static void updatePairSplines(IHierarchicalNodeLayout layout) {
        AbstractLayoutVisitor pairUpdate = new AbstractLayoutVisitor() {
            @Override
            public void preVisit(IHierarchicalNodeLayout layout) {
            }
            @Override
            public void postVisit(IHierarchicalNodeLayout layout) {
                if(layout instanceof  IHierarchicalPairLayout){
                    ((IHierarchicalPairLayout)layout).computeSpline();
                }
            }
        };
        pairUpdate.visit(layout);
    }
}
