package org.datagr4m.drawing.model.items.hierarchical.graph;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

import edu.uci.ics.jung.graph.Graph;

public class HierarchicalGraphModel extends AbstractHierarchicalGraphModel{
    public HierarchicalGraphModel(){}
    
    public HierarchicalGraphModel(IHierarchicalModel parent){
        super(parent);
    }
    
    @Override
	public int getNumberOfRepulsors(){
        int t = 0;
        for(IBoundedItem src: repulsors.keySet()){
            t += repulsors.get(src).size();
        }
        return t;
    }

    /**
     * Load a graph data model using the layout model factory.
     * The input graph links are loaded in the local graph.
     */
    public <V,E> HierarchicalGraphModel(Graph<V,E> graph, IHierarchicalModelFactory factory){
        this.graph = graph; // keep source data model
        
        // nodes
        for(V node: graph.getVertices()){
            IBoundedItem item = factory.getLayoutModel(node);
            //addChild(item);
            registerChild(node, item);
            setNodeDegree(item, graph.degree(node));
        }
                
        // local edges
        for(E edge: graph.getEdges()){
            V v1 = graph.getEndpoints(edge).getFirst();
            V v2 = graph.getEndpoints(edge).getSecond();
            IBoundedItem i1 = getItem(v1);
            IBoundedItem i2 = getItem(v2);            
            Pair<IBoundedItem,IBoundedItem> pair = new Pair<IBoundedItem,IBoundedItem>(i1, i2);
            addLocalEdge(pair);
        }
        
        // make forces
        createAllLocalEdgeAttractors(graph);
        createAllMutualRepulsors();
        
        try {
            verify();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <V, E> void createAllLocalEdgeAttractors(Graph<V, E> graph) {
        for(E edge: graph.getEdges()){
            V v1 = graph.getEndpoints(edge).getFirst();
            V v2 = graph.getEndpoints(edge).getSecond();
            IBoundedItem i1 = getItem(v1);
            IBoundedItem i2 = getItem(v2);            
            Pair<IBoundedItem,IBoundedItem> pair = new Pair<IBoundedItem,IBoundedItem>(i1, i2);
            addAttractionEdgeForce(pair);
        }
    }
    
    public Graph<?, ?> getGraph() {
        return graph;
    }
    
    @Override
	public String toString(){
        return "(" + this.getClass().getSimpleName() + ") " + label;
    }

    protected Graph<?,?> graph;
    private static final long serialVersionUID = -7424310577112528523L;
}
