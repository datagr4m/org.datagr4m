package org.datagr4m.drawing.layout.hierarchical.graph.edges.post;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;

public interface IEdgePostProcessor {

    public void postProcess(IHierarchicalEdgeModel model);

    public void bendNonOrthogonalPathRecursively(IHierarchicalEdge edge);

}