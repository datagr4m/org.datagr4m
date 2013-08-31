package com.datagr4m.neo4j.topology;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;

import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;

import edu.uci.ics.jung.graph.Graph;

public class Neo4jTopology extends Topology<IPropertyNode, IPropertyEdge> {
    public static String TOPOLOGY_NAME = "default";

    public Neo4jTopology() {
        super();
    }

    public Neo4jTopology(Graph<IPropertyNode, IPropertyEdge> graph) {
        super(graph, new ArrayList<Group<IPropertyNode>>());
    }

    public Neo4jTopology(Graph<IPropertyNode, IPropertyEdge> graph, Neo4jGraphModel model) {
        super(graph, new ArrayList<Group<IPropertyNode>>());//new TypeGroupHierarchy().build(graph, model));
    }

    public Neo4jTopology(Graph<IPropertyNode, IPropertyEdge> graph, List<Group<IPropertyNode>> hierarchy) {
        super(graph, hierarchy);
    }

    @Override
	public String getName() {
        return TOPOLOGY_NAME;
    }

    /* */

    public TopologyEditor edit(){
        return new TopologyEditor(this);
    }

    private static final long serialVersionUID = 7273472182713399395L;
}
