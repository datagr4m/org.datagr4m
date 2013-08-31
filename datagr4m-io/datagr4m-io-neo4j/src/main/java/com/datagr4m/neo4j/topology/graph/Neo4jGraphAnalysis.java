package com.datagr4m.neo4j.topology.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.topology.graph.GenericGraphAnalysis;
import org.datagr4m.topology.graph.NodeType;
import org.neo4j.graphdb.Node;

import com.datagr4m.neo4j.topology.nodes.Neo4jNode;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Neo4jGraphAnalysis extends GenericGraphAnalysis<Node> {
    @Override
    public NodeType newNodeType(Node node) {
        return Neo4jNode.newNodeType(node);
    }

    @Override
    public Set<String> relationTypes(Node node) {
        return Neo4jNode.relationTypes(node);
    }

    @Override
    public Multimap<String, Pair<String, Node>> collectNodeAttributes(Collection<Node> nodes) {
        Multimap<String, Pair<String, Node>> m = ArrayListMultimap.create();

        for (Node node : nodes) {
            Iterator<String> keys = node.getPropertyKeys().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = node.getProperty(key).toString();

                m.put(key, valueNode(node, value));
            }
        }

        return m;
    }
}
