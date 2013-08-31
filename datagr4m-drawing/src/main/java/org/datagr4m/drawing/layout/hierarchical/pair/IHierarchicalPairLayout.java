package org.datagr4m.drawing.layout.hierarchical.pair;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;

public interface IHierarchicalPairLayout extends IHierarchicalLayout {
    public int getInterItemMargin();
    public void setInterItemMargin(int interItemMargin);

    public int getOrientation();
    public void setOrientation(int orientation);
    
    public void compute();
    public void computeSpline();
}