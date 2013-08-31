package org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos;

import java.io.Serializable;
import java.util.List;

public interface IEdgeInfo extends Serializable{
	public List<String> flattenInfoAsString();
    public String flattenInfo();
}
