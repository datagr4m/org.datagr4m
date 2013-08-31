package org.datagr4m.drawing.model.items.hierarchical.flower;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;


/**
 * The TrioStructure gather:
 * a center item
 * a set of neighbours, containing the central item
 * a common edge item to connect center and neighbours
 * 
 * 
 * @author martin
 */
public class HyperEdgeStructure {
    public HyperEdgeStructure(IBoundedItem center, IBoundedItem edge) {
        this.center = center;
        this.edge = edge;
        this.neighbours = new ArrayList<IBoundedItem>();
    }

    public HyperEdgeStructure(IBoundedItem edge, List<IBoundedItem> neighbours) {
        this.edge = edge;
        this.neighbours = neighbours;
    }
    
    public HyperEdgeStructure(IBoundedItem edge, IBoundedItem center, List<IBoundedItem> neighbours) {
        this.center = center;
        this.edge = edge;
        this.neighbours = new ArrayList<IBoundedItem>(neighbours);
        this.neighbours.add(center);
    }
    
    public IBoundedItem getCenter() {
        return center;
    }
    public IBoundedItem getEdge() {
        return edge;
    }
    public List<IBoundedItem> getNeighbours() {
        return neighbours;
    }
    
    public boolean containsNeighbour(IBoundedItem i){
        if(neighbours.contains(i))
            return true;
        else
            return false;
    }
    
    public int size(){
        return neighbours.size();
    }

    protected IBoundedItem center;
    protected IBoundedItem edge;
    protected List<IBoundedItem> neighbours;
}
