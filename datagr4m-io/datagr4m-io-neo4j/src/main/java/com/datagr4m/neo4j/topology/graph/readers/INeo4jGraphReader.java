package com.datagr4m.neo4j.topology.graph.readers;

import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.tooling.GlobalGraphOperations;

import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;

import edu.uci.ics.jung.graph.Graph;

public interface INeo4jGraphReader {
    public String getName();
    public Graph<IPropertyNode,IPropertyEdge> read(GlobalGraphOperations operation, Neo4jGraphModel model);
    public IPropertyNode newNode(Node node, Neo4jGraphModel model);
    public IPropertyEdge newEdge(Relationship relationship, Neo4jGraphModel model);
}
