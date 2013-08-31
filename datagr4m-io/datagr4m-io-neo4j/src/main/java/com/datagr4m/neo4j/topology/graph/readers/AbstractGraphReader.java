package com.datagr4m.neo4j.topology.graph.readers;

import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.datagr4m.neo4j.topology.edges.Neo4jEdge;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import com.datagr4m.neo4j.topology.nodes.Neo4jNode;

public abstract class AbstractGraphReader implements INeo4jGraphReader{
    public boolean isPropertyNode(IPropertyEdge edge, IPropertyNode node1, IPropertyNode node2) {
        return node1.isPropertyNode() || node2.isPropertyNode();
    }

    public boolean isPropertyNode(IPropertyNode node) {
        return node.isPropertyNode();
    }


    @Override
	public IPropertyNode newNode(Node node, Neo4jGraphModel model){
        return new Neo4jNode(node, model);
    }

    @Override
	public IPropertyEdge newEdge(Relationship relationship, Neo4jGraphModel model){
        return new Neo4jEdge(relationship);
    }
}