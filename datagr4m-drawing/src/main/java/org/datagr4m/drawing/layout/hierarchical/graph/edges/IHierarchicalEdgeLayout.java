package org.datagr4m.drawing.layout.hierarchical.graph.edges;

import java.io.Serializable;
import java.util.List;

import org.datagr4m.drawing.layout.ILayoutListener;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.bundling.IEdgeBundling;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.post.IEdgePostProcessor;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.monitors.ITimeMonitorable;


public interface IHierarchicalEdgeLayout extends ITimeMonitorable, Serializable{
	public void build(IHierarchicalEdgeModel model);
    
    public IItemSlotLayout getItemSlotLayout();
	public IEdgeBundling getEdgeBundler();
	public IEdgePostProcessor getEdgePostProcess();

	public void setItemSlotLayout(IItemSlotLayout itemSlotLayout);
	public void setEdgeBundler(IEdgeBundling edgeBundler);
	public void setEdgePostProcess(IEdgePostProcessor edgePostProcess);
    
    public List<ILayoutListener> getListeners();
    public void addListener(ILayoutListener listener);
    public void removeListener(ILayoutListener listener);
}