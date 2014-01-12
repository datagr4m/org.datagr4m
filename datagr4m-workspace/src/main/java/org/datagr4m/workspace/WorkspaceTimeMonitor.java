package org.datagr4m.workspace;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.hierarchical.visitor.AbstractLayoutVisitor;
import org.datagr4m.drawing.monitors.TimeMonitorCollection;

public class WorkspaceTimeMonitor extends TimeMonitorCollection{
    public static String LAYOUT_RUNNER = "Runner";
    public static String NODE_LAYOUT = "Nodes";
    
    public static String EDGE_LAYOUT = "Edges";
    public static String EDGE_SLOT_LAYOUT = "Slots";
    public static String EDGE_BUNDLING_LAYOUT = "Bundling";
    
    public static String LABEL_LAYOUT = "Labels";
    
    protected Workspace workspace;
    
    public WorkspaceTimeMonitor(Workspace workspace){
        monitor(workspace);
    }
    
    public void monitor(final Workspace workspace){
        this.workspace = workspace;
        
        if(workspace!=null){
            addMonitorable(LAYOUT_RUNNER, workspace.getRunner());
            addMonitorable(NODE_LAYOUT, workspace.getNodeLayout());
            addMonitorable(EDGE_LAYOUT, workspace.getNodeLayout().getEdgeLayout());
            
            addMonitorableChildren(NODE_LAYOUT, workspace.getNodeLayout());
        }
    }

    public void addMonitorableChildren(final String prefix, IHierarchicalNodeLayout root) {
        AbstractLayoutVisitor v = new AbstractLayoutVisitor() {
            int k=0;
            @Override
            public void preVisit(IHierarchicalNodeLayout layout) {
                addMonitorable(" " + prefix + ":" + layout.getClass().getSimpleName() + (k++), layout);
            }
            
            @Override
            public void postVisit(IHierarchicalNodeLayout layout) {
                // TODO Auto-generated method stub
                
            }
        };
        v.visit(root);
    }
}
