package org.datagr4m.drawing.navigation.plugin;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.topology.Topology;

import edu.uci.ics.jung.graph.Graph;

/**
 * Hold a Topology<V,E> model if there exist one in the provided hierarchical model.
 * 
 * @author Martin Pernollet
 * @param <V> topology vertex type
 * @param <E> topology edge type
 */
public class PluginDataModelHolder<V,E> {
	public PluginDataModelHolder(IHierarchicalNodeModel model){
		if (model instanceof HierarchicalGraphModel) {
            HierarchicalGraphModel graphModel = (HierarchicalGraphModel) model;
            if(graphModel.getObject() instanceof Topology<?,?>){
                topology = (Topology<V, E>) graphModel.getObject();
            }
        }
	}
	
	public Topology<V,E> getTopology(){
		return topology;
	}
	
	public Graph<V,E> getGraph(){
		if(getTopology()!=null)
			return getTopology().getGraph();
		else
			return null;
	}
	
	protected Topology<V,E> topology;
}
