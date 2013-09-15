package org.datagr4m.topology;

import java.util.Collection;

import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;

public interface ITopologyNodeIndex<I,V> {
	public void index(Topology<IPropertyNode, IPropertyEdge> topology);
	public void index(I index, IPropertyNode node);
	public Collection<V> find(I filter);
}
