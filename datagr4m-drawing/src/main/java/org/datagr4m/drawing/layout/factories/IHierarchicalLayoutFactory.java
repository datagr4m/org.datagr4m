package org.datagr4m.drawing.layout.factories;

import java.util.Map;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.IHierarchicalEdgeLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;



public interface IHierarchicalLayoutFactory {
    public IHierarchicalLayout getLayout(IHierarchicalModel model, IHierarchicalEdgeModel tube);
    public IHierarchicalLayout getLayout(IHierarchicalModel model);

    public IHierarchicalLayout getHierarchicalNodeLayout(IHierarchicalModel model);
    public IHierarchicalLayout getNodeLayoutByModelType(IHierarchicalModel model);
    public IHierarchicalLayout getNodeLayoutByModelName(IHierarchicalModel model);    
    public IHierarchicalLayout getNodeLayoutByName(String name) throws IllegalArgumentException;
    
    public IHierarchicalEdgeLayout getHierarchicalEdgeLayout(IHierarchicalModel model);
    
    public void setModelLayoutMapping(Map<String,String> modelToLayout);
    public Map<String,String> getModelLayoutMapping();
}
