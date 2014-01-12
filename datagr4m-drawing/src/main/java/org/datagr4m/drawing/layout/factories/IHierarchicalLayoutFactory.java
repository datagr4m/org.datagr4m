package org.datagr4m.drawing.layout.factories;

import java.util.Map;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;



public interface IHierarchicalLayoutFactory {
    public IHierarchicalNodeLayout getLayout(IHierarchicalNodeModel model, IHierarchicalEdgeModel tube);
    public IHierarchicalNodeLayout getLayout(IHierarchicalNodeModel model);

    public IHierarchicalNodeLayout getHierarchicalNodeLayout(IHierarchicalNodeModel model);
    public IHierarchicalNodeLayout getNodeLayoutByModelType(IHierarchicalNodeModel model);
    public IHierarchicalNodeLayout getNodeLayoutByModelName(IHierarchicalNodeModel model);    
    public IHierarchicalNodeLayout getNodeLayoutByName(String name) throws IllegalArgumentException;
    
    public IHierarchicalEdgeLayout getHierarchicalEdgeLayout(IHierarchicalNodeModel model);
    
    public void setModelLayoutMapping(Map<String,String> modelToLayout);
    public Map<String,String> getModelLayoutMapping();
}
