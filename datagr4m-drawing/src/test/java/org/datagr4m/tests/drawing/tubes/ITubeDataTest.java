package org.datagr4m.tests.drawing.tubes;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;


public interface ITubeDataTest {
    public IHierarchicalModel getItemModel();
    public IHierarchicalEdgeModel getEdgeModel();
    public IHierarchicalLayout makeTestLayout();
}
