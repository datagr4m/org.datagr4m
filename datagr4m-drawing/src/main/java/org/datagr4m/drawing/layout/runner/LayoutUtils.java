package org.datagr4m.drawing.layout.runner;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.pair.IHierarchicalPairLayout;
import org.datagr4m.drawing.layout.hierarchical.visitor.AbstractLayoutVisitor;

public class LayoutUtils {
    public static void updatePairSplines(IHierarchicalLayout layout) {
        AbstractLayoutVisitor pairUpdate = new AbstractLayoutVisitor() {
            @Override
            public void preVisit(IHierarchicalLayout layout) {
            }
            @Override
            public void postVisit(IHierarchicalLayout layout) {
                if(layout instanceof  IHierarchicalPairLayout){
                    ((IHierarchicalPairLayout)layout).computeSpline();
                }
            }
        };
        pairUpdate.visit(layout);
    }
}
