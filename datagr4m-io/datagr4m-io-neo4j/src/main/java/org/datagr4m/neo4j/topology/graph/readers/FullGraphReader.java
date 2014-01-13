package org.datagr4m.neo4j.topology.graph.readers;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.tooling.GlobalGraphOperations;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class FullGraphReader extends AbstractGraphReader implements INeo4jGraphReader {
    public static final String FILTER_NAME = "full-graph";

    @Override
    public Graph<IPropertyNode, IPropertyEdge> read(GlobalGraphOperations operation, Neo4jGraphModel model) {
        Iterator<Node> nit = operation.getAllNodes().iterator();
        Iterator<Relationship> rit = operation.getAllRelationships().iterator();

        // a structure storing Node hierarchy
        // <parentnode,relationtype> -> {node,node,...}
        Multimap<Pair<Node, RelationshipType>, IPropertyNode> nodeHierarchy = ArrayListMultimap.create();

        // make a graph of non property nodes
        Graph<IPropertyNode, IPropertyEdge> graph = new DirectedSparseGraph<IPropertyNode, IPropertyEdge>();

        int k = 0;
        
        while (nit.hasNext()) {
            Node current = nit.next();
            IPropertyNode currentNode = newNode(current, model);
            
            model.registerModel(current, currentNode);
            
            //if (!isPropertyNode(currentNode))
                graph.addVertex(currentNode);
            
         // look for all its relations and remember it is child of some nodes
            storeParentChildRelation(nodeHierarchy, current, currentNode);
            
            /*// if node is a property node
            else {
                
            }*/
            k++;
        }
        Logger.getLogger(FullGraphReader.class).info("graph has " + k + " nodes");
        
        while (rit.hasNext()) {
            Relationship r = rit.next();
            IPropertyEdge edge = newEdge(r, model);
            IPropertyNode node1 = model.getModel(r.getStartNode());
            IPropertyNode node2 = model.getModel(r.getEndNode());
            
            if (!isPropertyNode(edge, node1, node2))
                graph.addEdge(edge, node1, node2);
        }
        
        // attach each property node to each of its parent node
        registerChildren(model, nodeHierarchy);
                
        return graph;
    }

    protected void registerChildren(Neo4jGraphModel model, Multimap<Pair<Node, RelationshipType>, IPropertyNode> nodeHierarchy) {
        for(Pair<Node, RelationshipType> parentKey: nodeHierarchy.keySet()){
            IPropertyNode parentNode = model.getModel(parentKey.a);
            RelationshipType type = parentKey.b;
            if(parentNode!=null){
                Collection<IPropertyNode> children = nodeHierarchy.get(parentKey);
                for(IPropertyNode child: children){
                    parentNode.addInformationNode(type.name(), child);
                    //System.out.println(parentNode.getLabel()  + "[" + type + "]-> " + child.getLabel());
                }
            }
            else
                Logger.getLogger(FullGraphReader.class).warn("no model node for parent node " + parentKey);
        }
    }

    protected void storeParentChildRelation(Multimap<Pair<Node, RelationshipType>, IPropertyNode> nodeHierarchy, Node current, IPropertyNode currentNode) {
        for (Relationship r : current.getRelationships()) {
            RelationshipType type = r.getType();
            Node parent;
            if (current.equals(r.getEndNode()))
                parent = r.getStartNode();
            else
                parent = r.getEndNode();

            nodeHierarchy.put(new Pair<Node,RelationshipType>(parent, type), currentNode);
            // node.
        }
    }

    @Override
    public String getName() {
        return FILTER_NAME;
    }

}
