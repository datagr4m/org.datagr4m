package org.datagr4m.drawing.layout.hierarchical.graph.edges;

import java.io.Serializable;

import org.datagr4m.drawing.layout.slots.ISlotGroupLayout;
import org.datagr4m.drawing.layout.slots.ISlotLayout;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryPostProcessor;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.links.DirectedLink;


public interface IItemSlotLayout extends Serializable{
	public void build(IHierarchicalEdgeModel model);
	
    public ISlotLayout getSlotLayout();
	public ISlotGroupLayout getSlotGroupLayout();
	public ISlotGeometryPostProcessor getSlotGeometryGeomPostProcessor();
    public void setSlotLayout(ISlotLayout slotLayout);
	public void setSlotGroupLayout(ISlotGroupLayout slotGroupLayout);
	public void setSlotGeometryGeomPostProcessor(ISlotGeometryPostProcessor slotGeometryGeomPostProcessor);
	
	public DirectedLink newLink(IEdge e);
}
