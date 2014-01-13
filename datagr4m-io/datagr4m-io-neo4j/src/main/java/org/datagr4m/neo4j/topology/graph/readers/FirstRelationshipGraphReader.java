package org.datagr4m.neo4j.topology.graph.readers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.tooling.GlobalGraphOperations;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class FirstRelationshipGraphReader extends AbstractGraphReader implements INeo4jGraphReader{
    public static final String FILTER_NAME = "first-relationship";

    @Override
    public Graph<IPropertyNode,IPropertyEdge> read(GlobalGraphOperations operation, Neo4jGraphModel model) {
        Iterator<RelationshipType> tit = operation.getAllRelationshipTypes().iterator();
        while(tit.hasNext()){
            return read(operation, tit.next(), model);
        }
        return null;
    }

    public Graph<IPropertyNode,IPropertyEdge> read(GlobalGraphOperations operation, RelationshipType type, Neo4jGraphModel model) {
        Iterator<Relationship> rit = operation.getAllRelationships().iterator();
        Set<Node> existing = new HashSet<Node>();
        
        // make a graph
        Graph<IPropertyNode,IPropertyEdge> graph = new DirectedSparseGraph<IPropertyNode,IPropertyEdge>();
        
        while(rit.hasNext()){
            // make relationship as edge
            Relationship r = rit.next();
            if(!r.getType().equals(type))
                continue;
            IPropertyEdge edge = newEdge(r, model);

            // start node
            Node n1 = r.getStartNode();
            IPropertyNode node1 = newNode(n1, model);
            if(!existing.contains(node1))
                graph.addVertex(node1);
            
            // end node
            Node n2 = r.getEndNode();
            IPropertyNode node2 = newNode(n2, model);
            if(!existing.contains(node2))
                graph.addVertex(node2);
            
            // record edge and node
            graph.addEdge(edge, node1, node2);
        }
        return graph;
    }
    
    
    @Override
    public String getName() {
        return FILTER_NAME;
    }
}
