package org.datagr4m.drawing.tubes;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;


public interface ITubeDataTest {
    public IHierarchicalNodeModel getItemModel();
    public IHierarchicalEdgeModel getEdgeModel();
    public IHierarchicalNodeLayout makeTestLayout();
}
