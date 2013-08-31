package org.datagr4m.drawing.layout.algorithms.forces;

import java.util.Collection;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;


public interface IForceModel {
    /**
     * Return true if the graph already holds an attraction edge that is equal to the input edge.
     */
    public boolean hasAttractionEdges(Pair<IBoundedItem,IBoundedItem> edge);
    
    
    public boolean hasAttractionEdges(IBoundedItem source, IBoundedItem destination);
    
    /**
     * Return all the graph's edges that should be considered for computing a force based graph layout.
     */
    public Collection<Pair<IBoundedItem,IBoundedItem>> getAttractionEdges();
    
    /**
     * Return the collection of edges that should be considered for computing a force based graph layout, and that are attached to the given source.
     */
    public Collection<Pair<IBoundedItem, IBoundedItem>> getAttractionEdges(IBoundedItem source);
    
    
    /**
     * Return the collection of edges that should be considered for computing a force based graph layout, and that goes from source to destination.
     */
    public Collection<Pair<IBoundedItem,IBoundedItem>> getAttractionEdges(IBoundedItem source, IBoundedItem destination);
    
    /**
     * Return all attraction forces of this model
     */
    public List<IForce> getAttractionEdgeForces();
    
    /**
     * Return all attraction forces of this model where input item is the owner of this force.
     */
    public List<IForce> getAttractionEdgeForces(IBoundedItem item);
    
    /**
     * Return the {@link IForce}s that attract the given item,
     * or null if the item does not stand in this model.
     */
    public Collection<IForce> getAttractors(IBoundedItem item);

    /**
     * Return the {@link IForce}s that repulse the given item,
     * or null if the item does not stand in this model.
     */
    public Collection<IForce> getRepulsors(IBoundedItem item);
    
    public Collection<IForce> getAttractorForces();

    
    public int getNumberOfRepulsors();
}
