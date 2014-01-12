package org.datagr4m.drawing.model.items.hierarchical.visitor;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;


public class ModelEdgesVisitor extends AbstractItemVisitor{
	public ModelEdgesVisitor(){
	    edges = new ArrayList<Pair<IBoundedItem,IBoundedItem>>();
	}
	
	@Override
	public void doVisitElement(IHierarchicalNodeModel parent, IBoundedItem element, int depth){
        if(element instanceof IHierarchicalGraphModel){
            IHierarchicalGraphModel graph = (IHierarchicalGraphModel)element;
            edges.addAll(graph.getLocalEdges());
            //System.out.println("level " + depth + " : " + graph.getRenderingEdges().size() + " edges");
        }
    }
	
	public List<Pair<IBoundedItem,IBoundedItem>> getFoundEdges(){
	    return edges;
	}
	
	List<Pair<IBoundedItem,IBoundedItem>> edges;
	
}
