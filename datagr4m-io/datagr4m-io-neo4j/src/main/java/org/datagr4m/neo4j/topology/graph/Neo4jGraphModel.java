package org.datagr4m.neo4j.topology.graph;

import org.datagr4m.neo4j.topology.nodes.Neo4jNode;
import org.datagr4m.topology.graph.GenericGraphModel;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.NodeType;
import org.neo4j.graphdb.Node;

public class Neo4jGraphModel extends GenericGraphModel<Node>{
    @Override
    public Node getSourceNode(IPropertyNode node) {
        return (Node)node.getNode();
    }

    @Override
    public NodeType newNodeType(Node node) {
        return Neo4jNode.newNodeType(node);
    }

    @Override
    public String readLabel(Node node, NodeType type) throws Exception {
        return node.getProperty(type.getLabel()).toString();
    }
}
