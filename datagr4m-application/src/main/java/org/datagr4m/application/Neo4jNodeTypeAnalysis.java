package org.datagr4m.application;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.neo4j.topology.graph.Neo4jGraphAnalysis;
import org.datagr4m.neo4j.topology.graph.Neo4jGraphModelIO;
import org.datagr4m.topology.graph.NodeType;
import org.jzy3d.io.SimpleFile;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import com.google.common.collect.Multimap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class Neo4jNodeTypeAnalysis {
    public static void main(String[] args) throws Exception {
        String db = null;
        if(args.length>0){
            db = args[0];
        }
        else{
            System.err.println("requires the relative db path as parameter: data/databases/???");
            return;
        }
            
        // Get graph data to build model
        GraphDatabaseFactory f = new GraphDatabaseFactory();
        GraphDatabaseService s = f.newEmbeddedDatabase(db);
        GlobalGraphOperations o = GlobalGraphOperations.at(s);

        // Build all netscope models
        
        
        //Multimap<NodeType, Node> getNodeTypes(Collection<Node> nodes)
        
        String st = nodeTypes(getGraph(o));
        System.out.println(st);
        
        File dbf = new File(db);
        String pt = dbf.getParent() + "/" + dbf.getName() + "-types.ns";
        SimpleFile.write(st, pt);
        System.out.println("--------------");
        System.out.println("this was saved to " + pt);
    }

    public static String nodeTypes(Graph<Node, Relationship> graph) {
        Collection<Node> nodes = graph.getVertices();
        Neo4jGraphAnalysis analysis = new Neo4jGraphAnalysis();
        Multimap<NodeType, Node> types = analysis.getNodeTypes(nodes);
        
        StringBuilder sb = new StringBuilder();
        Neo4jGraphModelIO manager = new Neo4jGraphModelIO();
        manager.serialize(types.keySet(), sb);
        return sb.toString();
    }
    
    public static void attributesAndValuesToConsole(Map<RelationshipType, Graph<Node, Relationship>> models) {
        Neo4jGraphAnalysis analysis = new Neo4jGraphAnalysis();
        
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<RelationshipType, Graph<Node,Relationship>> e: models.entrySet()){
            RelationshipType type = e.getKey();
            
            sb.append("-------------------------------------\n");
            sb.append("RELATIONSHIP TYPE: " + type + "\n");
            sb.append("-------------------------------------\n");
            
            Collection<Node> nodes = e.getValue().getVertices();
            
            Multimap<String,Pair<String,Node>> m = analysis.collectNodeAttributes(nodes);
            for(String attribute: m.keySet()){
                sb.append("+ node attribute=\"" + attribute + "\" {");

                for(Pair<String,Node> value: m.get(attribute)){
                    sb.append("\"" + value.a + "\", ");
                }
                sb.append("}\n");

            }
        }
        System.out.println(sb.toString());
    }

    public static void nodeTypes(Map<RelationshipType, Graph<Node, Relationship>> models) {
        Neo4jGraphAnalysis analysis = new Neo4jGraphAnalysis();
        
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<RelationshipType, Graph<Node,Relationship>> e: models.entrySet()){
            RelationshipType type = e.getKey();
            
            sb.append("-------------------------------------\n");
            sb.append("RELATIONSHIP TYPE: " + type + "\n");
            sb.append("-------------------------------------\n");
            
            Collection<Node> nodes = e.getValue().getVertices();
            
            Multimap<NodeType, Node> types = analysis.getNodeTypes(nodes);
            
            for(NodeType nt: types.keySet()){
                sb.append("type : " + nt.getName() + "\n");
                for(String property: nt.getProperties()){
                    sb.append("- property : " + property + "\n");
                }
                //sb.append("\n");
                sb.append("> instances ("+types.get(nt).size()+")\n\n");
                /*for(Node n: types.get(nt)){
                    sb.append(n);
                }*/
            }
        }
        System.out.println(sb.toString());
    }
    
    public static Graph<Node,Relationship> getGraph(GlobalGraphOperations o) {
        Iterator<Node> nit = o.getAllNodes().iterator();
        Iterator<Relationship> rit = o.getAllRelationships().iterator();

        // make a graph
        Graph<Node,Relationship> graph = new DirectedSparseGraph<Node, Relationship>();
        
        while(nit.hasNext()){
            Node n = nit.next();
            graph.addVertex(n);
        }
        
        while(rit.hasNext()){
            Relationship r = rit.next();
            graph.addEdge(r, r.getStartNode(), r.getEndNode());
        }
        return graph;
    }
    
}
