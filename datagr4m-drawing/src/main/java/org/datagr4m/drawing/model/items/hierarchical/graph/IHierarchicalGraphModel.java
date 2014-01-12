package org.datagr4m.drawing.model.items.hierarchical.graph;

import java.util.Collection;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.algorithms.forces.IForceModel;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;


/**
 * A graph model is able to indicates attracting and repulsing items for each
 * graph node, considering that each node is represented by a {@link IBoundedItem},
 * and each edge as a {@link Pair<IBoundedItem,IBoundedItem>}. 
 * 
 * Graph model implementation can describe edges as {@link CommutativePair<IBoundedItem>}
 * to indicate a non directed edge. CommutativePair and Pair are comparables, their difference
 * only stand in their {@link equals()} and {@link hashCode()} methods. A graph model supports
 * several edges for a given pair of items.
 * 
 * Last, the graph model provides with statistical information on the graph such as 
 * the degree of a node.
 */
public interface IHierarchicalGraphModel extends IHierarchicalNodeModel, IForceModel{
    
    /**
     * Return true if the graph already holds a rendering edge that is equal to the input edge.
     */
    public boolean hasLocalEdge(Pair<IBoundedItem,IBoundedItem> edge);
    
    /**
     * Return all the graph's edges that should be rendered.
     */
    public Collection<Pair<IBoundedItem,IBoundedItem>> getLocalEdges();
    
    /**
     * Return the collection of edges that should be rendered, and that goes from source to destination.
     */
    public Collection<Pair<IBoundedItem,IBoundedItem>> getLocalEdges(IBoundedItem source, IBoundedItem destination);
    
    /** 
     * Return the node degree, or -1 if the node is not part of this model
     * or not computed properly at startup.
     */
    public int getNodeDegree(IBoundedItem item);
}
