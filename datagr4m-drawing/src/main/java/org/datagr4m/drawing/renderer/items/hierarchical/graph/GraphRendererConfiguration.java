package org.datagr4m.drawing.renderer.items.hierarchical.graph;

public class GraphRendererConfiguration {

    // may also require to have scalable labels in item settings
    protected boolean allowHierarchicalEdgeManagement = false;
    
    // scaling without hierarchical label gathering
    // makes less smart label size
    protected boolean applySmartLabelScaling = true;
    
    public boolean isAllowHierarchicalEdgeManagement() {
        return allowHierarchicalEdgeManagement;
    }

    public void setAllowHierarchicalEdgeManagement(boolean allowHierarchicalEdgeManagement) {
        this.allowHierarchicalEdgeManagement = allowHierarchicalEdgeManagement;
    }

    public boolean isApplySmartLabelScaling() {
        return applySmartLabelScaling;
    }

    public void setApplySmartLabelScaling(boolean applySmartLabelScaling) {
        this.applySmartLabelScaling = applySmartLabelScaling;
    }

}
