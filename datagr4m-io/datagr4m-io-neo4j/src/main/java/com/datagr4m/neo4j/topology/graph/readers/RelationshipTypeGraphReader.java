package com.datagr4m.neo4j.topology.graph.readers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.tooling.GlobalGraphOperations;

import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class RelationshipTypeGraphReader extends AbstractGraphReader implements INeo4jGraphReader{
    public static final String FILTER_NAME = "relationship-graph";
    protected RelationshipType type;
    
    public RelationshipTypeGraphReader(RelationshipType type) {
        this.type = type;
    }

    @Override
    public Graph<IPropertyNode, IPropertyEdge> read(GlobalGraphOperations operation, Neo4jGraphModel model) {
        Iterator<Relationship> rit = operation.getAllRelationships().iterator();
        Set<Node> existing = new HashSet<Node>();
        
        // make a graph
        Graph<IPropertyNode, IPropertyEdge> graph = new DirectedSparseGraph<IPropertyNode, IPropertyEdge>();
        
        while(rit.hasNext()){
            Relationship r = rit.next();
            if(!r.getType().equals(type))
                continue;
            IPropertyEdge edge = newEdge(r, model);
            
            Node n1 = r.getStartNode();
            IPropertyNode node1 = newNode(n1, model);
            if(!existing.contains(node1))
                graph.addVertex(node1);
            
            Node n2 = r.getEndNode();
            IPropertyNode node2 = newNode(n2, model);
            if(!existing.contains(node2))
                graph.addVertex(node2);
            
            graph.addEdge(edge, node1, node2);
        }
        return graph;
    }
    
    @Override
    public String getName() {
        return FILTER_NAME;
    }
}
