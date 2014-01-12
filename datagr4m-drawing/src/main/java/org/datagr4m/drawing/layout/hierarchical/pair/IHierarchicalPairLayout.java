package org.datagr4m.drawing.layout.hierarchical.pair;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;

public interface IHierarchicalPairLayout extends IHierarchicalNodeLayout {
    public int getInterItemMargin();
    public void setInterItemMargin(int interItemMargin);

    public int getOrientation();
    public void setOrientation(int orientation);
    
    public void compute();
    public void computeSpline();
}