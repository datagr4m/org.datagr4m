package org.datagr4m.application.neo4j.factories;

import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;

import com.datagr4m.neo4j.topology.Neo4jTopology;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;

import edu.uci.ics.jung.graph.Graph;

public class Neo4jTopologyFactory {
    //protected static String DEFAULT = "default";
    public Neo4jTopology newTopology(String name, Graph<IPropertyNode,IPropertyEdge> graph, Neo4jGraphModel model){
        if(Neo4jTopology.TOPOLOGY_NAME.equals(name))
            return new Neo4jTopology(graph, model);
        else{
            throw new RuntimeException("unexpected topology name: " + name);
        }
    }
}
