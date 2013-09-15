package org.datagr4m.topology;

import java.util.Collection;

import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class TopologyNodeLabelIndex implements ITopologyNodeIndex<String, IPropertyNode> {
	protected Multimap<String,IPropertyNode> index = HashMultimap.create();
	
	public TopologyNodeLabelIndex(Topology<IPropertyNode,IPropertyEdge> topology){
		index(topology);
	}

	@Override
	public void index(Topology<IPropertyNode, IPropertyEdge> topology) {
		for(IPropertyNode node: topology.getGraph().getVertices()){
			String label = node.getLabel();
			index(label, node);
		}
	}
	
	@Override
	public void index(String idx, IPropertyNode node) {
		index.put(idx, node);
	}
	
	@Override
	public Collection<IPropertyNode> find(String filter) {
		return index.get(filter);
	}
}
