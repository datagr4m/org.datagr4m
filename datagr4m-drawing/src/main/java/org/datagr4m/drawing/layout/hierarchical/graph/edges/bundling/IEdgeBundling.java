package org.datagr4m.drawing.layout.hierarchical.graph.edges.bundling;

import java.io.Serializable;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.jzy3d.maths.Coord2d;


public interface IEdgeBundling extends Serializable{
	public void bundle(IHierarchicalEdgeModel model);
	
    public void build(IHierarchicalEdge tube);
    public void build(IEdge path);
    public void clear(IEdge path);
    public void clear(IHierarchicalEdge tube);

    public void fixTube(IHierarchicalEdge tube, Coord2d source, Coord2d target);
    
    //public void addNoTube(String from, String to);
}
